package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder;


import java.util.List;
// import nz.ac.vuw.ecs.swen225.gp6.app.*;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.helpers.StartStop;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.record.RecordStartCommand;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.record.RecordStopCommand;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.record.Recorder;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.replay.Replay;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.replay.ReplayStartCommand;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.replay.ReplayStopCommand;

/** 
 * The Model class is responsible for grouping functionality into a single, simple, coherent class.
 * It's primary role is to provide functions for the controller to use.
 * The Model enables recording and playback of the game.
 */
public class Model {
    private StartStop recordStartStop;
    private StartStop replayStartStop;
    private Recorder recorder;
    private Replay replay;

    // Methods used to record a game
    public void startRecording(){
        recorder = new Recorder();
        recordStartStop = new StartStop(new RecordStartCommand(recorder), new RecordStopCommand(recorder));
        recordStartStop.start();
    }
    public void stopRecording(){recordStartStop.stop();}
    public void addToRecording(List<String> actions){recorder.addActions(actions);}

    // Methods used to replay a game
    public void startReplay(String game){
        replay = new Replay();
        replayStartStop = new StartStop(new ReplayStartCommand(replay), new ReplayStopCommand(replay));
        replay.load(game); // persistency.load(game);}
    }
    public void autoPlay(){replayStartStop.start();}
    public void setReplaySpeed(float speed){replay.setSpeed(speed);}
    public void stepForwardReplay(){replay.step();}
    public void stopReplay(){replayStartStop.stop();}

    // testing methods only
    public void addReplayActions(){replay.addActions();}
}
