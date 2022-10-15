package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

public interface Tile {
  //INFO ABOUT TILE:

  /**
   * Each tile object will hold a reference to one TileState enum, which determines the behaviour of
   * a number of the tiles important methods.
   *
   * @return the enum type of this tile, if custom tile then TileType.Other is returned.
   */
  TileType type();

  /**
   * Each tile  will hold an info object, which stores most "field-like" info that the tile may
   * have. Eg location of tile, number of pings that tile has seen, image name of the tile, etc.
   * This allows us to pass only one parameter when constructing a tile.
   *
   * @return TileInfo object of the this tile
   */
  TileInfo info();

  /**
   * Each tile type should have a symbol, this is for testing purposes.
   *
   * @return symbol character for this tile type
   */
  char symbol();

  //MOVING ON AND OFF THE TILE:

  /**
   * This method checks wether a given tile can cause damage/kill the hero. this is not a field,
   * since tiles may behave different depending on the state of the game. e.g having armor in
   * inventory NOTE: does not alter the domain or this tile in anyway.
   *
   * @param d game domain to check on
   * @return true if damages hero, if not implemented, defaults to false
   */
  default boolean damagesHero(Domain d) {
    return false;
  }

  /**
   * Checks wether this tile is an obstruction for hero tile ,in a given domain. NOTE: does not
   * alter the domain or this tile in anyway.
   *
   * @param d game domain to check on
   * @return true if obstructs hero, if not implemented, defaults to false.
   */
  default boolean obstructsHero(Domain d) {
    return false;
  }

  /**
   * Checks wether this tile is an obstruction for enemy tile ,in a given domain. NOTE: does not
   * alter the domain or this tile in anyway.
   *
   * @param d game domain to check on
   * @return true if obstructs enemy, if not implemented, defaults to false.
   */
  default boolean obstructsEnemy(Domain d) {
    return false;
  }

  /**
   * when an actor is moved onto this tile, then is to move off of it, replace it with the tile that
   * this method returns.
   *
   * @return tile that is to replace this tile after an actor going over it. If not implemented,
   * defaults to Floor.
   */
  default Tile replaceWith() {
    return TileType.makeTile(TileType.Floor, new TileInfo(info().loc(), info().ping(), "", ""));
  } 

  /**
   * Sets the given tile t instead of this tile on maze, changing the domain to do so. NOTE1: may
   * alter domain. NOTE2: does not check wether it's possible for tile t to move on this
   * tile(thoroughly)! Although often it has a set of exceptions that will be thrown if a completely
   * unexpected input is given.
   * <p>
   * if not implemented defaults to replacing this tile with given tile on maze.
   *
   * @param t tile to replace this tile with, can not be null.
   * @param d domain where this change is happening, can not be null.
   * @throws NullPointerException if t or d are null (may also throw other types of runtime
   *                              exceptions for unexpected inputs, in overriden classes)
   */
  default void setOn(Tile t, Domain d) {
    if (t == null || d == null) {
      throw new NullPointerException("tile or domain can not be null");
    }
    d.getCurrentMaze().setTileAt(info().loc(), t);
  }

  //PING

  /**
   * Calculates the next state of the tile in the domain(maze/inventory). Based on the tile and
   * domain state, this method may alter the state of the tile and given domain object. NOTE: may
   * alter domain.
   *
   * @param d domain object, (maze/inventory), where this change is happening.
   */
  default void ping(Domain d) {
  }
}
