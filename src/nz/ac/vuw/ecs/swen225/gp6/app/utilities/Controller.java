package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.Action;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.Action.*;

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
         * Converts the key combo to a string.
         *
         * @param modifier The modifier keys pressed.
         * @param key The key pressed.
         * @return The string representation of the key combo.
         */
        public static String toString(int modifier, int key){
            return (modifier == 0 ? "": KeyEvent.getModifiersExText(modifier) + " + " ) + KeyEvent.getKeyText(key);
        }

        @Override
        public String toString(){
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
        EnumMap<Action, Key> actionKeyBindings = app.getConfiguration().getUserKeyBindings();
        Actions actions = app.getActions();
        actionsPressed = new HashMap<>();
        actionsPressed.put(actionKeyBindings.get(MOVE_UP), actions::actionUp);    // Move up
        actionsPressed.put(actionKeyBindings.get(MOVE_DOWN), actions::actionDown);  // Move down
        actionsPressed.put(actionKeyBindings.get(MOVE_LEFT), actions::actionLeft);  // Move left
        actionsPressed.put(actionKeyBindings.get(MOVE_RIGHT), actions::actionRight); // Move right
        actionsPressed.put(actionKeyBindings.get(PAUSE_GAME), actions::actionPause); // Pause game
        actionsPressed.put(actionKeyBindings.get(RESUME_GAME), actions::actionResume);// Resume game
        actionsPressed.put(actionKeyBindings.get(TO_LEVEL_1), actions::actionToLevel1); // Jump to level 1
        actionsPressed.put(actionKeyBindings.get(TO_LEVEL_2), actions::actionToLevel2); // Jump to level 2
        actionsPressed.put(actionKeyBindings.get(QUIT_GAME), actions::actionQuit);  // Quit game
        actionsPressed.put(actionKeyBindings.get(SAVE_GAME), actions::actionSave);  // Save game
        actionsPressed.put(actionKeyBindings.get(LOAD_GAME), actions::actionLoad); // Reload game
    }

    @Override
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread(): "Controller: keyPressed: Not in EDT";
        actionsPressed.getOrDefault(new Key(e.getModifiersEx(),e.getKeyCode()), ()->{}).run();
    }
}