package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.NONE;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;
import nz.ac.vuw.ecs.swen225.gp6.app.App;

/**
 * Package-private class for Controller class for the App class. Handles all the controllable key
 * actions.
 *
 * @author Jeff Lin
 */
public class Controller extends KeyAdapter {

  private final App app;
  private Map<Key, Actions> actionKeyBindings;

  /**
   * Constructor for the Controller class. Initializes the actions and key bindings.
   *
   * @param app The App object that the controller will be controlling.
   */
  public Controller(App app) {
    this.app = app;
    update();
  }

  /**
   * Updates the key bindings to the current setting.
   */
  public void update() {
    actionKeyBindings = new HashMap<>();
    app.getConfiguration().getUserKeyBindings()
        .forEach((action, key) -> actionKeyBindings.put(key, action));
  }

  @Override
  public void keyPressed(KeyEvent e) {
    assert SwingUtilities.isEventDispatchThread() : "Controller: keyPressed: Not in EDT";
    actionKeyBindings.getOrDefault(new Key(e.getModifiersEx(), e.getKeyCode()), NONE).run(app);
  }

  /**
   * The key object that represents the key pressed.
   *
   * @param modifier The modifier key pressed.
   * @param key      The key pressed.
   */
  public record Key(int modifier, int key) {

    /**
     * Converts the key combo to a string.
     *
     * @param modifier The modifier keys pressed.
     * @param key      The key pressed.
     * @return The string representation of the key combo.
     */
    public static String toString(int modifier, int key) {
      return (modifier == 0 ? "" : KeyEvent.getModifiersExText(modifier) + " + ")
          + KeyEvent.getKeyText(key);
    }

    @Override
    public String toString() {
      return (modifier == 0 ? "" : KeyEvent.getModifiersExText(modifier) + " + ")
          + KeyEvent.getKeyText(key);
    }
  }
}