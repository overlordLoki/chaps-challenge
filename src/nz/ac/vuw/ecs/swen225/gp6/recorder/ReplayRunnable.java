package nz.ac.vuw.ecs.swen225.gp6.recorder;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.Pair;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.TimelineRunnable;

public class ReplayRunnable implements Runnable {
    private TimelineRunnable timeline;
    private Pair<Long, Runnable> queuedAction;
    private App app;
    private boolean isRunning = false;
    private long time;
    float speed;

    /**
     * Constructor takes in an app to observe
     * @param app
     */
    public ReplayRunnable(App app){
        this.speed = 1;
        this.app = app;
    }

    @Override
    public void run() {
        time = app.getTime();
        if (!isRunning) {return;}
        autoPlayActions();
    }

    /**
     * Load method
     */
    public ReplayRunnable load(String game){
        // this.timeline = Persistency.load(game);
        this.timeline = new TimelineRunnable();
        this.speed = 1;
        return this;
    }

    /**
     * Method queues the next action in the timeline if available.
     * If there is no next action, the method will popup a message.
     */
    public void step(){
        System.out.println("Stepping");
        checkGame();
        Pair<Long, Runnable> nextAction = timeline.next();
        app.setTime(nextAction.getKey());
        nextAction.getValue().run();
    }

    /**
     * Method enables the autoPlay functionality.
     * 
     */
    public void autoPlay(){
        System.out.println("Auto Replay started");
        checkGame();
        isRunning = true;
    }

    /**
     * Method to pause the autoplay feature.
     */
    public void pauseReplay(){
        // app.pauseTimer();   // discuss with app
    }

    /**
     * Method sets the speed of the autoplay.
     * @param speed
     */
    public void setSpeed(float speed) {
        this.speed = speed; 
    }

    /**
     * Method exits the replay mode.
     */
    public void stopReplay(){
        System.out.println("Replay stopped");
        // app.setReplay(false);    // discuss with app
    }
    
    
    //================================================================================================================//
    //=========================================== Helper Methods =====================================================//
    //================================================================================================================//


    /**
     * Method checks if a timeline has been initialized.
     * If not, it will display a popup message.
     */
    private void checkGame(){
        if (!timeline.hasNext()){
            System.out.println("Please reload a game.");
            return;
        }
    }
    
    /**
     * Methods checks if the next action in the timeline is ready to be executed.
     * If it is, it executes the action.
     */
    private void autoPlayActions() { 
        if(queuedAction != null && queuedAction.getKey() <= this.time){
            queuedAction.getValue().run();
        }
        if (!timeline.hasNext()){ return; }
        queuedAction = timeline.next();
    }
}