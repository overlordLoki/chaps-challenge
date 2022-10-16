package nz.ac.vuw.ecs.swen225.gp6.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This class is used to create a log panel that the user can input commands into.
 *
 * @author loki
 */
public class LogPanel extends JPanel {

  //-----------------------------------------------------fields-------------------------------------------------------//
  private final JTextArea textDisplay;
  private final Commands commands;
//-----------------------------------------------------constructor-------------------------------------------------------//

  /**
   * Constructor for logPanel.
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
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          if (history.size() == 0) {
            return;
          }
          String lastCommand = history.get(history.size() - 1);
          inputField.setText(lastCommand);
          historyTemp.add(lastCommand);
          history.remove(history.size() - 1);
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          if (historyTemp.size() == 0) {
            return;
          }
          String lastCommand = historyTemp.get(historyTemp.size() - 1);
          inputField.setText(lastCommand);
          history.add(lastCommand);
          historyTemp.remove(historyTemp.size() - 1);
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          historyTemp.clear();
          String input = inputField.getText();
          if (input.isBlank()) {
            return;
          }
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
   * appends String to textArea.
   *
   * @param s String to be appended
   * @return the String that was appended
   */
  public String print(String s) {
    textDisplay.append(s);
    this.repaint();
    return s;
  }

  /**
   * appends String to textArea with a new line.
   *
   * @param s String to be appended
   * @return the String that was appended
   */
  public String println(String s) {
    return print(s + "\n");
  }

  /**
   * clears the text area.
   */
  public void clear() {
    textDisplay.setText("");
  }


  /**
   * adds a command to the commands.
   *
   * @param command     command to be added
   * @param description description of the command
   * @param action      action to be performed
   */
  public void addCommands(String command, String description, Runnable action) {
    commands.addCommands(command, description, action);
  }

  /**
   * sets the mazeRenderer.
   *
   * @param mazeRenderer mazeRenderer to be set
   */
  public void setRenderer(MazeRenderer mazeRenderer) {
    commands.setRenderer(mazeRenderer);
  }
}
