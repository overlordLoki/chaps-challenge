package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Graphics;

public class logPanel extends JPanel{
    
    private String LogText = "";

    public logPanel() {
        
        JScrollPane scrollPane = new JScrollPane(this);
    }

    //wrtie the log to the panel
    public void writeLog(String log) {
        LogText += log;
        this.repaint();
    }
    //clear the log
    public void clearLog() {
        LogText = "";
        this.repaint();
    }
    //get the log
    public String getLog() {
        return LogText;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(LogText, 0, 0);
    }

}
