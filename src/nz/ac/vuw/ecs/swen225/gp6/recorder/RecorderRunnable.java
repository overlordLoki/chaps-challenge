package nz.ac.vuw.ecs.swen225.gp6.recorder;

import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.TimelineRunnable;

public class RecorderRunnable  {
    private TimelineRunnable timeline;

    public void startRecording(){
        this.timeline = new TimelineRunnable();
    }
    public void addActions(long time, Runnable actions) {
        this.timeline.add(time, actions);
    }
    public void stopRecording(){
        if(timeline == null){
            System.out.println("No timeline to save");
            return;
        }
        // Persistency.save(timeline);
    }
}


