package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.renderer.InventoryPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MazeRenderer;
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
import java.util.function.Supplier;


/**
 * Package-private class for creating panels used by the App.
 *
 * @author Jeff Lin
 */
public class PanelCreator{
    /**
     * The title sized text font.
     */
    public static final int TITLE = 1;
    /**
     * The subtitle sized text font.
     */
    public static final int SUBTITLE = 2;
    /**
     * The normal-sized text font.
     */
    public static final int TEXT = 3;

    static final String MENU        = "Menu";
    static final String NEW_GAME    = "Start New Game!";
    static final String LOAD_GAME   = "Load Game";
    static final String SETTINGS    = "Settings";
    static final String HOW_TO_PLAY = "How to play";
    static final String CREDITS     = "Credits";
    static final String EXIT        = "Exit";

    static final String GAME     = "Game";
    static final String LOOSE    = "Loose";
    static final String VICTORY  = "Victory";

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
    static void configureMenuScreen(App app, JPanel backPanel, CardLayout cardLayout){
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
    static void configureGameScreen(App app, JPanel backPanel, CardLayout cardLayout){
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

    private static JPanel configurePanelMenu(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring: Menu...... ");

        MazeRenderer r = app.getRender();
        JPanel pnMenu = createBackgroundPanel(Images.Background, BoxLayout.Y_AXIS);

        List<JLabel> labels = List.of(
                createActionLabel(NEW_GAME, r, SUBTITLE, true, ()->cardLayout.show(backPanel, NEW_GAME)),
                createActionLabel(LOAD_GAME, r, SUBTITLE, true, ()->cardLayout.show(backPanel, LOAD_GAME)),
                createActionLabel(SETTINGS, r, SUBTITLE, true, ()->cardLayout.show(backPanel, SETTINGS)),
                createActionLabel(HOW_TO_PLAY, r, SUBTITLE, true, ()->cardLayout.show(backPanel, HOW_TO_PLAY)),
                createActionLabel(CREDITS, r, SUBTITLE, true, ()->cardLayout.show(backPanel, CREDITS)),
                createActionLabel(EXIT, r, SUBTITLE, true, ()->cardLayout.show(backPanel, EXIT))
        );
        // assemble the panel
        pnMenu.add(Box.createVerticalGlue());
        labels.forEach(pnMenu::add);

        System.out.println("Done!");
        return pnMenu;
    }

    private static JPanel configurePanelNewGame(App app) {
        System.out.print("Configuring: NewGame...... ");

        JPanel pnStartNew = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender(), BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("Starting new game...", app.getRender(), TITLE, true);
        JLabel lbConfirm = createActionLabel("Confirm", app.getRender(), SUBTITLE, true, app::transitionToGameScreen);

        // assemble this panel
        addAll(pnStartNew, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbConfirm);

        System.out.println("Done!");
        return pnStartNew;
    }

    private static JPanel configurePanelLoad(App app) {
        System.out.print("Configuring: Load...... ");

        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender(), BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("Load and Resume Games", app.getRender(), TITLE, true);
        JLabel lbConfirm = createActionLabel("Back", app.getRender(),SUBTITLE, true, app::transitionToMenuScreen);
        JPanel pnLoad1 = createLoadGamePanel("Load 1", app, app.getRender());
        JPanel pnLoad2 = createLoadGamePanel("Load 2", app, app.getRender());

        // assemble this panel
        addAll(pnLoad, lbTitle, Box.createVerticalGlue(), pnLoad1, Box.createVerticalGlue(),pnLoad2,Box.createVerticalGlue(), lbConfirm);

        System.out.println("Done!");
        return pnLoad;
    }

    private static JPanel createLoadGamePanel(String title, App app, MazeRenderer render) {
        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Wall, render, BoxLayout.X_AXIS);
        JPanel pnInfo = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnStatus = createClearPanel(BoxLayout.X_AXIS);
        JPanel pnInventory = new InventoryPanel(app.getGame(), false);
        JPanel pnOptions = createClearPanel(BoxLayout.X_AXIS);

        JLabel lbTitle = createLabel(title, render, SUBTITLE, true);
        JLabel lbLevel = createLabel("Level: 1", render, TEXT, true);
        JLabel lbTime = createLabel("Time Left: 02:00", render, TEXT, true);
        JLabel lbScore = createLabel("Score: 0", render, TEXT, true);
        JLabel lbConfirm = createActionLabel("Load!", render, SUBTITLE, true, app::transitionToGameScreen);
        JLabel lbDelete = createActionLabel("Delete", render, SUBTITLE, true, app::transitionToMenuScreen);

        // assemble this panel
        setSize(pnLoad, 800, 200, 800, 200, 800, 200);
        setSize(pnInventory, 675,75, 675,75, 675,75);
        setSize(pnStatus, 675, 30, 675, 30, 675, 30);
        // assemble this panel
        addAll(pnStatus, lbLevel, Box.createHorizontalGlue(), lbTime, Box.createHorizontalGlue(), lbScore);
        addAll(pnInfo, pnStatus, pnInventory);
        addAll(pnOptions, Box.createHorizontalGlue(), lbConfirm, Box.createHorizontalGlue(),lbDelete, Box.createHorizontalGlue());
        addAll(pnLoad, lbTitle, pnInfo, pnOptions);
        return pnLoad;
    }

    private static JPanel configurePanelSettings(App app, List<String> actionNames, List<String> actionKeyBindings) {
        System.out.print("Configuring: Settings...... ");

        MazeRenderer r = app.getRender();
        JPanel pnSettings = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender(), BoxLayout.Y_AXIS);
        JPanel pnMiddle = createClearPanel(BoxLayout.X_AXIS);
        JPanel pnBindingL = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnBindingR = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnTexturePack = createClearPanel(BoxLayout.X_AXIS);

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
                app.getController().resetController();
            }
        });

        // setting layout
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

        JPanel pnHowToPlay = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender(), BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("How To Play", app.getRender(), TITLE, true);
        JLabel lbBack = createActionLabel("Back", app.getRender(), SUBTITLE, true, app::transitionToMenuScreen);

        // assemble this panel
        addAll(pnHowToPlay, lbTitle, Box.createVerticalGlue(), Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnHowToPlay;
    }

    private static JPanel configurePanelCredits(App app) {
        System.out.print("Configuring: Credits...... ");

        JPanel pnCredits = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender(), BoxLayout.Y_AXIS);
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

        // assemble this panel
        addAll(pnCredits, lbTitle, Box.createVerticalGlue());
        addAll(pnCredits, credits);
        addAll(pnCredits, Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnCredits;
    }

    private static JPanel configurePanelExit(App app) {
        System.out.print("Configuring: Exit...... ");

        JPanel pnExit   = createRepeatableBackgroundPanel(Images.Pattern_2, app.getRender(), BoxLayout.Y_AXIS);
        JPanel pnOption = createClearPanel(BoxLayout.X_AXIS);
        JLabel lbTitle  = createLabel("Chaps Challenge!", app.getRender(), TITLE, true);
        JLabel lbMessage = createLabel("Are you sure you want to exit?", app.getRender(), SUBTITLE, true);
        JLabel lbYes    = createActionLabel("Yes", app.getRender(),SUBTITLE, true, ()->System.exit(0));
        JLabel lbNo     = createActionLabel("No", app.getRender(),SUBTITLE, true, app::transitionToMenuScreen);

        // combine all components
        addAll(pnOption, Box.createHorizontalGlue(), lbNo, Box.createHorizontalGlue(), lbYes, Box.createHorizontalGlue());
        addAll(pnExit, lbTitle, Box.createVerticalGlue(), lbMessage, Box.createVerticalGlue(), pnOption, Box.createVerticalGlue());

        System.out.println("Done!");
        return pnExit;
    }


    //================================================================================================================//
    //============================================= Game Panels ======================================================//
    //================================================================================================================//

    private static JPanel configurePanelGame(App app, DomainController game, MazeRenderer mazeRender) {
        System.out.print("Configuring: Game...... ");

        // outermost panel
        JPanel pnGame = createRepeatableBackgroundPanel(Images.Pattern, mazeRender, BoxLayout.X_AXIS);
        // 3 panels on top of outermost panel: left/mid/right
        JPanel pnStatus = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnMaze   = createClearPanel(new GridBagLayout());
        JPanel pnRight  = createClearPanel(BoxLayout.Y_AXIS);
        // inner panels for panels: left/mid/right
        JPanel pnStatusTop = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnStatusMid = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnStatusBot = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnInventory = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnInventories = new InventoryPanel(game, true);
        // status bars
        JLabel lbLevelTitle = createLabel("Level", mazeRender, SUBTITLE, false);
        JLabel lbLevel      = createInfoLabel(()->app.getGame().getCurrentLevel()+"", mazeRender, SUBTITLE, false);
        JLabel lbTimerTitle = createLabel("Time", mazeRender, SUBTITLE, false);
        JLabel lbTimer      = createInfoLabel(app::getTimeInMinutes, mazeRender, SUBTITLE, false);
        JLabel lbTreasuresTitle = createLabel("Treasures", mazeRender, SUBTITLE, false);
        JLabel lbTreasures  = createInfoLabel(()->app.getGame().getTreasuresLeft()+"", mazeRender, SUBTITLE, false);
        JLabel lbPause      = createActionLabel("Menu", app.getRender(),SUBTITLE, false, app::transitionToMenuScreen);
        JLabel lbInventoryTitle = createLabel("Inventory", mazeRender, SUBTITLE, false);

        // setting size
        int width = 75*2, height = 75*4;
        pnStatus.setMaximumSize(new Dimension(200, 1000));
        pnRight.setMaximumSize(new Dimension(200, 1000));
        setSize(mazeRender, 700, 700, 600, 600, 800, 800);
        setSize(pnMaze, 700, 700, 600, 600, 800, 800);
        setSize(pnInventory, width, height, width, height, width, height);


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
        return createClearPanel(BoxLayout.Y_AXIS);
    }

    private static JPanel configurePanelDeath() {
        System.out.print("Configuring: Death...... ");

        System.out.println("Done!");
        return createClearPanel(BoxLayout.Y_AXIS);
    }


    //================================================================================================================//
    //=========================================== Helper Method ======================================================//
    //================================================================================================================//

    /**
     * Sets the size of a component
     *
     * @param Component the component to be set
     * @param pX the preferred width of the component
     * @param pY the preferred height of the component
     * @param miX the minimum width of the component
     * @param miY the minimum height of the component
     * @param maX the maximum width of the component
     * @param maY the maximum height of the component
     */
    public static void setSize(JComponent Component, int pX, int pY, int miX, int miY, int maX, int maY) {
        Component.setPreferredSize(new Dimension(pX, pY));
        Component.setMinimumSize(new Dimension(miX, miY));
        Component.setMaximumSize(new Dimension(maX, maY));
    }

    /**
     * Adds all components to a panel
     *
     * @param parent the panel to be added to
     * @param components the components to be added
     */
    public static void addAll(JComponent parent, Component... components) {
        Arrays.stream(components).forEach(parent::add);
    }

    //================================================================================================================//
    //========================================== Factory Methods =====================================================//
    //================================================================================================================//

    /**
     * Creates a panel with a clear see-through panel that can be used to group other components
     *
     * @param mgr the layout manager of the panel
     * @return a clear JPanel
     */
    public static JPanel createClearPanel(LayoutManager mgr) {
        return new JPanel() {{
            setLayout(mgr);
            setOpaque(false);
        }};
    }

    /**
     * Creates a BoxLayout panel with a clear see-through panel that can be used to group other components
     *
     * @param axis the axis to lay out components along. Can be one of:
     *             {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *             BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @return a clear JPanel
     */
    public static JPanel createClearPanel(int axis) {
        return new JPanel() {{
            setLayout(new BoxLayout(this, axis));
            setOpaque(false);
        }};
    }

    /**
     * Creates a panel with a background image that fits the size of the frame
     *
     * @param img  the background image
     * @param axis the axis to lay out components along. Can be one of:
     *             {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *             BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @return a JPanel with a background image
     */
    public static JPanel createBackgroundPanel(Images img, int axis) {
        return new JPanel() {{
            setLayout(new BoxLayout(this, axis));}
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(img.getImg(), 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
    }

    /**
     * Creates a panel with a repeating image that fits the size of the frame
     *
     * @param img    the image pattern to fill the background
     * @param render the render to be used to get the size of the frame
     * @param axis the axis to lay out components along. Can be one of:
     *            {@code BoxLayout.X_AXIS, BoxLayout.Y_AXIS,
     *             BoxLayout.LINE_AXIS} or {@code BoxLayout.PAGE_AXIS}
     * @return a JPanel with a repeating pattern background image
     */
    public static JPanel createRepeatableBackgroundPanel(Images img, MazeRenderer render, int axis) {
        return new JPanel(){{
            setLayout(new BoxLayout(this, axis));}
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
     *
     * @param name     the name of the label
     * @param render   the renderer object
     * @param textType size of the text, should use the constants TITLE, SUBTITLE, and TEXT to specify
     * @param Centered true if this label should be center aligned
     * @return the JLabel
     */
    public static JLabel createLabel(String name, MazeRenderer render, int textType, boolean Centered) {
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
     * This method is used to create a JLabel with texture-dynamic fonts.
     *
     * @param display  method to invoke the information to be displayed
     * @param render   the renderer object
     * @param textType size of the text, should use the constants TITLE, SUBTITLE, and TEXT to specify
     * @param Centered true if this label should be center aligned
     * @return the JLabel
     */
    public static JLabel createInfoLabel(Supplier<String> display, MazeRenderer render, int textType, boolean Centered) {
        return new JLabel(display.get()) {{
            setForeground(render.getCurrentTexturePack().getColorDefault());
            if (Centered) setAlignmentX(CENTER_ALIGNMENT);}
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setText(display.get());
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
    public static JLabel createActionLabel(String name, MazeRenderer render, int textType, boolean Centered, Runnable runnable) {
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

