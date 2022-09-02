package Recorder;

import java.util.List;

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
            // App may need a check here
            return;
        }
        Pair actions = timeline.next();
        FakeApp.queueActions(actions);
        System.out.println("Replaying actions: " + actions.toString());
    }

    /**
     * Auto play methods
     */
    public void autoPlay(){
        FakeApp.time = 0;   // simulate replay time
        System.out.println("Auto Replay started");
        if(!timeline.hasNext()) {
            System.out.println("Please reload a game.");
            return;
        }
        Pair actions = timeline.next();
        while(timeline.hasNext()){
            if(FakeApp.time == actions.getTime()){
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
        timeline.add(new Pair(1, List.of("action1")));
        timeline.add(new Pair(22, List.of("action2")));
        timeline.add(new Pair(33, List.of("action3")));
        timeline.add(new Pair(44, List.of("action4")));
        timeline.add(new Pair(55, List.of("action5")));
        timeline.add(new Pair(66, List.of("action6")));
        timeline.add(new Pair(77, List.of("action7")));
        timeline.add(new Pair(88, List.of("action8")));
        timeline.add(new Pair(1000, List.of("action9")));
        timeline.add(new Pair(10000, List.of("action10")));
    }
}


record ReplayStartCommand(Replay replay) implements Command {
    @Override
    public void execute() { replay.autoPlay(); }
}

record ReplayStopCommand(Replay replay) implements Command {
    @Override
    public void execute() {replay.stopReplay();}
}

