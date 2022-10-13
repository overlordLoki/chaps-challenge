package nz.ac.vuw.ecs.swen225.gp6.recorder;

import static org.junit.jupiter.api.DynamicTest.stream;

import org.dom4j.DocumentException;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.Pair;
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
public class Replay implements Runnable {
    private ReplayTimeline<Actions> timeline;
    private App app;
    private long time;
    private boolean step = false;

    /**
     * Constructor takes in an app to observe
     * @param app the app to observe
     */
    public Replay(App app){
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
            executeAction(timeline.next().getValue());
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
        app.getGameClock().start();
        this.step = true;
        return this;
    }

    /**
     * Method enables the autoPlay functionality.
     * @return this replay object to chain methods
     */
    public Replay autoPlay(){
        System.out.println("Auto Replay turned on");

        app.getGameClock().start();
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
    public Replay setSpeed(int speed) {
        int delay = 34 / speed;    // default delay is 34ms
        this.app.getGameClock().setReplaySpeed(delay);
        return this;
    }

    /**
     * Method exits the replay mode.
     * @return this replay object to chain methods
     */
    public Replay stopReplay(){
        System.out.println("Replay stopped");
        this.app.getGameClock().stop();
        return this;
    }
    
    
    //================================================================================================================//
    //=========================================== Helper Methods =====================================================//
    //================================================================================================================//

    private boolean actionReady(){
        if(!checkNextIsValid()){
            app.getGameClock().stop();
            System.out.println("Replay finished");
            return false;
        }
        if(timeline.peek().getKey() <= time){
            return true;
        }
        return false;
    }

    /**
     * Method checks if the timeline is valid
     */
    private boolean checkNextIsValid(){
        if (timeline == null){
            System.out.println("No game loaded");
            return false;
        }
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
        action.run(app);
        if(step) {
            app.getGameClock().stop();
            this.step = false;
        }
        System.out.println("action executed");
    }
}