package Recorder;

import java.util.List;

public class FakeApp {
    
    public static void main(String[] args){
        Model model = new Model();
        model.startRecording();

        List<String> actions = List.of("character spawned", "bug moved");
        model.addToRecording(actions);
        actions = List.of("up", "down");
        model.addToRecording(actions);
        actions = List.of("left", "right");
        model.addToRecording(actions);
        model.stopRecording();
        
        model.startReplay("myGame");
        model.addReplayActions();
        model.autoPlay();    // exhaust actions
        model.stopReplay(); 
        model.stepBackwardReplay(); // rewind 1 list of actions
        model.stepBackwardReplay();
        model.stepBackwardReplay();
        model.stepForwardReplay();
        model.setReplaySpeed(2);
        model.autoPlay();

        // model.autoPlay();
        // model.stopReplay();
        // model.addReplayActions();
        // model.stepReplay(Direction.BACKWARD);
        // model.autoPlay();
        // model.startReplay("lol");
        // model.addReplayActions();
        // model.autoPlay();
        // model.stopReplay();
        // model.setReplaySpeed(2);

    }
}
