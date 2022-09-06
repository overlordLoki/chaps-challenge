package App;

import App.tempDomain.Game;
import Renderer.tempDomain.*;
import Renderer.Renderer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static App.PanelCreator.*;

/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    private final List<String> actionNames = List.of("Move Up","Move Down","Move Left","Move Right","Pause Game",
            "Resume Game","Jump To Level 1","Jump To Level 2","Quit Game","Save And Quit Game","Reload Game");
    @SuppressWarnings("FieldMayBeFinal")
    private List<String> actionKeyBindings = new ArrayList<>(List.of("Up","Down","Left","Right","Space",
            "Escape","1","2","X","S","R"));
    private int indexOfKeyToSet = -1;
    private Controller controller;

    private Game game;

    private Runnable closePhase = ()->{};

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
            }
        });
        menuScreen();
    }

    /**
     * Enters the menu screen, where the user can start a new game, load a game, quit the game, or change key bindings.
     */
    private void menuScreen(){
        // shell to hold all the components
        var pnOuterMost = new JPanel();
        var cardLayout = new CardLayout();
        setContentPane(PanelCreator.configureMenuScreen(this, pnOuterMost, cardLayout, actionKeyBindings, actionNames));
        closePhase.run();
        closePhase = ()->remove(pnOuterMost);
        cardLayout.show(pnOuterMost, MENU);
        setPreferredSize(new Dimension(1200, 600));
        pack();
    }

    /**
     * Enters the game screen, and starts the game loop.
     * <p></p>
     * This method is called when the user clicks the "Start Game" button.
     * It initializes the game and controller, then starts the game loop.
     */
    public void gameScreen(){
        var pnOuterMost = new JPanel();
        var cardLayout = new CardLayout();

        // initialise game settings
        game = new Game();
        controller = new Controller(actionKeyBindings, game);
        var gameRenderer = new Renderer(new Maze());

        // set up the GUI
        this.setContentPane(PanelCreator.configureGameScreen(pnOuterMost, cardLayout,
                this, gameRenderer));
        cardLayout.show(pnOuterMost, MENU);

        // kickstart the game panel
        closePhase.run();
        closePhase = ()->remove(pnOuterMost);
        setPreferredSize(new Dimension(1200, 600));
        pack();
    }


    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Sets the index of the action to set a different key binding.
     *
     * @param indexOfKeyToSet the index of the action to set key
     */
    public void setIndexOfKeyToSet(int indexOfKeyToSet) {
        this.indexOfKeyToSet = indexOfKeyToSet;
    }

    /**
     * exits the key setting mode so another action can be selected for setting key binding.
     */
    public void exitKeySettingMode(){
        indexOfKeyToSet = -1;
    }


    //================================================================================================================//
    //============================================ Getter Method =====================================================//
    //================================================================================================================//

    /**
     * Gets the current game.
     *
     * @return the game object
     */
    public Game getGame() {
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
