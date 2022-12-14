package nz.ac.vuw.ecs.swen225.gp6.app.gui;

import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.CREDITS;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.EXIT;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.LOAD_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.LOGS;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.LOOSE;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.MENU;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.MODE_NORMAL;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.MODE_REPLAY;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.NEW_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.SAVE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.SETTINGS;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.STATUS_PAUSE;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.STATUS_RESUME;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI.Components.VICTORY;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.SUBTITLE;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.TEXT;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.TITLE;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.addAll;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.creatTransparentPanel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createActionLabel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createBackgroundPanel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createClearPanel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createInfoActionLabel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createInfoLabel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createLabel;
import static nz.ac.vuw.ecs.swen225.gp6.app.gui.SwingFactory.createRepeatableBackgroundPanel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.persistency.DomainPersistency;
import nz.ac.vuw.ecs.swen225.gp6.renderer.InventoryPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.LogPanel;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MazeRenderer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack;
import nz.ac.vuw.ecs.swen225.gp6.renderer.TexturePack.Images;

/**
 * Graphical User Interface class responsible for all GUI components and logic used by the App.
 *
 * @author Jeff Lin
 */
public class GUI extends JFrame {

  /**
   * Minimum Width of the window.
   */
  public static final int WIDTH = 1200;
  /**
   * Minimum Height of the window.
   */
  public static final int HEIGHT = 800;
  private static final LogPanel logPanel = new LogPanel();
  private final JPanel outerPanel = createClearPanel(BoxLayout.Y_AXIS);
  private final JPanel menuPanel = createClearPanel(BoxLayout.Y_AXIS);
  private final JPanel gamePanel = createClearPanel(BoxLayout.Y_AXIS);
  private final JPanel pausePanel = createClearPanel(BoxLayout.Y_AXIS);
  private final JPanel functionPanel = createClearPanel(BoxLayout.Y_AXIS);
  private final CardLayout outerCardLayout = new CardLayout();
  private final CardLayout menuCardLayout = new CardLayout();
  private final CardLayout gameCardLayout = new CardLayout();
  private final CardLayout functionCardLayout = new CardLayout();
  private final CardLayout pauseCardLayout = new CardLayout();
  private final MazeRenderer renderPanel;
  private final InventoryPanel inventoryPanel;
  private final InventoryPanel[] saveInventoryPanels;
  private final InventoryPanel[] loadInventoryPanels;

  /**
   * Constructor for GUI.
   *
   * @param app The App object that is using this
   */
  public GUI(App app) {
    renderPanel = new MazeRenderer(app.getGame());
    inventoryPanel = new InventoryPanel(app.getGame(), true, renderPanel);
    logPanel.setRenderer(renderPanel);
    saveInventoryPanels = new InventoryPanel[]{
        InventoryPanel.of(renderPanel),
        InventoryPanel.of(renderPanel),
        InventoryPanel.of(renderPanel)
    };
    loadInventoryPanels = new InventoryPanel[]{
        InventoryPanel.of(renderPanel),
        InventoryPanel.of(renderPanel),
        InventoryPanel.of(renderPanel)
    };
  }

  /**
   * Gets the log panel of this GUI.
   *
   * @return the log panel
   */
  public static LogPanel getLogPanel() {
    return logPanel;
  }

  /**
   * Configures the App GUI to be displayed.
   *
   * @param app The App to attach the GUI to.
   */
  public void configureGUI(App app) {
    assert SwingUtilities.isEventDispatchThread() : "Not in EDT";

    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setIconImage(TexturePack.Images.Hero.getImg());
    this.setVisible(true);
    this.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent e) {
        System.out.println("Application closed with exit code 0");
        System.exit(0);
      }
    });
    this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
    this.setContentPane(outerPanel);
    outerPanel.setLayout(outerCardLayout);
    menuPanel.setLayout(menuCardLayout);
    gamePanel.setLayout(gameCardLayout);
    functionPanel.setLayout(functionCardLayout);
    pausePanel.setLayout(pauseCardLayout);
    renderPanel.setFocusable(true);
    this.configureMenuScreen(app);
    this.configureGameScreen(app);
    outerPanel.add(menuPanel, MENU.name);
    outerPanel.add(gamePanel, GAME.name);
    this.pack();
  }

  /**
   * Creates the menu screen.
   *
   * @param app The App object.
   */
  void configureMenuScreen(App app) {
    menuPanel.add(configurePanelMenu(app), MENU.name);
    menuPanel.add(configurePanelLoad(app), LOAD_GAME.name);
    menuPanel.add(configurePanelSave(app), SAVE_GAME.name);
    menuPanel.add(configurePanelSettings(app), SETTINGS.name);
    menuPanel.add(configurePanelCredits(), CREDITS.name);
    menuPanel.add(configurePanelExit(), EXIT.name);
  }

  /**
   * Creates the Game Screen panel.
   *
   * @param app The App object.
   */
  void configureGameScreen(App app) {
    gamePanel.add(configurePanelGame(app), GAME.name);
    gamePanel.add(configurePanelLost(app), LOOSE.name);
    gamePanel.add(configurePanelVictory(app), VICTORY.name);
  }

  //==============================================================================================//
  //==================================== Menu Panels =============================================//
  //==============================================================================================//

  private JPanel configurePanelMenu(App app) {
    System.out.print("Configuring Menu Panel... ");

    final JPanel pnMenu = createBackgroundPanel(Images.Background, BoxLayout.Y_AXIS);
    // assemble the panel
    pnMenu.add(Box.createVerticalGlue());
    List.of(createActionLabel(NEW_GAME.name, renderPanel, SUBTITLE, true, app::startNewGame),
        createActionLabel(LOAD_GAME.name, renderPanel, SUBTITLE, true,
            () -> transitionToLoadScreen(app)),
        createActionLabel(SETTINGS.name, renderPanel, SUBTITLE, true,
            () -> menuCardLayout.show(menuPanel, SETTINGS.name)),
        createActionLabel(LOGS.name, renderPanel, SUBTITLE, true, () -> {
          JFrame frame = new JFrame("Logs");
          SwingFactory.setSize(frame, 500, 500, 500, 500, 500, 500);
          frame.add(logPanel);
          frame.setVisible(true);
        }),
        createActionLabel(CREDITS.name, renderPanel, SUBTITLE, true,
            () -> menuCardLayout.show(menuPanel, CREDITS.name)),
        createActionLabel(EXIT.name, renderPanel, SUBTITLE, true,
            () -> menuCardLayout.show(menuPanel, EXIT.name))
    ).forEach(pnMenu::add);

    System.out.println("Done!");
    return pnMenu;
  }

  private JPanel configurePanelLoad(App app) {
    System.out.print("Configuring Load Panel... ");

    final JPanel pnLoad = createRepeatableBackgroundPanel(Images.Pattern_2, renderPanel,
        BoxLayout.Y_AXIS);
    final JPanel pnLoadGame = createClearPanel(BoxLayout.X_AXIS);
    // assemble this panel
    addAll(pnLoadGame,
        Box.createHorizontalGlue(),
        configureSaveLoadGameSubPanel(app, 1, false),
        Box.createHorizontalGlue(),
        configureSaveLoadGameSubPanel(app, 2, false),
        Box.createHorizontalGlue(),
        configureSaveLoadGameSubPanel(app, 3, false),
        Box.createHorizontalGlue());
    addAll(pnLoad,
        createLabel("Load and Resume Games", renderPanel, TITLE, true),
        Box.createVerticalGlue(),
        pnLoadGame,
        Box.createVerticalGlue(),
        createActionLabel("Return to menu", renderPanel, SUBTITLE, true,
            this::transitionToMenuScreen));

    System.out.println("Done!");
    return pnLoad;
  }

  private JPanel configurePanelSave(App app) {
    System.out.print("Configuring Save Panel... ");

    final JPanel pnSave = createRepeatableBackgroundPanel(Images.Pattern_2, renderPanel,
        BoxLayout.Y_AXIS);
    final JPanel pnSaveGame = createClearPanel(BoxLayout.X_AXIS);
    // assemble this panel
    addAll(pnSaveGame,
        Box.createHorizontalGlue(),
        configureSaveLoadGameSubPanel(app, 1, true),
        Box.createHorizontalGlue(),
        configureSaveLoadGameSubPanel(app, 2, true),
        Box.createHorizontalGlue(),
        configureSaveLoadGameSubPanel(app, 3, true),
        Box.createHorizontalGlue());
    addAll(pnSave,
        createLabel("Save the current game!", renderPanel, TITLE, true),
        Box.createVerticalGlue(),
        pnSaveGame,
        Box.createVerticalGlue(),
        createActionLabel("Return to Menu", renderPanel, SUBTITLE, true,
            this::transitionToMenuScreen));

    System.out.println("Done!");
    return pnSave;
  }

  /**
   * Creates a load game panel for a single load.
   *
   * @param app  the app to be used to get the render
   * @param slot the slot of the load (start at 1)
   * @return a JPanel with the specified index load game
   */
  private JPanel configureSaveLoadGameSubPanel(App app, int slot, boolean isSave) {
    final JPanel pnLoad = createRepeatableBackgroundPanel(TexturePack.Images.Wall, renderPanel,
        BoxLayout.Y_AXIS);
    final JPanel pnInfo = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnStatus = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnOptions = createClearPanel(BoxLayout.X_AXIS);

    // assemble this panel
    addAll(pnLoad,
        createInfoLabel(() -> "Slot " + (slot), renderPanel, SUBTITLE, true),
        pnInfo,
        pnOptions);
    addAll(pnStatus,
        createInfoLabel(() -> "Level: " + app.getSave(slot).getCurrentLevel(), renderPanel, TEXT,
            true),
        createInfoLabel(() -> "Treasures Left: " + app.getSave(slot).getTreasuresLeft(),
            renderPanel, TEXT, true));
    if (isSave) {    // Options for Saving
      JPanel pnSaveInv = saveInventoryPanels[slot - 1];
      SwingFactory.setSize(pnSaveInv, 150, 300, 150, 300, 150, 300);
      addAll(pnInfo, pnStatus, pnSaveInv);
      addAll(pnOptions,
          Box.createHorizontalGlue(),
          createActionLabel("Save here!", renderPanel, SUBTITLE, true, () -> {
            try {
              app.getGame().setCurrentTime(app.getGameClock().getTimePlayed());
              DomainPersistency.save(app.getGame(), slot);
              app.getRecorder().saveRecording(slot);
              app.refreshSaves();
              this.repaint();
            } catch (IOException e) {
              System.out.println("Failed to save game in slot: " + slot);
              e.printStackTrace();
              JOptionPane.showMessageDialog(null,
                  "There is an error in saving the game slot: " + slot);
            }
          }),
          Box.createHorizontalGlue());
    } else {  // Options for Loading
      JPanel pnLoadInv = loadInventoryPanels[slot - 1];
      SwingFactory.setSize(pnLoadInv, 150, 300, 150, 300, 150, 300);
      addAll(pnInfo, pnStatus, pnLoadInv);
      addAll(pnOptions,
          Box.createHorizontalGlue(),
          createActionLabel("Resume!", renderPanel, TEXT, true, () -> {
            if (app.getSave(slot).getGameState() != Domain.GameState.PLAYING) {
              JOptionPane.showMessageDialog(null, "This save is for replay only!");
            } else {
              app.startSavedGame(slot);
            }
          }),
          Box.createHorizontalGlue(),
          createActionLabel("Replay", renderPanel, TEXT, true, () -> app.startSavedReplay(slot)),
          Box.createHorizontalGlue(),
          createActionLabel("Delete", renderPanel, TEXT, true, () -> {
            try {
              DomainPersistency.delete(slot);
              app.refreshSaves();
              this.repaint();
            } catch (Exception e) {
              System.out.println("Failed to delete save file.");
              e.printStackTrace();
              JOptionPane.showMessageDialog(null,
                  "There is an error in saving the game slot: " + slot);
            }
          }),
          Box.createHorizontalGlue());
    }
    return pnLoad;
  }

  private JPanel configurePanelSettings(App app) {
    System.out.print("Configuring Settings Panel... ");

    final JPanel pnSettings = createRepeatableBackgroundPanel(Images.Pattern_2, renderPanel,
        BoxLayout.Y_AXIS);
    final JPanel pnMiddle = createClearPanel(BoxLayout.X_AXIS);
    final JPanel pnBindingL = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnBindingR = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnTexturePack = createClearPanel(BoxLayout.X_AXIS);
    final JPanel pnViewDistance = createClearPanel(BoxLayout.X_AXIS);

    // setting layout
    pnBindingL.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
    pnBindingR.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

    // assemble this panel
    addAll(pnViewDistance,
        createActionLabel("<<<  ", renderPanel, TEXT, false,
            () -> runAndRepaint(renderPanel::decreaseViewDistance).run()),
        createInfoLabel(() -> renderPanel.getRenderSize() + "", renderPanel, TEXT, true),
        createActionLabel("  >>>", renderPanel, TEXT, false,
            () -> runAndRepaint(renderPanel::increaseViewDistance).run()));
    addAll(pnTexturePack,
        createActionLabel("<<<  ", renderPanel, TEXT, false,
            () -> runAndRepaint(renderPanel::usePrevTexturePack).run()),
        createInfoLabel(() -> renderPanel.getCurrentTexturePack().getName() + "", renderPanel, TEXT,
            false),
        createActionLabel("  >>>", renderPanel, TEXT, false,
            () -> runAndRepaint(renderPanel::useNextTexturePack).run()));
    addAll(pnBindingL,
        createLabel("Play Sound", renderPanel, TEXT, false),
        createLabel("View Distance", renderPanel, TEXT, false),
        createLabel("Texture Pack", renderPanel, TEXT, false));
    addAll(pnBindingR,
        createInfoActionLabel(() -> app.getConfiguration().isMusicOn() ? "On" : "Off", renderPanel,
            TEXT, false, () -> false,
            () -> {
              app.getConfiguration().setMusicOn(!app.getConfiguration().isMusicOn());
              if (app.getConfiguration().isMusicOn()) {
                MusicPlayer.playMusic();
              } else {
                MusicPlayer.stopMusic();
              }
            }),
        pnViewDistance,
        pnTexturePack);
    AtomicReference<Actions> keyToSet = new AtomicReference<>(Actions.NONE);
    app.getConfiguration().getUserKeyBindings().forEach((action, key) -> {
      JLabel lbActionName = createLabel(action.getDisplayName(), renderPanel, TEXT, false);
      JLabel lbKey = createInfoActionLabel(
          () -> app.getConfiguration().getKeyBinding(action).toString(),
          renderPanel, TEXT, false,
          () -> !keyToSet.get().equals(Actions.NONE),
          () -> keyToSet.set(action));
      lbKey.addKeyListener(new KeyAdapter() {
        public void keyReleased(KeyEvent e) {
          int modifier = e.getModifiersEx();
          int key = e.getKeyCode();
          if (keyToSet.get().equals(Actions.NONE)) {
            return;
          }
          if (app.getConfiguration().checkKeyBinding(modifier, key)) {
            keyToSet.set(Actions.NONE);
            JOptionPane.showMessageDialog(lbKey, "Key already in use!");
          } else {
            app.getConfiguration().setKeyBinding(action, new Controller.Key(modifier, key));
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
    addAll(pnMiddle, Box.createHorizontalGlue(), pnBindingL, pnBindingR,
        Box.createHorizontalGlue());
    addAll(pnSettings,
        createLabel("Settings", renderPanel, TITLE, true),
        Box.createVerticalGlue(),
        pnMiddle,
        Box.createVerticalGlue(),
        createActionLabel("Confirm", renderPanel, SUBTITLE, true, () -> {
          app.getConfiguration().save(app);
          menuCardLayout.show(menuPanel, MENU.name);
        }));

    System.out.println("Done!");
    return pnSettings;
  }

  private JPanel configurePanelCredits() {
    System.out.print("Configuring Credits Panel... ");

    final JPanel pnCredits = createRepeatableBackgroundPanel(Images.Pattern_2, renderPanel,
        BoxLayout.Y_AXIS);
    // assemble this panel
    addAll(pnCredits, createLabel("Credits", renderPanel, TITLE, true), Box.createVerticalGlue());
    addAll(pnCredits, new JLabel[]{
        createLabel("App: Jeff", renderPanel, SUBTITLE, true),
        createLabel("Domain: Matty", renderPanel, SUBTITLE, true),
        createLabel("Fuzz: Ray", renderPanel, SUBTITLE, true),
        createLabel("Persistency: Ben", renderPanel, SUBTITLE, true),
        createLabel("Recorder: Jayden", renderPanel, SUBTITLE, true),
        createLabel("Renderer: Loki", renderPanel, SUBTITLE, true),
    });
    addAll(pnCredits, Box.createVerticalGlue(),
        createActionLabel("Back", renderPanel, SUBTITLE, true,
            () -> menuCardLayout.show(menuPanel, MENU.name)));

    System.out.println("Done!");
    return pnCredits;
  }

  private JPanel configurePanelExit() {
    System.out.print("Configuring Exit Panel... ");

    final JPanel pnExit = createRepeatableBackgroundPanel(Images.Pattern_2, renderPanel,
        BoxLayout.Y_AXIS);
    final JPanel pnOption = createClearPanel(BoxLayout.X_AXIS);
    // combine all components
    addAll(pnOption, Box.createHorizontalGlue(),
        createActionLabel("No", renderPanel, SUBTITLE, true,
            () -> menuCardLayout.show(menuPanel, MENU.name)),
        Box.createHorizontalGlue(),
        createActionLabel("Yes", renderPanel, SUBTITLE, true, () -> {
          System.out.println("Exiting...");
          System.exit(0);
        }),
        Box.createHorizontalGlue());
    addAll(pnExit,
        createLabel("Chaps Challenge!", renderPanel, TITLE, true),
        Box.createVerticalGlue(),
        createLabel("Are you sure you want to exit?", renderPanel, SUBTITLE, true),
        Box.createVerticalGlue(),
        pnOption,
        Box.createVerticalGlue());

    System.out.println("Done!");
    return pnExit;
  }

  //==============================================================================================//
  //==================================== Game Panels =============================================//
  //==============================================================================================//

  private JPanel configurePanelGame(App app) {
    System.out.print("Configuring Game Panel... ");

    // outermost panel
    final JPanel pnGame = createRepeatableBackgroundPanel(Images.Pattern, renderPanel,
        BoxLayout.X_AXIS);
    // 3 panels on top of outermost panel: left/mid/right
    final JPanel pnStatus = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnMaze = createClearPanel(new GridBagLayout());
    final JPanel pnRight = createClearPanel(BoxLayout.Y_AXIS);
    // inner panels for panels: left/mid/right
    final JPanel pnStatusTop = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnStatusMid = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnStatusBot = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnModeNormal = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnModeReplay = createClearPanel(BoxLayout.Y_AXIS);
    final JPanel pnInventory = createClearPanel(BoxLayout.Y_AXIS);

    // setting size
    final int width = 75 * 2;
    final int height = 75 * 4;
    pnStatus.setMaximumSize(new Dimension(200, 1000));
    pnRight.setMaximumSize(new Dimension(200, 1000));
    SwingFactory.setSize(renderPanel, 700, 700, 600, 600, 800, 800);
    SwingFactory.setSize(pnMaze, 700, 700, 600, 600, 800, 800);
    SwingFactory.setSize(pnInventory, width, height, width, height, width, height);
    SwingFactory.setSize(functionPanel, 200, 200, 200, 200, 200, 200);
    pnInventory.setAlignmentX(Component.CENTER_ALIGNMENT);
    renderPanel.setLayout(new GridBagLayout());

    functionPanel.add(pnModeNormal, MODE_NORMAL.name);
    functionPanel.add(pnModeReplay, MODE_REPLAY.name);
    pnModeNormal.add(
        createActionLabel("Pause", renderPanel, SUBTITLE, true, () -> Actions.PAUSE_GAME.run(app)));
    JPanel replaySpeed = createClearPanel(BoxLayout.X_AXIS);
    AtomicReference<Float> speed = new AtomicReference<>(1F);
    addAll(replaySpeed,
        createActionLabel("<<", renderPanel, TEXT, true,
            () -> {
              if (speed.get() <= 0.25) {
                return;
              }
              speed.set(speed.get() - 0.25F);
              app.getReplay().speedMultiplier(speed.get());
              replaySpeed.repaint();
            }
        ),
        createInfoLabel(() -> "" + speed.get(), renderPanel, TEXT, true),
        createActionLabel(">>", renderPanel, TEXT, true,
            () -> {
              if (speed.get() >= 10.0F) {
                return;
              }
              speed.set(speed.get() + 0.25F);
              app.getReplay().speedMultiplier(speed.get());
              replaySpeed.repaint();
            }
        )
    );
    addAll(pnModeReplay,
        createActionLabel("Menu", renderPanel, TEXT, true, () -> Actions.QUIT_TO_MENU.run(app)),
        createActionLabel("Pause", renderPanel, TEXT, true, () -> app.getReplay().stopReplay()),
        createActionLabel("Auto", renderPanel, TEXT, true, () -> app.getReplay().autoPlay()),
        createActionLabel("Step", renderPanel, TEXT, true, () -> app.getReplay().step()),
        replaySpeed);

    addAll(pnStatusTop,
        createLabel("Level", renderPanel, SUBTITLE, false),
        createInfoLabel(() -> app.getGame().getCurrentLevel() + "", renderPanel, SUBTITLE, false));
    addAll(pnStatusMid,
        createLabel("Time", renderPanel, SUBTITLE, false),
        createInfoLabel(app.getGameClock()::getTimeInMinutes, renderPanel, SUBTITLE, false));
    addAll(pnStatusBot,
        createLabel("Treasures", renderPanel, SUBTITLE, false),
        createInfoLabel(() -> app.getGame().getTreasuresLeft() + "", renderPanel, SUBTITLE, false));
    addAll(pnInventory,
        createLabel("Inventory", renderPanel, SUBTITLE, false), inventoryPanel);
    addAll(pnStatus, Box.createVerticalGlue(), pnStatusTop, Box.createVerticalGlue(), pnStatusMid,
        Box.createVerticalGlue(), pnStatusBot, Box.createVerticalGlue());
    renderPanel.add(configurePanelPause(app));
    pnMaze.add(renderPanel);
    addAll(pnRight, Box.createVerticalGlue(), functionPanel, Box.createVerticalGlue(), pnInventory,
        Box.createVerticalGlue());
    addAll(pnGame, Box.createHorizontalGlue(), pnStatus, Box.createHorizontalGlue(), pnMaze,
        Box.createHorizontalGlue(),
        pnRight, Box.createHorizontalGlue());

    System.out.println("Done!");
    return pnGame;
  }

  private JPanel configurePanelPause(App app) {
    System.out.print("Configuring Pause Panel... ");

    final JPanel pnOnPause = creatTransparentPanel(Images.Empty_tile, 0.8f);
    final JPanel pnOnResume = createClearPanel(BoxLayout.Y_AXIS);
    pausePanel.add(pnOnResume, STATUS_RESUME.name);
    pausePanel.add(pnOnPause, STATUS_PAUSE.name);

    addAll(pnOnPause,
        Box.createVerticalGlue(),
        createActionLabel("Resume", renderPanel, TITLE, true, () -> Actions.RESUME_GAME.run(app)),
        Box.createVerticalGlue(),
        createActionLabel("Save and return to menu", renderPanel, TITLE, true,
            () -> Actions.SAVE_GAME.run(app)),
        Box.createVerticalGlue(),
        createActionLabel("Quit to menu", renderPanel, TITLE, true,
            () -> Actions.QUIT_TO_MENU.run(app)),
        Box.createVerticalGlue());
    System.out.println("Done!");
    return pausePanel;
  }

  private JPanel configurePanelVictory(App app) {
    System.out.print("Configuring Victory Panel... ");

    final JPanel pnVictory = createBackgroundPanel(Images.WinScreen, BoxLayout.Y_AXIS);
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

    final JPanel pnLost = createBackgroundPanel(Images.LoseScreen, BoxLayout.Y_AXIS);
    pnLost.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        Actions.SAVE_GAME.run(app);
      }
    });

    System.out.println("Done!");
    return pnLost;
  }

  //==============================================================================================//
  //================================== Helper Methods ============================================//
  //==============================================================================================//

  private Runnable runAndRepaint(Runnable runnable) {
    return () -> {
      runnable.run();
      this.repaint();
    };
  }

  /**
   * Updates the current save inventory panel to the correct one.
   *
   * @param index the index of the inventory to be displayed
   * @param save  the save to be displayed
   */
  public void updateSaveInventory(int index, Domain save) {
    saveInventoryPanels[index].setMaze(save);
    loadInventoryPanels[index].setMaze(save);
  }

  //==============================================================================================//
  //================================== Getter Methods ============================================//
  //==============================================================================================//

  /**
   * Gets the Maze Renderer of this GUI.
   *
   * @return the Maze Renderer
   */
  public MazeRenderer getRenderPanel() {
    return renderPanel;
  }

  /**
   * Gets the Inventory Renderer of this GUI.
   *
   * @return the Inventory Renderer
   */
  public InventoryPanel getInventory() {
    return inventoryPanel;
  }

  /**
   * Transitions to the menu screen.
   */
  public void transitionToMenuScreen() {
    functionCardLayout.show(functionPanel, MODE_NORMAL.name);
    menuCardLayout.show(menuPanel, MENU.name);
    outerCardLayout.show(outerPanel, MENU.name);
  }

  //==============================================================================================//
  //================================ Transition Methods ==========================================//
  //==============================================================================================//

  /**
   * Brings up the save game screen.
   *
   * @param app the app to be loaded
   */
  public void transitionToLoadScreen(App app) {
    app.refreshSaves();
    menuCardLayout.show(menuPanel, LOAD_GAME.name);
    outerCardLayout.show(outerPanel, MENU.name);
  }

  /**
   * Brings up the save game screen.
   *
   * @param app the app to be saved
   */
  public void transitionToSaveScreen(App app) {
    app.refreshSaves();
    menuCardLayout.show(menuPanel, SAVE_GAME.name);
    outerCardLayout.show(outerPanel, MENU.name);
  }

  /**
   * Transitions to the game screen.
   */
  public void transitionToGameScreen() {
    functionCardLayout.show(functionPanel, MODE_NORMAL.name);
    pauseCardLayout.show(pausePanel, STATUS_RESUME.name);
    gameCardLayout.show(gamePanel, GAME.name);
    outerCardLayout.show(outerPanel, GAME.name);
    renderPanel.grabFocus();
  }

  /**
   * Transitions to the game screen.
   */
  public void transitionToReplayScreen() {
    functionCardLayout.show(functionPanel, MODE_REPLAY.name);
    pauseCardLayout.show(pausePanel, STATUS_RESUME.name);
    gameCardLayout.show(gamePanel, GAME.name);
    outerCardLayout.show(outerPanel, GAME.name);
  }

  /**
   * Transitions to the winning screen.
   */
  public void transitionToWinScreen() {
    gameCardLayout.show(gamePanel, VICTORY.name);
    outerCardLayout.show(outerPanel, GAME.name);
  }

  /**
   * Transitions to the losing screen.
   */
  public void transitionToLostScreen() {
    gameCardLayout.show(gamePanel, LOOSE.name);
    outerCardLayout.show(outerPanel, GAME.name);
  }

  /**
   * Brings up the pause screen.
   */
  public void showPausePanel() {
    pauseCardLayout.show(pausePanel, STATUS_PAUSE.name);
  }

  /**
   * Hides the pause screen and continue with resume screen.
   */
  public void showResumePanel() {
    pauseCardLayout.show(pausePanel, STATUS_RESUME.name);
  }

  enum Components {
    MENU("Menu"),
    NEW_GAME("Start New Game!"),
    LOAD_GAME("Load Game"),
    SAVE_GAME("Save Game"),
    SETTINGS("Settings"),
    LOGS("Logs"),
    CREDITS("Credits"),
    EXIT("Exit"),

    GAME("Game"),
    LOOSE("Loose"),
    VICTORY("Victory"),

    STATUS_PAUSE("Pause"),
    STATUS_RESUME("Resume"),
    MODE_REPLAY("Replay"),
    MODE_NORMAL("Normal");

    public final String name;

    Components(String name) {
      this.name = name;
    }
  }
}