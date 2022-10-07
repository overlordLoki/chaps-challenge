package test.nz.ac.vuw.ecs.swen225.gp6.fuzz;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.Main;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.awt.event.KeyEvent.*;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.*;

/**
 * This class is used to fuzz test the game.
 * It will randomly generate a sequence of actions and then play them.
 * It will then check if the game has crashed.
 * It will then repeat this process a number of times.
 * It will then print out the number of crashes.
 */
public class Fuzz {
    /**
     * The File path of random integers 'r'
     */
    static final Random r = new Random();
    /**
     * Setup App object and Robot object for test environment
     * actionsList is a list will store all the actions for robot to get redomly
     */
    static App app;
    static Robot robot;
    static List<Actions> actionsList = List.of();
    /**
     * Initialize the test robot
     */
    static {
        try {robot = new Robot();}
        catch (AWTException e) {throw new RuntimeException(e);}
    }

    /**
     * This method is used to generate a random action
     * @Input: int number of actions for test
     * @return a random action in App we initialized
     */
    public static void testLevel(int numOfInputs){
        SwingUtilities.invokeLater(()->{
            app = new App();
            actionsList = List.of(
                    MOVE_UP,
                    MOVE_DOWN,
                    MOVE_LEFT,
                    MOVE_RIGHT
            );
        });
        // Start robot automating sequence
        JOptionPane.showMessageDialog(null,"Start Fuzzing");
        app.startNewGame();
        app.transitionToGameScreen();
        for (int i =0; i < numOfInputs; i++){
            int randomIndex = r.nextInt(actionsList.size());
            actionsList.get(randomIndex).run(app);
            System.out.print("Action: " + i + " >>> " + randomIndex);
            robot.delay(100);
        }
        System.out.println("Testing Level: "+app.getGame().getCurrentLevel() + " Completed");
    }

    /**
     * This method is used to keep testing in one level
     * @return loop of random action in App we initialized until the first level is done
     */
    public static void unlimittest(){
        SwingUtilities.invokeLater(()->{
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
        int move = 0;
        boolean finish = true;
        robot.delay(1000); // wait 1 second before start testing
        while (finish){
            int randomIndex = r.nextInt(actionsList.size());
            actionsList.get(randomIndex).run(app);
            System.out.print("Action: " + move + " >>> " + randomIndex);
            robot.delay(100);
            move++;
            if(app.getGame().getCurrentLevel() ==2){
                finish = false;
            }
        }
        JOptionPane.showMessageDialog(null, "First level test Complete");
        System.exit(0);

    }

    /**
     * This method is used to test specific key combination
     * @return robot will press specific key combination
     */
    public static void keyTest() throws AWTException {
        App app = new App();
        app.startNewGame();
        app.transitionToGameScreen();

        Robot robot = new Robot();
        robot.setAutoDelay(100);

        robot.keyPress(KeyEvent.VK_UP);
        key_Event_Print(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);

        robot.keyPress(VK_W);
        key_Event_Print(VK_W);
        robot.keyRelease(VK_W);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_1);
        robot.setAutoDelay(100);
        System.out.println( KeyEvent.getKeyText(KeyEvent.VK_CONTROL) +"and " +
                            KeyEvent.getKeyText(KeyEvent.VK_1) +" pressed");
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_1);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_2);
        robot.setAutoDelay(100);
        System.out.println( KeyEvent.getKeyText(KeyEvent.VK_CONTROL)+ "and " +
                            KeyEvent.getKeyText(KeyEvent.VK_2) + " pressed");
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_2);
    }

    /**
     * This method is used to test specific mouse combination
     * @return robot will do the specific mouse event and return messages
     */
    public static void mouseTest() throws AWTException {
//        App app = new App();
//        app.transitionToGameScreen();
//        Robot robot = new Robot();
//        robot.setAutoDelay(100);
//
//        robot.mouseMove(600, 550);
//        System.out.println("Mouse move to: " + 600 + ", " + 550);
//        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
//        System.out.println("Mouse press");
//        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    /**
     * This method is just hardcode to test level 1 can be passed
     * @return print out the key event
     */
    static List<Actions> test1HC = new ArrayList<>();
    static Actions actions;
    public static void hardCode(){
        SwingUtilities.invokeLater(()->{
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
        JOptionPane.showMessageDialog(null,"Start Fuzzing");

        test1HC.add(MOVE_UP);
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,3).forEach(i->test1HC.add(MOVE_DOWN));

        IntStream.range(0,7).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_UP));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_LEFT));

        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_UP));
        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_UP));
        IntStream.range(0,10).forEach(i->test1HC.add(MOVE_LEFT));

        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_UP));

        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,3).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_UP));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_DOWN));

        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_UP));

        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,8).forEach(i->test1HC.add(MOVE_UP));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_UP));

        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_RIGHT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,4).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_UP));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_LEFT));
        IntStream.range(0,1).forEach(i->test1HC.add(MOVE_RIGHT));

        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_DOWN));
        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_RIGHT));

        IntStream.range(0,2).forEach(i->test1HC.add(MOVE_UP));
        test1HC.forEach(a->{
            a.run(app);
            robot.delay(100);
        });

    }

    /**
     * This method is used to print out the key event message what App get and action done
     * @return message of key event
     */
    public static void key_Event_Print(int e){
        System.out.println("Action key pressed: " + KeyEvent.getKeyText(e));
    }

    /**
     * Main method to run different test
     */
    public static void main(String[] args) throws AWTException {

        String[] buttons = { "hardCode", "testLevel(1000)", "testLevel(100)", "unlimittest" ,"Cancel" };
        int rc = JOptionPane.showOptionDialog(null, "Choose a test", "Test",
                JOptionPane.WARNING_MESSAGE, 0, null, buttons, buttons[4]);
        if (rc == 0) {
//            hardCode();
        } else if (rc == 1) {
            testLevel(1000);
        } else if (rc == 2) {
            testLevel(100);
        } else if (rc == 3) {
            unlimittest();
        } else {
            System.exit(0);
        }

        System.out.println("All Tests Complete");
        JOptionPane.showMessageDialog(null, "All Tests Complete");
        String[] buttons2 = { "Exit", "Show the log" };
        int rc2 = JOptionPane.showOptionDialog(null, "Choose a test", "Test",
                JOptionPane.WARNING_MESSAGE, 0, null, buttons2, buttons2[0]);

        System.exit(0);
    }

}