package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.*;
import java.awt.Graphics;

public class logPanel extends JPanel{
    
    private String LogText = "";
    //textArea
    private JTextArea textArea;

    public logPanel() {

        // make the text Area panel and set property's
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true); // pretty line wrap.
		textArea.setBackground(Color.black);
		Font font = new Font("Verdana", Font.BOLD, 12);
		textArea.setFont(font);
		textArea.setForeground(Color.green);

        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
    }

    //write the log to the panel
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

    	/*
	 * appends String to textArea and adds \n to the string
	 */
	public void printLine(String s) {
		textArea.append(s+"\n");
	}
    
	public void printLine(int i) {
		String s = Integer.toString(i);
		textArea.append(s+"\n");
	}
	public void printLine(double d) {
		String s = Double.toString(d);
		textArea.append(s+"\n");
	}
	
	/*
	 * Clears all text
	 */
	public void clearText() {
		textArea.setText("");
	}
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString(LogText, 0, 0);
    }

}
