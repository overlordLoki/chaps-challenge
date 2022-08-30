package Recorder;


public interface Command {
    void execute();
}

record StartStop(Command startCommand, Command stopCommand) {
    void start(){startCommand.execute();}
    void stop(){stopCommand.execute();}
    // void addActions(){addActionsCommand.execute();}
}
