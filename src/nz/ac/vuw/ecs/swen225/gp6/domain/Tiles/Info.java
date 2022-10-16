package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.AbstractTile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;

/**
 * This class represents a info tile which when only hero can move on, and will display hints for
 * the current level. Each level currently at most can have one of these tiles.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class Info extends AbstractTile { //future idea: not disappear after once usage

  /**
   * Create a info tile.
   *
   * @param info tile information
   */
  public Info(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.Info;
  }

  /**
   * gets the hint message to be displayed when hero moves on this tile.
   *
   * @return hint message
   */
  public String message() {
    return info().message();
  }

  @Override
  public boolean obstructsEnemy(Domain d) {
    return true;
  }

  @Override
  public Tile replaceWith() {
    return this;
  } //the info tile is permanent

}
