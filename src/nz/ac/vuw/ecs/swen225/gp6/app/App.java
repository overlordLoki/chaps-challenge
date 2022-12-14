package nz.ac.vuw.ecs.swen225.gp6.app;

import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.QUIT_TO_MENU;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.RESUME_GAME;

import java.io.IOException;
import java.util.stream.IntStream;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.GameClock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.GameState;
import nz.ac.vuw.ecs.swen225.gp6.persistency.AppPersistency;
import nz.ac.vuw.ecs.swen225.gp6.persistency.DomainPersistency;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Interceptor;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Record;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Replay;
import nz.ac.vuw.ecs.swen225.gp6.renderer.LogPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;
import org.dom4j.DocumentException;


/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App {

  private final Domain[] saves = new Domain[3];
  private final GameClock gameClock = new GameClock(this);
  private final GUI gui = new GUI(this);
  private final Configuration config = AppPersistency.load();
  private final Controller controller = new Controller(this);
  private final Record recorder = Record.INSTANCE;
  private final Replay replay = Replay.INSTANCE;
  // Core components of the game
  private Domain game = DomainPersistency.getInitial();
  private boolean inResume = false;


  /**
   * Constructor for the App class. Initializes the GUI and the main loop.
   */
  public App() {
    System.setOut(new Interceptor(System.out)); // intercepts the output of print/println
    System.setErr(new Interceptor(System.err));
    System.out.print("Application boot... ");
    assert SwingUtilities.isEventDispatchThread() : "boot failed: Not in EDT";
    System.out.println("GUI thread started");
    refreshSaves();
    initialiseGui();
    initialiseCommands();
    replay.setReplay(this);
    config.update(this);
    controller.update();
  }

  /**
   * Initializes the GUI and displays menu screen.
   */
  public void initialiseGui() {
    gui.configureGUI(this);
    gui.getRenderPanel().addKeyListener(controller);
    QUIT_TO_MENU.run(this);
  }

  private void initialiseCommands() {
    LogPanel logPanel = GUI.getLogPanel();
    logPanel.addCommands("nextLvl", "Jump to the next level", this::runWinEvent);
    logPanel.addCommands("win", "Win the game", () -> {
      gameClock.stop();
      inResume = false;
      gui.transitionToWinScreen();
    });
  }

  //==============================================================================================//
  //================================= Transition Method ==========================================//
  //==============================================================================================//

  /**
   * Function to invoke the winning sequence, it also handles the transition to the next level.
   */
  public void runWinEvent() {
    gameClock.stop();
    inResume = false;
    if (game.nextLvl()) {
      System.out.println("Next level");
      gui.getRenderPanel().changeLevel(() -> {
        gameClock.reset();
        gameClock.start();
        RESUME_GAME.run(this);
      });
    } else {
      System.out.println("You win!");
      gui.transitionToWinScreen();
    }
  }

  /**
   * Function to invoke the losing sequence.
   */
  public void runLoseEvent() {
    gameClock.stop();
    inResume = false;
    game.setGameState(GameState.LOST);
    System.out.println("You lose!");
    this.gui.transitionToLostScreen();
  }

  /**
   * Transitions to the game screen.
   */
  public void transitionToGameScreen() {
    System.out.print("Transitioning to game screen... ");
    gameClock.start();
    gui.transitionToGameScreen();
    MusicPlayer.useGameMusic();
    if (config.isMusicOn()) {
      MusicPlayer.playMusic();
    }
    System.out.println("Complete");
  }

  //==============================================================================================//
  //=================================== Setter Method ============================================//
  //==============================================================================================//

  /**
   * Sets the game to a new game and enters game play mode.
   */
  public void startNewGame() {
    updateGameComponents(DomainPersistency.getInitial());
    gameClock.useGameTimer();
    transitionToGameScreen();
  }

  /**
   * Sets the game to a saved game and enters game play mode.
   *
   * @param slot the save file to load
   */
  public void startSavedGame(int slot) {
    updateGameComponents(saves[slot - 1]);
    gameClock.reset();
    gameClock.setTimePlayed(game.getCurrentTime());
    replay.load(slot);
    recorder.stitchRecording(slot);
    transitionToGameScreen();
  }

  /**
   * Sets the game to a saved game and enters replay mode.
   *
   * @param slot the save file to load
   */
  public void startSavedReplay(int slot) {
    updateGameComponents(DomainPersistency.getInitial());
    gameClock.useReplayTimer();
    replay.load(slot);
    System.out.print("Transitioning to replay screen... ");
    gui.transitionToReplayScreen();
    MusicPlayer.useGameMusic();
    if (config.isMusicOn()) {
      MusicPlayer.playMusic();
    }
    System.out.println("Complete");
  }

  /**
   * Updates the game components to the correct state.
   *
   * @param game the new game to be updated
   */
  private void updateGameComponents(Domain game) {
    this.game = game;
    this.gui.getRenderPanel().setMaze(game);
    this.gui.getInventory().setMaze(game);
    this.game.addEventListener(DomainEvent.onWin, this::runWinEvent);
    this.game.addEventListener(DomainEvent.onLose, this::runLoseEvent);
    this.inResume = true;
    this.recorder.startRecording();
    this.gameClock.reset();
  }

  /**
   * Refreshes the saved games array.
   */
  public void refreshSaves() {
    IntStream.range(0, 3).forEach(index -> {
      int slot = index + 1;
      try {
        saves[index] = DomainPersistency.loadSave(slot);
        gui.updateSaveInventory(index, saves[index]);
      } catch (DocumentException e) {
        System.out.printf("Failed to load save %d.%n", slot);
        e.printStackTrace();
        String[] options = {"Reset", "Delete"};
        int choice = JOptionPane.showOptionDialog(null,
            "Failed to load save " + slot + ". What would you like to do?",
            "Save file corrupted",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
          saves[index] = DomainPersistency.getInitial();
        } else if (choice == 1) {
          try {
            DomainPersistency.delete(slot);
          } catch (IOException ex) {
            System.out.println("Error deleting save slot: " + slot);
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting save slot: " + slot);
          }
        }
      }
    });
  }

  /**
   * Gets the current game.
   *
   * @return the game object
   */
  public Domain getGame() {
    return game;
  }

  //==============================================================================================//
  //=================================== Getter Method ============================================//
  //==============================================================================================//

  /**
   * Gets the current controller.
   *
   * @return the controller object
   */
  public Controller getController() {
    return controller;
  }

  /**
   * Gets the current configuration.
   *
   * @return the Configuration object
   */
  public Configuration getConfiguration() {
    return config;
  }

  /**
   * Gets the current game clock.
   *
   * @return the game clock object
   */
  public GameClock getGameClock() {
    return gameClock;
  }

  /**
   * Gets the current Recorder.
   *
   * @return the recorder object
   */
  public Record getRecorder() {
    return recorder;
  }

  /**
   * Gets the current Replay.
   *
   * @return the replay object
   */
  public Replay getReplay() {
    return replay;
  }

  /**
   * Gets the gui object.
   *
   * @return the GUI object
   */
  public GUI getGUI() {
    return gui;
  }

  /**
   * Returns if the game is in resume mode or not.
   *
   * @return true if in resume mode, false otherwise
   */
  public boolean isResuming() {
    return inResume;
  }

  /**
   * Sets the game to be in resuming mode or not.
   *
   * @param isResuming true if in resume mode, false otherwise
   */
  public void setResuming(boolean isResuming) {
    inResume = isResuming;
  }

  /**
   * Gets a saved game indicated by the save slot.
   *
   * @param slot which save slot to access
   * @return The corresponding game save
   */
  public Domain getSave(int slot) {
    return saves[slot - 1];
  }
}