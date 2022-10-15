package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key;

/**
 * A class representing yellow key which when picked up by hero, can be used to unlock yellow
 * locks.
 */
public class YellowKey extends Key {

  /**
   * Create a yellow key.
   *
   * @param info tile information
   */
  public YellowKey(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.YellowKey;
  }

  @Override
  public KeyColor color() {
    return KeyColor.YELLOW;
  }
}
