package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.helpers;

public record StartStop(Runnable startCommand, Runnable stopCommand) {
    public void start(){startCommand.run();}
    public void stop(){stopCommand.run();}
    // void addActions(){addActionsCommand.execute();}
}
