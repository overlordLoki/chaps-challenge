package test.nz.ac.vuw.ecs.swen225.gp6.fuzz;

import static java.awt.event.KeyEvent.VK_W;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.LOAD_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_DOWN;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_LEFT;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_RIGHT;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_UP;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.PAUSE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.QUIT_TO_MENU;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.RESUME_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.SAVE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.TO_LEVEL_1;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.TO_LEVEL_2;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import org.junit.Test;

/**
 * This class is used to fuzz test the game. It will randomly generate a sequence of actions and
 * then play them. It will then check if the game has crashed. It will then repeat this process a
 * number of times. It will then print out the number of crashes.
 */
public class FuzzTest {

  /**
   * The File path of random integers 'r'.
   */
  static final Random r = new Random();
  /**
   * Setup App object and Robot object for test environment. actionsList is a list will store all
   * the actions for robot to get randomly.
   */
  static App app;
  static Robot robot;
  static List<Actions> actionsList = List.of();
  /**
   * This method is just hardcode to test level 1 can be passed.
   */
  static List<Actions> test1HC = new ArrayList<>();

  static {
    try {
      robot = new Robot();
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * This method is used to get the random action from the actionsList.
   */
  public static void showLog() {
    //set the log panel visible during the test
    JFrame frame = new JFrame("Logs");
    frame.setPreferredSize(new Dimension(500, 500));
    frame.setMinimumSize(new Dimension(500, 500));
    frame.setMaximumSize(new Dimension(500, 500));
    frame.add(GUI.getLogPanel());
    frame.setVisible(true);
  }

  /**
   * This method is used to when test get the random action 'PAUSE_GAME',
   * it will try to test SAVE_GAME in the pause menu.
   *
   */
  public static void pausesStrategy(App app) throws AWTException {
    SAVE_GAME.run(app);
    System.out.println("s = 2");
    robot.delay(1000);

    mouseTest(200, 650);
    System.out.println("Saved game: 2");
    robot.delay(1000);

    QUIT_TO_MENU.run(app);
    System.out.println("Quit to menu");
    robot.delay(1000);

    LOAD_GAME.run(app);
    app.startSavedGame(1);
    System.out.println("Loaded game: 2");
    robot.delay(1000);
  }

  /**
   * This method is used to get the random action from the actionsList.
   */
  public static void testLevel1() {
    //initialize the app environment, set all the actions into a list
    SwingUtilities.invokeLater(() -> {
      app = new App();
      actionsList = List.of(
          MOVE_UP,
          MOVE_DOWN,
          MOVE_LEFT,
          MOVE_RIGHT,
          PAUSE_GAME,
          RESUME_GAME
      );
    });
    // Start robot automating sequence
    JOptionPane.showMessageDialog(null, "Start Fuzzing");
    long startTime = System.currentTimeMillis();
    app.startNewGame();
    app.transitionToGameScreen();

    // after 1 minute, the test closed
    while (true) {
      testingStuff();
      // after 1 minute, the test closed
      if (System.currentTimeMillis() - startTime > 60000) {
        System.out.println("1 min Test finished");
        System.exit(0);
      }
    }
  }

  private static void testingStuff() {
    showLog();
    while (true) {
      //get a random action from the action list
      int randomIndex = r.nextInt(actionsList.size());
      Actions action = actionsList.get(randomIndex);
      if (action == PAUSE_GAME) {
        if (r.nextInt(50) == 0) {
          PAUSE_GAME.run(app);
          robot.delay(1000);
          System.out.println("Paused game");
          RESUME_GAME.run(app);
        } else {
          continue;
        }
      } else {
        action.run(app);
      }
      System.out.print("Action: " + actionsList.get(randomIndex) + " >>> \n");
      robot.delay(100);

      if (app.getGame().getGameState().name().equals("LOST") || app.getGame().getGameState().name()
          .equals("WON")) {
        break;
      }
    }
  }

  /**
   * This method is used to get the random action from the actionsList.
   */
  public static void testLevel2() {
    //initialize the app environment, set all the actions into a list
    SwingUtilities.invokeLater(() -> {
      app = new App();
      actionsList = List.of(
          MOVE_UP,
          MOVE_DOWN,
          MOVE_LEFT,
          MOVE_RIGHT,
          PAUSE_GAME,
          RESUME_GAME
      );
    });
    // Start robot automating sequence
    JOptionPane.showMessageDialog(null, "Start Fuzzing");
    app.startNewGame();
    app.transitionToGameScreen();
    TO_LEVEL_2.run(app);
    long startTime = System.currentTimeMillis();

    // after 1 minute, the test closed
    while (true) {
      testingStuff();
      // after 1 minute, the test closed
      if (System.currentTimeMillis() - startTime > 60000) {
        System.out.println("1 min Test finished");
        System.exit(0);
      }
    }
  }

  /**
   * This method is used to keep testing in one level.
   */
  public static void unlimitedTest() throws AWTException {
    SwingUtilities.invokeLater(() -> {
      app = new App();
      app.startNewGame();
      app.transitionToGameScreen();
      actionsList = List.of(
          MOVE_UP,
          MOVE_DOWN,
          MOVE_LEFT,
          MOVE_RIGHT,
          PAUSE_GAME,
          RESUME_GAME,
          TO_LEVEL_2,
          TO_LEVEL_1
      );
    });

    robot.delay(1000); // wait 1 second before start testing
    while (true) {
      //get a random action from the action list
      int randomIndex = r.nextInt(actionsList.size());
      Actions action = actionsList.get(randomIndex);
      if (action == PAUSE_GAME) {
        if (r.nextInt(50) == 0) {
          PAUSE_GAME.run(app);
          robot.delay(1000);
          System.out.println("Paused game");
          pausesStrategy(app);
        } else {
          continue;
        }
      } else if (action == TO_LEVEL_2) {
        if (r.nextInt(50) == 0) {
          TO_LEVEL_2.run(app);
          robot.delay(1000);
          System.out.println("To level 2");
        } else {
          continue;
        }
      } else if (action == TO_LEVEL_1) {
        if (r.nextInt(50) == 0) {
          TO_LEVEL_1.run(app);
          robot.delay(1000);
          System.out.println("To level 1");
        } else {
          continue;
        }
      } else {
        action.run(app);
      }

      System.out.print("Action: " + actionsList.get(randomIndex) + " >>> \n");
      robot.delay(100);

      if (app.getGame().getGameState().name().equals("LOST") || app.getGame().getGameState().name()
          .equals("WON")) {
        break;
      }
    }
    JOptionPane.showMessageDialog(null, "First level test Complete");
    System.exit(0);

  }

  /**
   * This method is used to test specific key combination.
   */
  public static void switchKeyTest(int switchLevel) throws AWTException {
    SwingUtilities.invokeLater(() -> {
      app = new App();
      app.startNewGame();
      app.transitionToGameScreen();
    });
    JOptionPane.showMessageDialog(null, "Start Fuzzing");
    Robot robot = new Robot();
    robot.setAutoDelay(100);

    robot.keyPress(KeyEvent.VK_UP);
    key_Event_Print(KeyEvent.VK_UP);
    robot.keyRelease(KeyEvent.VK_UP);

    robot.keyPress(VK_W);
    key_Event_Print(VK_W);
    robot.keyRelease(VK_W);

    try {
      if (switchLevel == 1) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_1);
        robot.setAutoDelay(100);

        key_Event_Print(KeyEvent.VK_1);
        robot.keyRelease(KeyEvent.VK_1);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        System.out.println(KeyEvent.getKeyText(KeyEvent.VK_CONTROL) + "and "
            + KeyEvent.getKeyText(KeyEvent.VK_1) + " pressed");

      } else if (switchLevel == 2) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_2);
        robot.setAutoDelay(100);
        key_Event_Print(KeyEvent.VK_2);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_2);

        System.out.println(KeyEvent.getKeyText(KeyEvent.VK_CONTROL) + "and "
            + KeyEvent.getKeyText(KeyEvent.VK_2) + " pressed");
      }

    } catch (Exception e) {
      System.out.println("The Switch Level should be 1 or 2" + e);
    }
  }

  /**
   * This method is used to test specific mouse combination.
   */
  public static void mouseTest(int x, int y) throws AWTException {
    Robot robot = new Robot();
    robot.setAutoDelay(100);

    robot.mouseMove(x, y);
    System.out.println("Mouse move to: " + x + ", " + y);

    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    System.out.println("Mouse press");
    robot.delay(1000);
    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

    if (app.getSave(1) != null) {
      throw new AWTException("Save not successful");
    }

  }

  /**
   * This method is used hardCode to test first level.
   *
   */
  public static void hardCode() {
    SwingUtilities.invokeLater(() -> {
      app = new App();

      app.startNewGame();
      app.transitionToGameScreen();
      actionsList = List.of(
          MOVE_UP,
          MOVE_DOWN,
          MOVE_LEFT,
          MOVE_RIGHT
      );
    });

    JOptionPane.showMessageDialog(null, "Start Fuzzing");

    test1HC.add(MOVE_UP);
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 3).forEach(i -> test1HC.add(MOVE_DOWN));

    IntStream.range(0, 7).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_LEFT));

    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 10).forEach(i -> test1HC.add(MOVE_LEFT));

    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 3).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_DOWN));

    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_UP));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 8).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_UP));

    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_RIGHT));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_UP));
    test1HC.forEach(a -> {
      a.run(app);
      robot.delay(100);
    });

  }

  /**
   * This method is used to test does the music switch off will keep
   * until player pass first level.
   *
   */
  public static void music_test() {
    SwingUtilities.invokeLater(() -> {
      app = new App();
      app.startNewGame();
      app.transitionToGameScreen();
      app.getConfiguration().setMusicOn(false);
      actionsList = List.of(
          MOVE_UP,
          MOVE_DOWN,
          MOVE_LEFT,
          MOVE_RIGHT
      );
      System.out.println("Music 01: " + app.getConfiguration().isMusicOn());
    });
    JOptionPane.showMessageDialog(null, "Start Fuzzing");

    System.out.println("Music 02: " + app.getConfiguration().isMusicOn());

    test1HC.add(MOVE_UP);
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 3).forEach(i -> test1HC.add(MOVE_DOWN));

    IntStream.range(0, 7).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_LEFT));

    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 10).forEach(i -> test1HC.add(MOVE_LEFT));

    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 3).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_DOWN));

    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_UP));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 8).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_UP));

    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_RIGHT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 4).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_UP));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_LEFT));
    IntStream.range(0, 1).forEach(i -> test1HC.add(MOVE_RIGHT));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_DOWN));
    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_RIGHT));

    IntStream.range(0, 2).forEach(i -> test1HC.add(MOVE_UP));
    test1HC.forEach(a -> {
      a.run(app);
      robot.delay(100);
    });
    System.out.println("Music 03: " + app.getConfiguration().isMusicOn());
  }

  /**
   * This method is used to print out the key event message what App get and action done.
   */
  public static void key_Event_Print(int e) {
    System.out.println("Action key pressed: " + KeyEvent.getKeyText(e));
  }

  /**
   * Main method to run different test.
   */
  public static void main(String[] args) throws AWTException {

    //this panel is used to show the fuzz test method
    String[] buttons = {"hardCode", "testLevel2", "testLevel1", "unlimited", "Cancel"};
    switch (JOptionPane.showOptionDialog(null, "Choose a test", "Test",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[4])) {
      case 0 -> hardCode();
      case 1 -> testLevel2();
      case 2 -> testLevel1();
      case 3 -> unlimitedTest();
      default -> System.exit(0);
    }

    //exit the game after test complete
    System.out.println("All Tests Complete");
    JOptionPane.showMessageDialog(null, "All Tests Complete");
    System.exit(0);
  }

  @Test
  public void test_level1() {
    testLevel1();
  }

  @Test
  public void test_level2() {
    testLevel2();
  }

  @Test
  public void test_music_setting() {
    music_test();
    assert (!app.getConfiguration().isMusicOn());
  }

  @Test
  public void test_hardcode() {
    hardCode();
    assert app.getGame().getCurrentLevel() == 2;
  }

  @Test
  public void test_switchLevel1() throws AWTException {
    switchKeyTest(2);
    assert app.getGame().getCurrentLevel() == 2;
  }

  @Test
  public void test_switchLevel2() throws AWTException {
    switchKeyTest(1);
    assert app.getGame().getCurrentLevel() == 1;
  }
}