package test.nz.ac.vuw.ecs.swen225.gp6.fuzz;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import org.junit.Test;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;


// Property based testing on automatically generated data sets
public class Fuzz {

    static final Random r = new Random();

    public static void keyRandomTest(int times){
        int keyPressedTimes = times;
        App app = new App();
        Actions actions = new Actions();
        app.transitionToGameScreen();

        List<Runnable> a = List.of(actions::actionUp, actions::actionDown,
                                   actions::actionLeft, actions::actionRight,
                                   actions::actionPause, actions::actionResume,
                                   actions::actionToLevel2, actions::actionToLevel1,
                                   actions::actionQuit, actions::actionSave,
                                   actions::actionLoad );

//        for(int i = 0; i < times; i++){
//            int randomIndex = r.nextInt(a.size());
//            a.get(randomIndex).run();
//        }

    }

    public static void keyTest() throws AWTException {
        App app = new App();
        Actions actions = new Actions();
        app.transitionToGameScreen();
        Robot robot = new Robot();
        robot.setAutoDelay(100);

        robot.keyPress(KeyEvent.VK_UP);
        System.out.println(KeyEvent.getKeyText(KeyEvent.VK_UP) + " pressed");
        robot.keyRelease(KeyEvent.VK_UP);

        robot.keyPress(KeyEvent.VK_CONTROL);
        System.out.println(KeyEvent.getKeyText(KeyEvent.VK_CONTROL) + " pressed");
        robot.keyPress(KeyEvent.VK_1);
        System.out.println(KeyEvent.getKeyText(KeyEvent.VK_1) + " pressed");
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_1);
    }

    public static void main(String[] args) throws AWTException {
        keyTest();
    }

}
