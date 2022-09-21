package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.renderer.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> actionKeyBindings = new ArrayList<>(List.of("Up","Down","Left","Right","Space",
            "Escape","1","2","X","S","R"));
    private int indexOfKeyToSet = -1;

    private DomainController game;
    private Renderer render;
    private Controller controller;
    private Actions actions;

    static final int WIDTH = 1200;
    static final int HEIGHT = 800;
    private final JPanel outerPanel = new JPanel();
    private final JPanel menuPanel = new JPanel();
    private final JPanel gamePanel = new JPanel();
    private final CardLayout outerCardLayout = new CardLayout();
    private final CardLayout menuCardLayout = new CardLayout();
    private final CardLayout gameCardLayout = new CardLayout();

    Runnable closePhase = ()->{};
    private Timer timer;
    private long time = 0;
    private long timeStart = 0;

    /**
     * Constructor for the App class. Initializes the GUI and the main loop.
     */
    public App(){
        assert SwingUtilities.isEventDispatchThread();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                closePhase.run();
            }}
        );
        initialiseGame();
        initialiseGUI();
    }

    private void initialiseGame() {
        game = new DomainController();
        render = new Renderer(game);
        actions = new Actions(this);
        controller = new Controller(this);
        addKeyListener(controller);
        setTimer(new Timer(34, unused -> {
            assert SwingUtilities.isEventDispatchThread();
            game.pingAll();
            time = System.nanoTime() - timeStart;
            repaint();
        }));
    }

    /**
     * Initializes the GUI and displays menu screen.
     */
    private void initialiseGUI(){
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
        System.out.println("Toggling to menu screen");
        menuCardLayout.show(menuPanel, MENU);
        outerCardLayout.show(outerPanel, MENU);
        actions.actionPause();
        System.out.println("Menu shown");
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        System.out.println("Toggling to game screen");
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
        actions.actionResume();
        System.out.println("Game shown");
    }

    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

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
        this.time += time;
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
    public Renderer getRender() {
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
        long time = this.time;
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
    public List<String> getActionKeyBindings() {
        return actionKeyBindings;
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
