package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Component.CENTER_ALIGNMENT;

/**
 * Main class of the application. Includes the main method, GUI, and the main loop.
 *
 * @author Jeff Lin
 */
public class PanelCreator{
    private static final String FONT = "Agency FB";
    private static final int STYLE = Font.BOLD;

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

    private void menuScreen(){
        /*// shell to hold all the components
        var pnOuterMost = new JPanel();
        var cardLayout = new CardLayout();

        // components to be added to the shell
        var pnMenu = configurePanelMenu(pnOuterMost, cardLayout);
        var pnSettings = new JPanel();
        var pnHowToPlay = new JPanel();
        var pnCredits = new JPanel();
        var pnStart = new JPanel();
        var pnLoad = new JPanel();
        var pnExit = configurePanelExit(pnOuterMost, cardLayout);

        // add components to the shell
        pnOuterMost.setLayout(cardLayout);
        pnOuterMost.add(pnMenu, MENU);
        pnOuterMost.add(pnStart, "start");
        pnOuterMost.add(pnLoad, "load");
        pnOuterMost.add(pnSettings, "settings");
        pnOuterMost.add(pnHowToPlay, "howToPlay");
        pnOuterMost.add(pnCredits, "credits");
        pnOuterMost.add(pnExit, EXIT);

        this.setContentPane(pnOuterMost);
        cardLayout.show(pnOuterMost, MENU);
        setPreferredSize(new Dimension(1200, 600));
        pack();*/
    }

    //================================================================================================================//
    //============================================= Menu Stages ======================================================//
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
        pnExit.setBackground(Color.PINK);
        pnOption.setLayout(new BoxLayout(pnOption, BoxLayout.X_AXIS));
        pnOption.setBackground(Color.PINK);
        jlWelcome.setAlignmentX(CENTER_ALIGNMENT);
        jlWelcome.setFont(new Font(FONT, STYLE, 80));
        jlText.setAlignmentX(CENTER_ALIGNMENT);
        jlText.setFont(new Font(FONT, STYLE, 40));
        jlYes.setFont(new Font(FONT, STYLE, 40));
        jlNo.setFont(new Font(FONT, STYLE, 40));

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



    public static void setLabelsAndAttachToPanel(JPanel pnToAttach, List<JLabel> labels, JPanel pnOuterMost, CardLayout cardLayout) {
        labels.forEach(l->{
            l.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){l.setForeground(Color.RED);}
                public void mouseExited(MouseEvent e) {l.setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {
                    // trigger panel switching
                    System.out.println(l.getText());
                    cardLayout.show(pnOuterMost, l.getText());
                }
            });
            l.setAlignmentX(CENTER_ALIGNMENT);
            l.setFont(new Font(FONT, STYLE, 40));
            pnToAttach.add(l);
        });
    }
}
