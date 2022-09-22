package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.replay;


public record ReplayStartCommand(Replay replay) implements Runnable {
    @Override
    public void run() { replay.autoPlay(); }
}