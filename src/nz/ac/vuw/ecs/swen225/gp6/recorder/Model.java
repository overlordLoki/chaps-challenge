package nz.ac.vuw.ecs.swen225.gp6.recorder;


// import nz.ac.vuw.ecs.swen225.gp6.app.*;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Replay;

/** 
 * The Model class is responsible for grouping functionality into a single, simple, coherent class.
 * It's primary role is to provide functions for the controller to use.
 * The Model enables recording and playback of the game.
 */
public class Model implements Runnable {
    private Recorder recorder;
    private Replay replay;
    private App app;
    private long time;

    public Model(App app){
        
    }

    // Methods used to record a game
    @Override
    public void run() {
        time = app.getTime();
    }
    public void startRecording() {
        recorder = new Recorder();
        recorder.startRecording();
    }
    public void stopRecording(){recorder.stopRecording();}
    public void addToRecording(Runnable actions){
        recorder.addActions(time, actions);
    }

    // Methods used to replay a game
    public void startReplay(String game){
        replay = new Replay();
        replay.load(game); // persistency.load(game);}
    }
    public void autoPlay(){replay.autoPlay(0L);}
    public void setReplaySpeed(float speed){replay.setSpeed(speed);}
    public void stepForwardReplay(){replay.step();}
    public void stopReplay(){replay.stopReplay();}

    // testing methods only
    public void logAction(Runnable keyPressed){System.out.println(keyPressed);}
}
