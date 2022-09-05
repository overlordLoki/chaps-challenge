package App;

import App.tempDomain.Game;
import Renderer.tempDomain.*;
import Renderer.Renderer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import static App.PanelCreator.*;

/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    public Controller controller;
    private List<String> keyBindings = new ArrayList<>(List.of("Up","Down","Left","Right","Space","Escape","1","2","X","S","R"));
    private List<String> keyNames = List.of("Move Up","Move Down","Move Left","Move Right","Pause Game",
            "Resume Game","Jump To Level 1","Jump To Level 2","Quit Game","Save And Quit Game","Reload Game");
    int settingKey = -1;

    public Game game;

    public Runnable closePhase = ()->{};

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

    private void menuScreen(){
        // shell to hold all the components
        var pnOuterMost = new JPanel();
        var cardLayout = new CardLayout();
        this.setContentPane(PanelCreator.configureMenuScreen(this, pnOuterMost, cardLayout, keyBindings, keyNames));
        closePhase.run();
        closePhase = ()->remove(pnOuterMost);
        cardLayout.show(pnOuterMost, MENU);
        setPreferredSize(new Dimension(1200, 600));
        pack();
    }

    /**
     *
     */
    public void gameScreen(){
        var pnOuterMost = new JPanel();
        var cardLayout = new CardLayout();

        // initialise game settings
        game = new Game();
        controller = new Controller(keyBindings, game);
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
