package Recorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeApp {
    
    public static void main(String[] args){
        Model model = new Model();
        model.startRecording();

        List<String> actions = List.of("character spawned", "bug moved");
        int time = 0;
        model.addToRecording(time++, actions);
        actions = List.of("up", "down");
        model.addToRecording(time++, actions);
        actions = List.of("left", "right");
        model.addToRecording(time++, actions);
        // actions.put(1, "character moved up");
        
        
        model.stopRecording();
        model.startReplay();
        model.stopReplay();
    }
}
