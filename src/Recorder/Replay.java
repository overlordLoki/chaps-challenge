package Recorder;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Replay {
    Stack<Map<Integer, List<String>>> FutureActions;    // time, actions
    int CurrentState;
    int currentTime;
    double speed;

    public void startReplay(){
        System.out.println("Replay started");
    }
    public void stopReplay(){
        System.out.println("Replay stopped");
    }
    public void addActions(Map<Integer, List<String>> actions) {
        System.out.println("Actions adding...");
        FutureActions.add(actions);
        System.out.println("Actions added");
    }
}


record ReplayStartCommand(Replay replay) implements Command {
    @Override
    public void execute() {
        replay.startReplay();
    }
}

record ReplayStopCommand(Replay replay) implements Command {
    @Override
    public void execute() {
        replay.stopReplay();
    }
}