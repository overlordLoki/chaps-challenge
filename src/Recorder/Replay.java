package Recorder;

import java.util.List;
import java.util.Stack;

public class Replay {
    Stack<List<String>> futureActions;
    Stack<List<String>> pastActions; 
    int currentState;   
    float speed;
    String game;
    

    /**
     * Load method
     */
    public void load(String game){
        // FutureActions = Persistency.load(game);
        this.game = game;
        futureActions = new Stack<>();
        pastActions = new Stack<>();
        currentState = 0;   // initialised by loaded game
        speed = 1;          // changed by the app
        System.out.println("Loading game: " + game);
    }

    /**
     * Step methods
     */
    public void stepForward(){
        if (futureActions.isEmpty()){
            System.out.println("End of file, cannot step forward.");
            return;
        }
        List<String> actions = futureActions.pop();
        pastActions.add(actions);
        System.out.println("Replaying actions: " + actions.toString());
        System.out.println("state: " + ++currentState);
        // App.execute(actions);
    }
    public void stepBackward(){
        if(pastActions.isEmpty()){
            System.out.println("Start of file, cannot rewind further");
            return;
        }
        List<String> actions = pastActions.pop();
        futureActions.add(actions);
        System.out.println("Rewinding actions: " + actions.toString());
        System.out.println("state: " + --currentState);
    }
    // public void step(Direction direction) {
    // }

    /**
     * Auto play methods
     */
    public void autoPlay(){
        System.out.println("Auto Replay started");
        if(futureActions.isEmpty()) {
            System.out.println("Please reload a game.");
            return;
        }
        while(!futureActions.isEmpty()){
            stepForward();
            try {Thread.sleep((long) (1000/speed));} 
            catch (InterruptedException e) {e.printStackTrace();}
        }
    }
    public void stopReplay(){System.out.println("Replay stopped"); }
    public void setSpeed(float speed) {this.speed = speed; }

    /**
     * Methods for testing replay functionality
     */
    public void addActions(){
        futureActions.add(List.of("character spawned", "bug moved"));
        futureActions.add(List.of("up", "down"));
        futureActions.add(List.of("left", "right"));
        futureActions = new ReverseStack(futureActions).execute();
    }
}


record ReplayStartCommand(Replay replay) implements Command {
    @Override
    public void execute() { replay.autoPlay(); }
}

record ReplayStopCommand(Replay replay) implements Command {
    @Override
    public void execute() {replay.stopReplay();}
}

