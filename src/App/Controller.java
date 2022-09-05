package App;

import App.tempDomain.Game;

import javax.swing.SwingUtilities;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.event.KeyEvent.VK_CONTROL;

/**
 * Controller class for the App class. Handles all the controllable key actions.
 *
 * @author Jeff Lin
 */
class Controller implements KeyListener {
    Actions actions;
    List<String> keyBindings;
    private Map<String, Runnable> actionsPressed = new HashMap<>();
    private Map<String, Runnable> actionsReleased = new HashMap<>();
    Boolean ctrlPressed = false;

    /**
     * Constructor for the Controller class. Initializes the actions and key bindings.
     *
     * @param keyBindings List of key bindings.
     * @param game The game for the controller to be attached to.
     */
    public Controller(List<String> keyBindings, Game game) {
        this.keyBindings = keyBindings;
        this.actions = new Actions(game);
        setController(keyBindings);
    }

    private void setController(List<String> keyBindings){
        setAction(keyBindings.get(0), actions::actionUp, ()->{});    // up
        setAction(keyBindings.get(1), actions::actionDown, ()->{});  // down
        setAction(keyBindings.get(2), actions::actionLeft, ()->{});  // left
        setAction(keyBindings.get(3), actions::actionRight, ()->{}); // right
        setAction(keyBindings.get(4), actions::actionPause, ()->{});    // Pause game
        setAction(keyBindings.get(5), actions::actionResume, ()->{});   // Resume game
        setAction(keyBindings.get(6), this::level1, ()->{});    // level 1
        setAction(keyBindings.get(7), this::level2, ()->{});    // level 2
        setAction(keyBindings.get(8), this::quitGame, ()->{});      // Quit game
        setAction(keyBindings.get(9), this::saveAndQuit, ()->{});   // Save game
        setAction(keyBindings.get(10), this::reloadGame, ()->{});   // Reload game
    }

    private void setAction(String keyName, Runnable onPressed, Runnable onReleased) {
        actionsPressed.put(keyName, onPressed);
        actionsReleased.put(keyName, onReleased);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
//        System.out.print("keyPressed="+KeyEvent.getKeyText(e.getKeyCode()) + "  ");
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = true;
        actionsPressed.getOrDefault(KeyEvent.getKeyText(e.getKeyCode()), ()->{}).run();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = false;
        actionsReleased.getOrDefault(KeyEvent.getKeyText(e.getKeyCode()), ()->{}).run();
    }


    //=========================================================================//
    //=========================== CTRL METHODS ================================//
    //=========================================================================//

    private void level1(){
        if (! ctrlPressed) return;
        System.out.println("Jump to Level 1");
    }

    private void level2(){
        if (! ctrlPressed) return;
        System.out.println("Jump to Level 2");
    }

    private void quitGame(){
        if (! ctrlPressed) return;
        actions.actionQuit();
    }

    private void saveAndQuit(){
        if (! ctrlPressed) return;
        actions.actionSave();
    }

    private void reloadGame(){
        if (! ctrlPressed) return;
        actions.actionLoad();
    }
}