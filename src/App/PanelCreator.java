package App;

import App.tempDomain.Game;
import Renderer.Renderer;
import Renderer.TexturePack;
import Renderer.TexturePack.Images;
import Renderer.tempDomain.Tiles.Tile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
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
    private static final int TEXT_SIZE_1 = 40;
    private static final int TEXT_SIZE_2 = 30;

    public static final String MENU        = "Menu";
    public static final String NEW_GAME    = "Start New Game!";
    public static final String LOAD_GAME   = "Load Game";
    public static final String SETTINGS    = "Settings";
    public static final String HOW_TO_PLAY = "How to play";
    public static final String CREDITS     = "Credits";
    public static final String EXIT        = "Exit";

    public static final String GAME        = "Game";
    public static final String DEATH       = "Death";
    public static final String VICTORY     = "Victory";

    /**
     * Should never be called.
     */
    private PanelCreator(){}

    /**
     * Creates the menu screen.
     *
     * @param app The App object.
     * @param pnOuterMost The outermost panel for everything to assemble to.
     * @param cardLayout The card layout for toggling between scenes.
     * @param keyBindings The list of key bindings.
     * @param keyNames The list of action names.
     *
     * @return The menu screen.
     */
    public static JPanel configureMenuScreen(App app, JPanel pnOuterMost, CardLayout cardLayout, List<String> keyBindings, List<String> keyNames){
        // components to be added to the shell
        JPanel pnMenu      = configurePanelMenu(pnOuterMost, cardLayout);
        JPanel pnNewGame   = configurePanelNewGame(app, pnOuterMost, cardLayout);
        JPanel pnLoad      = configurePanelLoad(pnOuterMost, cardLayout);
        JPanel pnSettings  = configurePanelSettings(pnOuterMost, cardLayout, app, keyBindings, keyNames);
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

    /**
     * Creates the Game Screen panel.
     *
     * @param app         The App object.
     * @param pnOuterMost The outermost panel for everything to assemble to.
     * @param cardLayout  The card layout for toggling between scenes.
     * @param r           The Renderer object.
     * @return The Game Screen panel.
     */
    public static JPanel configureGameScreen(App app, JPanel pnOuterMost, CardLayout cardLayout, Renderer r){
        // components to be added to the shell
        JPanel pnGameWindow  = configurePanelGame(pnOuterMost, cardLayout, app, r);
        JPanel pnGameDeath   = configurePanelDeath(pnOuterMost, cardLayout);
        JPanel pnGameVictory = configurePanelVictory(pnOuterMost, cardLayout);

        // add components to the shell
        pnOuterMost.setLayout(cardLayout);
        pnOuterMost.add(pnGameWindow, GAME);
        pnOuterMost.add(pnGameDeath, DEATH);
        pnOuterMost.add(pnGameVictory, VICTORY);
        return pnOuterMost;
    }


    //================================================================================================================//
    //============================================= Menu Panels ======================================================//
    //================================================================================================================//

    public static JPanel configurePanelMenu(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Menu");

        JPanel pnMenu = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Images.getImage(Images.Background), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
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

    private static JPanel configurePanelNewGame(App app, JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: NewGame");

        var pnStartNew = new JPanel();
        var lbTitle = new JLabel("Starting new game...");
        var lbConfirm = createActionLabel("Confirm", app::transitionToGameScreen);

        // setting layout
        pnStartNew.setLayout(new BoxLayout(pnStartNew, BoxLayout.Y_AXIS));
        pnStartNew.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbConfirm);
        lbTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        lbConfirm.setFont(new Font(FONT, STYLE, TEXT_SIZE_1));
        // assemble this panel
        addAll(pnStartNew, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbConfirm);
        return pnStartNew;
    }

    private static JPanel configurePanelLoad(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Load");

        var pnLoad = new JPanel();
        var lbTitle = new JLabel("Load and Resume Saved Games");
        var lbConfirm = createBackToMenuLabel("Confirm", pnOuterMost, cardLayout, Color.RED);

        // setting layout
        pnLoad.setLayout(new BoxLayout(pnLoad, BoxLayout.Y_AXIS));
        pnLoad.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbConfirm);
        lbTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        lbConfirm.setFont(new Font(FONT, STYLE, TEXT_SIZE_1));

        // assemble this panel
        pnLoad.add(lbTitle);
        pnLoad.add(Box.createVerticalGlue());
        // add Loading box here?
        pnLoad.add(Box.createVerticalGlue());
        pnLoad.add(lbConfirm);
        return pnLoad;
    }

    private static JPanel configurePanelSettings(JPanel pnOuterMost, CardLayout cardLayout, App app, List<String> keyBindings, List<String> keyNames) {
        System.out.println("Configuring: Settings");

        JPanel pnSettings = new JPanel();
        JPanel pnMiddle = new JPanel();
        JPanel pnBindingL = new JPanel();
        JPanel pnBindingR = new JPanel();
        JPanel pnTexturePack = new JPanel();

        JLabel lbTitle = new JLabel("Settings");
        JLabel lbConfirm = createBackToMenuLabel("Confirm", pnOuterMost, cardLayout, Color.RED);
        JLabel lbTexturePack = new JLabel("Texture Pack");
        JLabel lbCurrentTexture = new JLabel(app.getRender().getCurrentTexturePack()+"");
        JLabel lbNextTexture = createActionLabel("  >>>", ()->{
            int newTexture = (app.getRender().getCurrentTexturePack().ordinal()+1)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            app.getRender().setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
        });
        JLabel lbPrevTexture = createActionLabel("<<<  ", ()->{
            int newTexture = (app.getRender().getCurrentTexturePack().ordinal()-1+TexturePack.values().length)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            app.getRender().setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
        });

        List<JLabel> lbsActionNames = new ArrayList<>();
        List<JLabel> lbsActionKeys = new ArrayList<>();
        for (int i = 0; i < keyBindings.size(); i++) {
            lbsActionNames.add(new JLabel(keyNames.get(i)));
            int finalI = i;
            lbsActionKeys.add(new JLabel((finalI < 6 ? "": "Ctrl + ") + keyBindings.get(finalI)){{
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e){
                        if (app.inSettingKeyMode()) return;
                        setForeground(Color.ORANGE);
                    }
                    public void mouseExited(MouseEvent e) {
                        if (app.inSettingKeyMode()) return;
                        setForeground(Color.BLACK);
                    }
                    public void mousePressed(MouseEvent e){
                        if (app.inSettingKeyMode()) return;
                        setForeground(Color.RED);
                        app.setIndexOfKeyToSet(finalI);
                    }
                });
            }});
        }

        app.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (! app.inSettingKeyMode()) return;
                var label = lbsActionKeys.get(app.indexOfKeyToSet());
                if (keyBindings.contains(KeyEvent.getKeyText(e.getKeyCode()))){
                    app.exitKeySettingMode();
                    label.setForeground(Color.BLACK);
                    return;
                }
                keyBindings.set(app.indexOfKeyToSet(), KeyEvent.getKeyText(e.getKeyCode()));
                label.setText((app.indexOfKeyToSet() < 6 ? "": "Ctrl + " ) + KeyEvent.getKeyText(e.getKeyCode()));
                label.setForeground(Color.BLACK);
                app.exitKeySettingMode();
            }
        });

        // setting layout
        setAllBoxLayout(BoxLayout.Y_AXIS, pnSettings, pnBindingL, pnBindingR);
        setAllBoxLayout(BoxLayout.X_AXIS, pnMiddle, pnTexturePack);
        pnBindingL.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        pnBindingR.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        setAllOpaque(false, pnMiddle, pnBindingL, pnBindingR, pnTexturePack);
        pnSettings.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbConfirm);
        lbTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        lbConfirm.setFont(new Font(FONT, STYLE, TEXT_SIZE_1));
        setAllFont(FONT, STYLE, TEXT_SIZE_2, lbsActionNames.toArray(new JLabel[0]));
        setAllFont(FONT, STYLE, TEXT_SIZE_2, lbsActionKeys.toArray(new JLabel[0]));
        setAllFont(FONT, STYLE, TEXT_SIZE_2, lbTexturePack,lbNextTexture, lbPrevTexture, lbCurrentTexture);
        // assemble this panel
        addAll(pnTexturePack, lbPrevTexture, lbCurrentTexture, lbNextTexture);
        pnBindingL.add(lbTexturePack);
        pnBindingR.add(pnTexturePack);
        lbsActionNames.forEach(pnBindingL::add);
        lbsActionKeys.forEach(pnBindingR::add);
        addAll(pnMiddle, Box.createHorizontalGlue(), pnBindingL, pnBindingR, Box.createHorizontalGlue());
        addAll(pnSettings, lbTitle, Box.createVerticalGlue(), pnMiddle, Box.createVerticalGlue(), lbConfirm);
        return pnSettings;
    }

    private static JPanel configurePanelHowToPlay(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: HowToPlay");

        JPanel pnHowToPlay = new JPanel();
        JLabel lbTitle = new JLabel("How to play");
        JLabel lbBack = createBackToMenuLabel("Back", pnOuterMost, cardLayout, Color.RED);

        // setting layout
        pnHowToPlay.setLayout(new BoxLayout(pnHowToPlay, BoxLayout.Y_AXIS));
        pnHowToPlay.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbBack);
        lbTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        lbBack.setFont(new Font(FONT, STYLE, TEXT_SIZE_1));
        // assemble this panel
        addAll(pnHowToPlay, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbBack);
        return pnHowToPlay;
    }

    private static JPanel configurePanelCredits(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Credits");

        JPanel pnCredits = new JPanel();
        JLabel lbTitle = new JLabel("Credits");
        JLabel lbBack = createBackToMenuLabel("Back", pnOuterMost, cardLayout, Color.RED);
        JLabel[] credits = new JLabel[]{
                new JLabel("App: Jeff"),
                new JLabel("Domain: Matty"),
                new JLabel("Fuzz: Ray"),
                new JLabel("Persistency: Ben"),
                new JLabel("Recorder: Jayden"),
                new JLabel("Renderer: Loki")
        };

        // setting layout
        pnCredits.setLayout(new BoxLayout(pnCredits, BoxLayout.Y_AXIS));
        pnCredits.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbBack);
        setAllAlignmentX(CENTER_ALIGNMENT, credits);
        lbTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        lbBack.setFont(new Font(FONT, STYLE, TEXT_SIZE_1));
        setAllFont(FONT, STYLE, TEXT_SIZE_1, credits);
        // assemble this panel
        addAll(pnCredits, lbTitle, Box.createVerticalGlue());
        addAll(pnCredits, credits);
        addAll(pnCredits, Box.createVerticalGlue(), lbBack);
        return pnCredits;
    }

    public static JPanel configurePanelExit(JPanel pnOuterMost, CardLayout cardLayout) {
        System.out.println("Configuring: Exit");

        JPanel pnExit = new JPanel();
        JPanel pnOption = new JPanel();
        JLabel lbTitle = new JLabel("Chaps Challenge!");
        JLabel lbMessage = new JLabel("Are you sure you want to exit?");
        JLabel lbYes = createActionLabel("Yes", ()->System.exit(0));
        JLabel lbNo = createBackToMenuLabel("No", pnOuterMost, cardLayout, Color.GREEN);

        // setting layout
        pnExit.setLayout(new BoxLayout(pnExit, BoxLayout.Y_AXIS));
        pnOption.setLayout(new BoxLayout(pnOption, BoxLayout.X_AXIS));
        lbTitle.setFont(new Font(FONT, STYLE, TITLE_SIZE));
        setAllFont(FONT, STYLE, TEXT_SIZE_1, lbMessage, lbYes, lbNo);
        setAllBackground(Color.PINK, pnExit, pnOption);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbMessage, lbYes, lbNo);
        // combine all components
        addAll(pnOption, Box.createHorizontalGlue(), lbNo, Box.createHorizontalGlue(), lbYes, Box.createHorizontalGlue());
        addAll(pnExit, lbTitle, Box.createVerticalGlue(), lbMessage, Box.createVerticalGlue(), pnOption, Box.createVerticalGlue());
        return pnExit;
    }


    //================================================================================================================//
    //============================================= Game Panels ======================================================//
    //================================================================================================================//

    private static JPanel configurePanelGame(JPanel pnOuterMost, CardLayout cardLayout,App app, Renderer mazeRender) {
        app.addKeyListener(app.getController());
        mazeRender.setFocusable(true);
//        app.setTimer(new Timer(34, unused -> {
//            assert SwingUtilities.isEventDispatchThread();
////            app.getGame().pingAll();
//            mazeRender.repaint();
//        }));

        // outermost panel
        var pnGame = new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage img =  Images.Pattern.getImg();
                int size = mazeRender.getPatternSize();
                for (int i = 0; i < this.getWidth(); i+=size) {
                    for (int j = 0; j < this.getHeight(); j+=size) {
                        g.drawImage(img, i, j, size,size,null);
                    }
                }
            }
        };
        // 3 panels on top of outermost panel: left/mid/right
        JPanel pnStatus = new JPanel();
        JPanel pnMaze = new JPanel();
        JPanel pnRight = new JPanel();
        // inner panels for panels: left/mid/right
        JPanel pnStatusTop = new JPanel();
        JPanel pnStatusMid = new JPanel();
        JPanel pnStatusBot = new JPanel();
        JPanel pnInventory = new JPanel();
        JPanel pnInventories = new JPanel();
        // status bars
        JLabel lbLevelTitle = new JLabel("Level");
        JLabel lbLevel = new JLabel(app.getGame().getCurrentLevel()+"");
        JLabel lbTimerTitle = new JLabel("Time Left");
        JLabel lbTimer = new JLabel("120");
        JLabel lbTreasuresTitle = new JLabel("Treasures");
        JLabel lbTreasures = new JLabel(app.getGame().getTreasuresLeft()+"");
        JLabel lbPause = createActionLabel("Menu", app::transitionToMenuScreen);
        JLabel lbInventoryTitle = new JLabel("Inventory");

        // setting layout
        pnGame.setLayout(new BoxLayout(pnGame, BoxLayout.X_AXIS));
        setAllBoxLayout(BoxLayout.Y_AXIS, pnStatus, pnRight, pnStatusTop, pnStatusMid, pnStatusBot, pnInventory);
        pnMaze.setLayout(new GridBagLayout());
        pnInventories.setLayout(new GridLayout(4,2));
        pnStatus.setMaximumSize(new Dimension(200, 1000));
        pnRight.setMaximumSize(new Dimension(200, 1000));
        setSize(mazeRender, 700, 700, 600, 600, 800, 800);
        setSize(pnMaze, 700, 700, 600, 600, 800, 800);
        int size = 75;
        setSize(pnInventory, size*2, size*4, size*2, size*4, size*2, size*4);
        setAllFont(FONT, STYLE, TEXT_SIZE_1, lbLevelTitle, lbPause, lbLevel, lbTimerTitle, lbTimer, lbTreasuresTitle,
                lbTreasures, lbInventoryTitle);
        setAllOpaque(false, pnStatus, pnMaze, pnRight, pnStatusTop, pnStatusMid, pnStatusBot, pnInventory, pnInventories);

        List<Tile> inventory = new Game().getInventory();
        for(int i = 0; i < 8; i++) {
            int finalX = i;
            pnInventories.add(new JLabel(){
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    BufferedImage img =  Images.Empty_tile.getImg();
                    g.drawImage(img, 0, 0, getWidth(),getHeight(),null);
                    int size = Math.min(getWidth(), getHeight());
                    if (finalX >= inventory.size()) return;
                    g.drawImage(Images.getImage(inventory.get(finalX)), (getWidth()-size)/2, (getHeight()-size)/2, size,size,null);
                }
            });
        }

        addAll(pnStatusTop, lbLevelTitle, lbLevel);
        addAll(pnStatusMid, lbTimerTitle, lbTimer);
        addAll(pnStatusBot, lbTreasuresTitle, lbTreasures);
        addAll(pnInventory, lbInventoryTitle, pnInventories);
        addAll(pnStatus, Box.createVerticalGlue(), pnStatusTop, Box.createVerticalGlue(), pnStatusMid,
                        Box.createVerticalGlue(), pnStatusBot, Box.createVerticalGlue());
        pnMaze.add(mazeRender);
        addAll(pnRight, Box.createVerticalGlue(), lbPause, Box.createVerticalGlue(), pnInventory, Box.createVerticalGlue());
        addAll(pnGame, Box.createHorizontalGlue(), pnStatus, Box.createHorizontalGlue(), pnMaze, Box.createHorizontalGlue(),
                        pnRight, Box.createHorizontalGlue());
        return pnGame;
    }



    private static JPanel configurePanelVictory(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
    }

    private static JPanel configurePanelDeath(JPanel pnOuterMost, CardLayout cardLayout) {
        return new JPanel();
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

    private static void setAllAlignmentY(float alignment, JComponent... components) {
        Arrays.stream(components).forEach(label -> label.setAlignmentY(alignment));
    }

    private static void setAllBoxLayout(int axis, JPanel... pns) {
        Arrays.stream(pns).forEach(pn -> pn.setLayout(new BoxLayout(pn, axis)));
    }

    private static void setAllOpaque(boolean b, JComponent... components) {
        Arrays.stream(components).forEach(c -> c.setOpaque(b));
    }
    private static void setSize(JComponent Component, int pX, int pY, int miX, int miY, int maX, int maY) {
        Component.setPreferredSize(new Dimension(pX, pY));
        Component.setMinimumSize(new Dimension(miX, miY));
        Component.setMaximumSize(new Dimension(maX, maY));
    }

    private static void addAll(JComponent parent, Component... components) {
        Arrays.stream(components).forEach(parent::add);
    }

    private static JLabel createBackToMenuLabel(String text, JPanel pnOuterMost, CardLayout cardLayout, Color color) {
        return new JLabel(text) {{
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(color);}
                public void mouseExited(MouseEvent e) {setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {cardLayout.show(pnOuterMost, MENU);}
        });}};
    }

    private static JLabel createActionLabel(String name, Runnable runnable) {
        return new JLabel(name) {{
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(Color.RED);}
                public void mouseExited(MouseEvent e) {setForeground(Color.BLACK);}
                public void mousePressed(MouseEvent e) {
                    runnable.run();
                }
            });
        }};
    }
}
