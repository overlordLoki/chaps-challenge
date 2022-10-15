package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.AbstractTile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;

/**
 * A class representing an empty INVENTORY tile. The name can be confusing, but this tile is only to
 * be used to draw the empty inventory tiles in the game.
 */
public class Empty extends AbstractTile {

  /**
   * Create empty(INVENTORY ONLY) tile
   *
   * @param info tile information
   */
  public Empty(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.Empty;
  }
}
