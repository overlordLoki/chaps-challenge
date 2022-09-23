package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;

import javax.swing.JFrame;

import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Model;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.ReplayPanel;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.datastructures.Pair;


public class FakeApp<E> {
    public static Long time = 0L;
    public Model<E> model;

    public FakeApp() {
        this.model = new Model<E>();
    }

    public static void performActions(Pair<Long, List<String>> timeActions){
        System.out.println("Actual time: " + time +
                            ", Recorded time: " + timeActions.getKey() +
                            ", Recorded actions: " + timeActions.getValue().toString());
    }
    public static void queueActions(Pair<Long, List<String>> actions){
        System.out.println("App time: " + time + 
                            ", App queued time: " + actions.getKey() + 
                            ", actions: " + actions.getValue().toString());

    }
    
    public static void main(String[] args){
        JFrame frame = new JFrame();
        ReplayPanel panel = new ReplayPanel();
        panel.setBounds(40,80,200,200);
        frame.add(panel);
        frame.setContentPane(panel);
        frame.setSize(500,500);
        frame.setVisible(true);

        // app.model.startRecording();

        // List<String> actions = List.of("character spawned", "bug moved");
        // app.model.addToRecording(actions);
        // time++;
        // actions = List.of("up", "down");
        // app.model.addToRecording(actions);
        // time++;
        // actions = List.of("left", "right");
        // app.model.addToRecording(actions);
        // time++;
        // app.model.stopRecording();
        
        // app.model.startReplay("myGame");
        // app.model.addReplayActions();
        // app.model.checkActions();
        // app.model.autoPlay();    // exhaust actions
        // app.model.stopReplay(); 
        // model.stepForwardReplay();
        // model.setReplaySpeed(2);
        // model.autoPlay();
        // model.toString();
        // model.autoPlay();
        // model.stopReplay();
        // model.addReplayActions();
        // model.autoPlay();
        // model.startReplay("lol");
        // model.addReplayActions();
        // model.autoPlay();
        // model.stopReplay();
        // model.setReplaySpeed(2);

    }
}
