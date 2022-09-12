package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;
import nz.ac.vuw.ecs.swen225.gp6.app.*;

public class Recorder {
    private Timeline timeline;

    public void startRecording(){
        System.out.println("Recording started");
        this.timeline = new Timeline();
    }
    public void addActions(List<String> actions) {
        this.timeline.add(new Pair(FakeApp.time, actions));
        System.out.println("Adding Action: " + actions.toString()); 
    }
    public void stopRecording(){
        System.out.println("Recording stopped");
        System.out.println("Saving to disk..." + this.timeline.toString());
        // Persistency.save(actions);
        System.out.println("Recording saved");
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

