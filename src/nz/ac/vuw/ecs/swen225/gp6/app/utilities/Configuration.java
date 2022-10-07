package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import java.util.EnumMap;

import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller.Key;

/**
 * Configuration class for the App class. Stores all the settings used by the App class.
 *
 * @author Jeff Lin
 */
public class Configuration {
    private boolean isMusicOn;
    private final EnumMap<Actions, Key> userKeyBindings;

    /**
     * Constructor for the Configuration class
     *
     * @param isMusicOn Whether the music is on or off.
     * @param userKeyBindings The key bindings for the game.
     */
    public Configuration(boolean isMusicOn, EnumMap<Actions, Key> userKeyBindings){
        this.isMusicOn = isMusicOn;
        this.userKeyBindings = userKeyBindings;
    }

    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Sets the key binding for the action.
     *
     * @param action the action to set key binding
     * @param key the key to set
     */
    public void setKeyBinding(Actions action, Controller.Key key){this.userKeyBindings.put(action,key);}

    /**
     * Sets playing music to true or false
     * @param musicOn true if music is to be played, false otherwise
     */
    public void setMusicOn(boolean musicOn) {this.isMusicOn = musicOn;}


    //================================================================================================================//
    //============================================ Getter Method =====================================================//
    //================================================================================================================//

    /**
     * Gets the key binding for the action.
     *
     * @param action The action to get the key binding for.
     * @return the key binding for the action
     */
    public Controller.Key getKeyBinding(Actions action){return userKeyBindings.get(action);}

    /**
     * Gets the list of action key bindings.
     *
     * @return the list of action key bindings
     */
    public EnumMap<Actions, Key> getUserKeyBindings() {return userKeyBindings;}

    /**
     *  Check if this key combo is already bound to an action.
     *
     * @param modifier The key modifier
     * @param key the key code
     * @return true if the key is bound to an action, false otherwise
     */
    public boolean checkKeyBinding(int modifier, int key){
        return userKeyBindings.values().stream().anyMatch(k -> k.equals(new Key(modifier,key)));
    }

    /**
     * Checks if the user is playing music.
     * @return true if the user is playing music, false otherwise
     */
    public boolean isMusicOn() {return isMusicOn;}
}
