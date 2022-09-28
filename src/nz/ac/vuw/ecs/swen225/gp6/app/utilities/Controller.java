package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package-private class for Controller class for the App class. Handles all the controllable key actions.
 *
 * @author Jeff Lin
 */
public class Controller extends KeyAdapter {
    /**
     * The key object that represents the key pressed.
     * @param modifier The modifier key pressed.
     * @param key The key pressed.
     */
    public record Key(int modifier, int key){

        /**
         * Creates a new Key object.
         *
         * @param modifier The modifier keys pressed.
         * @param key The key pressed.
         * @return The new Key object.
         */
        public static Key key(int modifier, int key){return new Key(modifier, key);}

        /**
         * Converts the key combo to a string.
         *
         * @param modifier The modifier keys pressed.
         * @param key The key pressed.
         * @return The string representation of the key combo.
         */
        public static String toString(int modifier, int key){
            return (modifier == 0 ? "": KeyEvent.getModifiersExText(modifier) + " + " ) + KeyEvent.getKeyText(key);
        }
    }

    private final App app;
    private Map<Key, Runnable> actionsPressed;

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
    public void update(){
        List<Key> keyBindings = app.getConfiguration().getUserKeyBindings();
        Actions actions = app.getActions();
        actionsPressed = new HashMap<>();
        actionsPressed.put(keyBindings.get(0), actions::actionUp);    // Move up
        actionsPressed.put(keyBindings.get(1), actions::actionDown);  // Move down
        actionsPressed.put(keyBindings.get(2), actions::actionLeft);  // Move left
        actionsPressed.put(keyBindings.get(3), actions::actionRight); // Move right
        actionsPressed.put(keyBindings.get(4), actions::actionPause); // Pause game
        actionsPressed.put(keyBindings.get(5), actions::actionResume);// Resume game
        actionsPressed.put(keyBindings.get(6), actions::actionToLevel2); // Jump to level 1
        actionsPressed.put(keyBindings.get(7), actions::actionToLevel1); // Jump to level 2
        actionsPressed.put(keyBindings.get(8), actions::actionQuit);  // Quit game
        actionsPressed.put(keyBindings.get(9), actions::actionSave);  // Save game
        actionsPressed.put(keyBindings.get(10), actions::actionLoad); // Reload game
    }

    @Override
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread(): "Controller: keyPressed: Not in EDT";
        actionsPressed.getOrDefault(Key.key(e.getModifiersEx(),e.getKeyCode()), ()->{}).run();
    }
}