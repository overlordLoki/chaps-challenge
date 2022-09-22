package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Model;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Pair;


public class FakeApp {
    public static Long time = 0L;

    public static void performActions(Pair<Long, List<String>> timeActions){
        System.out.println("Actual time: " + time +
                            ", Recorded time: " + timeActions.getKey() +
                            ", Recorded actions: " + timeActions.getValue().toString());
    }
    public static void queueActions(Pair<Long, List<String>> actions){
        System.out.println("App time: " + time + 
                            ", App queued time: " + actions.getKey() + 
                            ", actions: " + actions.getValue().toString());
    }
    
    public static void main(String[] args){
        Model model = new Model();
        model.startRecording();

        List<String> actions = List.of("character spawned", "bug moved");
        model.addToRecording(actions);
        time++;
        actions = List.of("up", "down");
        model.addToRecording(actions);
        time++;
        actions = List.of("left", "right");
        model.addToRecording(actions);
        time++;
        model.stopRecording();
        
        model.startReplay("myGame");
        model.addReplayActions();
        model.autoPlay();    // exhaust actions
        model.stopReplay(); 
        model.stepForwardReplay();
        model.setReplaySpeed(2);
        model.autoPlay();

        model.autoPlay();
        model.stopReplay();
        model.addReplayActions();
        model.autoPlay();
        model.startReplay("lol");
        model.addReplayActions();
        model.autoPlay();
        model.stopReplay();
        model.setReplaySpeed(2);

    }
}
