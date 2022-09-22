package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.recorder.FakeApp;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Pair;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Timeline;

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
            // may need a check here
            return;
        }
        Pair<Long, List<String>> actions = timeline.next();
        FakeApp.queueActions(actions);
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
        Pair<Long,List<String>> actions = timeline.next();
        while(timeline.hasNext()){;
            if(time.equals(actions.getKey())){
                FakeApp.performActions(actions);
                actions = timeline.next();
            }
            time++;    // simulate how time is suppose to work with autoplay
        }
        // FakeApp.queueActions(actions);  // takes care of last case of timeline
        System.out.println(FakeApp.time);
        System.out.println("End of file.");
    }
    public void stopReplay(){System.out.println("Replay stopped"); }
    public void setSpeed(float speed) {this.speed = speed; }

    /**
     * Simulate a loaded game.
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
        timeline.add(101L, List.of("action8.5"));
        timeline.add(125L, List.of("action8.5"));
        timeline.add(126L, List.of("action8.5"));
        timeline.add(128L, List.of("action8.5"));
        timeline.add(129L, List.of("action8.5"));
        timeline.add(130L, List.of("action8.5"));
        timeline.add(135L, List.of("action8.5"));
        timeline.add(151L, List.of("action8.5"));
        timeline.add(250L, List.of("action9"));
        timeline.add(599L, List.of("action9.5"));
        timeline.add(1000L, List.of("action10"));
        timeline.add(10000L, List.of("action11"));
    }
    public void checkActions(){
        System.out.println(timeline.toString());
    }
}