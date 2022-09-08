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
    private static final int SUBTITLE_SIZE = 40;
    private static final int TEXT_SIZE = 30;

    private static final int TITLE = 1;
    private static final int SUBTITLE = 2;
    private static final int TEXT = 3;

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
     * @param app         The App object.
     * @param pnOuterMost The outermost panel for everything to assemble to.
     * @param cardLayout  The card layout for toggling between scenes.
     * @return The menu screen.
     */
    public static JPanel configureMenuScreen(App app, JPanel pnOuterMost, CardLayout cardLayout){
        // components to be added to the shell
        JPanel pnMenu      = configurePanelMenu(pnOuterMost, cardLayout, app);
        JPanel pnNewGame   = configurePanelNewGame(app);
        JPanel pnLoad      = configurePanelLoad(app);
        JPanel pnSettings  = configurePanelSettings(app, app.getActionNames(), app.getActionKeyBindings());
        JPanel pnHowToPlay = configurePanelHowToPlay(app);
        JPanel pnCredits   = configurePanelCredits(app);
        JPanel pnExit      = configurePanelExit(app);

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
        JPanel pnGameWindow  = configurePanelGame(app, r);
        JPanel pnGameDeath   = configurePanelDeath();
        JPanel pnGameVictory = configurePanelVictory();

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

    public static JPanel configurePanelMenu(JPanel pnOuterMost, CardLayout cardLayout, App app) {
        System.out.println("Configuring: Menu");

        Renderer r = app.getRender();
        JPanel pnMenu = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(Images.getImage(Images.Background), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };

        List<JLabel> labels = List.of(
                createActionLabel(NEW_GAME, r, SUBTITLE, ()->cardLayout.show(pnOuterMost, NEW_GAME)),
                createActionLabel(LOAD_GAME, r, SUBTITLE, ()->cardLayout.show(pnOuterMost, LOAD_GAME)),
                createActionLabel(SETTINGS, r, SUBTITLE, ()->cardLayout.show(pnOuterMost, SETTINGS)),
                createActionLabel(HOW_TO_PLAY, r, SUBTITLE, ()->cardLayout.show(pnOuterMost, HOW_TO_PLAY)),
                createActionLabel(CREDITS, r, SUBTITLE, ()->cardLayout.show(pnOuterMost, CREDITS)),
                createActionLabel(EXIT, r, SUBTITLE, ()->cardLayout.show(pnOuterMost, EXIT))
        );
        // setting layout
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));
        pnMenu.add(Box.createVerticalGlue());
        labels.forEach(l->{
            l.setAlignmentX(CENTER_ALIGNMENT);
            pnMenu.add(l);
        });
        return pnMenu;
    }

    private static JPanel configurePanelNewGame(App app) {
        System.out.println("Configuring: NewGame");

        JPanel pnStartNew = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("Starting new game...", app.getRender(), TITLE);
        JLabel lbConfirm = createActionLabel("Confirm", app.getRender(), SUBTITLE, app::transitionToGameScreen);

        // setting layout
        pnStartNew.setLayout(new BoxLayout(pnStartNew, BoxLayout.Y_AXIS));
        pnStartNew.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbConfirm);
        // assemble this panel
        addAll(pnStartNew, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbConfirm);
        return pnStartNew;
    }

    private static JPanel configurePanelLoad(App app) {
        System.out.println("Configuring: Load");

        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("Load and Resume Saved Games", app.getRender(), TITLE);
        JLabel lbConfirm = createActionLabel("Confirm", app.getRender(),SUBTITLE, app::transitionToMenuScreen);

        // setting layout
        pnLoad.setLayout(new BoxLayout(pnLoad, BoxLayout.Y_AXIS));
        pnLoad.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbConfirm);

        // assemble this panel
        pnLoad.add(lbTitle);
        pnLoad.add(Box.createVerticalGlue());
        // add Loading box here?
        pnLoad.add(Box.createVerticalGlue());
        pnLoad.add(lbConfirm);
        return pnLoad;
    }

    private static JPanel configurePanelSettings(App app, List<String> actionNames, List<String> actionKeyBindings) {
        System.out.println("Configuring: Settings");

        Renderer r = app.getRender();
        JPanel pnSettings = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JPanel pnMiddle = new JPanel();
        JPanel pnBindingL = new JPanel();
        JPanel pnBindingR = new JPanel();
        JPanel pnTexturePack = new JPanel();

        JLabel lbTitle = createLabel("Settings", r, TITLE);
        JLabel lbConfirm = createActionLabel("Confirm", r, SUBTITLE, app::transitionToMenuScreen);
        JLabel lbTexturePack = createLabel("Texture Pack", r, TEXT);
        JLabel lbCurrentTexture = createLabel(r.getCurrentTexturePack()+"" , r, TEXT);
        JLabel lbNextTexture = createActionLabel("  >>>", app.getRender(),TEXT, ()->{
            int newTexture = (app.getRender().getCurrentTexturePack().ordinal()+1)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            app.getRender().setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
            pnSettings.repaint();
        });
        JLabel lbPrevTexture = createActionLabel("<<<  ", app.getRender(),SUBTITLE, ()->{
            int newTexture = (app.getRender().getCurrentTexturePack().ordinal()-1+TexturePack.values().length)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            app.getRender().setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
            pnSettings.repaint();
        });

        List<JLabel> lbsActionNames = new ArrayList<>();
        List<JLabel> lbsActionKeys = new ArrayList<>();
        for (int i = 0; i < actionKeyBindings.size(); i++) {
            lbsActionNames.add(createLabel(actionNames.get(i), r, TEXT));
            int finalI = i;
            lbsActionKeys.add(new JLabel((finalI < 6 ? "": "Ctrl + ") + actionKeyBindings.get(finalI)){{
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
                if (actionKeyBindings.contains(KeyEvent.getKeyText(e.getKeyCode()))){
                    app.exitKeySettingMode();
                    label.setForeground(Color.BLACK);
                    return;
                }
                actionKeyBindings.set(app.indexOfKeyToSet(), KeyEvent.getKeyText(e.getKeyCode()));
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
        setAllFont(FONT, STYLE, TEXT_SIZE, lbsActionKeys.toArray(new JLabel[0]));
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

    private static JPanel configurePanelHowToPlay(App app) {
        System.out.println("Configuring: HowToPlay");

        JPanel pnHowToPlay = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("How To Play", app.getRender(), TITLE);
        JLabel lbBack = createActionLabel("Back", app.getRender(),SUBTITLE, app::transitionToMenuScreen);

        // setting layout
        pnHowToPlay.setLayout(new BoxLayout(pnHowToPlay, BoxLayout.Y_AXIS));
        pnHowToPlay.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbBack);
        // assemble this panel
        addAll(pnHowToPlay, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbBack);
        return pnHowToPlay;
    }

    private static JPanel configurePanelCredits(App app) {
        System.out.println("Configuring: Credits");

        JPanel pnCredits = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("Credits", app.getRender(), TITLE);
        JLabel lbBack = createActionLabel("Back", app.getRender(),SUBTITLE, app::transitionToMenuScreen);
        JLabel[] credits = new JLabel[]{
                createLabel("App: Jeff", app.getRender(), TEXT),
                createLabel("Domain: Matty", app.getRender(), TEXT),
                createLabel("Fuzz: Ray", app.getRender(), TEXT),
                createLabel("Persistency: Ben", app.getRender(), TEXT),
                createLabel("Recorder: Jayden", app.getRender(), TEXT),
                createLabel("Renderer: Loki", app.getRender(), TEXT),
        };

        // setting layout
        pnCredits.setLayout(new BoxLayout(pnCredits, BoxLayout.Y_AXIS));
        pnCredits.setBackground(Color.PINK);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbBack);
        setAllAlignmentX(CENTER_ALIGNMENT, credits);
        // assemble this panel
        addAll(pnCredits, lbTitle, Box.createVerticalGlue());
        addAll(pnCredits, credits);
        addAll(pnCredits, Box.createVerticalGlue(), lbBack);
        return pnCredits;
    }

    public static JPanel configurePanelExit(App app) {
        System.out.println("Configuring: Exit");

        JPanel pnExit = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JPanel pnOption = new JPanel();
        JLabel lbTitle = createLabel("Chaps Challenge!", app.getRender(), TITLE);
        JLabel lbMessage = createLabel("Are you sure you want to exit?", app.getRender(), SUBTITLE);
        JLabel lbYes = createActionLabel("Yes", app.getRender(),SUBTITLE, ()->System.exit(0));
        JLabel lbNo = createActionLabel("No", app.getRender(),SUBTITLE, app::transitionToMenuScreen);

        // setting layout
        pnExit.setLayout(new BoxLayout(pnExit, BoxLayout.Y_AXIS));
        pnOption.setLayout(new BoxLayout(pnOption, BoxLayout.X_AXIS));
        setAllBackground(Color.PINK, pnExit, pnOption);
        setAllAlignmentX(CENTER_ALIGNMENT, lbTitle, lbMessage, lbYes, lbNo);
        pnOption.setOpaque(false);
        // combine all components
        addAll(pnOption, Box.createHorizontalGlue(), lbNo, Box.createHorizontalGlue(), lbYes, Box.createHorizontalGlue());
        addAll(pnExit, lbTitle, Box.createVerticalGlue(), lbMessage, Box.createVerticalGlue(), pnOption, Box.createVerticalGlue());
        return pnExit;
    }


    //================================================================================================================//
    //============================================= Game Panels ======================================================//
    //================================================================================================================//

    private static JPanel configurePanelGame(App app, Renderer mazeRender) {
        // outermost panel
        JPanel pnGame = createRepeatableBackgroundPanel(Images.Pattern, mazeRender);
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
        JLabel lbLevelTitle = createLabel("Level", mazeRender, SUBTITLE);
        JLabel lbLevel      = createLabel(app.getGame().getCurrentLevel()+"" , mazeRender, SUBTITLE);
        JLabel lbTimerTitle = createLabel("Time Left", mazeRender, SUBTITLE);
        JLabel lbTimer      = createLabel(app.getTimeLeft()+"" , mazeRender, SUBTITLE);
        JLabel lbTreasuresTitle = createLabel("Treasures", mazeRender, SUBTITLE);
        JLabel lbTreasures = createLabel(app.getGame().getTreasuresLeft()+"", mazeRender, SUBTITLE);
        JLabel lbPause      = createActionLabel("Menu", app.getRender(),SUBTITLE, app::transitionToMenuScreen);
        JLabel lbInventoryTitle = createLabel("Inventory", mazeRender, SUBTITLE);

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


    private static JPanel configurePanelVictory() {
        return new JPanel();
    }

    private static JPanel configurePanelDeath() {
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


    private static JPanel createRepeatableBackgroundPanel(Images img, Renderer render) {
        return new JPanel(){
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                int size = render.getPatternSize();
                for (int i = 0; i < this.getWidth(); i+=size) {
                    for (int j = 0; j < this.getHeight(); j+=size) {
                        g.drawImage(img.getImg(), i, j, size,size,null);
                    }
                }
            }
        };
    }

    /**
     * This method is used to create a JLabel with texture-dynamic fonts.
     * @param name the name of the label
     * @param r the renderer object
     * @param textSize size of the text, should use the constants TITLE, SUBTITLE, and TEXT to specify
     * @return the JLabel
     */
    private static JLabel createLabel(String name, Renderer r, int textSize) {
        return new JLabel(name) {{
            setForeground(r.getCurrentTexturePack().getColorDefault());}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                TexturePack tp = r.getCurrentTexturePack();
                int size = textSize;
                if (size == TITLE) size = tp.getTitleSize();
                else if (size == SUBTITLE) size = tp.getSubtitleSize();
                else if (size == TEXT) size = tp.getTextSize();
                setFont(new Font(tp.getFont(), tp.getStyle(), size));
                setForeground(tp.getColorDefault());
            }
        };
    }


    private static JLabel createActionLabel(String name, Renderer r, int textSize, Runnable runnable) {
        return new JLabel(name) {{
            setForeground(r.getCurrentTexturePack().getColorDefault());
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(r.getCurrentTexturePack().getColorHover());}
                public void mouseExited(MouseEvent e) {setForeground(r.getCurrentTexturePack().getColorDefault());}
                public void mousePressed(MouseEvent e) {
                    runnable.run();
                }
            });}
            public void paintComponent(Graphics g) {
                TexturePack tp = r.getCurrentTexturePack();
                int size = textSize;
                if (size == TITLE) size = tp.getTitleSize();
                else if (size == SUBTITLE) size = tp.getSubtitleSize();
                else if (size == TEXT) size = tp.getTextSize();
                setFont(new Font(tp.getFont(), tp.getStyle(), size));
                super.paintComponent(g);
            }
        };
    }
}

