package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.record;


public record RecordStopCommand(Recorder recorder) implements Runnable {
    @Override
    public void run() {
        recorder.stopRecording();
    }
}
