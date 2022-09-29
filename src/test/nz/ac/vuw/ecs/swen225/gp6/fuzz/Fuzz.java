package test.nz.ac.vuw.ecs.swen225.gp6.fuzz;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.Main;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;
import static java.awt.event.KeyEvent.*;

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
    static List<Runnable> actionsList = List.of();

    static {
        try {robot = new Robot();}
        catch (AWTException e) {throw new RuntimeException(e);}
    }

    public static void testLevel(int numOfInputs){
        SwingUtilities.invokeLater(()->{
            app = new App();
            Actions actions = app.getActions();
            actionsList = List.of(actions::actionUp, actions::actionDown, actions::actionLeft, actions::actionRight);
        });
        // Start robot automating sequence
        JOptionPane.showMessageDialog(null,"Start Fuzzing");
        app.startNewGame();
        app.transitionToGameScreen();
        for (int i =0; i < numOfInputs; i++){
            int randomIndex = r.nextInt(actionsList.size());
            actionsList.get(randomIndex).run();
            System.out.print("Action: " + i + " >>> " + randomIndex);
            robot.delay(100);
        }
        System.out.println("Testing Level: "+app.getGame().getCurrentLevel() + " Completed");
    }

    public static void unlimittest(){
        SwingUtilities.invokeLater(()->{
            app = new App();
            Actions actions = app.getActions();
            app.startNewGame();
            app.transitionToGameScreen();
            actionsList = List.of(actions::actionUp, actions::actionDown, actions::actionLeft, actions::actionRight);
        });
        int move = 0;
        boolean finish = true;
        robot.delay(1000); // wait 1 second before start testing
        while (finish){
            int randomIndex = r.nextInt(actionsList.size());
            actionsList.get(randomIndex).run();
            System.out.print("Action: " + move + " >>> " + randomIndex);
            robot.delay(100);
            move++;
            if(app.getGame().getCurrentLevel() ==2){
                finish = false;
            }
        }

    }

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

    public static void key_Event_Print(int e){
        System.out.println("Action key pressed: " + KeyEvent.getKeyText(e));
    }

    public static void main(String[] args) throws AWTException {
//        keyTest();
        testLevel( 100);
//        testLevel( 100);
//        unlimittest();
        System.out.println("All Tests Complete");
        JOptionPane.showMessageDialog(null, "All Tests Complete");
        System.exit(0);
    }

}