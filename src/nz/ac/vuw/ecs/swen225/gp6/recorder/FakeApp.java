package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;

// import javax.swing.JFrame;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Pair;


public class FakeApp {
    public Long time = 0L;
    private Model model;

    public void performActions(Pair<Long, Runnable> timeActions){
        System.out.println("Actual time: " + time +
                            ", Recorded time: " + timeActions.getKey() +
                            ", Recorded actions: " + timeActions.getValue().toString());
    }
    public void queueActions(Pair<Long, Runnable> actions){
        System.out.println("App time: " + time + 
                            ", App queued time: " + actions.getKey() + 
                            ", actions: " + actions.getValue().toString());

    }
    
    public static void main(String[] args){
        FakeApp app = new FakeApp();
        // app.model = new Model<String>(app);
        // List<String> actions = List.of("character spawned", "bug moved");
        // app.model.addToRecording(actions);
        // app.time++;
        // actions = List.of("up", "down");
        // app.model.addToRecording(actions);
        // app.time++;
        // actions = List.of("left", "right");
        // app.model.addToRecording(actions);
        // app.time++;
        // app.model.stopRecording();
        
        // app.model.startReplay("myGame");
        // app.model.addReplayActions();
        // app.model.checkActions();
        // app.model.autoPlay();    // exhaust actions
        // app.model.stopReplay(); 
        // model.stepForwardReplay();
        // model.setReplaySpeed(2);
        // model.autoPlay();
        // model.toString();
        // model.autoPlay();
        // model.stopReplay();
        // model.addReplayActions();
        // model.autoPlay();
        // model.startReplay("lol");
        // model.addReplayActions();
        // model.autoPlay();
        // model.stopReplay();
        // model.setReplaySpeed(2);

    }
}
