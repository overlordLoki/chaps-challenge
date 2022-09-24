package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
import java.awt.*;
import java.io.IOException;

public class LogPanel extends JPanel{
    
    //textArea
    private JTextArea textArea;

    /**
     * Constructor for logPanel
     */
    public LogPanel() {

        this.setLayout(new GridLayout(1,1)); //so it fills the whole panel
        // make the text Area panel and set property's
		textArea = new JTextArea(); 
		textArea.setLineWrap(true); 
		textArea.setWrapStyleWord(true); // pretty line wrap.
		textArea.setBackground(Color.black);
		Font font = new Font("Verdana", Font.BOLD, 12);
		textArea.setFont(font); // set font
		textArea.setForeground(Color.green); // set color
        textArea.setEditable(false); // set editable to false
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
    }

    /**
     * read the log file and display it in the text area
     */
    public void readLogs(){
        try {
            Persistency.getLogs().forEach(log -> {
                textArea.append(log.toString());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
	 * appends String to textArea and adds \n to the string
     * @param text
	 */
	public String printLine(String s) {
		textArea.append(s+"\n");
        return s;
	}

    /**
     * appends String to textArea
     * @param s
     * @return
     */
    public String print(String s) {
        textArea.append(s);
        this.repaint();
        return s;
    }
	/**
	 * Clears all text
	 */
	public void clearText() {
		textArea.setText("");
	}
}
