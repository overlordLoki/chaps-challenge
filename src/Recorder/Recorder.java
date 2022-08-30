package Recorder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import App.*;
import Persistency.*;

public class Recorder {
    private Map<Integer, List<String>> timeActions;   // time, actions

    public void startRecording(){
        System.out.println("Recording started");
        timeActions = new HashMap<>();
    }
    public void addActions(int time, List<String> actions) {
        timeActions.put(time, actions);
        System.out.println("Adding... : Time: " + time + ", Action: " + actions.toString()); 
    }
    public void stopRecording(){
        System.out.println("Recording stopped");
        System.out.println("Saving to disk..." + timeActions.toString());
        // Persistency.save(timeActions);
        System.out.println("Recording saved");
        // get rid of timeActions
        // timeActions = null ?? will it be garbage collected?
    }
}

record RecordStartCommand(Recorder recorder) implements Command {
    @Override
    public void execute() {
        recorder.startRecording();
    }
}

record RecordStopCommand(Recorder recorder) implements Command {
    @Override
    public void execute() {
        recorder.stopRecording();
    }
}