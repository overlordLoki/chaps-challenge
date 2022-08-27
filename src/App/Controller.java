package App;


import Domain.Domain;

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
        /*
        setAction(keyBindings.get(0), c.set(Domain.Direction::up), c.set(Domain.Direction::unUp));
        setAction(keyBindings.get(1), c.set(Domain.Direction::up), c.set(Domain.Direction::unUp));
        setAction(keyBindings.get(2), c.set(Domain.Direction::up), c.set(Domain.Direction::unUp));
        setAction(keyBindings.get(3), c.set(Domain.Direction::up), c.set(Domain.Direction::unUp));
        setAction(keyBindings.get(4), Controller::actionPause, ()->{});
        setAction(keyBindings.get(5), Controller::actionResume, ()->{});
        setAction(keyBindings.get(6), Controller::actionLevel1, ()->{});
        setAction(keyBindings.get(7), Controller::actionLevel2, ()->{});
        setAction(keyBindings.get(8), Controller::actionQuit, ()->{});
        setAction(keyBindings.get(9), Controller::actionSaveAndQuit, ()->{});
        setAction(keyBindings.get(10), Controller::actionResumeSelection, ()->{});
        setAction(keyBindings.get(11), ()->setCtrl(true), ()->setCtrl(false));*/
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

}