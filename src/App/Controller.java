package App;

import App.tempDomain.Game;

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
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, Runnable> actionsPressed;
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, Runnable> actionsReleased;
    private final Actions actions;
    private Boolean ctrlPressed = false;

    /**
     * Constructor for the Controller class. Initializes the actions and key bindings.
     *
     * @param keyBindings List of key bindings.
     * @param game The game for the controller to be attached to.
     */
    public Controller(List<String> keyBindings, Game game) {
        this.actions = new Actions(game);
        setController(keyBindings);
    }

    public void setController(List<String> keyBindings){
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

    private void setAction(String keyName, Runnable onPressed, Runnable onReleased) {
        actionsPressed.put(keyName, onPressed);
        actionsReleased.put(keyName, onReleased);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = true;
        actionsPressed.getOrDefault(KeyEvent.getKeyText(e.getKeyCode()), ()->{}).run();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = false;
        actionsReleased.getOrDefault(KeyEvent.getKeyText(e.getKeyCode()), ()->{}).run();
    }


    //===================================================================================================//
    //======================================== CTRL METHODS =============================================//
    //===================================================================================================//

    private Runnable runIfCtrlPressed(Runnable runnable){
        return () -> {if (ctrlPressed) runnable.run();};
    }
}