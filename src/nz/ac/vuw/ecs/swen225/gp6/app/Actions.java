package nz.ac.vuw.ecs.swen225.gp6.app;

/**
 *  This class is used to define the actions that can be performed by the user.
 *
 *  @author Jeff Lin
 */
public class Actions {

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
    }

    /**
     * Moves the player down.
     */
    public void actionDown(){
        app.getGame().moveDown();
    }

    /**
     * Moves the player left.
     */
    public void actionLeft(){
        app.getGame().moveLeft();
    }

    /**
     * Moves the player right.
     */
    public void actionRight(){
        app.getGame().moveRight();
    }

    /**
     * Pause the game.
     */
    public void actionPause(){
        System.out.println("Game paused");
        app.getTimer().stop();
        app.getRender().stopMusic();
        System.out.println("Time: " + app.getTime());
    }

    /**
     * Resume the game.
     */
    public void actionResume(){
        System.out.println("Game resumed");
        app.getTimer().start();
        app.setStartingTime(System.nanoTime());
        app.getRender().playMusic();
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
}

