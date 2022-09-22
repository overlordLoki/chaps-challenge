package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.recorder.FakeApp;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.*;

public class Replay {
    private Timeline<String> timeline;
    float speed;
    String game;
    
    /**
     * Load method
     */
    public void load(String game){
        // FutureActions = Persistency.load(game);
        this.game = game;
        timeline = new Timeline<String>();
        speed = 1;          // changed by the app
        System.out.println("Loading game: " + game);
    }

    /**
     * Step methods
     */
    public void step(){
        if(!timeline.hasNext()){
            System.out.println("End of timeline");
            // App may need a check here
            return;
        }
        Pair<Long, List<String>> actions = timeline.next();
        FakeApp.queueActions(actions);
        System.out.println("Replaying actions: " + actions.toString());
    }

    /**
     * Auto play methods
     */
    public void autoPlay(){
        FakeApp.time = 0L;   // simulate replay time
        System.out.println("Auto Replay started");
        if(!timeline.hasNext()) {
            System.out.println("Please reload a game.");
            return;
        }
        Pair<Long,List<String>> actions = timeline.next();
        while(timeline.hasNext()){
            if(FakeApp.time == actions.getKey()){
                FakeApp.performActions(actions);
                actions = timeline.next();
            }
            FakeApp.time++;    // simulate how time is suppose to work with autoplay
        }
        FakeApp.queueActions(actions);  // takes care of last case of timeline
        System.out.println("End of file.");
    }
    public void stopReplay(){System.out.println("Replay stopped"); }
    public void setSpeed(float speed) {this.speed = speed; }

    /**
     * Methods for testing replay functionality
     */
    public void addActions(){
        // MUST BE SEQUEUNTIAL
        timeline.add(1L, List.of("action1"));
        timeline.add(22L, List.of("action2"));
        timeline.add(33L, List.of("action3"));
        timeline.add(44L, List.of("action4"));
        timeline.add(55L, List.of("action5"));
        timeline.add(66L, List.of("action6"));
        timeline.add(77L, List.of("action7"));
        timeline.add(88L, List.of("action8"));
        timeline.add(1000L, List.of("action9"));
        timeline.add(10000L, List.of("action10"));
    }
}