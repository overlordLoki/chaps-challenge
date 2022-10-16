package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key;

/**
 * A class representing orange key which when picked up by hero, can be used to unlock orange
 * locks.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class OrangeKey extends Key {

  /**
   * Create a orange key.
   *
   * @param info tile information
   */
  public OrangeKey(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.OrangeKey;
  }

  @Override
  public KeyColor color() {
    return KeyColor.ORANGE;
  }
}
