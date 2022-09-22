package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.replay;


public record ReplayStopCommand(Replay replay) implements Runnable {
    @Override
    public void run() {replay.stopReplay();}
}
