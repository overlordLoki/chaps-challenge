package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key;

/**
 * A class representing blue key which when picked up by hero, can be used to unlock blue locks.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class BlueKey extends Key {

  /**
   * Create a blue key.
   *
   * @param info tile information
   */
  public BlueKey(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.BlueKey;
  }

  @Override
  public KeyColor color() {
    return KeyColor.BLUE;
  }

}
