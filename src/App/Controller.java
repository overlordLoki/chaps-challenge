package App;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.event.KeyEvent.VK_CONTROL;

class Controller implements KeyListener {
    List<String> keyBindings;
    private Map<String, Runnable> actionsPressed = new HashMap<>();
    private Map<String, Runnable> actionsReleased = new HashMap<>();
    Boolean ctrlPressed = false;

    public Controller(List<String> keyBindings) {
        this.keyBindings = keyBindings;
        setController(keyBindings);
    }

    public void setController(List<String> keyBindings){
        setAction(keyBindings.get(0), Actions::actionUp, ()->{});
        setAction(keyBindings.get(1), Actions::actionDown, ()->{});
        setAction(keyBindings.get(2), Actions::actionLeft, ()->{});
        setAction(keyBindings.get(3), Actions::actionRight, ()->{});
        setAction(keyBindings.get(4), Actions::actionStart, ()->{});
        setAction(keyBindings.get(5), Actions::actionResume, ()->{});
        setAction(keyBindings.get(6), ()->{}, ()->{}); // level 1
        setAction(keyBindings.get(7), ()->{}, ()->{}); // level 2
        setAction(keyBindings.get(8), Actions::actionQuit, ()->{});
        setAction(keyBindings.get(9), Actions::actionSave, ()->{});
        setAction(keyBindings.get(10), Actions::actionResume, ()->{});
        setAction(keyBindings.get(11), ()->setCtrl(true), ()->setCtrl(false));
    }

    public void setAction(String keyName, Runnable onPressed, Runnable onReleased) {
        actionsPressed.put(keyName, onPressed);
        actionsReleased.put(keyName, onReleased);
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = true;
        actionsPressed.getOrDefault(KeyEvent.getKeyText(e.getKeyCode()), ()->{}).run();
    }

    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        if (e.getKeyCode() == VK_CONTROL) ctrlPressed = false;
        actionsReleased.getOrDefault(KeyEvent.getKeyText(e.getKeyCode()), ()->{}).run();
    }

    private void setCtrl(boolean isPressed){
        System.out.println("ctrl pressed: " + isPressed);
        ctrlPressed = isPressed;
    }

    //=========================================================================//
    //=========================== GUI METHODS =================================//
    //=========================================================================//

    private void actionPause(){}
    private void actionResume(){}

    private void actionLevel1(){
        if (! ctrlPressed) return;
//        Domain.currentLevel = level1;
    }
    private void actionLevel2(){
        if (! ctrlPressed) return;
//        Domain.currentLevel = level1;
    }

    private void actionQuit(){
        if (! ctrlPressed) return;
    }
    private void actionSaveAndQuit(){
        if (! ctrlPressed) return;
    }
    private void actionResumeSelection(){
        if (! ctrlPressed) return;
    }


    //=========================================================================//
    //========================== PLAYER METHODS ===============================//
    //=========================================================================//

    private void actionMoveUp(App app) {
        if (!ctrlPressed) return;
        app.game.moveUp();
    }

}