package nz.ac.vuw.ecs.swen225.gp6.recorder.innerrecorder;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import nz.ac.vuw.ecs.swen225.gp6.app.PanelCreator;

public class ReplayPanel extends JPanel{

    public ReplayPanel(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    }

    public JPanel makeReplayPanel(){
        PanelCreator.createClearPanel(BoxLayout.X_AXIS);

        return new JPanel();
    }


    public enum Images{
        PLAY, PAUSE, SPEED_UP, SPEED_DOWN;
    }
}
