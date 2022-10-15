package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key;

/**
 * A class representing green key which when picked up by hero, can be used to unlock green locks.
 */
public class GreenKey extends Key {

  /**
   * Create a green key.
   *
   * @param info tile information
   */
  public GreenKey(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.GreenKey;
  }

  @Override
  public KeyColor color() {
    return KeyColor.GREEN;
  }

}
