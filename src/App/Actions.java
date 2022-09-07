package App;

import App.tempDomain.Game;

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
        game.pauseGame();
    }

    /**
     * Resume the game.
     */
    public void actionResume(){
        game.resumeGame();
    }

    /**
     * Load a saved game.
     */
    public void actionLoad(){
        game.loadGame();
    }

    /**
     * Save and quits the current game.
     */
    public void actionSave(){
        game.saveGame();
    }

    /**
     * Quit the game without saving.
     */
    public void actionQuit(){
        game.quitGame();
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
        game.replayMode();
    }

    /**
     * Auto reply through the game.
     */
    public void actionReplayAuto(){
        game.replayAuto();
    }

    /**
     * Replays the game step by step, with the user pressing a button to move to the next step.
     */
    public void actionReplayStep(){
        game.replayStep();
    }
}

