package App;

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
    private static final String FONT = "Agency FB";
    private static final int STYLE = Font.BOLD;
    public Controller controller;
    private List<String> keyBindings = new ArrayList<>(List.of("Up","Down","Left","Right","Space","Esc","1","2","X","S","R","Ctrl"));
    private List<String> keyNames = List.of("Up","Down","Left","Right","Space","Esc","1","2","X","S","R","Ctrl");
    private int settingKey = -1;
    public Runnable closePhase = ()->{};
    private List<Runnable> stages = new ArrayList<>();

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
        this.setContentPane(PanelCreator.configureMenuScreen(this, pnOuterMost, cardLayout));
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
        this.setContentPane(PanelCreator.configureGameScreen(pnOuterMost, cardLayout,
                this, new Renderer(new Maze())));
        cardLayout.show(pnOuterMost, MENU);
        closePhase.run();
        closePhase = ()->remove(pnOuterMost);
        setPreferredSize(new Dimension(1200, 600));
        pack();
    }

    private JPanel setLevel(Renderer l){
        // use the level model provided from persistent to generate the level and pass information to domain

        return null;
    }

    public int getCurrentLevel() {
        return 1;
    }

    public int getTreasuresLeft() {
        return 12;
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
