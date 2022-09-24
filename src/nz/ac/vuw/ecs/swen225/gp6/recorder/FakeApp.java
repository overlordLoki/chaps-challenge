package nz.ac.vuw.ecs.swen225.gp6.recorder;


import nz.ac.vuw.ecs.swen225.gp6.app.App;

// import javax.swing.JFrame;



public class FakeApp {
    public Long time = 0L;
    
    public static void main(String[] args){
        Recorder r = new Recorder();
        Replay replay = new Replay(new App());
        r.startRecording();
        r.addActions(0L, () -> System.out.println("action1!"));
        r.addActions(1L, () -> System.out.println("action2!"));
        r.addActions(2L, () -> System.out.println("action3!"));
        r.stopRecording();
        replay.load("First Game");
        replay.autoPlay();
        
    }
}
