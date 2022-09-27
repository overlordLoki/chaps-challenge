package nz.ac.vuw.ecs.swen225.gp6.recorder;


import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.Actions.Action;

// import javax.swing.JFrame;



public class FakeApp {
    public Long time = 0L;
    
    public static void main(String[] args){
        RecorderEnum r = new RecorderEnum();
        ReplayRunnable replay = new ReplayRunnable(new App());
        r.startRecording();
        r.addActions(0L, Action.MOVE_DOWN);
        r.addActions(1L, Action.MOVE_RIGHT);
        r.addActions(2L, Action.MOVE_DOWN);
        r.addActions(3L, Action.MOVE_UP);
        r.addActions(4L, Action.MOVE_LEFT);
        r.addActions(5L, Action.MOVE_RIGHT);
        r.stopRecording();
        replay.load("First Game");
        replay.autoPlay();
        
    }
}
