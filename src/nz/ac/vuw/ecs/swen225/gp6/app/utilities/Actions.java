package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;

/**
 *  This class is used to define the actions that can be performed by the user.
 *
 *  @author Jeff Lin
 */
public enum Actions {
    /** Represents the move player move up. */
    MOVE_UP("Move Up") {
        public void run(App app){
            if (!app.isResuming()) return;
            app.getGame().moveUp();
            app.getRecorder().addActions(app.getGameClock().getTimePlayed(), this);
        }
        public void replay(App app){
            app.getGame().moveUp();
        }
    },

    /** Represents the move player move down. */
    MOVE_DOWN("Move Down"){
        public void run(App app){
            if (!app.isResuming()) return;
            app.getGame().moveDown();
            app.getRecorder().addActions(app.getGameClock().getTimePlayed(), this);
        }
        public void replay(App app){
            app.getGame().moveDown();
        }
    },

    /** Represents the move player move left. */
    MOVE_LEFT("Move Left"){
        public void run(App app){
            if (!app.isResuming()) return;
            app.getGame().moveLeft();
            app.getRecorder().addActions(app.getGameClock().getTimePlayed(),this);
        }
        public void replay(App app){
            app.getGame().moveLeft();
        }
    },

    /** Represents the move player move right. */
    MOVE_RIGHT("Move Right"){
        public void run(App app){
            if (!app.isResuming()) return;
            app.getGame().moveRight();
            app.getRecorder().addActions(app.getGameClock().getTimePlayed(), this);
        }
        public void replay(App app){
            app.getGame().moveRight();
        }
    },

    /** Represents the action pausing game. */
    PAUSE_GAME("Pause Game"){
        public void run(App app){
            if (!app.isResuming()) return;
            app.getGameClock().stop();
            app.getGUI().showPausePanel();
            MusicPlayer.stopGameMusic();
            app.setResuming(false);
        }
    },

    /** Represents the action resuming game. */
    RESUME_GAME("Resume Game"){
        public void run(App app){
            if (app.isResuming()) return;
            app.getGameClock().start();
            app.getGUI().showResumePanel();
            MusicPlayer.playGameMusic();
            app.setResuming(true);
        }
    },

    /** Represents the action jump straight to level 1. */
    TO_LEVEL_1("Jump To Level 1"){
        public void run(App app){
            System.out.println("Jump to Level 1");
            app.startNewGame();
            app.getGame().setCurrentLevel(1);
        }
    },

    /** Represents the action jump straight to level 2. */
    TO_LEVEL_2("Jump To Level 2"){
        public void run(App app){
            System.out.println("Jump to Level 2");
            app.startNewGame();
            app.getGame().setCurrentLevel(2);
        }
    },

    /** Represents the action reloading from saved game. */
    LOAD_GAME("Reload Game"){
        public void run(App app){
            QUIT_TO_MENU.run(app);
            app.refreshSaves();
            app.getGUI().transitionToLoadScreen(app);
        }
    },

    /** Represents the action saving and quit game. */
    SAVE_GAME("Save And Quit To Menu"){
        public void run(App app){
            QUIT_TO_MENU.run(app);
            app.refreshSaves();
            app.getGUI().transitionToSaveScreen(app);
        }
    },

    /** Represents the action quitting without saving game. */
    QUIT_TO_MENU("Quit To Menu"){
        public void run(App app){
            System.out.println("Quit Game without saving, returning to menu");
            app.getGameClock().stop();
            app.setResuming(false);
            MusicPlayer.stopGameMusic();
            if(app.getConfiguration().isMusicOn()) MusicPlayer.playMenuMusic();
            app.getGUI().transitionToMenuScreen();
        }
    },

    /** Represents no action is being performed. */
    NONE("No Action");

    private final String name;
    Actions(String name){this.name = name;}

    /**
     * Runs the action associated with the enum.
     *
     * @param app The app object that the action will be performed on.
     */
    public void run(App app){}

    /**
     * Replays the action associated with the enum.
     *
     * @param app The app object that the action will be performed on.
     */
    public void replay(App app){}

    /**
     * Gets the display name of the action.
     * @return the display name of the action.
     */
    public String getDisplayName(){return name;}
}

