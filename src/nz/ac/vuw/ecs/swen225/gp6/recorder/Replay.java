package nz.ac.vuw.ecs.swen225.gp6.recorder;

import org.dom4j.DocumentException;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.ReplayTimeline;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.persistency.RecorderPersistency;

/**
 * Class for replaying a recorded game.
 * The Replay class observes the App to be updated of the time, 
 * to make sure actions are in sync with the game.
 * This allows the App to still have control of the game during replay.
 *
 * @author: Jayden Hooper
 */
public final class Replay implements Runnable {
    public static final Replay INSTANCE = new Replay();
    private ReplayTimeline<Actions> timeline;
    private App app;
    private long time;
    private boolean step = false;

    /** Constructor takes in an app to observe */
    private Replay(){}

    /** Sets the Replay object up with an App. */
    public void setReplay(App app){ 
        if(app == null) {
            throw new IllegalArgumentException("App cannot be null");
        }
        this.app = app;
        app.getGameClock().setObserver(this);
    }

    @Override
    public void run() {
        this.time = app.getGameClock().getTimePlayed();
        if(actionReady()) {
            executeAction(timeline.next().value());
        }
    }

    /**
     * Load method will load the game from the given file name
     * @param game the name of the game to load
     * @return this replay object to chain methods
     * @throws DocumentException
     */
    public Replay load(int slot) {
        try {
            this.timeline = new ReplayTimeline<Actions>(RecorderPersistency.loadTimeline(slot));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Method queues the next action in the timeline if available.
     * If there is no next action, the method will popup a message.
     * @return this replay object to chain methods
     */
    public Replay step(){
        if(!checkNextIsValid()) {
            return this;   
        }
        app.getGameClock().start();
        this.step = true;
        return this;
    }

    /**
     * Method enables the autoPlay functionality.
     * @return this replay object to chain methods
     */
    public Replay autoPlay(){
        if(checkNextIsValid()) {
            app.getGameClock().start();
        }
        return this;
    }

    /**
     * Method to pause the autoplay feature.
     * @return this replay object to chain methods
     */
    public Replay pauseReplay(){
        app.getGameClock().stop();
        return this;
    }

    /**
     * Method sets the speed of the autoplay.
     * @param speed the speed to set the replay to
     * @return this replay object to chain methods
     */
    public Replay speedMultiplier(float speed) {
        this.app.getGameClock().setReplaySpeed(speed);
        return this;
    }

    /**
     * Method exits the replay mode.
     * @return this replay object to chain methods
     */
    public Replay stopReplay(){
        this.app.getGameClock().stop();
        return this;
    }
    
    
    //================================================================================================================//
    //=========================================== Helper Methods =====================================================//
    //================================================================================================================//

    /** Method checks if the next action is valid. */
    private boolean actionReady(){
        if(!checkNextIsValid()){
            app.getGameClock().stop();
            System.out.println("Replay finished");
            return false;
        }
        return timeline.peek().key() <= time;
    }

    /** Method checks if the timeline is valid */
    private boolean checkNextIsValid(){
        if (!timeline.hasNext()){
            System.out.println("Replay finished"); 
            return false;
        }
        return true;
    }

    /**
     * Method executes action.
     * Throws IllegalArgumentException if the action is null or not recognized.
     * @param action Action to be executed
     */
    private void executeAction(Actions action) throws IllegalArgumentException {
        if(action == null) {throw new IllegalArgumentException("Null action encountered");}
        action.replay(app);
        if(step) {
            app.getGame().pingDomain();
            app.getGameClock().stop();
            this.step = false;
        }
    }
}