package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_X;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.LOAD_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_DOWN;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_LEFT;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_RIGHT;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_UP;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.PAUSE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.QUIT_TO_MENU;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.RESUME_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.SAVE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.TO_LEVEL_1;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.TO_LEVEL_2;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller.Key;

import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.JOptionPane;
import nz.ac.vuw.ecs.swen225.gp6.app.App;
import nz.ac.vuw.ecs.swen225.gp6.persistency.AppPersistency;
import nz.ac.vuw.ecs.swen225.gp6.renderer.MazeRenderer;

/**
 * Configuration class for the App class. Stores all the settings used by the App class.
 *
 * @author Jeff Lin
 */
public class Configuration {

  private final EnumMap<Actions, Key> userKeyBindings;
  private boolean isMusicOn;
  private String texturePack;
  private int viewDistance;

  /**
   * Constructor for the Configuration class.
   *
   * @param isMusicOn       Whether the music is on or off.
   * @param userKeyBindings The key bindings for the game.
   */
  public Configuration(boolean isMusicOn, EnumMap<Actions, Key> userKeyBindings) {
    this.isMusicOn = isMusicOn;
    this.userKeyBindings = userKeyBindings;
    this.texturePack = "default";
    this.viewDistance = 7;
  }

  /**
   * Constructor for the Configuration class.
   *
   * @param isMusicOn       Whether the music is on or off.
   * @param texturePack     The texture pack for the game.
   * @param viewDistance    The view distance for the game.
   * @param userKeyBindings The key bindings for the game.
   */
  public Configuration(boolean isMusicOn, String texturePack, int viewDistance,
      EnumMap<Actions, Key> userKeyBindings) {
    this.isMusicOn = isMusicOn;
    this.texturePack = texturePack;
    this.viewDistance = viewDistance;
    this.userKeyBindings = userKeyBindings;
  }

  /**
   * Gets the default configuration if it fails to read default config file.
   *
   * @return the default configuration
   */
  public static Configuration getDefaultConfiguration() {
    return new Configuration(true, "Dogs", 7, new EnumMap<>(Map.ofEntries(
        Map.entry(MOVE_UP, new Controller.Key(0, VK_UP)),
        Map.entry(MOVE_DOWN, new Controller.Key(0, VK_DOWN)),
        Map.entry(MOVE_LEFT, new Controller.Key(0, VK_LEFT)),
        Map.entry(MOVE_RIGHT, new Controller.Key(0, VK_RIGHT)),
        Map.entry(PAUSE_GAME, new Controller.Key(0, VK_SPACE)),
        Map.entry(RESUME_GAME, new Controller.Key(0, VK_ESCAPE)),
        Map.entry(TO_LEVEL_1, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_1)),
        Map.entry(TO_LEVEL_2, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_2)),
        Map.entry(QUIT_TO_MENU, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_X)),
        Map.entry(SAVE_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_S)),
        Map.entry(LOAD_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_R))
    )));
  }

  //==============================================================================================//
  //=================================== Setter Method ============================================//
  //==============================================================================================//

  /**
   * Updates the configuration.
   *
   * @param app The App object that the configuration will be controlling.
   */
  public void update(App app) {
    MazeRenderer renderer = app.getGUI().getRenderPanel();
    renderer.setRenderSize(viewDistance);
    renderer.setTexturePack(texturePack);
  }

  /**
   * Sets the key binding for the action.
   *
   * @param action the action to set key binding
   * @param key    the key to set
   */
  public void setKeyBinding(Actions action, Controller.Key key) {
    this.userKeyBindings.put(action, key);
  }

  //==============================================================================================//
  //=================================== Getter Method ============================================//
  //==============================================================================================//

  /**
   * Gets the key binding for the action.
   *
   * @param action The action to get the key binding for.
   * @return the key binding for the action
   */
  public Controller.Key getKeyBinding(Actions action) {
    return userKeyBindings.get(action);
  }

  /**
   * Gets the list of action key bindings.
   *
   * @return the list of action key bindings
   */
  public EnumMap<Actions, Key> getUserKeyBindings() {
    return userKeyBindings.clone();
  }

  /**
   * Check if this key combo is already bound to an action.
   *
   * @param modifier The key modifier
   * @param key      the key code
   * @return true if the key is bound to an action, false otherwise
   */
  public boolean checkKeyBinding(int modifier, int key) {
    return userKeyBindings.values().stream().anyMatch(k -> k.equals(new Key(modifier, key)));
  }

  /**
   * Checks if the user is playing music.
   *
   * @return true if the user is playing music, false otherwise
   */
  public boolean isMusicOn() {
    return isMusicOn;
  }

  /**
   * Sets playing music to true or false.
   *
   * @param musicOn true if music is to be played, false otherwise
   */
  public void setMusicOn(boolean musicOn) {
    this.isMusicOn = musicOn;
  }

  /**
   * Gets the texture pack.
   *
   * @return the texture pack
   */
  public String getTexturePack() {
    return texturePack;
  }

  /**
   * Gets the view distance.
   *
   * @return the view distance
   */
  public int getViewDistance() {
    return viewDistance;
  }

  @Override
  public String toString() {
    return String.format(
        "Configuration{ isMusicOn=%s, texturePack=%s, viewDistance=%d, userKeyBindings=%s }",
        isMusicOn, texturePack, viewDistance, userKeyBindings);
  }

  /**
   * saves the configuration to a file.
   *
   * @param app the app object
   */
  public void save(App app) {
    this.viewDistance = app.getGUI().getRenderPanel().getRenderSize();
    this.texturePack = MazeRenderer.getTexturePack().getName();
    try {
      AppPersistency.save(this);
    } catch (IOException e) {
      System.err.println("Failed to save configuration.");
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Failed to save configuration.");
    }
  }
}
