package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MusicPlayer;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *  This class is used to define the actions that can be performed by the user.
 *
 *  @author Jeff Lin
 */
public enum Actions {
    /** Represents the move player move up. */
    MOVE_UP("Move Up") {{ this.action = (app)->{
        if (!app.isResuming()) return;
        app.getGame().moveUp();
        app.getRecorder().addActions(app.getGameClock().getTime(), this);
    };}},

    /** Represents the move player move down. */
    MOVE_DOWN("Move Down"){{ this.action = (app)->{
        if (!app.isResuming()) return;
        app.getGame().moveDown();
        app.getRecorder().addActions(app.getGameClock().getTime(), this);
    };}},

    /** Represents the move player move left. */
    MOVE_LEFT("Move Left"){{ this.action = (app)->{
        if (!app.isResuming()) return;
        app.getGame().moveLeft();
        app.getRecorder().addActions(app.getGameClock().getTime(),this);
    };}},

    /** Represents the move player move right. */
    MOVE_RIGHT("Move Right"){{ this.action = (app)->{
        if (!app.isResuming()) return;
        app.getGame().moveRight();
        app.getRecorder().addActions(app.getGameClock().getTime(), this);
    };}},

    /** Represents the action pausing game. */
    PAUSE_GAME("Pause Game"){{ this.action = (app)->{
        app.getGameClock().stop();
        app.getGameClock().setTime(System.nanoTime() - app.getGameClock().getTimeStart() + app.getGameClock().getTime());
        app.getGUI().showPausePanel();
        MusicPlayer.stopGameMusic();
        app.setResuming(false);
    };}},

    /** Represents the action resuming game. */
    RESUME_GAME("Resume Game"){{ this.action = (app)->{
        app.getGameClock().start();
        app.getGameClock().setStartingTime(System.nanoTime());
        app.getGUI().showResumePanel();
        MusicPlayer.playGameMusic();
        app.setResuming(true);
    };}},

    /** Represents the action jump straight to level 1. */
    TO_LEVEL_1("Jump To Level 1"){{ this.action = (app)->{
        System.out.println("Jump to Level 1");
    };}},

    /** Represents the action jump straight to level 2. */
    TO_LEVEL_2("Jump To Level 2"){{ this.action = (app)->{
        System.out.println("Jump to Level 2");
    };}},

    /** Represents the action reloading from saved game. */
    LOAD_GAME("Reload Game"){{ this.action = (app)->{
        app.getGameClock().stop();
        app.getGameClock().setTime(System.nanoTime() - app.getGameClock().getTimeStart() + app.getGameClock().getTime());
        app.getGUI().showPausePanel();
        MusicPlayer.stopGameMusic();
        app.setResuming(false);
        app.refreshSaves();
        app.transitionToMenuScreen();
        app.getGUI().transitionToLoadPanel();
    };}},

    /** Represents the action saving and quit game. */
    SAVE_GAME("Save And Quit Game"){{ this.action = (app)->{
        app.getGameClock().stop();
        app.getGameClock().setTime(System.nanoTime() - app.getGameClock().getTimeStart() + app.getGameClock().getTime());
        app.getGUI().showPausePanel();
        MusicPlayer.stopGameMusic();
        app.setResuming(false);
        app.refreshSaves();
        app.transitionToMenuScreen();
        app.getGUI().transitionToSavePanel();
    };}},

    /** Represents the action quitting without saving game. */
    QUIT_GAME("Quit Game"){{ this.action = (app)->{
        System.exit(0);
    };}},

    /** Represents no action is being performed. */
    NONE("No Action");

    private final String name;
    protected Consumer<App> action = (app)->{};

    Actions(String name){
        this.name = name;
    }

    /**
     * Runs the action associated with the enum.
     *
     * @param app The app object that the action will be performed on.
     */
    public void run(App app){action.accept(app);}

    /**
     * Gets the display name of the action.
     * @return the display name of the action.
     */
    public String getDisplayName(){return name;}
}

