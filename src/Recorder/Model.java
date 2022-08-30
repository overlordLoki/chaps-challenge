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

    public Model(){
        // currentState = 0;
        // currentTime = 0;
        // speed = 1;
        recorder = new Recorder();
        recordStartStop = new StartStop(new RecordStopCommand(recorder), new RecordStartCommand(recorder));
        Replay replay = new Replay();
        replayStartStop = new StartStop(new ReplayStartCommand(replay), new ReplayStopCommand(replay));
    }

    public void startRecording(){recordStartStop.start();}
    public void addToRecording(Map<Integer, List<String>> timeActions){recorder.addActions(timeActions);}
    public void stopRecording(){recordStartStop.stop();}
    public void startReplay(){replayStartStop.start();}
    public void stopReplay(){replayStartStop.stop();}
}
