package App;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.awt.Component.CENTER_ALIGNMENT;

/**
 * Package-private class for creating panels used by the App.
 *
 * @author Jeff Lin
 */
class PanelCreator{
    private static final String FONT = "Agency FB";
    private static final int STYLE = Font.BOLD;
    private static final int TITLE_SIZE = 80;
    private static final int TEXT_SIZE = 40;

    public static final String MENU        = "Menu";
    public static final String NEW_GAME    = "New Game";
    public static final String LOAD_GAME   = "Load Game";
    public static final String SETTINGS    = "Settings";
    public static final String HOW_TO_PLAY = "How to play";
    public static final String CREDITS     = "Credits";
    public static final String EXIT        = "Exit";


    /**
     * Constructor for the App class. Initializes the GUI and the main loop.
     */
    private PanelCreator(){
    }

    public static JPanel configureMenuScreen(JPanel pnOuterMost, CardLayout cardLayout){
        // components to be added to the shell
        JPanel pnMenu      = configurePanelMenu(pnOuterMost, cardLayout);
        JPanel pnNewGame   = configurePanelNewGame(pnOuterMost, cardLayout);
        JPanel pnLoad      = configurePanelLoad(pnOuterMost, cardLayout);
        JPanel pnSettings  = configurePanelSettings(pnOuterMost, cardLayout);
        JPanel pnHowToPlay = configurePanelPlay(pnOuterMost, cardLayout);
        JPanel pnCredits   = configurePanelCredits(pnOuterMost, cardLayout);
        JPanel pnExit      = configurePanelExit(pnOuterMost, cardLayout);

        // add components to the shell
        pnOuterMost.setLayout(cardLayout);
        pnOuterMost.add(pnMenu, MENU);
        pnOuterMost.add(pnNewGame, NEW_GAME);
        pnOuterMost.add(pnLoad, LOAD_GAME);
        pnOuterMost.add(pnSettings, SETTINGS);
        pnOuterMost.add(pnHowToPlay, HOW_TO_PLAY);
        pnOuterMost.add(pnCredits, CREDITS);
        pnOuterMost.add(pnExit, EXIT);
        return pnOuterMost;
    }


    //================================================================================================================//
    //============================================= Menu Panels ======================================================//
    //================================================================================================================//

    public static JPanel configurePanelMenu(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Menu");

        var pnMenu = new JPanel();
        var jlWelcome = new JLabel("Chaps Challenge!");
        List<JLabel> labels = new ArrayList<>(List.of(
                new JLabel("Start New Game!"),
                new JLabel("Load Game"),
                new JLabel("Settings"),
                new JLabel("How to play"),
                new JLabel("Credits"),
                new JLabel(EXIT))
        );
        // setting layout
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));
        pnMenu.setBackground(Color.PINK);
        jlWelcome.setAlignmentX(CENTER_ALIGNMENT);
        jlWelcome.setFont(new Font(FONT, STYLE, 80));

        // assemble this frame
        pnMenu.add(jlWelcome);
        pnMenu.add(Box.createVerticalGlue());
        setLabelsAndAttachToPanel(pnMenu, labels, pnOuterMost, cardLayout);
        return pnMenu;
    }

    private static JPanel configurePanelCredits(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
    }

    private static JPanel configurePanelPlay(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
    }

    private static JPanel configurePanelSettings(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
    }

    private static JPanel configurePanelLoad(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
    }

    private static JPanel configurePanelNewGame(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
    }

    public static JPanel configurePanelExit(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Exit");

        var pnExit = new JPanel();
        var pnOption = new JPanel();
        var jlWelcome = new JLabel("Chaps Challenge!");
        var jlText = new JLabel("Are you sure you want to exit?");
        var jlYes = new JLabel("Yes"){{
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(Color.RED);}
                public void mouseExited(MouseEvent e) {setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {System.exit(0);}
        });}};
        var jlNo = new JLabel("No"){{
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(Color.GREEN);}
                public void mouseExited(MouseEvent e) {setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {cardLayout.show(pnOuterMost, MENU);}
        });}};

        // setting layout
        pnExit.setLayout(new BoxLayout(pnExit, BoxLayout.Y_AXIS));
        pnOption.setLayout(new BoxLayout(pnOption, BoxLayout.X_AXIS));
        pnExit.setBackground(Color.PINK);
        pnOption.setBackground(Color.PINK);
        jlWelcome.setAlignmentX(CENTER_ALIGNMENT);
        jlText.setAlignmentX(CENTER_ALIGNMENT);
        jlWelcome.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        setFontAll(FONT, STYLE, TEXT_SIZE, jlText, jlYes, jlNo);

        // assemble options panel
        pnOption.add(Box.createHorizontalGlue());
        pnOption.add(jlNo);
        pnOption.add(Box.createHorizontalGlue());
        pnOption.add(jlYes);
        pnOption.add(Box.createHorizontalGlue());
        // assemble Exit panel
        pnExit.add(jlWelcome);
        pnExit.add(Box.createVerticalGlue());
        pnExit.add(jlText);
        pnExit.add(Box.createVerticalGlue());
        pnExit.add(pnOption);
        pnExit.add(Box.createVerticalGlue());
        return pnExit;
    }




    //================================================================================================================//
    //=========================================== Helper Method ======================================================//
    //================================================================================================================//

    private static void setFontAll(String font, int style, int size, JLabel... labels) {
        Arrays.stream(labels).forEach(label -> label.setFont(new Font(font, style, size)));
    }

    public static void setLabelsAndAttachToPanel(JPanel pnToAttach, List<JLabel> labels,
                                                 JPanel pnOuterMost, CardLayout cardLayout) {
        labels.forEach(l->{
            l.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){l.setForeground(Color.RED);}
                public void mouseExited(MouseEvent e) {l.setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {
                    // trigger panel switching
                    cardLayout.show(pnOuterMost, l.getText());
                }
            });
            l.setAlignmentX(CENTER_ALIGNMENT);
            l.setFont(new Font(FONT, STYLE, 40));
            pnToAttach.add(l);
        });
    }


/*
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
    }*/
}
