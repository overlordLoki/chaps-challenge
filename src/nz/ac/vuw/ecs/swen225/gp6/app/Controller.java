package nz.ac.vuw.ecs.swen225.gp6.app;

import javax.swing.SwingUtilities;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.event.KeyEvent.VK_CONTROL;

/**
 * Package-private class for Controller class for the App class. Handles all the controllable key actions.
 *
 * @author Jeff Lin
 */
class Controller extends KeyAdapter {
    private final App app;
    private Map<Integer, Runnable> actionsPressed;
    private Map<Integer, Runnable> actionsReleased;
    private Boolean ctrlPressed = false;

    /**
     * Constructor for the Controller class. Initializes the actions and key bindings.
     *
     * @param app The App object that the controller will be controlling.
     */
    public Controller(App app) {
        this.app = app;
        resetController();
    }

    public void resetController(){
        List<Integer> keyBindings = app.getActionKeyBindings();
        Actions actions = app.getActions();
        actionsPressed = new HashMap<>();
        actionsReleased = new HashMap<>();
        setAction(keyBindings.get(0), actions::actionUp, ()->{});    // up
        setAction(keyBindings.get(1), actions::actionDown, ()->{});  // down
        setAction(keyBindings.get(2), actions::actionLeft, ()->{});  // left
        setAction(keyBindings.get(3), actions::actionRight, ()->{}); // right
        setAction(keyBindings.get(4), actions::actionPause, ()->{});    // Pause game
        setAction(keyBindings.get(5), actions::actionResume, ()->{});   // Resume game
        setAction(keyBindings.get(6), runIfCtrlPressed(actions::actionToLevel2), ()->{});  // level 1
        setAction(keyBindings.get(7), runIfCtrlPressed(actions::actionToLevel1), ()->{});  // level 2
        setAction(keyBindings.get(8), runIfCtrlPressed(actions::actionQuit), ()->{});  // Quit game
        setAction(keyBindings.get(9), runIfCtrlPressed(actions::actionSave), ()->{});  // Save game
        setAction(keyBindings.get(10), runIfCtrlPressed(actions::actionLoad), ()->{}); // Reload game
    }

    private void setAction(int keyName, Runnable onPressed, Runnable onReleased) {
        actionsPressed.put(keyName, onPressed);
        actionsReleased.put(keyName, onReleased);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread(): "Controller: keyPressed: Not in EDT";
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = true;
        actionsPressed.getOrDefault(e.getKeyCode(), ()->{}).run();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread(): "Controller: keyReleased: Not in EDT";
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = false;
        actionsReleased.getOrDefault(e.getKeyCode(), ()->{}).run();
    }


    //===================================================================================================//
    //======================================== CTRL METHODS =============================================//
    //===================================================================================================//

    private Runnable runIfCtrlPressed(Runnable runnable){
        return () -> {if (ctrlPressed) runnable.run();};
    }
}