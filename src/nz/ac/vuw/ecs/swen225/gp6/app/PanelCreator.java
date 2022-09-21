package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.renderer.Renderer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Package-private class for creating panels used by the App.
 *
 * @author Jeff Lin
 */
class PanelCreator{
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

    public static final String GAME     = "Game";
    public static final String LOOSE    = "Loose";
    public static final String VICTORY  = "Victory";

    /**
     * Should never be called.
     */
    private PanelCreator(){}

    /**
     * Creates the menu screen.
     *
     * @param app The App object.
     * @param backPanel The outermost panel for everything to assemble to.
     * @param cardLayout The card layout for toggling between scenes.
     */
    public static void configureMenuScreen(App app, JPanel backPanel, CardLayout cardLayout){
        // components to be added to the shell
        JPanel pnMenu      = configurePanelMenu(backPanel, cardLayout, app);
        JPanel pnNewGame   = configurePanelNewGame(app);
        JPanel pnLoad      = configurePanelLoad(app);
        JPanel pnSettings  = configurePanelSettings(app, app.getActionNames(), app.getActionKeyBindings());
        JPanel pnHowToPlay = configurePanelHowToPlay(app);
        JPanel pnCredits   = configurePanelCredits(app);
        JPanel pnExit      = configurePanelExit(app);

        // add components to the shell
        backPanel.setLayout(cardLayout);
        backPanel.add(pnMenu, MENU);
        backPanel.add(pnNewGame, NEW_GAME);
        backPanel.add(pnLoad, LOAD_GAME);
        backPanel.add(pnSettings, SETTINGS);
        backPanel.add(pnHowToPlay, HOW_TO_PLAY);
        backPanel.add(pnCredits, CREDITS);
        backPanel.add(pnExit, EXIT);
    }

    /**
     * Creates the Game Screen panel.
     *
     * @param app        The App object.
     * @param backPanel  The outermost panel for everything to assemble to.
     * @param cardLayout The card layout for toggling between scenes.
     */
    public static void configureGameScreen(App app, JPanel backPanel, CardLayout cardLayout){
        // components to be added to the shell
        JPanel pnGameWindow  = configurePanelGame(app, app.getGame(), app.getRender());
        JPanel pnGameDeath   = configurePanelDeath();
        JPanel pnGameVictory = configurePanelVictory();

        // add components to the shell
        backPanel.setLayout(cardLayout);
        backPanel.add(pnGameWindow, GAME);
        backPanel.add(pnGameDeath, LOOSE);
        backPanel.add(pnGameVictory, VICTORY);
    }


    //================================================================================================================//
    //============================================= Menu Panels ======================================================//
    //================================================================================================================//

    public static JPanel configurePanelMenu(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring: Menu...... ");

        Renderer r = app.getRender();
        JPanel pnMenu = createBackgroundPanel(Images.Background);

        List<JLabel> labels = List.of(
                createActionLabel(NEW_GAME, r, SUBTITLE, true, ()->cardLayout.show(backPanel, NEW_GAME)),
                createActionLabel(LOAD_GAME, r, SUBTITLE, true, ()->cardLayout.show(backPanel, LOAD_GAME)),
                createActionLabel(SETTINGS, r, SUBTITLE, true, ()->cardLayout.show(backPanel, SETTINGS)),
                createActionLabel(HOW_TO_PLAY, r, SUBTITLE, true, ()->cardLayout.show(backPanel, HOW_TO_PLAY)),
                createActionLabel(CREDITS, r, SUBTITLE, true, ()->cardLayout.show(backPanel, CREDITS)),
                createActionLabel(EXIT, r, SUBTITLE, true, ()->cardLayout.show(backPanel, EXIT))
        );
        // setting layout
        pnMenu.setLayout(new BoxLayout(pnMenu, BoxLayout.Y_AXIS));
        pnMenu.add(Box.createVerticalGlue());
        labels.forEach(pnMenu::add);

        System.out.println("Done!");
        return pnMenu;
    }

    private static JPanel configurePanelNewGame(App app) {
        System.out.print("Configuring: NewGame...... ");

        JPanel pnStartNew = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("Starting new game...", app.getRender(), TITLE, true);
        JLabel lbConfirm = createActionLabel("Confirm", app.getRender(), SUBTITLE, true, app::transitionToGameScreen);

        // setting layout
        pnStartNew.setLayout(new BoxLayout(pnStartNew, BoxLayout.Y_AXIS));
        // assemble this panel
        addAll(pnStartNew, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbConfirm);

        System.out.println("Done!");
        return pnStartNew;
    }

    private static JPanel configurePanelLoad(App app) {
        System.out.print("Configuring: Load...... ");

        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("Load and Resume Games", app.getRender(), TITLE, true);
        JLabel lbConfirm = createActionLabel("Back", app.getRender(),SUBTITLE, true, app::transitionToMenuScreen);
        JPanel pnLoad1 = createLoadGamePanel("Load 1", app, app.getRender());
        JPanel pnLoad2 = createLoadGamePanel("Load 2", app, app.getRender());

        // setting layout
        pnLoad.setLayout(new BoxLayout(pnLoad, BoxLayout.Y_AXIS));
        // assemble this panel
        addAll(pnLoad, lbTitle, Box.createVerticalGlue(), pnLoad1, Box.createVerticalGlue(),pnLoad2,Box.createVerticalGlue(), lbConfirm);

        System.out.println("Done!");
        return pnLoad;
    }

    private static JPanel createLoadGamePanel(String title, App app, Renderer render) {
        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Wall, render);
        JPanel pnInfo = createClearPanel();
        JPanel pnStatus = createClearPanel();
        JPanel pnInventory = createInventoryPanel();;
        JPanel pnOptions = createClearPanel();

        JLabel lbTitle = createLabel(title, render, SUBTITLE, true);
        JLabel lbLevel = createLabel("Level: 1", render, TEXT, true);
        JLabel lbTime = createLabel("Time Left: 02:00", render, TEXT, true);
        JLabel lbScore = createLabel("Score: 0", render, TEXT, true);
        JLabel lbConfirm = createActionLabel("Load!", render, SUBTITLE, true, app::transitionToGameScreen);
        JLabel lbDelete = createActionLabel("Delete", render, SUBTITLE, true, app::transitionToMenuScreen);

        pnLoad.setLayout(new BoxLayout(pnLoad, BoxLayout.Y_AXIS));
        pnStatus.setLayout(new BoxLayout(pnStatus, BoxLayout.X_AXIS));
        pnOptions.setLayout(new BoxLayout(pnOptions, BoxLayout.X_AXIS));
        pnInventory.setLayout(new GridLayout(1, 9));
        setSize(pnLoad, 800, 200, 800, 200, 800, 200);
        setSize(pnInventory, 675,75, 675,75, 675,75);
        setSize(pnStatus, 675, 30, 675, 30, 675, 30);
        addAll(pnStatus, lbLevel, Box.createHorizontalGlue(), lbTime, Box.createHorizontalGlue(), lbScore);
        addAll(pnInfo, pnStatus, pnInventory);
        addAll(pnOptions, Box.createHorizontalGlue(), lbConfirm, Box.createHorizontalGlue(),lbDelete, Box.createHorizontalGlue());
        addAll(pnLoad, lbTitle, pnInfo, pnOptions);
        return pnLoad;
    }

    private static JPanel configurePanelSettings(App app, List<String> actionNames, List<String> actionKeyBindings) {
        System.out.print("Configuring: Settings...... ");

        Renderer r = app.getRender();
        JPanel pnSettings = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JPanel pnMiddle = createClearPanel();
        JPanel pnBindingL = createClearPanel();
        JPanel pnBindingR = createClearPanel();
        JPanel pnTexturePack = createClearPanel();

        JLabel lbTitle = createLabel("Settings", r, TITLE, true);
        JLabel lbConfirm = createActionLabel("Confirm", r, SUBTITLE, true, app::transitionToMenuScreen);
        JLabel lbTexturePack = createLabel("Texture Pack", r, TEXT, false);
        JLabel lbCurrentTexture = createLabel(r.getCurrentTexturePack()+"" , r, TEXT, false);
        JLabel lbNextTexture = createActionLabel("  >>>", app.getRender(),TEXT, false, ()->{
            int newTexture = (app.getRender().getCurrentTexturePack().ordinal()+1)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            app.getRender().setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
            pnSettings.repaint();
        });
        JLabel lbPrevTexture = createActionLabel("<<<  ", app.getRender(),SUBTITLE, false, ()->{
            int newTexture = (app.getRender().getCurrentTexturePack().ordinal()-1+TexturePack.values().length)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            app.getRender().setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
            pnSettings.repaint();
        });

        List<JLabel> lbsActionNames = new ArrayList<>();
        List<JLabel> lbsActionKeys = new ArrayList<>();
        for (int i = 0; i < actionKeyBindings.size(); i++) {
            lbsActionNames.add(createLabel(actionNames.get(i), r, TEXT, false));
            int finalI = i;
            lbsActionKeys.add(new JLabel((finalI < 6 ? "": "Ctrl + ") + actionKeyBindings.get(finalI)){{
                TexturePack currentTexture = app.getRender().getCurrentTexturePack();
                setForeground(currentTexture.getColorDefault());
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e){
                        if (app.inSettingKeyMode()) return;
                        setForeground(currentTexture.getColorHover());
                    }
                    public void mouseExited(MouseEvent e) {
                        if (app.inSettingKeyMode()) return;
                        setForeground(currentTexture.getColorDefault());
                    }
                    public void mousePressed(MouseEvent e){
                        if (app.inSettingKeyMode()) return;
                        setForeground(currentTexture.getColorSelected());
                        app.setIndexOfKeyToSet(finalI);
                    }
                });}
                public void paintComponent(Graphics g) {
                    TexturePack currentTexture = app.getRender().getCurrentTexturePack();
                    setFont(currentTexture.getTextFont());
                    super.paintComponent(g);
                }
            });
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
                app.getController().setController(actionKeyBindings);
            }
        });

        // setting layout
        setAllBoxLayout(BoxLayout.Y_AXIS, pnSettings, pnBindingL, pnBindingR);
        setAllBoxLayout(BoxLayout.X_AXIS, pnMiddle, pnTexturePack);
        pnBindingL.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        pnBindingR.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        // assemble this panel
        addAll(pnTexturePack, lbPrevTexture, lbCurrentTexture, lbNextTexture);
        pnBindingL.add(lbTexturePack);
        pnBindingR.add(pnTexturePack);
        lbsActionNames.forEach(pnBindingL::add);
        lbsActionKeys.forEach(pnBindingR::add);
        addAll(pnMiddle, Box.createHorizontalGlue(), pnBindingL, pnBindingR, Box.createHorizontalGlue());
        addAll(pnSettings, lbTitle, Box.createVerticalGlue(), pnMiddle, Box.createVerticalGlue(), lbConfirm);

        System.out.println("Done!");
        return pnSettings;
    }

    private static JPanel configurePanelHowToPlay(App app) {
        System.out.print("Configuring: HowToPlay...... ");

        JPanel pnHowToPlay = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("How To Play", app.getRender(), TITLE, true);
        JLabel lbBack = createActionLabel("Back", app.getRender(), SUBTITLE, true, app::transitionToMenuScreen);

        // setting layout
        pnHowToPlay.setLayout(new BoxLayout(pnHowToPlay, BoxLayout.Y_AXIS));
        // assemble this panel
        addAll(pnHowToPlay, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnHowToPlay;
    }

    private static JPanel configurePanelCredits(App app) {
        System.out.print("Configuring: Credits...... ");

        JPanel pnCredits = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JLabel lbTitle = createLabel("Credits", app.getRender(), TITLE, true);
        JLabel lbBack = createActionLabel("Back", app.getRender(),SUBTITLE, true, app::transitionToMenuScreen);
        JLabel[] credits = new JLabel[]{
                createLabel("App: Jeff", app.getRender(), TEXT, true),
                createLabel("Domain: Matty", app.getRender(), TEXT, true),
                createLabel("Fuzz: Ray", app.getRender(), TEXT, true),
                createLabel("Persistency: Ben", app.getRender(), TEXT, true),
                createLabel("Recorder: Jayden", app.getRender(), TEXT, true),
                createLabel("Renderer: Loki", app.getRender(), TEXT, true),
        };

        // setting layout
        pnCredits.setLayout(new BoxLayout(pnCredits, BoxLayout.Y_AXIS));
        // assemble this panel
        addAll(pnCredits, lbTitle, Box.createVerticalGlue());
        addAll(pnCredits, credits);
        addAll(pnCredits, Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnCredits;
    }

    public static JPanel configurePanelExit(App app) {
        System.out.print("Configuring: Exit...... ");

        JPanel pnExit   = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender());
        JPanel pnOption = createClearPanel();
        JLabel lbTitle  = createLabel("Chaps Challenge!", app.getRender(), TITLE, true);
        JLabel lbMessage = createLabel("Are you sure you want to exit?", app.getRender(), SUBTITLE, true);
        JLabel lbYes    = createActionLabel("Yes", app.getRender(),SUBTITLE, true, ()->System.exit(0));
        JLabel lbNo     = createActionLabel("No", app.getRender(),SUBTITLE, true, app::transitionToMenuScreen);

        // setting layout
        pnExit.setLayout(new BoxLayout(pnExit, BoxLayout.Y_AXIS));
        pnOption.setLayout(new BoxLayout(pnOption, BoxLayout.X_AXIS));
        // combine all components
        addAll(pnOption, Box.createHorizontalGlue(), lbNo, Box.createHorizontalGlue(), lbYes, Box.createHorizontalGlue());
        addAll(pnExit, lbTitle, Box.createVerticalGlue(), lbMessage, Box.createVerticalGlue(), pnOption, Box.createVerticalGlue());

        System.out.println("Done!");
        return pnExit;
    }


    //================================================================================================================//
    //============================================= Game Panels ======================================================//
    //================================================================================================================//

    private static JPanel configurePanelGame(App app, DomainController game, Renderer mazeRender) {
        System.out.print("Configuring: Game...... ");

        // outermost panel
        JPanel pnGame = createRepeatableBackgroundPanel(Images.Pattern, mazeRender);
        // 3 panels on top of outermost panel: left/mid/right
        JPanel pnStatus = createClearPanel();
        JPanel pnMaze   = createClearPanel();
        JPanel pnRight  = createClearPanel();
        // inner panels for panels: left/mid/right
        JPanel pnStatusTop = createClearPanel();
        JPanel pnStatusMid = createClearPanel();
        JPanel pnStatusBot = createClearPanel();
        JPanel pnInventory = createClearPanel();
        JPanel pnInventories = createInventoryPanel();
        // status bars
        JLabel lbLevelTitle = createLabel("Level", mazeRender, SUBTITLE, false);
        JLabel lbLevel      = createLabel(game.getCurrentLevel()+"" , mazeRender, SUBTITLE, false);
        JLabel lbTimerTitle = createLabel("Time Left", mazeRender, SUBTITLE, false);
        JLabel lbTimer      = createLabel(app.getTimeLeft()+"" , mazeRender, SUBTITLE, false);
        JLabel lbTreasuresTitle = createLabel("Treasures", mazeRender, SUBTITLE, false);
        JLabel lbTreasures  = createLabel(game.getTreasuresLeft()+"", mazeRender, SUBTITLE, false);
        JLabel lbPause      = createActionLabel("Menu", app.getRender(),SUBTITLE, false, app::transitionToMenuScreen);
        JLabel lbInventoryTitle = createLabel("Inventory", mazeRender, SUBTITLE, false);

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

        System.out.println("Done!");
        return pnGame;
    }


    private static JPanel configurePanelVictory() {
        System.out.print("Configuring: Victory...... ");

        System.out.println("Done!");
        return createClearPanel();
    }

    private static JPanel configurePanelDeath() {
        System.out.print("Configuring: Death...... ");

        System.out.println("Done!");
        return createClearPanel();
    }


    //================================================================================================================//
    //=========================================== Helper Method ======================================================//
    //================================================================================================================//

    private static void setAllBoxLayout(int axis, JPanel... pns) {
        Arrays.stream(pns).forEach(pn -> pn.setLayout(new BoxLayout(pn, axis)));
    }

    private static void setSize(JComponent Component, int pX, int pY, int miX, int miY, int maX, int maY) {
        Component.setPreferredSize(new Dimension(pX, pY));
        Component.setMinimumSize(new Dimension(miX, miY));
        Component.setMaximumSize(new Dimension(maX, maY));
    }

    private static void addAll(JComponent parent, Component... components) {
        Arrays.stream(components).forEach(parent::add);
    }

    //================================================================================================================//
    //========================================== Factory Methods =====================================================//
    //================================================================================================================//

    /**
     * Creates a panel with a clear see-through panel that can be used to group other components
     *
     * @return a clear JPanel
     */
    private static JPanel createClearPanel() {
        return new JPanel() {{setOpaque(false);}};
    }

    /**
     * Creates a panel with a background image that fits the size of the frame
     *
     * @param img the background image
     * @return a JPanel with a background image
     */
    private static JPanel createBackgroundPanel(Images img) {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img.getImg(), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
    }

    /**
     * Creates a panel with a repeating image that fits the size of the frame
     *
     * @param img the image pattern to fill the background
     * @return a JPanel with a repeating pattern background image
     */
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
     * Creates a panel which displays the inventory the player has
     *
     * @return a JPanel with the inventory
     */
    private static JPanel createInventoryPanel() {
        JPanel pnInventory = createClearPanel();
        for(int i = 0; i < 8; i++) {
            int finalX = i;
            pnInventory.add(new JLabel(){
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(Images.Empty_tile.getImg(), 0, 0, getWidth(),getHeight(),null);
                    int size = Math.min(getWidth(), getHeight());
//                    List<Tile> inventory = List.of();
//                    if (finalX >= inventory.size()) return;
//                    g.drawImage(Images.getImage(inventory.get(finalX)), (getWidth()-size)/2, (getHeight()-size)/2, size,size,null);
                }
            });
        }
        return pnInventory;
    }

    /**
     * This method is used to create a JLabel with texture-dynamic fonts.
     *
     * @param name     the name of the label
     * @param render   the renderer object
     * @param textType size of the text, should use the constants TITLE, SUBTITLE, and TEXT to specify
     * @param Centered true if this label should be center aligned
     * @return the JLabel
     */
    private static JLabel createLabel(String name, Renderer render, int textType, boolean Centered) {
        return new JLabel(name) {{
            setForeground(render.getCurrentTexturePack().getColorDefault());
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                TexturePack tp = render.getCurrentTexturePack();
                setFont(switch (textType) {
                    case TITLE    -> tp.getTitleFont();
                    case SUBTITLE -> tp.getSubtitleFont();
                    default       -> tp.getTextFont();
                });
                setForeground(tp.getColorDefault());
            }
        };
    }

    /**
     * This method is used to create a JLabel with texture-dynamic fonts with executable action upon pressed.
     *
     * @param name     the name of the label
     * @param render   the renderer object
     * @param textType size of the text, should use the constants TITLE, SUBTITLE, and TEXT to specify
     * @param Centered true if this label should be center aligned
     * @param runnable the action to be executed when the label is pressed
     * @return the JLabel
     */
    private static JLabel createActionLabel(String name, Renderer render, int textType, boolean Centered, Runnable runnable) {
        return new JLabel(name) {{
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);
            TexturePack tp = render.getCurrentTexturePack();
            setForeground(tp.getColorDefault());
            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){setForeground(tp.getColorHover());}
                public void mouseExited(MouseEvent e) {setForeground(tp.getColorDefault());}
                public void mousePressed(MouseEvent e) {runnable.run();}
            });}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                TexturePack tp = render.getCurrentTexturePack();
                setFont(switch (textType) {
                    case TITLE    -> tp.getTitleFont();
                    case SUBTITLE -> tp.getSubtitleFont();
                    default       -> tp.getTextFont();
                });
            }
        };
    }
}

