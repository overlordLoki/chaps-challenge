package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Pair;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Timeline;

public class Replay {
    private Timeline timeline;
    float speed;
    String game;
    
    /**
     * Load method
     */
    public void load(String game){
        // FutureActions = Persistency.load(game);
        this.game = game;
        timeline = new Timeline();
        speed = 1;          // changed by the app
        System.out.println("Loading game: " + game);
    }

    /**
     * Step methods
     */
    public void step(){
        if(!timeline.hasNext()){
            System.out.println("End of timeline");
            // may need a check here
            return;
        }
        Pair<Long, Runnable> actions = timeline.next();
        // FakeApp.queueActions(actions);
        System.out.println("Replaying actions: " + actions.toString());
    }

    /**
     * Auto play methods
     */
    public void autoPlay(Long time){
        System.out.println("Auto Replay started");
        if(!timeline.hasNext()) {
            System.out.println("Please reload a game.");
            return;
        }
        Pair<Long, Runnable> timeActions = timeline.next();
        while(timeline.hasNext()){;
            if(time.equals(timeActions.getKey())){
                // FakeApp.performActions(actions);
                System.out.println("FakeApp performs actions: ");
                System.out.println("Actual time: " + time +
                                    ", Recorded time: " + timeActions.getKey() +
                                    ", Recorded actions: " + timeActions.getValue().toString());
                timeActions = timeline.next();
            }
            time++;    // simulate how time is suppose to work with autoplay
        }
        // FakeApp.queueActions(actions);  // takes care of last case of timeline
        System.out.println(time);
        System.out.println("End of file.");
    }
    public void stopReplay(){System.out.println("Replay stopped"); }
    public void setSpeed(float speed) {this.speed = speed; }
}