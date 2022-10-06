package nz.ac.vuw.ecs.swen225.gp6.app.gui;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
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
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    private static final String SAVE_GAME = "Save Game";
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

    private final JPanel outerPanel = createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel menuPanel  = createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel gamePanel  = createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel pnPause = createClearPanel(BoxLayout.Y_AXIS);
    private final JPanel functionPanel = createClearPanel(BoxLayout.Y_AXIS);

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
        JPanel pnMenu     = configurePanelMenu(menuPanel, menuCardLayout, app);
        JPanel pnLoad     = configurePanelLoad(app);
        JPanel pnSave     = configurePanelSave(app);
        JPanel pnSettings = configurePanelSettings(menuPanel, menuCardLayout, app);
        JPanel pnCredits  = configurePanelCredits(menuPanel, menuCardLayout, app);
        JPanel pnExit     = configurePanelExit(menuPanel, menuCardLayout, app);

        // add components to the shell
        menuPanel.add(pnMenu, MENU);
        menuPanel.add(pnLoad, LOAD_GAME);
        menuPanel.add(pnSave, SAVE_GAME);
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

    private JPanel configurePanelMenu(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring Menu Panel... ");

        MazeRenderer r = render;
        JPanel pnMenu = createBackgroundPanel(Images.Background, BoxLayout.Y_AXIS);

        List<JLabel> labels = List.of(
                createActionLabel(NEW_GAME, r, SUBTITLE, true, app::startNewGame),
                createActionLabel(LOAD_GAME, r, SUBTITLE, true, ()->{
                    app.refreshSaves();
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

    private JPanel configurePanelLoad(App app) {
        System.out.print("Configuring Load Panel... ");

        JPanel pnLoad = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("Load and Resume Games", render, TITLE, true);
        JLabel lbBack = createActionLabel("Return to menu", render,SUBTITLE, true, this::transitionToMenuScreen);

        JPanel pnLoadGame = createClearPanel(BoxLayout.X_AXIS);
        // assemble this panel
        addAll(pnLoadGame,
                Box.createHorizontalGlue(),
                configureSaveLoadGameSubPanel(1, app, false),
                Box.createHorizontalGlue(),
                configureSaveLoadGameSubPanel(2, app, false),
                Box.createHorizontalGlue(),
                configureSaveLoadGameSubPanel(3, app, false),
                Box.createHorizontalGlue());
        addAll(pnLoad, lbTitle, Box.createVerticalGlue(), pnLoadGame, Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnLoad;
    }

    private JPanel configurePanelSave(App app) {
        System.out.print("Configuring Save Panel... ");

        JPanel pnSave = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JLabel lbTitle = createLabel("Save the current game!", render, TITLE, true);
        JLabel lbBack = createActionLabel("Return to Menu", render,SUBTITLE, true, this::transitionToMenuScreen);

        JPanel pnSaveGame = createClearPanel(BoxLayout.X_AXIS);
        // assemble this panel
        addAll(pnSaveGame,
                Box.createHorizontalGlue(),
                configureSaveLoadGameSubPanel(1, app, true),
                Box.createHorizontalGlue(),
                configureSaveLoadGameSubPanel(2, app, true),
                Box.createHorizontalGlue(),
                configureSaveLoadGameSubPanel(3, app, true),
                Box.createHorizontalGlue());
        addAll(pnSave, lbTitle, Box.createVerticalGlue(), pnSaveGame, Box.createVerticalGlue(), lbBack);

        System.out.println("Done!");
        return pnSave;
    }

    private JPanel configurePanelSettings(JPanel backPanel, CardLayout cardLayout, App app) {
        System.out.print("Configuring Settings Panel... ");

        JPanel pnSettings = createRepeatableBackgroundPanel(Images.Pattern_2, render, BoxLayout.Y_AXIS);
        JPanel pnMiddle = createClearPanel(BoxLayout.X_AXIS);
        JPanel pnBindingL = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnBindingR = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnTexturePack = createClearPanel(BoxLayout.X_AXIS);

        JLabel lbCurrentTexture = createLabel(render.getCurrentTexturePack()+"" , render, TEXT, false);

        // setting layout
        pnBindingL.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        pnBindingR.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        // assemble this panel
        addAll(pnTexturePack,
                createActionLabel("<<<  ", render,SUBTITLE, false, ()->{
                    int newTexture = (render.getCurrentTexturePack().ordinal()-1+TexturePack.values().length)%TexturePack.values().length;
                    TexturePack currentPack = TexturePack.values()[newTexture];
                    render.setTexturePack(currentPack);
                    lbCurrentTexture.setText(currentPack+"");
                    app.repaint();
                }),
                lbCurrentTexture,
                createActionLabel("  >>>", render,TEXT, false, ()->{
                    int newTexture = (render.getCurrentTexturePack().ordinal()+1)%TexturePack.values().length;
                    TexturePack currentPack = TexturePack.values()[newTexture];
                    render.setTexturePack(currentPack);
                    lbCurrentTexture.setText(currentPack+"");
                    app.repaint();
                }));
        addAll(pnBindingL,
                createLabel("Play Sound", render, TEXT, false),
                createLabel("Texture Pack", render, TEXT, false));
        addAll(pnBindingR,
                createInfoActionLabel(()->app.getConfiguration().isMusicOn()? "On" : "Off", render, TEXT, false, ()->false,
                        ()->{app.getConfiguration().setMusicOn(!app.getConfiguration().isMusicOn());
                            if (app.getConfiguration().isMusicOn()) {
                                MusicPlayer.playMenuMusic();
                            } else {
                                MusicPlayer.stopMenuMusic();
                            }}),
                pnTexturePack);
        AtomicReference<Actions> keyToSet = new AtomicReference<>(Actions.NONE);
        app.getConfiguration().getUserKeyBindings().forEach((action, key) -> {
            JLabel lbActionName = createLabel(action.getDisplayName(), render, TEXT, false);
            JLabel lbKey = createInfoActionLabel(
                            ()->app.getConfiguration().getKeyBinding(action).toString(),
                            render, TEXT, false,
                            ()->!keyToSet.get().equals(Actions.NONE),
                            ()->keyToSet.set(action));
            lbKey.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    int modifier = e.getModifiersEx();
                    int key = e.getKeyCode();
                    if (keyToSet.get().equals(Actions.NONE)) return;
                    if (app.getConfiguration().checkKeyBinding(modifier,key)){
                        keyToSet.set(Actions.NONE);
                        JOptionPane.showMessageDialog(lbKey, "Key already in use!");
                    }else{
                        app.getConfiguration().setKeyBinding(action, new Controller.Key(modifier,key));
                        lbKey.setText(Controller.Key.toString(modifier, key));
                        app.getController().update();
                    }
                    lbKey.setForeground(Color.BLACK);
                    keyToSet.set(Actions.NONE);
                }
            });
            pnBindingL.add(lbActionName);
            pnBindingR.add(lbKey);
        });
        addAll(pnMiddle, Box.createHorizontalGlue(), pnBindingL, pnBindingR, Box.createHorizontalGlue());
        addAll(pnSettings,
                createLabel("Settings", render, TITLE, true),
                Box.createVerticalGlue(),
                pnMiddle,
                Box.createVerticalGlue(),
                createActionLabel("Confirm", render, SUBTITLE, true, ()->cardLayout.show(backPanel, MENU)));

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

    /**
     * Creates a load game panel for a single load
     *
     * @param slot the slot of the load (start at 1)
     * @param app   the app to be used to get the render
     * @return a JPanel with the specified index load game
     */
    private JPanel configureSaveLoadGameSubPanel(int slot, App app, boolean isSave) {
        JPanel pnLoad = createRepeatableBackgroundPanel(TexturePack.Images.Wall, render, BoxLayout.Y_AXIS);
        JPanel pnInfo = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnStatus = createClearPanel(BoxLayout.Y_AXIS);
        JPanel pnInventory = new InventoryPanel(new DomainController(app.getSave(slot)), true);
        JPanel pnOptions = createClearPanel(BoxLayout.X_AXIS);

        // assemble this panel
        setSize(pnInventory, 150,300, 150,300, 150,300);
        addAll(pnInfo, pnStatus, pnInventory);
        addAll(pnLoad,
                createInfoLabel(()->"Slot "+(slot), render, SUBTITLE, true),
                pnInfo,
                pnOptions);
        addAll(pnStatus,
                createInfoLabel(()->"Level: " + app.getSave(slot).getLvl(), render, TEXT, true),
                createInfoLabel(()->"Time Left: " + app.getGameClock().getTimeInMinutes(), render, TEXT, true),
                createInfoLabel(()->"Score: " + app.getSave(slot).getTreasuresLeft(), render, TEXT, true));
        if (isSave){    // Options for Saving
            addAll(pnOptions,
                    Box.createHorizontalGlue(),
                    createActionLabel("Save here!", render, SUBTITLE, true, ()->{
                        try {
                            // TODO: change to saving current game when domain implement getDomain()
                            Persistency.saveDomain(app.getSave(slot), slot);
                            app.refreshSaves();
                            app.repaint();
                        }catch (IOException e){
                            System.out.println("Failed to save game in slot: " + slot);
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "There is an error in saving the game slot: " + slot);
                        }}),
                    Box.createHorizontalGlue());
        }else{  // Options for Loading
            addAll(pnOptions,
                    Box.createHorizontalGlue(),
                    createActionLabel("Resume!", render, SUBTITLE, true, ()->app.startSavedGame(slot)),
                    Box.createHorizontalGlue(),
                    createActionLabel("Replay", render, SUBTITLE, true, ()->app.startSavedReplay(slot)),
                    Box.createHorizontalGlue(),
                    createActionLabel("Delete", render, SUBTITLE, true, ()->{
                        try {
                            Persistency.deleteSave(slot);
                            app.refreshSaves();
                            app.repaint();
                        }catch (Exception e){
                            System.out.println("Failed to delete save file.");
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, "There is an error in saving the game slot: " + slot);
                        }}),
                    Box.createHorizontalGlue());
        }
        return pnLoad;
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
        JLabel lbPauseNormal = createActionLabel("Pause", render,SUBTITLE, true,()->Actions.PAUSE_GAME.run(app));
        JLabel lbPauseReplay = createActionLabel("Pause", render,SUBTITLE, true, ()->Actions.PAUSE_GAME.run(app));
        JLabel lbReplayTitle = createLabel("Replay Mode", render,SUBTITLE, true);
        JLabel lbReplayAuto = createActionLabel("Auto", render,SUBTITLE, true, app::transitionToReplayScreen);
        JLabel lbReplayStep = createActionLabel("Step", render,SUBTITLE, true, app::transitionToReplayScreen);
        JLabel lbInventoryTitle = createLabel("Inventory", mazeRender, SUBTITLE, false);

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
        addAll(pnModeNormal, lbPauseNormal
                // TODO: remove after debugging
                , createActionLabel("win", render, SUBTITLE, true, this::transitionToWinScreen)
                , createActionLabel("lost", render, SUBTITLE, true, this::transitionToLostScreen)
        );
        addAll(pnModeReplay, lbReplayTitle, lbPauseReplay, lbReplayAuto, lbReplayStep);

        addAll(pnStatusTop, lbLevelTitle, lbLevel);
        addAll(pnStatusMid, lbTimerTitle, lbTimer);
        addAll(pnStatusBot, lbTreasuresTitle, lbTreasures);
        addAll(pnInventory, lbInventoryTitle, pnInventories);
        addAll(pnStatus, Box.createVerticalGlue(), pnStatusTop, Box.createVerticalGlue(), pnStatusMid,
                        Box.createVerticalGlue(), pnStatusBot, Box.createVerticalGlue());
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

        addAll(pnOnPause,
                Box.createVerticalGlue(),
                createActionLabel("Resume", render, TITLE, true, ()->Actions.LOAD_GAME.run(app)),
                Box.createVerticalGlue(),
                createActionLabel("Save and return to menu", render, TITLE, true, ()->Actions.SAVE_GAME.run(app)),
                Box.createVerticalGlue(),
                createActionLabel("Quit to menu", render, TITLE, true, app::transitionToMenuScreen),
                Box.createVerticalGlue());
        System.out.println("Done!");
        return pnPause;
    }

    private JPanel configurePanelVictory(App app) {
        System.out.print("Configuring Victory Panel... ");

        JPanel pnVictory = createBackgroundPanel(Images.WinScreen, BoxLayout.Y_AXIS);
        pnVictory.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Actions.SAVE_GAME.run(app);
            }
        });

        System.out.println("Done!");
        return pnVictory;
    }

    private JPanel configurePanelLost(App app) {
        System.out.print("Configuring Lost Panel... ");

        JPanel pnLost = createBackgroundPanel(Images.LoseScreen, BoxLayout.Y_AXIS);
        pnLost.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Actions.SAVE_GAME.run(app);
            }
        });

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
     * Brings up the save game screen.
     */
    public void transitionToLoadPanel(){
        menuCardLayout.show(menuPanel, LOAD_GAME);
        outerCardLayout.show(outerPanel, MENU);
    }

    /**
     * Brings up the save game screen.
     */
    public void transitionToSavePanel(){
        menuCardLayout.show(menuPanel, SAVE_GAME);
        outerCardLayout.show(outerPanel, MENU);
    }

    /**
     * Transitions to the game screen.
     */
    public void transitionToGameScreen(){
        functionCardLayout.show(functionPanel, MODE_NORMAL);
        gameCardLayout.show(gamePanel, GAME);
        outerCardLayout.show(outerPanel, GAME);
        render.grabFocus();
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


