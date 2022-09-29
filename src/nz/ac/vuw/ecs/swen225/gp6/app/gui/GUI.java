package nz.ac.vuw.ecs.swen225.gp6.app.gui;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess.DomainController;
import nz.ac.vuw.ecs.swen225.gp6.renderer.InventoryPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.LogPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MazeRenderer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack.Images;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.*;

/**
 * Package-private class for creating panels used by the App.
 *
 * @author Jeff Lin
 */
public class GUI {
    private static final String MENU      = "Menu";
    private static final String NEW_GAME  = "Start New Game!";
    private static final String LOAD_GAME = "Load Game";
    private static final String SETTINGS  = "Settings";
    private static final String LOGS      = "Logs";
    private static final String CREDITS   = "Credits";
    private static final String EXIT      = "Exit";

    private static final String GAME    = "Game";
    private static final String LOOSE   = "Loose";
    private static final String VICTORY = "Victory";

    private static final String STATUS_PAUSE = "Pause";
    private static final String STATUS_RESUME = "Resume";

    private static final String MODE_REPLAY = "Replay";
    private static final String MODE_NORMAL = "Normal";

    private static final LogPanel logPanel = new LogPanel();

    private final JPanel outerPanel = new JPanel();
    private final JPanel menuPanel  = new JPanel();
    private final JPanel gamePanel  = new JPanel();
    private final JPanel pnPause = createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel functionPanel = createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel loadGamePanel = createClearPanel(BoxLayout.X_AXIS);

    private final CardLayout outerCardLayout = new CardLayout();
    private final CardLayout menuCardLayout = new CardLayout();
    private final CardLayout gameCardLayout = new CardLayout();
    private final CardLayout functionCardLayout = new CardLayout();
    private final CardLayout pauseCardLayout = new CardLayout();

    private final MazeRenderer render;
    private final InventoryPanel pnInventory;

    /**
     *
     * @param app The App object that is using this
     */
    public GUI(App app){
        render = new MazeRenderer(app.getGame());
        pnInventory = new InventoryPanel(app.getGame(), true);
    }

    /**
     * Configures the App GUI to be displayed.
     * @param app The App to attach the GUI to.
     */
    public void configureGUI(App app) {
        assert SwingUtilities.isEventDispatchThread(): "Not in EDT";
        outerPanel.setLayout(outerCardLayout);
        menuPanel.setLayout(menuCardLayout);
        gamePanel.setLayout(gameCardLayout);
        functionPanel.setLayout(functionCardLayout);
        pnPause.setLayout(pauseCardLayout);
        render.setFocusable(true);
        configureMenuScreen(app);
        configureGameScreen(app);
        outerPanel.add(menuPanel, MENU);
        outerPanel.add(gamePanel, GAME);
    }

    /**
     * Creates the menu screen.
     *
     * @param app The App object.
     */
    void configureMenuScreen(App app){
        // components to be added to the shell
        JPanel pnMenu     = configurePanelMenu(menuPanel, menuCardLayout, loadGamePanel, app);
        JPanel pnLoad     = configurePanelLoad(menuPanel, menuCardLayout, loadGamePanel, app);
        JPanel pnSettings = configurePanelSettings(menuPanel, menuCardLayout, app);
        JPanel pnCredits  = configurePanelCredits(menuPanel, menuCardLayout, app);
        JPanel pnExit     = configurePanelExit(menuPanel, menuCardLayout, app);

        // add components to the shell
        menuPanel.add(pnMenu, MENU);
        menuPanel.add(pnLoad, LOAD_GAME);
        menuPanel.add(pnSettings, SETTINGS);
        menuPanel.add(pnCredits, CREDITS);
        menuPanel.add(pnExit, EXIT);
    }

    /**
     * Creates the Game Screen panel.
     *
     * @param app The App object.
     */
    void configureGameScreen(App app){
        // components to be added to the shell
        JPanel pnGameVictory = configurePanelVictory(app);
        JPanel pnGameDeath   = configurePanelLost(app);
        JPanel pnPause       = configurePanelPause(app);
        JPanel pnGameWindow  = configurePanelGame(pnPause, functionPanel, pnInventory, render, app);

        // add components to the shell
        gamePanel.add(pnGameWindow, GAME);
        gamePanel.add(pnGameDeath, LOOSE);
        gamePanel.add(pnGameVictory, VICTORY);
    }


    //================================================================================================================//
    //============================================= Menu Panels ======================================================//
    //================================================================================================================//

    private JPanel configurePanelMenu(JPanel backPanel, CardLayout cardLayout, JPanel loadGamePanel, App app) {
        System.out.print("Configuring Menu Panel... ");

        MazeRenderer r = render;
        JPanel pnMenu = createBackgroundPanel(Images.Background, BoxLayout.Y_AXIS);

        List<JLabel> labels = List.of(
                createActionLabel(NEW_GAME, r, SUBTITLE, true, app::startNewGame),
                createActionLabel(LOAD_GAME, r, SUBTITLE, true, ()->{
                    refreshLoadGamesPanel(loadGamePanel, app);
                    cardLayout.show(backPanel, LOAD_GAME);
                }),
                createActionLabel(SETTINGS, r, SUBTITLE, true, ()->cardLayout.show(backPanel, SETTINGS)),
                createActionLabel(LOGS, r, SUBTITLE, true, ()-> {
                    JFrame frame = new JFrame("Logs");
                    setSize(frame, 500,500,500,500,500,500);
                    frame.add(logPanel);
                    frame.setVisible(true);
                }),
                createActionLabel(CREDITS, r, SUBTITLE, true, ()->cardLayout.show(backPanel, CREDITS)),
                createActionLabel(EXIT, r, SUBTITLE, true, ()->cardLayout.show(backPanel, EXIT))
        );
        // assemble the panel
        pnMenu.add(Box.createVerticalGlue());
        labels.forEach(pnMenu::add);

        System.out.println("Done!");
        return pnMenu;
    }

    private JPanel configurePanelLoad(JPanel backPanel, CardLayout cardLayout, JPanel loadGamePanel, App app) {
        System.out.print("Configuring Load Panel... ");

        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("Load and Resume Games", render, TITLE, true);
        JLabel lbBack = createActionLabel("Back", render,SUBTITLE, true, ()->cardLayout.show(backPanel, MENU));

        // assemble this panel
        refreshLoadGamesPanel(loadGamePanel, app);
        addAll(pnLoad, lbTitle, Box.createVerticalGlue(), loadGamePanel, Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnLoad;
    }

    private JPanel configurePanelSettings(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring Settings Panel... ");

        MazeRenderer r = render;
        JPanel pnSettings = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JPanel pnMiddle = createClearPanel(BoxLayout.X_AXIS);
        JPanel pnBindingL = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnBindingR = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnTexturePack = createClearPanel(BoxLayout.X_AXIS);

        JLabel lbTitle = createLabel("Settings", r, TITLE, true);
        JLabel lbConfirm = createActionLabel("Confirm", r, SUBTITLE, true, ()->cardLayout.show(backPanel, MENU));
        JLabel lbTexturePack = createLabel("Texture Pack", r, TEXT, false);
        JLabel lbCurrentTexture = createLabel(r.getCurrentTexturePack()+"" , r, TEXT, false);
        JLabel lbNextTexture = createActionLabel("  >>>", render,TEXT, false, ()->{
            int newTexture = (render.getCurrentTexturePack().ordinal()+1)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            render.setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
            app.repaint();
        });
        JLabel lbPrevTexture = createActionLabel("<<<  ", render,SUBTITLE, false, ()->{
            int newTexture = (render.getCurrentTexturePack().ordinal()-1+TexturePack.values().length)%TexturePack.values().length;
            TexturePack currentPack = TexturePack.values()[newTexture];
            render.setTexturePack(currentPack);
            lbCurrentTexture.setText(currentPack+"");
            app.repaint();
        });
        JLabel lbPlayMusic = createLabel("Play Sound", r, TEXT, false);
        JLabel lbIsMusicOn = createActionLabel(app.getConfiguration().isMusicOn()+"", r, TEXT, false, ()->{});
        lbIsMusicOn.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                app.getConfiguration().setMusicOn(!app.getConfiguration().isMusicOn());
                lbIsMusicOn.setText(app.getConfiguration().isMusicOn()+"");
                if (app.getConfiguration().isMusicOn()) {
                    MusicPlayer.playMenuMusic();
                } else {
                    MusicPlayer.stopMenuMusic();
                }
            }
        });

        List<JLabel> lbsActionNames = new ArrayList<>();
        List<JLabel> lbsActionKeys = new ArrayList<>();
        for (int i = 0; i < app.getConfiguration().getUserKeyBindings().size(); i++) {
            lbsActionNames.add(createLabel(app.getConfiguration().getActionName(i), r, TEXT, false));
            int finalI = i;
            Controller.Key key = app.getConfiguration().getKeyBinding(finalI);
            lbsActionKeys.add(new JLabel((key.modifier() == 0  ? "": KeyEvent.getModifiersExText(key.modifier()) + " + ") + KeyEvent.getKeyText(key.key())){{
                TexturePack currentTexture = render.getCurrentTexturePack();
                setForeground(currentTexture.getColorDefault());
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e){
                        if (app.getConfiguration().inSettingKeyMode()) return;
                        setForeground(currentTexture.getColorHover());
                    }
                    public void mouseExited(MouseEvent e) {
                        if (app.getConfiguration().inSettingKeyMode()) return;
                        setForeground(currentTexture.getColorDefault());
                    }
                    public void mousePressed(MouseEvent e){
                        if (app.getConfiguration().inSettingKeyMode()) return;
                        setForeground(currentTexture.getColorSelected());
                        app.getConfiguration().setIndexOfKeyToSet(finalI);
                    }
                });}
                public void paintComponent(Graphics g) {
                    TexturePack currentTexture = render.getCurrentTexturePack();
                    setFont(currentTexture.getTextFont());
                    super.paintComponent(g);
                }
            });
        }

        app.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (! app.getConfiguration().inSettingKeyMode()) return;
                int modifier = e.getModifiersEx();
                int key = e.getKeyCode();
                var label = lbsActionKeys.get(app.getConfiguration().indexOfKeyToSet());
                if (app.getConfiguration().checkKeyBinding(Controller.Key.key(modifier,key))){
                    app.getConfiguration().exitKeySettingMode();
                    label.setForeground(Color.BLACK);
                    return;
                }
                app.getConfiguration().setKeyBinding(app.getConfiguration().indexOfKeyToSet(), Controller.Key.key(modifier,key));
                label.setText(Controller.Key.toString(modifier, key));
                label.setForeground(Color.BLACK);
                app.getConfiguration().exitKeySettingMode();
                app.getController().update();
            }
        });

        // setting layout
        pnBindingL.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        pnBindingR.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        // assemble this panel
        addAll(pnTexturePack, lbPrevTexture, lbCurrentTexture, lbNextTexture);
        addAll(pnBindingL, lbPlayMusic, lbTexturePack);
        addAll(pnBindingR, lbIsMusicOn, pnTexturePack);
        lbsActionNames.forEach(pnBindingL::add);
        lbsActionKeys.forEach(pnBindingR::add);
        addAll(pnMiddle, Box.createHorizontalGlue(), pnBindingL, pnBindingR, Box.createHorizontalGlue());
        addAll(pnSettings, lbTitle, Box.createVerticalGlue(), pnMiddle, Box.createVerticalGlue(), lbConfirm);

        System.out.println("Done!");
        return pnSettings;
    }

    private JPanel configurePanelCredits(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring Credits Panel... ");

        JPanel pnCredits = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("Credits", render, TITLE, true);
        JLabel lbBack = createActionLabel("Back", render,SUBTITLE, true, ()->cardLayout.show(backPanel, MENU));
        JLabel[] credits = new JLabel[]{
                createLabel("App: Jeff", render, SUBTITLE, true),
                createLabel("Domain: Matty", render, SUBTITLE, true),
                createLabel("Fuzz: Ray", render, SUBTITLE, true),
                createLabel("Persistency: Ben", render, SUBTITLE, true),
                createLabel("Recorder: Jayden", render, SUBTITLE, true),
                createLabel("Renderer: Loki", render, SUBTITLE, true),
        };

        // assemble this panel
        addAll(pnCredits, lbTitle, Box.createVerticalGlue());
        addAll(pnCredits, credits);
        addAll(pnCredits, Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnCredits;
    }

    private JPanel configurePanelExit(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring Exit Panel... ");

        JPanel pnExit   = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JPanel pnOption = createClearPanel(BoxLayout.X_AXIS);
        JLabel lbTitle  = createLabel("Chaps Challenge!", render, TITLE, true);
        JLabel lbMessage = createLabel("Are you sure you want to exit?", render, SUBTITLE, true);
        JLabel lbYes    = createActionLabel("Yes", render,SUBTITLE, true, ()->{
                                            System.out.println("Application closed with exit code 0");
                                            System.exit(0);});
        JLabel lbNo     = createActionLabel("No", render,SUBTITLE, true, ()->cardLayout.show(backPanel, MENU));

        // combine all components
        addAll(pnOption, Box.createHorizontalGlue(), lbNo, Box.createHorizontalGlue(), lbYes, Box.createHorizontalGlue());
        addAll(pnExit, lbTitle, Box.createVerticalGlue(), lbMessage, Box.createVerticalGlue(), pnOption, Box.createVerticalGlue());

        System.out.println("Done!");
        return pnExit;
    }


    //================================================================================================================//
    //============================================= Game Panels ======================================================//
    //================================================================================================================//

    private JPanel configurePanelGame(JPanel pnPause, JPanel functionPanel, JPanel pnInventories, MazeRenderer mazeRender, App app) {
        System.out.print("Configuring Game Panel... ");

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
        JPanel pnModeNormal = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnModeReplay = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnInventory = createClearPanel(BoxLayout.Y_AXIS);
        // status bars
        JLabel lbLevelTitle = createLabel("Level", mazeRender, SUBTITLE, false);
        JLabel lbLevel      = createInfoLabel(()->app.getGame().getCurrentLevel()+"", mazeRender, SUBTITLE, false);
        JLabel lbTimerTitle = createLabel("Time", mazeRender, SUBTITLE, false);
        JLabel lbTimer      = createInfoLabel(app.getGameClock()::getTimeInMinutes, mazeRender, SUBTITLE, false);
        JLabel lbTreasuresTitle = createLabel("Treasures", mazeRender, SUBTITLE, false);
        JLabel lbTreasures  = createInfoLabel(()->app.getGame().getTreasuresLeft()+"", mazeRender, SUBTITLE, false);
        JLabel lbPauseNormal = createActionLabel("Pause", render,SUBTITLE, true,()->app.getActions().actionPause());
        JLabel lbPauseReplay = createActionLabel("Pause", render,SUBTITLE, true, ()->app.getActions().actionPause());
        JLabel lbReplayTitle = createLabel("Replay Mode", render,SUBTITLE, true);
        JLabel lbReplayAuto = createActionLabel("Auto", render,SUBTITLE, true, app::transitionToReplayScreen);
        JLabel lbReplayStep = createActionLabel("Step", render,SUBTITLE, true, app::transitionToReplayScreen);
        JLabel lbInventoryTitle = createLabel("Inventory", mazeRender, SUBTITLE, false);

        // TODO: for debugging, remove later
        JLabel lbWin = createActionLabel("Win!", mazeRender, SUBTITLE, true, app::transitionToWinScreen);
        JLabel lbLose = createActionLabel("Lose!", mazeRender, SUBTITLE, true, app::transitionToLostScreen);

        // setting size
        int width = 75*2, height = 75*4;
        pnStatus.setMaximumSize(new Dimension(200, 1000));
        pnRight.setMaximumSize(new Dimension(200, 1000));
        setSize(mazeRender, 700, 700, 600, 600, 800, 800);
        setSize(pnMaze, 700, 700, 600, 600, 800, 800);
        setSize(pnInventory, width, height, width, height, width, height);
        setSize(functionPanel, 200,200,200,200,200,200);
        pnInventory.setAlignmentX(Component.CENTER_ALIGNMENT);
        mazeRender.setLayout(new GridBagLayout());

        functionPanel.add(pnModeNormal, MODE_NORMAL);
        functionPanel.add(pnModeReplay, MODE_REPLAY);
        addAll(pnModeNormal, lbPauseNormal, lbWin, lbLose);
        addAll(pnModeReplay, lbReplayTitle, lbPauseReplay, lbReplayAuto, lbReplayStep);

        addAll(pnStatusTop, lbLevelTitle, lbLevel);
        addAll(pnStatusMid, lbTimerTitle, lbTimer);
        addAll(pnStatusBot, lbTreasuresTitle, lbTreasures);
        addAll(pnInventory, lbInventoryTitle, pnInventories);
        addAll(pnStatus, Box.createVerticalGlue(), pnStatusTop, Box.createVerticalGlue(), pnStatusMid,
                        Box.createVerticalGlue(), pnStatusBot, Box.createVerticalGlue());
        // TODO: finish pause panel
        mazeRender.add(pnPause);
        pnMaze.add(mazeRender);
        addAll(pnRight, Box.createVerticalGlue(), functionPanel, Box.createVerticalGlue(), pnInventory, Box.createVerticalGlue());
        addAll(pnGame, Box.createHorizontalGlue(), pnStatus, Box.createHorizontalGlue(), pnMaze, Box.createHorizontalGlue(),
                        pnRight, Box.createHorizontalGlue());

        System.out.println("Done!");
        return pnGame;
    }

    private JPanel configurePanelPause(App app) {
        System.out.print("Configuring Pause Panel... ");

        JPanel pnOnPause = creatTransparentPanel(Images.Empty_tile, 0.8f);
        JPanel pnOnResume = createClearPanel(BoxLayout.Y_AXIS);
        pnPause.add(pnOnResume, STATUS_RESUME);
        pnPause.add(pnOnPause, STATUS_PAUSE);


        JLabel lbResume = createActionLabel("Resume", render, TITLE, true, ()->app.getActions().actionResume());
        JLabel lbRestart = createActionLabel("Restart", render, TITLE, true, ()->{});
        JLabel lbSave = createActionLabel("Save", render, TITLE, true, ()->{});
        JLabel lbMenu = createActionLabel("Quit to Menu", render, TITLE, true, app::transitionToMenuScreen);

        addAll(pnOnPause,
                        Box.createVerticalGlue(),
                        lbResume,
                        Box.createVerticalGlue(),
                        lbRestart,
                        Box.createVerticalGlue(),
                        lbSave,
                        Box.createVerticalGlue(),
                        lbMenu,
                        Box.createVerticalGlue());

        System.out.println("Done!");
        return pnPause;
    }

    private JPanel configurePanelVictory(App app) {
        System.out.print("Configuring Victory Panel... ");

        JPanel pnVictory = createBackgroundPanel(Images.WinScreen, BoxLayout.Y_AXIS);
        JLabel lbMenu = createActionLabel("Menu", render, TITLE, true, app::transitionToMenuScreen);

        addAll(pnVictory, Box.createVerticalGlue(), Box.createVerticalGlue(), lbMenu);

        System.out.println("Done!");
        return pnVictory;
    }

    private JPanel configurePanelLost(App app) {
        System.out.print("Configuring Lost Panel... ");

        JPanel pnLost = createBackgroundPanel(Images.LoseScreen, BoxLayout.Y_AXIS);
        JLabel lbMenu = createActionLabel("Menu", render, TITLE, true, app::transitionToMenuScreen);

        addAll(pnLost, Box.createVerticalGlue(), Box.createVerticalGlue(), lbMenu);

        System.out.println("Done!");
        return pnLost;
    }


    //================================================================================================================//
    //=========================================== Getter Methods =====================================================//
    //================================================================================================================//

    /**
     * Gets the outermost panel of this GUI
     *
     * @return the outermost panel
     */
    public JPanel getOuterPanel() {return outerPanel;}

    /**
     * Gets the Maze Renderer of this GUI
     *
     * @return the Maze Renderer
     */
    public MazeRenderer getRender() {return render;}

    /**
     * Gets the Inventory Renderer of this GUI
     *
     * @return the Inventory Renderer
     */
    public InventoryPanel getInventory() {return pnInventory;}

    /**
     * Gets the log panel of this GUI
     *
     * @return the log panel
     */
    public static LogPanel getLogPanel(){return logPanel;}


    //================================================================================================================//
    //=========================================== Helper Method ======================================================//
    //================================================================================================================//

    /**
     * refreshes the save files in load game panel
     * @param pnLoadGame the panel to be refreshed
     * @param app the app object
     */
    public void refreshLoadGamesPanel(JComponent pnLoadGame, App app){
        pnLoadGame.removeAll();
        List<Domain> saves;
        try {
            saves = Persistency.loadSaves();
        } catch (Exception e) {
            System.out.print("Failed to load saves, resetting save files.");
            JOptionPane.showMessageDialog(null, "Error loading saved games, resetting save files.");
            saves = List.of(Persistency.getInitialDomain(),Persistency.getInitialDomain(),Persistency.getInitialDomain());
        }
        addAll(pnLoadGame,
                Box.createHorizontalGlue(),
                createLoadGamePanel(1, app, render, new DomainController(saves.get(0))),
                Box.createHorizontalGlue(),
                createLoadGamePanel(2, app, render, new DomainController(saves.get(1))),
                Box.createHorizontalGlue(),
                createLoadGamePanel(3, app, render, new DomainController(saves.get(2))),
                Box.createHorizontalGlue());
        pnLoadGame.revalidate();
        pnLoadGame.repaint();
    }


    //================================================================================================================//
    //========================================= Transition Methods ===================================================//
    //================================================================================================================//

    /**
     * Transitions to the menu screen.
     */
    public void transitionToMenuScreen(){
        functionCardLayout.show(functionPanel, MODE_NORMAL);
        menuCardLayout.show(menuPanel, MENU);
        outerCardLayout.show(outerPanel, MENU);
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        functionCardLayout.show(functionPanel, MODE_NORMAL);
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToReplayScreen(){
        functionCardLayout.show(functionPanel, MODE_REPLAY);
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
    }

    /**
     * Transitions to the winning screen.
     */
    public void transitionToWinScreen(){
        gameCardLayout.show(gamePanel, VICTORY);
        outerCardLayout.show(outerPanel, GAME);
    }

    /**
     * Transitions to the losing screen.
     */
    public void transitionToLostScreen(){
        gameCardLayout.show(gamePanel, LOOSE);
        outerCardLayout.show(outerPanel, GAME);
    }

    /**
     * Brings up the pause screen.
     */
    public void showPausePanel(){
        pauseCardLayout.show(pnPause, STATUS_PAUSE);
    }

    /**
     * Hides the pause screen and continue with resume screen.
     */
    public void showResumePanel(){
        pauseCardLayout.show(pnPause, STATUS_RESUME);
    }
}