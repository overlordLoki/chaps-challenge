package nz.ac.vuw.ecs.swen225.gp6.app;

import nz.ac.vuw.ecs.swen225.gp6.app.tempDomain.Game;

/**
 *  This class is used to define the actions that can be performed by the user.
 *
 *  @author Jeff Lin
 */
public class Actions {

    private final Game game;

    /**
     * Constructor for the Actions class.
     *
     * @param game The game object that the actions will be performed on.
     */
    public Actions(Game game){
        this.game = game;
    }
    public Actions(){
        this.game = new Game();
    }

    /**
     * Moves the player up.
     */
    public void actionUp() {
        game.moveUp();
    }

    /**
     * Moves the player down.
     */
    public void actionDown(){
        game.moveDown();
    }

    /**
     * Moves the player left.
     */
    public void actionLeft(){
        game.moveLeft();
    }

    /**
     * Moves the player right.
     */
    public void actionRight(){
        game.moveRight();
    }

    /**
     * Pause the game.
     */
    public void actionPause(){
        System.out.println("Game paused");
    }

    /**
     * Resume the game.
     */
    public void actionResume(){
        System.out.println("Game resumed");
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
        System.out.println("Game quit");
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

    /**
     * Enters replay mode from a played game.
     */
    public void actionReplayMode(){
        System.out.println("Replay mode started");
    }

    /**
     * Auto reply through the game.
     */
    public void actionReplayAuto(){
        for (int i = 0; i < 10; i++) {
            System.out.println("Replay auto step " + i);
        }
    }

    /**
     * Replays the game step by step, with the user pressing a button to move to the next step.
     */
    public void actionReplayStep(){
        System.out.println("Replay step");
    }
}

