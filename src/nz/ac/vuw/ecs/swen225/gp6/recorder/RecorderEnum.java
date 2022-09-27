package nz.ac.vuw.ecs.swen225.gp6.recorder;

import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.TimelineGeneric;
import nz.ac.vuw.ecs.swen225.gp6.app.Actions.Action;

public class RecorderEnum {
    private TimelineGeneric<Action> timeline;

    public void startRecording(){
        this.timeline = new TimelineGeneric<Action>();
    }
    public void addActions(long time, Action actions) {
        this.timeline.add(time, actions); 
    }
    public void stopRecording(){
        if(timeline == null){
            System.out.println("Recording was not started");
            return;
        }
        if(!timeline.hasNext()){
            System.out.println("No events to save");
            return;
        }
        // Persistency.save(timeline);
    }
}
