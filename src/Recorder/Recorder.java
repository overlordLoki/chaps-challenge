package Recorder;

import java.util.List;
import java.util.Map;

import App.*;
import Persistency.*;

public class Recorder {
    public void startRecording(){
        System.out.println("Recording started");
    }
    public void stopRecording(){
        System.out.println("Recording stopped");
        System.out.println("Recording saved");
    }
    public void addActions(Map<Integer, List<String>> actions) {
        System.out.println("Actions adding...");

        System.out.println("Actions added");
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

// potential option
// record AddActionsCommand(Recorder recorder) implements Command {
//     @Override
//     public void execute() {
//         recorder.addActions();
//     }
// }