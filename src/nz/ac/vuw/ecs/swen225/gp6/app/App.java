package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MazeRenderer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.LogPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.awt.event.KeyEvent.*;
import static nz.ac.vuw.ecs.swen225.gp6.app.PanelCreator.*;


/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    static final long serialVersionUID = 1L;

    private final List<String> actionNames = List.of("Move Up","Move Down","Move Left","Move Right","Pause Game",
            "Resume Game","Jump To Level 1","Jump To Level 2","Quit Game","Save And Quit Game","Reload Game");
    @SuppressWarnings("FieldMayBeFinal")
    private List<Integer> actionKeyBindings = new ArrayList<>(List.of(VK_UP, VK_DOWN, VK_LEFT, VK_RIGHT,
                                                                VK_SPACE, VK_ESCAPE, VK_1, VK_2, VK_X, VK_S, VK_R));
    private int indexOfKeyToSet = -1;

    private DomainController game;
    private MazeRenderer render;
    private Controller controller;
    private Actions actions;
    private LogPanel logPanel = new LogPanel();

    static final int WIDTH = 1200;
    static final int HEIGHT = 800;
    private final JPanel outerPanel = new JPanel();
    private final JPanel menuPanel = new JPanel();
    private final JPanel gamePanel = new JPanel();
    private final CardLayout outerCardLayout = new CardLayout();
    private final CardLayout menuCardLayout = new CardLayout();
    private final CardLayout gameCardLayout = new CardLayout();

    private Timer timer;
    private long time = 0;          // used to store accumulated time from the previous pause
    private long timeStart = 0;     // starting time of current pause
    private long playedTime = 0;    // total time played in a level

    private boolean inReplay = false;
    private Runnable ob = ()->{}; // observer for the replay mode
//    private Recorder recorder = new Recorder();

    /**
     * Constructor for the App class. Initializes the GUI and the main loop.
     */
    public App(){
        System.setOut(new Interceptor(System.out)); // intercepts the output of System.out.print/println
        System.setErr(new Interceptor(System.err)); // intercepts the output of System.err.print/println
        System.out.print( "Application boot... ");
        assert SwingUtilities.isEventDispatchThread(): "boot failed: Not in EDT";
        System.out.println("GUI thread started");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("Application closed with exit code 0");
                System.exit(0);
            }}
        );
        initialiseGame();
        initialiseGUI();
    }

    /**
     * Initializes the game.
     */
    public void initialiseGame() {
        game = new DomainController(Persistency.getInitialDomain());
        render = new MazeRenderer(game);
        actions = new Actions(this);
        controller = new Controller(this);
        addKeyListener(controller);
        setTimer(new Timer(34, unused -> {
            assert SwingUtilities.isEventDispatchThread();
            game.pingAll();
            playedTime = System.nanoTime() - timeStart + time;
            if (inReplay) ob.run();
            repaint();
        }));
    }

    /**
     * Initializes the GUI and displays menu screen.
     */
    public void initialiseGUI(){
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setContentPane(outerPanel);
        outerPanel.setLayout(outerCardLayout);
        render.setFocusable(true);
        PanelCreator.configureMenuScreen(this, menuPanel, menuCardLayout);
        PanelCreator.configureGameScreen(this, gamePanel, gameCardLayout);
        outerPanel.add(menuPanel, MENU);
        outerPanel.add(gamePanel, GAME);
        transitionToMenuScreen();
        pack();
    }

    /**
     * Transitions to the menu screen.
     */
    public void transitionToMenuScreen(){
        System.out.print("Transitioning to menu screen... ");
        actions.actionPause();
        menuCardLayout.show(menuPanel, MENU);
        outerCardLayout.show(outerPanel, MENU);
        System.out.println("Complete");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        System.out.print("Transitioning to game screen... ");
        System.out.print("pre" + game);
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
        actions.actionStartNew();
        System.out.println("post" +render.maze);
        System.out.print("Complete");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToReplayScreen(){
        System.out.print("Transitioning to replay screen... ");
        System.out.print("pre" + game);
        gameCardLayout.show(gamePanel, REPLAY);
        outerCardLayout.show(outerPanel, GAME);
        actions.actionStartNew();
        System.out.println("post" +render.maze);
        System.out.print("Complete");
    }

    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Sets the observer to fire.
     *
     * @param ob the observer to fire
     */
    public void setObserver(Runnable ob){
        this.ob = ob;
    }

    /**
     * Sets the game to a new game.
     *
     * @param save the save file to load
     * @return The app object
     */
    public App setGame(DomainController save) {
        System.out.print("Setting game... ");
        System.out.println(game);
        this.game = save;
        this.render.setMaze(save);
        System.out.println(game);
        System.out.println("Complete");
        return this;
    }

    /**
     * exits the key setting mode so another action can be selected for setting key binding.
     */
    public void exitKeySettingMode(){
        indexOfKeyToSet = -1;
    }

    /**
     * Sets the index of the action to set a different key binding.
     *
     * @param indexOfKeyToSet the index of the action to set key
     */
    public void setIndexOfKeyToSet(int indexOfKeyToSet) {
        this.indexOfKeyToSet = indexOfKeyToSet;
    }

    /**
     * Sets the timer and its action going to be used for the game loop
     *
     * @param timer the timer to use for the main loop
     */
    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    /**
     * Sets the starting time for the game loop.
     *
     * @param nanoTime the starting time for the game loop
     */
    public void setStartingTime(long nanoTime) {
        timeStart = nanoTime;
    }

    /**
     * Sets the time left for the current level.
     *
     * @param time the time left for the current level
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Resets the time left for the current level.
     */
    public void resetTime(){
        time = 0;
        timeStart = System.nanoTime();
        playedTime = 0;
    }

    //================================================================================================================//
    //============================================ Getter Method =====================================================//
    //================================================================================================================//

    /**
     * Gets the current game.
     *
     * @return the game object
     */
    public DomainController getGame() {
        return game;
    }

    /**
     * Gets the current controller.
     *
     * @return the controller object
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Gets the current renderer.
     *
     * @return the renderer object
     */
    public MazeRenderer getRender() {
        return render;
    }

    /**
     * Gets the controllable actions.
     *
     * @return the actions object
     */
    public Actions getActions() {
        return actions;
    }

    /**
     * Gets the timer for the game loop.
     *
     * @return the timer for the game loop
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * Gets the time when the timer starts
     *
     * @return the starting time of the timer
     */
    public long getTimeStart() {
        return timeStart;
    }

    /**
     * Gets the time elapsed since the start of the game.
     *
     * @return the time elapsed since the start of the game
     */
    public long getTime() {
        return time;
    }

    /**
     * Gets the time elapsed since the start of the game in Minutes and Seconds.
     *
     * @return the time elapsed since the start of the game
     */
    public String getTimeInMinutes() {
        long time = this.playedTime;
        long seconds = time / 1000000000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Gets the index of the action to set a different key binding.
     *
     * @return the setting key
     */
    public int indexOfKeyToSet() {
        return indexOfKeyToSet;
    }

    /**
     * Returns if any action is ready to be set to  different key binding.
     *
     * @return true if the key is bound to an action, false otherwise
     */
    public boolean inSettingKeyMode(){
        return indexOfKeyToSet != -1;
    }

    /**
     * Gets the list of action names.
     *
     * @return the list of action names
     */
    public List<String> getActionNames() {
        return actionNames;
    }

    /**
     * Gets the list of action key bindings.
     *
     * @return the list of action key bindings
     */
    public List<Integer> getActionKeyBindings() {
        return actionKeyBindings;
    }

    /**
     * Gets the log panel
     * @return the log panel
     */
    public JPanel getLogPanel() {
        return logPanel;
    }

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

    private class Interceptor extends PrintStream{
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
            logPanel.print(s);   // this line enables output to log panel
        }
        @Override
        public void println(String s){
            print(s + "\n");
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
    public static void main(String... args){
        SwingUtilities.invokeLater(App::new);
    }
}