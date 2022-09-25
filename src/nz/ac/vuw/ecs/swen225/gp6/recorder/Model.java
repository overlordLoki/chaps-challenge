package nz.ac.vuw.ecs.swen225.gp6.recorder;


import java.util.List;
// import nz.ac.vuw.ecs.swen225.gp6.app.*;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Recorder;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Replay;

/** 
 * The Model class is responsible for grouping functionality into a single, simple, coherent class.
 * It's primary role is to provide functions for the controller to use.
 * The Model enables recording and playback of the game.
 */
public class Model<E> implements Runnable {
    private Recorder<E> recorder;
    private Replay replay;

    // Methods used to record a game
    @Override
    public void run() {
        recorder = new Recorder<E>();
        recorder.startRecording();
    }
    public void stopRecording(){recorder.stopRecording();}
    public void addToRecording(List<E> actions){recorder.addActions(actions);}

    // Methods used to replay a game
    public void startReplay(String game){
        replay = new Replay();
        replay.load(game); // persistency.load(game);}
    }
    public void autoPlay(){replay.autoPlay(0L);}
    public void setReplaySpeed(float speed){replay.setSpeed(speed);}
    public void stepForwardReplay(){replay.step();}
    public void stopReplay(){replay.stopReplay();}
    public void checkActions(){replay.checkActions();}
    // testing methods only
    public void addReplayActions(){replay.addActions();}

    public void logAction(E keyPressed){System.out.println(keyPressed);}
}
