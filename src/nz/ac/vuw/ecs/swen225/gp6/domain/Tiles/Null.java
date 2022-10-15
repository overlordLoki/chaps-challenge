package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.AbstractTile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;

/**
 * This class is made to be used as a somewhat empty tile template for various circumstances
 * internally in domain(such as in inventory). This helps to not return a null tile when things go
 * awry. It is not actually a tile in the game and should never be on maze.
 */
public class Null extends AbstractTile {

  /**
   * Create a Null tile.
   *
   * @param info tile information
   */
  public Null(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.Null;
  }
}
