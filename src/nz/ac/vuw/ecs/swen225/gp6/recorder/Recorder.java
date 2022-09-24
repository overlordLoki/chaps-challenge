package nz.ac.vuw.ecs.swen225.gp6.recorder;

import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.datastructures.Timeline;

public class Recorder  {
    private Timeline timeline;

    public void startRecording(){
        // System.out.println("Recording started");
        this.timeline = new Timeline();
    }
    public void addActions(long time, Runnable actions) {
        this.timeline.add(time, actions);
        // System.out.println("Adding Action: " + actions.toString()); 
    }
    public void stopRecording(){
        // System.out.println("Recording stopped");
        // System.out.println("Saving to disk..." + this.timeline.toString());
        // Persistency.save(actions);
        // System.out.println("Recording saved");
    }
}


