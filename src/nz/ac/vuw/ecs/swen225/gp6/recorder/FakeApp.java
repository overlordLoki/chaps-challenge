package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Model;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.helpers.Pair;


public class FakeApp {
    public static int time = 0;

    public static void performActions(Pair timeActions){
        System.out.println("Actual time: " + time +
                            ", Recorded time: " + timeActions.getTime() +
                            ", Recorded actions: " + timeActions.getActions().toString());
    }
    public static void queueActions(Pair timeActions){
        System.out.println("App time: " + time + 
                            ", App queued time: " + timeActions.getTime() + 
                            ", actions: " + timeActions.getActions().toString());
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
