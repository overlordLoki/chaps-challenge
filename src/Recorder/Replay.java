package Recorder;

import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Replay {
    Stack<Map<Integer, List<String>>> FutureActions;    // time, actions
    int CurrentState;   
    int currentTime;
    double speed;
    String game;

    public void load(String game){
        // FutureActions = Persistency.load(game);
        this.game = game;
        System.out.println("Loading game: " + game);
    }
    public void startReplay(){
        if(game == null){
            System.out.println("No game loaded");
            return;
        }
        System.out.println("Replay started");
    }
    public void stopReplay(){
        System.out.println("Replay stopped");
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