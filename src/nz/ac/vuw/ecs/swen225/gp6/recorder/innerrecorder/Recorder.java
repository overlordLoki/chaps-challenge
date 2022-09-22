package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder;

import java.util.List;
import nz.ac.vuw.ecs.swen225.gp6.recorder.FakeApp;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Timeline;

public class Recorder {
    private Timeline<String> timeline;

    public void startRecording(){
        System.out.println("Recording started");
        this.timeline = new Timeline<String>();
    }
    public void addActions(List<String> actions) {
        this.timeline.add(FakeApp.time, actions);
        System.out.println("Adding Action: " + actions.toString()); 
    }
    public void stopRecording(){
        System.out.println("Recording stopped");
        System.out.println("Saving to disk..." + this.timeline.toString());
        // Persistency.save(actions);
        System.out.println("Recording saved");
    }
}


