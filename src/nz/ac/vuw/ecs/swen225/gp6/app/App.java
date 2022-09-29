package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.GameClock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Replay;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Record;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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
    private DomainController game       = new DomainController(Persistency.getInitialDomain());
    private final Actions actions       = new Actions(this);
    private final Configuration config  = new Configuration();
    private final Controller controller = new Controller(this);
    private final GameClock gameClock   = new GameClock(this);
    private final GUI gui               = new GUI(this);
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
        initialiseGUI();
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
        addKeyListener(controller);
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

    /**
     * Transitions to the winning screen.
     */
    public void transitionToWinScreen(){
        System.out.print("Transitioning to win screen... ");
        gui.transitionToWinScreen();
        System.out.println("Complete");
    }

    /**
     * Transitions to the losing screen.
     */
    public void transitionToLostScreen(){
        System.out.print("Transitioning to win screen... ");
        gui.transitionToLostScreen();
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
        updateGameComponents(new DomainController(Persistency.getInitialDomain()), gameClock.getTimer());
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters game play mode
     *
     * @param save the save file to load
     */
    public void startSavedGame(DomainController save) {
        updateGameComponents(save, gameClock.getTimer());
        replay.load("save");
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters replay mode
     *
     * @param save the save file to load
     */
    public void startSavedReplay(DomainController save) {
        updateGameComponents(save, gameClock.getTimer());
        replay.load("save");
        transitionToReplayScreen();
    }

    /**
     * Updates the game components to the correct state.
     *
     * @param game the new game to be updated
     * @param timer the new timer to be updated
     */
    private void updateGameComponents(DomainController game, Timer timer) {
        this.game = game;
        gui.getRender().setMaze(game);
        gui.getInventory().setMaze(game);
        try{
            this.game.addEventListener(DomainEvent.onWin, ()->{
                inResume = false;
                System.out.println("You win!");});
            this.game.addEventListener(DomainEvent.onLose, ()->{
//                inResume = false;
                System.out.println("You lose!");});
        }catch(Exception e){
            System.out.println("Failed to add event listener");
            e.printStackTrace();
        }
        gameClock.setTimer(timer);
        inResume = true;
        recorder.startRecording();
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
    public DomainController getGame() {return game;}

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
}