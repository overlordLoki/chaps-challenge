package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.*;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller.Key;

/**
 * Configuration class for the App class. Stores all the settings used by the App class.
 *
 * @author Jeff Lin
 */
public class Configuration {
    // Fields for the configuration
    private final List<String> actionNames = List.of("Move Up","Move Down","Move Left","Move Right","Pause Game",
            "Resume Game","Jump To Level 1","Jump To Level 2","Quit Game","Save And Quit Game","Reload Game");
    private int indexOfKeyToSet = -1;
    private final int NO_MOD = 0;

    // Default settings for the game
    private final boolean defaultMusicOn = true;
    private final List<Controller.Key> defaultKeyBindings =
            List.of(new Key(NO_MOD,VK_UP),new Key(NO_MOD,VK_DOWN),new Key(NO_MOD,VK_LEFT),new Key(NO_MOD,VK_RIGHT),
                    new Key(NO_MOD,VK_SPACE),new Key(NO_MOD,VK_ESCAPE),
                    new Key(InputEvent.CTRL_DOWN_MASK,VK_1),new Key(InputEvent.CTRL_DOWN_MASK,VK_2),
                    new Key(InputEvent.CTRL_DOWN_MASK,VK_X),new Key(InputEvent.CTRL_DOWN_MASK,VK_S),new Key(InputEvent.CTRL_DOWN_MASK,VK_R));

    // User settings for the game
    private boolean isMusicOn = defaultMusicOn;
    private List<Controller.Key> userKeyBindings = new ArrayList<>(defaultKeyBindings);


    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Loads the user's setting configuration.
     */
    public void loadUserSettings(){
        System.out.print("Loading user settings... ");
        this.userKeyBindings = new ArrayList<>(defaultKeyBindings);
        this.isMusicOn = true;
        System.out.println("Complete");
    }


    /**
     * exits the key setting mode so another action can be selected for setting key binding.
     */
    public void exitKeySettingMode(){this.indexOfKeyToSet = -1;}

    /**
     * Sets the index of the action to set a different key binding.
     *
     * @param indexOfKeyToSet the index of the action to set key
     */
    public void setIndexOfKeyToSet(int indexOfKeyToSet) {this.indexOfKeyToSet = indexOfKeyToSet;}

    /**
     * Sets the key binding for the action.
     * @param index the index of the action to set key
     * @param key the key to set
     */
    public void setKeyBinding(int index, Controller.Key key){this.userKeyBindings.set(index, key);}

    /**
     * Sets playing music to true or false
     * @param musicOn true if music is to be played, false otherwise
     */
    public void setMusicOn(boolean musicOn) {this.isMusicOn = musicOn;}


    //================================================================================================================//
    //============================================ Getter Method =====================================================//
    //================================================================================================================//

    /**
     * Gets the action name
     * @param index the index of the action
     * @return the name of the action
     */
    public String getActionName(int index){return actionNames.get(index);}

    /**
     * Gets the key binding for the action.
     * @param index the index of the action
     * @return the key binding for the action
     */
    public Controller.Key getKeyBinding(int index){return userKeyBindings.get(index);}

    /**
     * Gets the index of the action to set a different key binding.
     *
     * @return the setting key
     */
    public int indexOfKeyToSet() {return indexOfKeyToSet;}

    /**
     * Returns if any action is ready to be set to  different key binding.
     *
     * @return true if the key is bound to an action, false otherwise
     */
    public boolean inSettingKeyMode(){return indexOfKeyToSet != -1;}

    /**
     * Gets the list of action key bindings.
     *
     * @return the list of action key bindings
     */
    public List<Controller.Key> getUserKeyBindings() {return userKeyBindings;}

    /**
     *  Check if this key combo is already bound to an action.
     * @param key the key to check
     * @return true if the key is bound to an action, false otherwise
     */
    public boolean checkKeyBinding(Controller.Key key){return userKeyBindings.contains(key);}

    /**
     * Checks if the user is playing music.
     * @return true if the user is playing music, false otherwise
     */
    public boolean isMusicOn() {return isMusicOn;}
}
