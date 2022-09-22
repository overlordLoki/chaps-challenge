package nz.ac.vuw.ecs.swen225.gp6.recorder;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp6.app.PanelCreator;
import nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder.Model;

/*
 * This controller is a panel that can be added to the App.
 * The RecorderController can only communicate with the Model from innerrecorder and the App.
 * The purpose of this controller is to provide a GUI for the user 
 * to interact with the record and replay functionality.
 */
public class RecorderCommunicator<E> {

    private Model<E> model;

    public JPanel makeReplayPanel(){
        PanelCreator.createClearPanel(BoxLayout.X_AXIS);

        return new JPanel();
    }

    public void startReplay(){
        model.startReplay("myGame");
        model.autoPlay();
    }

    
}
