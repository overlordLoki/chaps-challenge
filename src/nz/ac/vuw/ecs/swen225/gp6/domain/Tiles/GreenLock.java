package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a green lock that can be unlocked by a green key.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class GreenLock extends Door {

  /**
   * Create a green lock.
   *
   * @param info tile information
   */
  public GreenLock(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.GreenLock;
  }

  @Override
  public KeyColor color() {
    return KeyColor.GREEN;
  }

}
