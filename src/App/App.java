package App;

import Renderer.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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
        var pnOuterMost = new JPanel();
        var jlWelcome = new JLabel("Chaps Challenge!");
        List<JLabel> labels = new ArrayList<>(List.of(
                new JLabel("Start New Game!"),
                new JLabel("Load Game"),
                new JLabel("Settings"),
                new JLabel("How to play"),
                new JLabel("Credits"),
                new JLabel("Exit"))
        );
        activateStages();
        // setting layout
        pnOuterMost.setLayout(new BoxLayout(pnOuterMost, BoxLayout.Y_AXIS));
        pnOuterMost.setBackground(Color.PINK);
        jlWelcome.setAlignmentX(CENTER_ALIGNMENT);
        jlWelcome.setFont(new Font(FONT, STYLE, 80));

        // assemble this frame
        pnOuterMost.add(jlWelcome);
        pnOuterMost.add(Box.createVerticalGlue());
        setLabelsAndAttachToPanel(pnOuterMost, labels);
        add(pnOuterMost);

        closePhase.run();
        closePhase = () -> remove(pnOuterMost);
        setPreferredSize(new Dimension(1200, 600));
        pack();
    }



    private void setLabelsAndAttachToPanel(JPanel pnOuterMost, List<JLabel> labels) {
        labels.forEach(l->{
            l.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){l.setForeground(Color.RED);}
                public void mouseExited(MouseEvent e) {l.setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {
                    // trigger panel switching
                    stages.get(labels.indexOf(l)).run();
                }
            });
            l.setAlignmentX(CENTER_ALIGNMENT);
            l.setFont(new Font(FONT, STYLE, 40));
            pnOuterMost.add(l);
        });
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


    //================================================================================================================//
    //============================================= Menu Stages ======================================================//
    //================================================================================================================//

    private void activateStages(){
        stages.add(this::stageNewGame);
        stages.add(this::stageLoad);
        stages.add(this::stageSettings);
        stages.add(this::stageInstructions);
        stages.add(this::stageCredits);
        stages.add(this::stageExit);
    }

    private void stageNewGame() {
        System.out.println("New Game");
    }

    private void stageLoad() {
        System.out.println("Load Game");
    }

    private void stageSettings() {
        System.out.println("Settings");
    }

    private void stageInstructions() {
        System.out.println("How to play");
    }

    private void stageCredits() {
        System.out.println("Credits");
    }

    private void stageExit() {
        if (JOptionPane.showOptionDialog(this,
                "Are you sure you want to quit the game?", "Select an Option" ,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null) == 0){
            System.exit(0);
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
