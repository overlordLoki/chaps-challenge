package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;

public class LogPanel extends JPanel{
    
    //textArea
    private JTextArea textArea;
    private Commands commands;
    private MazeRenderer mazeRenderer;

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
        textArea.setEditable(true); // set editable to false
        //add textArea to scrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane);
        commands = new Commands(this);

        //add key listeners to textArea
        textArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){
                    //get the previous line of text
                    String previousLine = getLastLine();
                    checkCommand(previousLine);
                }
            }
        });
        //add a cursor to the textArea
        textArea.getCaret().setVisible(true);
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

    //println
    /**
     * appends String to textArea with a new line
     */
    public String println(String s) {
        return print(s + "\n");
    }

    //every time user enter text check if is command.
    /**
     * checks if the previous line is a command
     * @param s
     */
    public void checkCommand(String s) {
       commands.invoke(s);
    }

    	
	/**
	 * gets the last line of text from textArea
	 */
	public String getLastLine() {
		//stackoverflow? like the run time error? never heard of that Website
		try {
			int end = textArea.getDocument().getLength();
			int start = Utilities.getRowStart(textArea, end);
		while (start == end){
		    end--;
		    start = Utilities.getRowStart(textArea, end);
		}
		return textArea.getText(start, end - start);
		} catch (BadLocationException e) {e.printStackTrace();}
		return "";
	}

    /**
     * return the text area
     * @return textArea
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * adds a command to the commands
     */
    public void addCommands(String command, String discription, Runnable action) {
        commands.addCommands(command, discription, action);
    }

    /**
     * sets the mazeRenderer
     * @param mazeRenderer
     */
    public void setRenderer(MazeRenderer mazeRenderer) {
        this.mazeRenderer = mazeRenderer;
        commands.setRenderer(mazeRenderer);
    }
}
