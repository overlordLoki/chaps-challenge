package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
    public static final String NEW_GAME    = "Start New Game!";
    public static final String LOAD_GAME   = "Load Game";
    public static final String SETTINGS    = "Settings";
    public static final String HOW_TO_PLAY = "How to play";
    public static final String CREDITS     = "Credits";
    public static final String EXIT        = "Exit";

    /**
     * Should never be called directly.
     */
    private PanelCreator(){}

    public static JPanel configureMenuScreen(JPanel pnOuterMost, CardLayout cardLayout){
        // components to be added to the shell
        JPanel pnMenu      = configurePanelMenu(pnOuterMost, cardLayout);
        JPanel pnNewGame   = configurePanelNewGame(pnOuterMost, cardLayout);
        JPanel pnLoad      = configurePanelLoad(pnOuterMost, cardLayout);
        JPanel pnSettings  = configurePanelSettings(pnOuterMost, cardLayout);
        JPanel pnHowToPlay = configurePanelHowToPlay(pnOuterMost, cardLayout);
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
                new JLabel(NEW_GAME),
                new JLabel(LOAD_GAME),
                new JLabel(SETTINGS),
                new JLabel(HOW_TO_PLAY),
                new JLabel(CREDITS),
                new JLabel(EXIT))
        );
        // setting layout
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));
        pnMenu.setBackground(Color.PINK);
        jlWelcome.setAlignmentX(CENTER_ALIGNMENT);
        jlWelcome.setFont(new Font(FONT, STYLE, 80));

        // assemble this panel
        pnMenu.add(jlWelcome);
        pnMenu.add(Box.createVerticalGlue());
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
            pnMenu.add(l);
        });
        return pnMenu;
    }

    private static JPanel configurePanelNewGame(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: NewGame");

        var pnStartNew = new JPanel();
        var jlTitle = new JLabel("Starting new game...");
        var jlConfirm = createBackToMenuLabel("Confirm", pnOuterMost, cardLayout, Color.RED);

        // setting layout
        pnStartNew.setLayout(new BoxLayout(pnStartNew, BoxLayout.Y_AXIS));
        pnStartNew.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, jlTitle, jlConfirm);
        jlTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        jlConfirm.setFont(new Font(FONT, STYLE, TEXT_SIZE));

        // assemble this panel
        pnStartNew.add(jlTitle);
        pnStartNew.add(Box.createVerticalGlue());
        // add image here?
        pnStartNew.add(Box.createVerticalGlue());
        pnStartNew.add(jlConfirm);
        return pnStartNew;
    }

    private static JPanel configurePanelLoad(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Load");

        var pnLoad = new JPanel();
        var jlTitle = new JLabel("Load and Resume Saved Games");
        var jlConfirm = createBackToMenuLabel("Confirm", pnOuterMost, cardLayout, Color.RED);

        // setting layout
        pnLoad.setLayout(new BoxLayout(pnLoad, BoxLayout.Y_AXIS));
        pnLoad.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, jlTitle, jlConfirm);
        jlTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        jlConfirm.setFont(new Font(FONT, STYLE, TEXT_SIZE));

        // assemble this panel
        pnLoad.add(jlTitle);
        pnLoad.add(Box.createVerticalGlue());
        // add Loading box here?
        pnLoad.add(Box.createVerticalGlue());
        pnLoad.add(jlConfirm);
        return pnLoad;
    }

    private static JPanel configurePanelSettings(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Settings");

        var pnSettings = new JPanel();
        var pnBindings = new JPanel();
        var jlTitle = new JLabel("Settings");
        var jlConfirm = createBackToMenuLabel("Confirm", pnOuterMost, cardLayout, Color.RED);

        // setting layout
        pnSettings.setLayout(new BoxLayout(pnSettings, BoxLayout.Y_AXIS));
        pnSettings.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, jlTitle, jlConfirm);
        jlTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        jlConfirm.setFont(new Font(FONT, STYLE, TEXT_SIZE));

        // assemble this panel
        pnSettings.add(jlTitle);
        pnSettings.add(Box.createVerticalGlue());
        // add keybinding labels here
        pnSettings.add(Box.createVerticalGlue());
        pnSettings.add(jlConfirm);
        return pnSettings;
    }

    private static JPanel configurePanelHowToPlay(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: HowToPlay");

        var pnHowToPlay = new JPanel();
        var jlTitle = new JLabel("How to play");
        var jlBack = createBackToMenuLabel("Back", pnOuterMost, cardLayout, Color.RED);

        // setting layout
        pnHowToPlay.setLayout(new BoxLayout(pnHowToPlay, BoxLayout.Y_AXIS));
        pnHowToPlay.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, jlTitle, jlBack);
        jlTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        jlBack.setFont(new Font(FONT, STYLE, TEXT_SIZE));

        // assemble this panel
        pnHowToPlay.add(jlTitle);
        pnHowToPlay.add(Box.createVerticalGlue());
        // add how to play text here
        pnHowToPlay.add(Box.createVerticalGlue());
        pnHowToPlay.add(jlBack);
        return pnHowToPlay;
    }

    private static JPanel configurePanelCredits(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Credits");

        var pnCredits = new JPanel();
        var jlTitle = new JLabel("Credits");
        var jlBack = createBackToMenuLabel("Back", pnOuterMost, cardLayout, Color.RED);

        List<JLabel> credits = new ArrayList<>(List.of(
                new JLabel("App: Jeff"),
                new JLabel("Domain: Madhi"),
                new JLabel("Fuzz: Ray"),
                new JLabel("Persistency: Ben"),
                new JLabel("Recorder: Jayden"),
                new JLabel("Renderer: Loki")
        ));

        // setting layout
        pnCredits.setLayout(new BoxLayout(pnCredits, BoxLayout.Y_AXIS));
        pnCredits.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, jlTitle, jlBack);
        jlTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        jlBack.setFont(new Font(FONT, STYLE, TEXT_SIZE));

        // assemble this panel
        pnCredits.add(jlTitle);
        pnCredits.add(Box.createVerticalGlue());
        IntStream.range(0, credits.size()).forEach(i -> {
            JLabel credit = credits.get(i);
            credit.setFont(new Font(FONT, STYLE, TEXT_SIZE));
            setAllAlignmentX(CENTER_ALIGNMENT, credits.toArray(new JLabel[0]));
            pnCredits.add(credit);
        });
        credits.forEach(pnCredits::add);
        pnCredits.add(Box.createVerticalGlue());
        pnCredits.add(jlBack);




        return pnCredits;
    }

    public static JPanel configurePanelExit(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Exit");

        var pnExit = new JPanel();
        var pnOption = new JPanel();
        var jlTitle = new JLabel("Chaps Challenge!");
        var jlMessage = new JLabel("Are you sure you want to exit?");
        var jlYes = new JLabel("Yes"){{
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(Color.RED);}
                public void mouseExited(MouseEvent e) {setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {System.exit(0);}
        });}};
        var jlNo = createBackToMenuLabel("No", pnOuterMost, cardLayout, Color.GREEN);

        // setting layout
        pnExit.setLayout(new BoxLayout(pnExit, BoxLayout.Y_AXIS));
        pnOption.setLayout(new BoxLayout(pnOption, BoxLayout.X_AXIS));
        jlTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        setAllFont(FONT, STYLE, TEXT_SIZE, jlMessage, jlYes, jlNo);
        setAllBackground(Color.PINK, pnExit, pnOption);
        setAllAlignmentX(CENTER_ALIGNMENT, jlTitle, jlMessage, jlYes, jlNo);

        // assemble options panel
        pnOption.add(Box.createHorizontalGlue());
        pnOption.add(jlNo);
        pnOption.add(Box.createHorizontalGlue());
        pnOption.add(jlYes);
        pnOption.add(Box.createHorizontalGlue());
        // assemble Exit panel
        pnExit.add(jlTitle);
        pnExit.add(Box.createVerticalGlue());
        pnExit.add(jlMessage);
        pnExit.add(Box.createVerticalGlue());
        pnExit.add(pnOption);
        pnExit.add(Box.createVerticalGlue());
        return pnExit;
    }

    //================================================================================================================//
    //=========================================== Helper Method ======================================================//
    //================================================================================================================//

    private static void setAllFont(String font, int style, int size, JLabel... labels) {
        Arrays.stream(labels).forEach(l -> l.setFont(new Font(font, style, size)));
    }
    private static void setAllBackground(Color color, JPanel... labels) {
        Arrays.stream(labels).forEach(p -> p.setBackground(color));
    }
    private static void setAllAlignmentX(float alignment, JLabel... labels) {
        Arrays.stream(labels).forEach(label -> label.setAlignmentX(alignment));
    }

    private static void setAllAlignmentY(float alignment, JLabel... labels) {
        Arrays.stream(labels).forEach(label -> label.setAlignmentY(alignment));
    }

    private static JLabel createBackToMenuLabel(String text, JPanel pnOuterMost, CardLayout cardLayout, Color color) {
        return new JLabel(text) {{
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(color);}
                public void mouseExited(MouseEvent e) {setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {cardLayout.show(pnOuterMost, MENU);}
        });}};
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
