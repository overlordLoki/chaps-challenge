package App;

import Domain.*;
import Persistency.*;
import Recorder.*;
import Renderer.*;
import Renderer.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static javax.swing.SwingConstants.CENTER;

/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class App extends JFrame {
    private JPanel panels;
    private JButton buttons;
    private JLabel labels;
    private JPanel mazeRender;
    private Controller controller;
    private List<String> keyBindings = new ArrayList<>(List.of("Up","Down","Left","Right","Space","Esc","1","2","X","S","R","Ctrl"));
    private List<String> keyNames = List.of("Up","Down","Left","Right","Space","Esc","1","2","X","S","R","Ctrl");
    private int settingKey = -1;
    private Runnable closePhase = ()->{};

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
        var jpOuterMost = new JPanel();
        var jpWelcome = new JPanel();
        var jpMenu = new JPanel();

        var jlWelcome = new JLabel("Chaps Challenge!");
        var btnStart = new JButton("Start!");
        var btnLoad = new JButton("Load");
        var btnSettings = new JButton("Controls");
        var btnInstructions = new JButton("How to play");
        var btnCredits = new JLabel("Credits");
        var btnExit = new JButton("Exit");


        var test = new JLabel("Controls");
        test.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){test.setForeground(Color.RED);}
            public void mouseExited(MouseEvent e) {test.setForeground(Color.BLACK);}
            public void mouseClicked(MouseEvent e) {
                JFrame jf=new JFrame("new one");
                jf.setBackground(Color.BLACK);
                jf.setSize(new Dimension(200,70));
                jf.setVisible(true);
                jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
        test.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            }
        });

        // setting layout
        jlWelcome.setHorizontalAlignment(CENTER);
        jpOuterMost.setLayout(new BoxLayout(jpOuterMost, BoxLayout.Y_AXIS));
        test.setAlignmentX(CENTER_ALIGNMENT);


        // assemble this frame
        jpOuterMost.add(jlWelcome);
//        createButtons(new ArrayList<>()).forEach(pnOuterMost::add);
        jpOuterMost.add(test);
        jpOuterMost.add(new JLabel(""));
        jpOuterMost.add(btnStart);





        add(jpOuterMost);
        closePhase.run();
        closePhase = () -> remove(jpOuterMost);
        btnStart.addActionListener(e -> level());
        setPreferredSize(new Dimension(800, 400));
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



    private List<JButton> createButtons(List<JButton> keyBindingButtons) {
        IntStream.range(0, keyNames.size()).forEach(i -> {
            var button = new JButton(keyNames.get(i) + keyBindings.get(i));
            button.addActionListener(unused -> settingKey = keyBindingButtons.indexOf(button));
            button.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {}
                public void keyPressed(KeyEvent e) {}
                public void keyReleased(KeyEvent e) {
                    if (settingKey == -1) return;
                    if (keyBindings.contains(KeyEvent.getKeyText(e.getKeyCode()))){
                        settingKey = -1;
                        return;
                    }
                    keyBindings.set(settingKey, KeyEvent.getKeyText(e.getKeyCode()));
                    button.setText(keyNames.get(settingKey) + KeyEvent.getKeyText(e.getKeyCode()));
                    settingKey = -1;
                }
            });
            keyBindingButtons.add(button);
        });
        return keyBindingButtons;
    }





    /**
     * Main method of the application.
     *
     * @param args No arguments required for this application
     */
    public static void main(String... args){
        SwingUtilities.invokeLater(App::new);
    }
}
