package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a yellow lock that can be unlocked by a yellow key.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class YellowLock extends Door {

  /**
   * Create a yellow lock.
   *
   * @param info tile information
   */
  public YellowLock(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.YellowLock;
  }

  @Override
  public KeyColor color() {
    return KeyColor.YELLOW;
  }
}
