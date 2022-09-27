package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.GameClock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
import nz.ac.vuw.ecs.swen225.gp6.renderer.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


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
     * Constructor used for testing purposes by Fuzz, it will load into the game directly.
     *
     * @param i which level to load
     */
    public App(int i){
        System.setOut(new Interceptor(System.out)); // intercepts the output of System.out.print/println
        System.setErr(new Interceptor(System.err)); // intercepts the output of System.err.print/println
        System.out.print( "Application boot... ");
        assert SwingUtilities.isEventDispatchThread(): "boot failed: Not in EDT";
        System.out.println("GUI thread started");
        initialiseGUI();
        game.setCurrentLevel(i);
        transitionToGameScreen();
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
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters replay mode
     *
     * @param save the save file to load
     */
    public void startSavedReplay(DomainController save) {
        updateGameComponents(save, gameClock.getTimer());
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
            this.game.addEventListener(DomainEvent.onWin, ()->System.out.println("You win!"));
            this.game.addEventListener(DomainEvent.onLose, ()->System.out.println("You lose!"));
        }catch(Exception e){
            System.out.println("Failed to add event listener");
            e.printStackTrace();
        }
        gameClock.setTimer(timer);
    }

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
    public Configuration getConfiguration(){return config;}

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


    //================================================================================================================//
    //============================================ Logger Method =====================================================//
    //================================================================================================================//

    private static class NonBlockingLog extends Thread{
        public BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        public void run(){
            try {
                while(true){
                    String msg;
                    while ((msg = queue.poll()) != null) {
                        Persistency.log(msg);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error writing to log file");
            }
        }
    }

    private static class Interceptor extends PrintStream{
        NonBlockingLog logger;
        public Interceptor(OutputStream out){
            super(out, true);
            logger = new NonBlockingLog();
            logger.start();
        }
        @Override
        public void print(String s){
//            super.print(s);      // this line enables output to stdout
            logger.queue.add(s); // this line enables output to log file
            GUI.getLogPanel().print(s);   // this line enables output to log panel
        }
        @Override
        public void println(String s){
            print(s + "\n");
        }
        @Override
        public PrintStream printf(String format, Object... args){
            print(String.format(format, args));
            return this;
        }
    }

    //================================================================================================================//
    //============================================= Main Method ======================================================//
    //================================================================================================================//

    /**
     * Main method of the application.
     *
     * @param args No arguments required for this application
     */
    public static void main(String... args){SwingUtilities.invokeLater(App::new);}
}