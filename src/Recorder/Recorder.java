package Recorder;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import App.*;
import Persistency.*;

public class Recorder {
    private Queue<List<String>> actions;   // time, actions
    

    public void startRecording(){
        System.out.println("Recording started");
        this.actions = new ArrayDeque<>();
    }
    public void addActions(List<String> actions) {
        this.actions.add(actions);
        System.out.println("Adding Action: " + actions.toString()); 
    }
    public void stopRecording(){
        System.out.println("Recording stopped");
        System.out.println("Saving to disk..." + this.actions.toString());
        // Persistency.save(actions);
        System.out.println("Recording saved");
        // get rid of actions
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

