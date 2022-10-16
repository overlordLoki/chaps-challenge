package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a blue lock that can be unlocked by a blue key.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class BlueLock extends Door {

  /**
   * Create a blue lock.
   *
   * @param info tile information
   */
  public BlueLock(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.BlueLock;
  }

  @Override
  public KeyColor color() {
    return KeyColor.BLUE;
  }
}
