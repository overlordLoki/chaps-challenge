package App;

import Renderer.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static App.PanelCreator.*;

/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    private static final String FONT = "Agency FB";
    private static final int STYLE = Font.BOLD;
    private Controller controller;
    private List<String> keyBindings = new ArrayList<>(List.of("Up","Down","Left","Right","Space","Esc","1","2","X","S","R","Ctrl"));
    private List<String> keyNames = List.of("Up","Down","Left","Right","Space","Esc","1","2","X","S","R","Ctrl");
    private int settingKey = -1;
    private Runnable closePhase = ()->{};
    private List<Runnable> stages = new ArrayList<>();

    /**
     * Constructor for the App class. Initializes the GUI and the main loop.
     */
    public App(){
        assert SwingUtilities.isEventDispatchThread();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        menuScreen();
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                closePhase.run();
            }
        });
    }

    private void menuScreen(){
        // shell to hold all the components
        var pnOuterMost = new JPanel();
        var cardLayout = new CardLayout();
        this.setContentPane(PanelCreator.configureMenuScreen(pnOuterMost, cardLayout));
        cardLayout.show(pnOuterMost, MENU);
        setPreferredSize(new Dimension(1200, 600));
        pack();
    }

    private void level(){
        // need to invoke persistent package to create level here
//        mazeRender = setLevel(Persistency.level());
    }

    private void deathScreen(){
        // need to invoke Render package to create create death screen here
//        mazeRender = Renderer.death();
    }

    private void victoryScreen(){
        // need to invoke Render package to create create death screen here
//        mazeRender = Renderer.victory();
    }

    private JPanel setLevel(Renderer l){
        // use the level model provided from persistent to generate the level and pass information to domain

        return null;
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
