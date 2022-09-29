package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;

import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.Action;

/**
 *  This class is used to define the actions that can be performed by the user.
 *
 *  @author Jeff Lin
 */
public class Actions {
    /**
     * The action enum that represents the action performed.
     */
    public enum Action {
        /** Represents the move player move up. */
        MOVE_UP,
        /** Represents the move player move down. */
        MOVE_DOWN,
        /** Represents the move player move left. */
        MOVE_LEFT,
        /** Represents the move player move right. */
        MOVE_RIGHT
    }

    private final App app;

    /**
     * Constructor for the Actions class.
     *
     * @param app The game object that the actions will be performed on.
     */
    public Actions(App app){
        this.app = app;
    }

    /**
     * Constructor for the Actions class.
     */
    public Actions(){
        this.app = new App();
    }

    /**
     * Moves the player up.
     */
    public void actionUp() {
        app.getGame().moveUp();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_UP);
    }

    /**
     * Moves the player down.
     */
    public void actionDown(){
        app.getGame().moveDown();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_DOWN);
    }

    /**
     * Moves the player left.
     */
    public void actionLeft(){
        app.getGame().moveLeft();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_LEFT);
    }

    /**
     * Moves the player right.
     */
    public void actionRight(){
        app.getGame().moveRight();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_RIGHT);
    }

    /**
     * Starts a new game
     */
    public void actionStartNew(){
        app.startNewGame();
    }

    /**
     * Pause the game.
     */
    public void actionPause(){
        app.getGameClock().stop();
        app.getGameClock().setTime(System.nanoTime() - app.getGameClock().getTimeStart() + app.getGameClock().getTime());
        MusicPlayer.stopGameMusic();
    }

    /**
     * Resume the game.
     */
    public void actionResume(){
        app.getGameClock().start();
        app.getGameClock().setStartingTime(System.nanoTime());
        MusicPlayer.playGameMusic();
    }

    /**
     * Load a saved game.
     */
    public void actionLoad(){
        System.out.println("Game loaded");
    }

    /**
     * Save and quits the current game.
     */
    public void actionSave(){
        System.out.println("Game saved");
    }

    /**
     * Quit the game without saving.
     */
    public void actionQuit(){
        System.exit(0);
    }

    /**
     * Go to level 1.
     */
    public void actionToLevel1(){
        System.out.println("Jump to Level 1");
    }

    /**
     * Go to level 2.
     */
    public void actionToLevel2(){
        System.out.println("Jump to Level 2");
    }
}

