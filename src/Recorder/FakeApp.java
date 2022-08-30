package Recorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeApp {
    
    public static void main(String[] args){
        Model recorder = new Model();
        recorder.startRecording();

        
        Map<Integer, List<String>> timeActions = new HashMap<Integer, List<String>>();
        List<String> actions = List.of("character spawned", "bug moved");
        timeActions.put(0, actions);
        recorder.addToRecording(timeActions);
        actions = List.of("up", "down");
        timeActions.put(1, actions);
        recorder.addToRecording(timeActions);
        actions = List.of("left", "right");
        timeActions.put(2, actions);
        recorder.addToRecording(timeActions);
        // actions.put(1, "character moved up");
        
        
        recorder.stopRecording();
        recorder.startReplay();
        recorder.stopReplay();
    }
}
