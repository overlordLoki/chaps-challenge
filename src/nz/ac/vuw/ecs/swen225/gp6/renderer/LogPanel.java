package nz.ac.vuw.ecs.swen225.gp6.renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;
/**
 * This class is used to create a log panel that the user can input commands into.
 * @author loki
 */
public class LogPanel extends JPanel{
//-----------------------------------------------------fields-------------------------------------------------------//
    private final JTextArea textDisplay;
    private final Commands commands;
//-----------------------------------------------------constructor-------------------------------------------------------//
    /**
     * Constructor for logPanel
     */
    public LogPanel() {
        commands = new Commands(this);
        textDisplay = new JTextArea();
        JTextField inputField = new JTextField();
        Font font = new Font("Verdana", Font.BOLD, 12);
        List<String> history = new ArrayList<>();
        List<String> historyTemp = new ArrayList<>();
        //settings for textDisplay
        textDisplay.setEditable(false);
        textDisplay.setLineWrap(true);
        textDisplay.setWrapStyleWord(true); // pretty line wrap.
        textDisplay.setBackground(Color.black);
        textDisplay.setForeground(Color.green);
        textDisplay.setFont(font);
        //settings for inputField
        inputField.setPreferredSize(new Dimension(500, 30));
        inputField.setMaximumSize(new Dimension(500, 30));
        inputField.setBackground(Color.green);
        inputField.setForeground(Color.black);
        inputField.setFont(font);
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP){
                    if (history.size() == 0) {return;}
                    String lastCommand = history.get(history.size() - 1);
                    inputField.setText(lastCommand);
                    historyTemp.add(lastCommand);
                    history.remove(history.size() - 1);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN){
                    if (historyTemp.size() == 0) return;
                    String lastCommand = historyTemp.get(historyTemp.size() - 1);
                    inputField.setText(lastCommand);
                    history.add(lastCommand);
                    historyTemp.remove(historyTemp.size() - 1);
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    historyTemp.clear();
                    String input = inputField.getText();
                    if (input.isBlank()) return;
                    history.add(input);
                    commands.invoke(input);
                    inputField.setText("");
                }
            }
        });
        // attach the components into this panel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(new JScrollPane(textDisplay)); // add textArea to scrollPane
        this.add(inputField);
    }
//-----------------------------------------------------methods-------------------------------------------------------//
    /**
     * appends String to textArea
     * @param s
     * @return
     */
    public String print(String s) {
        textDisplay.append(s);
        this.repaint();
        return s;
    }

    /**
     * appends String to textArea with a new line
     */
    public String println(String s) {
        return print(s + "\n");
    }

    /**
     * return the text area
     * @return textArea
     */
    public void clear() {
        textDisplay.setText("");
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
        commands.setRenderer(mazeRenderer);
    }
}
