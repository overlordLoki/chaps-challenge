package Recorder;


import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

/** 
 * The Model handles the recording a replay functionalities.
 */
class Model {
    // private int currentState;
    // private int currentTime;
    // private double speed;
    private final StartStop recordStartStop;
    private final StartStop replayStartStop;
    private final Recorder recorder;
    private final Replay replay;

    public Model(){
        // currentState = 0;
        // currentTime = 0;
        // speed = 1;
        recorder = new Recorder();
        recordStartStop = new StartStop(new RecordStartCommand(recorder), new RecordStopCommand(recorder));
        replay = new Replay();
        replayStartStop = new StartStop(new ReplayStartCommand(replay), new ReplayStopCommand(replay));
    }

    public void startRecording(){recordStartStop.start();}
    public void addToRecording(int time, List<String> action){recorder.addActions(time, action);}
    public void stopRecording(){recordStartStop.stop();}
    public void loadGame(String game){replay.load(game);} // persistency.load(game);}
    public void startReplay(){replayStartStop.start();}
    public void stopReplay(){replayStartStop.stop();}
}
