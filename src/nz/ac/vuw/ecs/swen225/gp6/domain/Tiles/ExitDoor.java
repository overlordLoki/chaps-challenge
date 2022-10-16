package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a exit door that represents the exit door when all coins/treasures aren't
 * yet collected. No one can pass through it until all coins are collected.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class ExitDoor extends Door {

  /**
   * Create an exit door tile.
   *
   * @param info tile information
   */
  public ExitDoor(TileInfo info) {
    super(info);
  }

  @Override
  public TileType type() {
    return TileType.ExitDoor;
  }

  @Override
  public KeyColor color() {
    return KeyColor.NONE;
  }

  @Override
  public boolean obstructsHero(Domain d) {
    return true;
  } //no one can go through un opened exit door

  @Override
  public boolean obstructsEnemy(Domain d) {
    return true;
  }
}
