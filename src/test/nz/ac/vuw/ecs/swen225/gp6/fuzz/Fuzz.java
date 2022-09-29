package test.nz.ac.vuw.ecs.swen225.gp6.fuzz;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.App;

import java.util.List;
import java.util.Random;

public class Fuzz {
    public static void main(String[] args) {
        keyRandomTest(10);
    }

    public static void keyRandomTest(int times){

        int keyPressedTimes = times;

        App app = new App();
        Actions actions = new Actions();
        app.transitionToGameScreen();

        List<Runnable> a = List.of(actions::actionUp, actions::actionDown, actions::actionLeft, actions::actionRight, actions::actionPause, actions::actionResume, actions::actionToLevel2, actions::actionToLevel1, actions::actionQuit, actions::actionSave, actions::actionLoad);
        Random r = new Random();

        for(int i = 0; i < times; i++){
            int randomIndex = r.nextInt(a.size());
            a.get(randomIndex).run();
        }
    }

    public static void keyTest(){
        App app = new App();
        Actions actions = new Actions();
        app.transitionToGameScreen();


    }

}
