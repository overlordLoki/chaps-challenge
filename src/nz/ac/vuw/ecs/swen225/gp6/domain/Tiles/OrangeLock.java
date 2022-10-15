package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a orange lock that can be unlocked by a orange key.
 */
public class OrangeLock extends Door {

  /**
   * Create a orange lock.
   *
   * @param info tile information
   */
  public OrangeLock(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.OrangeLock;
  }

  @Override
  public KeyColor color() {
    return KeyColor.ORANGE;
  }
}
