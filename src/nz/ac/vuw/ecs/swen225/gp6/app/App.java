package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.GameClock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;
import nz.ac.vuw.ecs.swen225.gp6.persistency.logging.Interceptor;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
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
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.Action.*;


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
    private Domain game                 = Persistency.getInitialDomain();
    private final Actions actions       = new Actions(this);
    private final Configuration config  = new Configuration(true,new EnumMap<>(Map.ofEntries(
            Map.entry(MOVE_UP, new Controller.Key(0,VK_UP)),
            Map.entry(MOVE_DOWN, new Controller.Key(0,VK_DOWN)),
            Map.entry(MOVE_LEFT, new Controller.Key(0,VK_LEFT)),
            Map.entry(MOVE_RIGHT, new Controller.Key(0,VK_RIGHT)),
            Map.entry(PAUSE_GAME, new Controller.Key(0,VK_SPACE)),
            Map.entry(RESUME_GAME, new Controller.Key(0,VK_ESCAPE)),
            Map.entry(TO_LEVEL_1, new Controller.Key(InputEvent.CTRL_DOWN_MASK,VK_1)),
            Map.entry(TO_LEVEL_2, new Controller.Key(InputEvent.CTRL_DOWN_MASK,VK_2)),
            Map.entry(QUIT_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK,VK_X)),
            Map.entry(SAVE_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK,VK_S)),
            Map.entry(LOAD_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK,VK_R))
    )));
    private final Controller controller = new Controller(this);
    private final GameClock gameClock   = new GameClock(this);
    private final GUI gui               = new GUI(this);
    private final Record recorder       = new Record();
    private final Replay replay         = new Replay(this);
    private boolean inResume            = false;
    private final Domain[] saves        = new Domain[3];

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
        controller.update();
    }

    /**
     * Refreshes the saved games array.
     */
    public void refreshSaves(){
        IntStream.range(1, 4).forEach(i ->{
            try {
                saves[i-1] = Persistency.loadSave(i);
            } catch (DocumentException e) {
                System.out.printf("Failed to load save %d.\n", i);
                e.printStackTrace();
                String[] options = {"Reset", "Delete"};
                int choice = JOptionPane.showOptionDialog(null,
                        "Failed to load save " + i + ". What would you like to do?",
                        "Save file corrupted",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 0) saves[i-1] = Persistency.getInitialDomain();
                else if (choice == 1) {
                    try {
                        Persistency.deleteSave(i);
                    } catch (IOException ex) {
                        System.out.println("Error deleting save slot: " + i);
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error deleting save slot: " + i);
                    }
                }
            }
        });
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
        gui.getRender().addKeyListener(controller);
        transitionToMenuScreen();
        pack();
    }

    //================================================================================================================//
    //========================================== Transition Method ===================================================//
    //================================================================================================================//

    /**
     * Transitions to the menu screen.
     */
    public void transitionToMenuScreen(){
        System.out.print("Transitioning to menu screen... ");
        gameClock.stop();
        gameClock.reset();
        gui.transitionToMenuScreen();
        useMenuMusic();
        System.out.println("Complete");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        System.out.print("Transitioning to game screen... ");
        gameClock.setReplayMode(false);
        gameClock.start();
        gui.transitionToGameScreen();
        gui.showResumePanel();
        useGameMusic();
        System.out.println("Complete");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToReplayScreen(){
        System.out.print("Transitioning to replay screen... ");
        gameClock.setReplayMode(true);
        gameClock.start();
        gui.transitionToReplayScreen();
        gui.showResumePanel();
        useGameMusic();
        System.out.println("Complete");
    }


    private void useGameMusic(){
        MusicPlayer.stopMenuMusic();
        if(config.isMusicOn()) MusicPlayer.playGameMusic();
    }

    private void useMenuMusic(){
        MusicPlayer.stopGameMusic();
        if(config.isMusicOn()) MusicPlayer.playMenuMusic();
    }

    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Sets the game to a new game and enters game play mode
     */
    public void startNewGame() {
        updateGameComponents(Persistency.getInitialDomain(), gameClock.getTimer());
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters game play mode
     *
     * @param slot the save file to load
     */
    public void startSavedGame(int slot) {
        updateGameComponents(saves[slot-1], gameClock.getTimer());
        replay.load("save");
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters replay mode
     *
     * @param slot the save file to load
     */
    public void startSavedReplay(int slot) {
        updateGameComponents(saves[slot], gameClock.getTimer());
        replay.load("save");
        transitionToReplayScreen();
    }

    /**
     * Updates the game components to the correct state.
     *
     * @param game the new game to be updated
     * @param timer the new timer to be updated
     */
    private void updateGameComponents(Domain game, Timer timer) {
        this.game = game;
        this.gui.getRender().setMaze(game);
        this.gui.getInventory().setMaze(game);
        this.game.addEventListener(DomainEvent.onWin, ()->{
            inResume = false;
            if (game.nextLvl()){
                System.out.println("Next level");
                // TODO: invoke renderer cutscene
                // TODO: after cutscene, restart game clock with new level
                inResume = true; // enter game mode
            }else{
                System.out.println("You win!");
                gui.transitionToWinScreen();
            }});
        this.game.addEventListener(DomainEvent.onLose, ()->{
//                inResume = false;
        System.out.println("You lose!");
        this.gui.transitionToLostScreen();});
        this.gameClock.setTimer(timer);
        this.inResume = true;
        this.recorder.startRecording();
    }

    /**
     * Sets the game to be in resuming mode or not
     * @param isResuming true if in resume mode, false otherwise
     */
    public void setResuming(boolean isResuming){inResume = isResuming;}

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
     * Gets the controllable actions.
     *
     * @return the actions object
     */
    public Actions getActions() {return actions;}

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