package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;

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
     * Moves the player up.
     */
    public void actionUp() {
        if (!app.isResuming()) return;
        app.getGame().moveUp();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_UP);
    }

    /**
     * Moves the player down.
     */
    public void actionDown(){
        if (!app.isResuming()) return;
        app.getGame().moveDown();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_DOWN);
    }

    /**
     * Moves the player left.
     */
    public void actionLeft(){
        if (!app.isResuming()) return;
        app.getGame().moveLeft();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_LEFT);
    }

    /**
     * Moves the player right.
     */
    public void actionRight(){
        if (!app.isResuming()) return;
        app.getGame().moveRight();
        app.getRecorder().addActions(app.getGameClock().getTime(),Action.MOVE_RIGHT);
    }

    /**
     * Pause the game.
     */
    public void actionPause(){
        app.getGameClock().stop();
        app.getGameClock().setTime(System.nanoTime() - app.getGameClock().getTimeStart() + app.getGameClock().getTime());
        app.getGUI().showPausePanel();
        MusicPlayer.stopGameMusic();
        app.setResuming(false);
    }

    /**
     * Resume the game.
     */
    public void actionResume(){
        app.getGameClock().start();
        app.getGameClock().setStartingTime(System.nanoTime());
        app.getGUI().showResumePanel();
        MusicPlayer.playGameMusic();
        app.setResuming(true);
    }

    /**
     * Load a saved game.
     */
    public void actionLoad(){
        actionPause();
        app.refreshSaves();
        app.transitionToMenuScreen();
        app.getGUI().transitionToLoadPanel();
    }

    /**
     * Save and quits the current game.
     */
    public void actionSave(){
        actionPause();
        app.refreshSaves();
        app.transitionToMenuScreen();
        app.getGUI().transitionToSavePanel();
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

