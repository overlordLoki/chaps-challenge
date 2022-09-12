package Recorder;


import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/** 
 * The Model handles the recording a replay functionalities.
 */
class Model {
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
