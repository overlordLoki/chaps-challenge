package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.GameClock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.renderer.LogPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Interceptor;
import nz.ac.vuw.ecs.swen225.gp6.persistency.DomainPersistency;
import nz.ac.vuw.ecs.swen225.gp6.persistency.AppPersistency;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Replay;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Record;
import org.dom4j.DocumentException;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.IntStream;

import static java.awt.event.KeyEvent.*;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.*;


/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    /** The serial version UID for this update */
    public static final long serialVersionUID = 1L;
    /** Minimum Width of the window. */
    public static final int WIDTH = 1200;
    /** Minimum Height of the window. */
    public static final int HEIGHT = 800;

    // Core components of the game
    private Domain game                 = DomainPersistency.getInitial();
    private final Domain[] saves        = new Domain[3];
    private final GameClock gameClock   = new GameClock(this);
    private final GUI gui               = new GUI(this);
    private final Configuration config  = AppPersistency.load();
    private final Controller controller = new Controller(this);
    private final Record recorder       = new Record();
    private final Replay replay         = new Replay(this);
    private boolean inResume            = false;


    /**
     * Constructor for the App class. Initializes the GUI and the main loop.
     */
    public App(){
        System.setOut(new Interceptor(System.out)); // intercepts the output of System.out.print/println
        System.setErr(new Interceptor(System.err)); // intercepts the output of System.err.print/println
        System.out.print( "Application boot... ");
        assert SwingUtilities.isEventDispatchThread(): "boot failed: Not in EDT";
        System.out.println("GUI thread started");
        refreshSaves();
        initialiseGUI();
        initialiseCommands();
        controller.update();
    }

    /**
     * Initializes the GUI and displays menu screen.
     */
    public void initialiseGUI(){
        gui.configureGUI(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(TexturePack.Images.Hero.getImg());
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("Application closed with exit code 0");
                System.exit(0);
            }}
        );
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setContentPane(gui.getOuterPanel());
        gui.getRenderPanel().addKeyListener(controller);
        System.out.print("Transitioning to menu screen... ");
        QUIT_TO_MENU.run(this);
        System.out.println("Complete");
        pack();
    }

    private void initialiseCommands(){
        LogPanel logPanel = GUI.getLogPanel();
        logPanel.addCommands("nextLevel", "Jump to the next level", this::runWinEvent);
    }


    //================================================================================================================//
    //========================================== Transition Method ===================================================//
    //================================================================================================================//

    /**
     * Function to invoke the winning sequence, it also handles the transition to the next level.
     */
    public void runWinEvent(){
        inResume = false;
        gameClock.stop();
        if (game.nextLvl()){
            System.out.println("Next level");
            gui.getRenderPanel().changeLevel(()->{
                gameClock.reset();
                gameClock.start();
                RESUME_GAME.run(this);
            } );
        }else{
            System.out.println("You win!");
            gui.transitionToWinScreen();
        }
    }

    /**
     * Function to invoke the losing sequence.
     */
    public void runLoseEvent(){
        inResume = false;
        System.out.println("You lose!");
        this.gui.transitionToLostScreen();
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        System.out.print("Transitioning to game screen... ");
        gameClock.start();
        gui.transitionToGameScreen();
        useGameMusic();
        System.out.println("Complete");
    }

    private void useGameMusic(){
        MusicPlayer.stopMenuMusic();
        if(config.isMusicOn()) MusicPlayer.playGameMusic();
    }

    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Sets the game to a new game and enters game play mode
     */
    public void startNewGame() {
        updateGameComponents(DomainPersistency.getInitial());
        gameClock.useGameTimer();
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters game play mode
     *
     * @param slot the save file to load
     */
    public void startSavedGame(int slot) {
        updateGameComponents(saves[slot-1]);
        gameClock.useGameTimer();
        replay.load(slot);
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters replay mode
     *
     * @param slot the save file to load
     */
    public void startSavedReplay(int slot) {
        updateGameComponents(DomainPersistency.getInitial());
        // TODO: load replay module
        gameClock.useReplayTimer();
        replay.load(slot);
        System.out.print("Transitioning to replay screen... ");
        gui.transitionToReplayScreen();
        useGameMusic();
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
     * Sets the game to be in resuming mode or not
     * @param isResuming true if in resume mode, false otherwise
     */
    public void setResuming(boolean isResuming){inResume = isResuming;}

    /**
     * Refreshes the saved games array.
     */
    public void refreshSaves(){
        IntStream.range(0, 3).forEach(index ->{
            int slot = index + 1;
            try {
                saves[index] = DomainPersistency.loadSave(slot);
                gui.updateSaveInventory(index, saves[index]);
            } catch (DocumentException e) {
                System.out.printf("Failed to load save %d.\n", slot);
                e.printStackTrace();
                String[] options = {"Reset", "Delete"};
                int choice = JOptionPane.showOptionDialog(null,
                        "Failed to load save " + slot + ". What would you like to do?",
                        "Save file corrupted",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 0) saves[index] = DomainPersistency.getInitial();
                else if (choice == 1) {
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


    //================================================================================================================//
    //============================================ Getter Method =====================================================//
    //================================================================================================================//

    /**
     * Gets the current game.
     *
     * @return the game object
     */
    public Domain getGame() {return game;}

    /**
     * Gets the current controller.
     *
     * @return the controller object
     */
    public Controller getController() {return controller;}

    /**
     * Gets the current configuration.
     *
     * @return the Configuration object
     */
    public Configuration getConfiguration() {return config;}


    /**
     * Gets the current game clock.
     *
     * @return the game clock object
     */
    public GameClock getGameClock() {return gameClock;}

    /**
     * Gets the current Recorder.
     *
     * @return the recorder object
     */
    public Record getRecorder() {return recorder;}

    /**
     * Gets the current Replay.
     *
     * @return the replay object
     */
    public Replay getReplay() {return replay;}

    /**
     * Gets the gui object.
     *
     * @return the GUI object
     */
    public GUI getGUI() {return gui;}

    /**
     * Returns if the game is in resume mode or not.
     *
     * @return true if in resume mode, false otherwise
     */
    public boolean isResuming(){return inResume;}

    /**
     * Gets a saved game indicated by the save slot
     *
     * @param slot which save slot to access
     * @return The corresponding game save
     */
    public Domain getSave(int slot) {return saves[slot-1];}
}