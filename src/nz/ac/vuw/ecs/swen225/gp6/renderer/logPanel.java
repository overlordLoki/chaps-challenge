package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.*;

public class logPanel extends JPanel{
    
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

    
    public void writeLog(String s) {
        printLine(s);
    }
	
	/*
	 * Clears all text
	 */
	public void clearText() {
		textArea.setText("");
	}

}
