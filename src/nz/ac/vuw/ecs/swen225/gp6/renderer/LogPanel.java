package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.*;

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
     * appends String to textArea
     * @param s
     * @return
     */
    public String print(String s) {
        textArea.append(s);
        this.repaint();
        return s;
    }
}
