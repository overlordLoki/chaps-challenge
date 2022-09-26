package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
import nz.ac.vuw.ecs.swen225.gp6.renderer.*;
import nz.ac.vuw.ecs.swen225.gp6.app.Controller.Key;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.awt.event.KeyEvent.*;
import static nz.ac.vuw.ecs.swen225.gp6.app.Controller.Key.key;
import static nz.ac.vuw.ecs.swen225.gp6.app.PanelCreator.*;


/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    static final long serialVersionUID = 1L;

    // Default settings for the game
    private final List<String> actionNames = List.of("Move Up","Move Down","Move Left","Move Right","Pause Game",
            "Resume Game","Jump To Level 1","Jump To Level 2","Quit Game","Save And Quit Game","Reload Game");
    private final int NO_MOD = 0;
    private final List<Key> defaultKeyBindings =
            List.of(key(NO_MOD,VK_UP),key(NO_MOD,VK_DOWN),key(NO_MOD,VK_LEFT),key(NO_MOD,VK_RIGHT),
                    key(NO_MOD,VK_SPACE),key(NO_MOD,VK_ESCAPE),
                    key(InputEvent.CTRL_DOWN_MASK,VK_1),key(InputEvent.CTRL_DOWN_MASK,VK_2),
                    key(InputEvent.CTRL_DOWN_MASK,VK_X),key(InputEvent.CTRL_DOWN_MASK,VK_S),key(InputEvent.CTRL_DOWN_MASK,VK_R));

    // User settings for the game
    private List<Key> userKeyBindings = new ArrayList<>(defaultKeyBindings);
    private int indexOfKeyToSet = -1;
    private boolean isMusicOn = true;

    // Core components of the game
    private DomainController game       = new DomainController(Persistency.getInitialDomain());
    private final MazeRenderer render   = new MazeRenderer(game);
    private final Actions actions       = new Actions(this);
    private final Controller controller = new Controller(this);
    private final LogPanel logPanel     = new LogPanel();

    // Timers for the game
    private boolean inReplayMode = false;
    private Runnable replayObserver = ()->{};   // observer for the replay mode
    private long time = 0;          // used to store accumulated time from the previous pause
    private long timeStart = 0;     // starting time of current pause
    private long playedTime = 0;    // total time played in a level
    private final Timer gameTimer = new Timer(34, unused -> {
        assert SwingUtilities.isEventDispatchThread();
        game.pingAll();
        playedTime = System.nanoTime() - timeStart + time;
        if (inReplayMode) replayObserver.run();
        repaint();
    });
    private Timer timer = gameTimer;

    // GUI components
    static final int WIDTH = 1200;
    static final int HEIGHT = 800;
    private final JPanel outerPanel = new JPanel();
    private final JPanel menuPanel  = new JPanel();
    private final JPanel gamePanel  = new JPanel();
    private final JPanel functionPanel = PanelCreator.createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel loadGamePanel = createClearPanel(BoxLayout.X_AXIS);
    private final InventoryPanel pnInventory = new InventoryPanel(game, true);
    private final CardLayout outerCardLayout = new CardLayout();
    private final CardLayout menuCardLayout = new CardLayout();
    private final CardLayout gameCardLayout = new CardLayout();
    private final CardLayout functionCardLayout = new CardLayout();

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
        setIconImage(TexturePack.Images.Hero.getImg());
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("Application closed with exit code 0");
                System.exit(0);
            }}
        );
        initialiseGUI();
        loadUserSettings();
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(TexturePack.Images.Hero.getImg());
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.out.println("Application closed with exit code 0");
                System.exit(0);
            }}
        );
        initialiseGUI();
        game.setCurrentLevel(i);
        transitionToGameScreen();
    }

    /**
     * Initializes the GUI and displays menu screen.
     */
    public void initialiseGUI(){
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setContentPane(outerPanel);
        outerPanel.setLayout(outerCardLayout);
        menuPanel.setLayout(menuCardLayout);
        gamePanel.setLayout(gameCardLayout);
        functionPanel.setLayout(functionCardLayout);
        render.setFocusable(true);
        PanelCreator.configureMenuScreen(this, menuPanel, menuCardLayout, loadGamePanel);
        PanelCreator.configureGameScreen(this, gamePanel, gameCardLayout, functionPanel, pnInventory);
        outerPanel.add(menuPanel, MENU);
        outerPanel.add(gamePanel, GAME);
        addKeyListener(controller);
        transitionToMenuScreen();
        pack();
    }

    //================================================================================================================//
    //============================================ GUI Method =====================================================//
    //================================================================================================================//

    /**
     * Transitions to the menu screen.
     */
    public void transitionToMenuScreen(){
        System.out.print("Transitioning to menu screen... ");
        timer.stop();
        setTime(System.nanoTime() - getTimeStart() + getTime());
        functionCardLayout.show(functionPanel, MODE_NORMAL);
        menuCardLayout.show(menuPanel, MENU);
        outerCardLayout.show(outerPanel, MENU);
        useMenuMusic();
        System.out.println("Complete");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        System.out.print("Transitioning to game screen... ");
        inReplayMode = false;
        resetTime();
        timer.restart();
        functionCardLayout.show(functionPanel, MODE_NORMAL);
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
        useGameMusic();
        System.out.println("Complete");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToReplayScreen(){
        System.out.print("Transitioning to replay screen... ");
        inReplayMode = true;
        resetTime();
        timer.restart();
        functionCardLayout.show(functionPanel, MODE_REPLAY);
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
        useGameMusic();
        System.out.println("Complete");
    }

    /**
     * Transitions to the winning screen.
     */
    public void transitionToWinScreen(){
        System.out.print("Transitioning to win screen... ");
        gameCardLayout.show(gamePanel, VICTORY);
        outerCardLayout.show(outerPanel, GAME);
        System.out.println("Complete");
    }

    /**
     * Transitions to the losing screen.
     */
    public void transitionToLostScreen(){
        System.out.print("Transitioning to win screen... ");
        gameCardLayout.show(gamePanel, LOOSE);
        outerCardLayout.show(outerPanel, GAME);
        System.out.println("Complete");
    }

    private void useGameMusic(){
        MusicPlayer.stopMenuMusic();
        if(isMusicOn) MusicPlayer.playGameMusic();
    }

    private void useMenuMusic(){
        MusicPlayer.stopGameMusic();
        if(isMusicOn) MusicPlayer.playMenuMusic();
    }

    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Loads the user's setting configuration.
     */
    public void loadUserSettings(){
        System.out.print("Loading user settings... ");
        this.userKeyBindings = new ArrayList<>(defaultKeyBindings);
        this.controller.resetController();
        System.out.println("Complete");
    }

    /**
     * Sets the game to a new game and enters game play mode
     */
    public void startNewGame() {
        updateGameComponents(new DomainController(Persistency.getInitialDomain()), gameTimer);
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters game play mode
     *
     * @param save the save file to load
     */
    public void startSavedGame(DomainController save) {
        updateGameComponents(save, gameTimer);
        transitionToGameScreen();
    }

    /**
     * Sets the game to a saved game and enters replay mode
     *
     * @param save the save file to load
     */
    public void startSavedReplay(DomainController save) {
        updateGameComponents(save, timer);
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
        this.render.setMaze(game);
        this.pnInventory.setMaze(game);
        try{
            this.game.addEventListener(DomainEvent.onWin, ()->System.out.println("You win!"));
            this.game.addEventListener(DomainEvent.onLose, ()->System.out.println("You lose!"));
        }catch(Exception e){
            System.out.println("Failed to add event listener");
            e.printStackTrace();
        }
        this.setTimer(timer);
    }

    /**
     * Sets the observer to fire.
     *
     * @param ob the observer to fire
     */
    public void setObserver(Runnable ob){
        this.replayObserver = ob;
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
     * Sets the key binding for the action.
     * @param index the index of the action to set key
     * @param key the key to set
     */
    public void setKeyBinding(int index, Key key){
        userKeyBindings.set(index, key);
    }

    /**
     * Sets playing music to true or false
     * @param musicOn true if music is to be played, false otherwise
     */
    public void setMusicOn(boolean musicOn) {
        this.isMusicOn = musicOn;
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
     * Gets the action name
     * @param index the index of the action
     * @return the name of the action
     */
    public String getActionName(int index){
        return actionNames.get(index);
    }

    /**
     * Gets the key binding for the action.
     * @param index the index of the action
     * @return the key binding for the action
     */
    public Key getKeyBinding(int index){
        return userKeyBindings.get(index);
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
     * Gets the list of action key bindings.
     *
     * @return the list of action key bindings
     */
    public List<Key> getUserKeyBindings() {
        return userKeyBindings;
    }

    /**
     *  Check if this key combo is already bound to an action.
     * @param key the key to check
     * @return true if the key is bound to an action, false otherwise
     */
    public boolean checkKeyBinding(Key key){
        return userKeyBindings.contains(key);
    }

    /**
     * Checks if the user is playing music.
     * @return true if the user is playing music, false otherwise
     */
    public boolean isMusicOn() {
        return isMusicOn;
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