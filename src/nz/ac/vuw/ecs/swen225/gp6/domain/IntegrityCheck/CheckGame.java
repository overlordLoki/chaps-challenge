package nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.GameState;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Coin;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.ExitDoorOpen;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Hero;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Null;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;


/**
 * This class contains a set of static method to check the integrity of the game: - before a ping
 * step is successfully completed (by comparing the previous maze and inv to next ones) - after a
 * ping (by looking at the new altered domain, and making sure certain rules are always followed)
 * <p>
 * this class should be UPDATED REGULARLY to keep up with any new futures the game will have.
 */
public final class CheckGame {

  /**
   * Checks the integrity of the game after a ping, and the game state is transitioning a step
   * forward. (so the domain have correctly transitioned from before ping state to after ping
   * state)
   *
   * @param preDomain  the pre-ping domain that will be changed by one step
   * @param afterDomain the post-ping domain that will be compared with the pre-ping one
   *
   * @throws IllegalStateException in its check methods, to inform if the game is in an invalid state,
   * it must also include string telling which rule has been broken
   */
  public static void checkStateChange(Domain preDomain, Domain afterDomain) {
    Maze preMaze = preDomain.getCurrentMaze();
    Inventory preInv = preDomain.getInv();
    Maze afterMaze = afterDomain.getCurrentMaze();
    Inventory afterInv = afterDomain.getInv();

    //if the game is won, lost or in between levels, behave appropriately
    if (afterDomain.getGameState() == GameState.WON
        || afterDomain.getGameState() == GameState.LOST) {
      return;
    }
    if (afterDomain.getGameState() == GameState.BETWEENLEVELS) {
      afterDomain.setGameState(GameState.PLAYING);
      return;
    }

    //HERO:
    checkHeroStateChange(preMaze, preInv, afterMaze, afterInv, preDomain);

    //Perhaps extend in future by having a way to identify all moving things and collectively check them

    //COINS:
    //check there is the same amounts of coins in the maze and inventory combined before and after
    int numCoinsBefore = getAllTiles(preMaze, TileType.Coin).size() + preInv.coins();
    int numCoinsAfter = getAllTiles(afterMaze, TileType.Coin).size() + afterInv.coins();
    if (numCoinsBefore != numCoinsAfter) {
      throw new IllegalStateException("There were a total of " + numCoinsBefore
          + " coins in game before, but now there are " + numCoinsAfter);
    }

  }

  /**
   * Checks the integrity of the maze and inventory of a given game.
   *
   * @param domain the game that the maze and inventory will be accessed of
   * 
   * @throws IllegalStateException in its check methods, to inform if the game is in an invalid state,
   * it must also include string telling which rule has been broken
   */
  public static void checkCurrentState(Domain domain) {
    Maze maze = domain.getCurrentMaze();
    Inventory inv = domain.getInv();

    //if the game is won, lost or in between levels, behave appropriately
    if (domain.getGameState() == GameState.WON) {
      checkWin(domain);
      return;
    }
    if (domain.getGameState() == GameState.LOST) {
      checkLose(domain);
      return;
    }
    if (domain.getGameState() == GameState.BETWEENLEVELS) {
      domain.setGameState(GameState.PLAYING);
      return;
    }

    //HERO:
    //check if hero is on maze, and there is exactly one hero on the maze
    if (!(getTile(maze, TileType.Hero) instanceof Hero)) {
      throw new IllegalStateException("Hero is not on maze");
    }

    //COIN:
    //check that there is at least 1 coin in total in game
    if (getAllTiles(maze, TileType.Coin).size() + inv.coins() < 1) {
      throw new IllegalStateException("There are no(or negative) coins in game");
    }

    //check door is closed if coins left on maze, open otherwise
    Tile closedDoor = getTile(maze, TileType.ExitDoor);
    Tile openDoor = getTile(maze, TileType.ExitDoorOpen);
    if (getAllTiles(maze, TileType.Coin).size() == 0) {
      if (!(closedDoor instanceof Null) || openDoor instanceof Null) {
        throw new IllegalStateException(
            "The exit door is not open, but there are no coins on maze.");
      }
    } else {
      if (closedDoor instanceof Null || !(openDoor instanceof Null)) {
        throw new IllegalStateException("The exit door is open, but there are coins left on maze.");
      }
    }

  }


  /**
   * helper method called by checkStateChange() method, checks the integrity of hero as the state of
   * game changes
   *
   * @param preMaze   pre ping maze
   * @param preInv    pre ping inventory
   * @param afterMaze post ping maze
   * @param afterInv  post ping inventory
   * @param preDomain pre ping domain
   * 
   * @throws IllegalStateException in its check methods, to inform if the game is in an invalid state,
   * it must also include string telling which rule has been broken
   */
  private static void checkHeroStateChange(Maze preMaze, Inventory preInv, Maze afterMaze,
      Inventory afterInv,
      Domain preDomain) {

    Hero h = (Hero) getTile(afterMaze, TileType.Hero);
    //check hero isn't out of bounds(not on a periphery tile or its memory of location is in bound)
    if (!Loc.checkInBound(h.info().loc(), afterMaze) ||
        h.tileOn().type() == TileType.Periphery) {
      throw new IllegalStateException("Hero has moved out of bounds");
    }

    Loc heroNewLoc = h.info().loc();
    Tile tileToOccupy = preMaze.getTileAt(heroNewLoc);
      if (tileToOccupy instanceof Hero) {
          return; //if hero hasn't moved then no need to check
      }

    //check if hero is moved on an obstruction
    if (tileToOccupy.obstructsHero(preDomain)) {
      throw new IllegalStateException("Hero has moved on an obstruction: "
          + tileToOccupy.type().name());
    }

    //check onTile field is replaced with tile's replaceWith method that hero is on
    if (tileToOccupy.replaceWith().type() != h.tileOn().type()) {
      throw new IllegalStateException("Hero has the wrong onTile field");
    }

    //check if hero in moved on a door that needs a key, correct key is in and removed from inventory
    if (tileToOccupy instanceof Door && ((Door) tileToOccupy).color() != KeyColor.NONE) {
      KeyColor doorColor = ((Door) tileToOccupy).color();
      List<Tile> items = preInv.getItems();
      Predicate<Tile> p = i -> i instanceof Key && ((Key) i).color() == doorColor;

      if (items.stream().noneMatch(p)) {
        throw new IllegalStateException(
            "Hero has moved on a door without having the correctly colored key");
      }

      if (preInv.countItem(p) != afterInv.countItem(p) + 1) {
        throw new IllegalStateException("The correct key has not been removed from the inventory");
      }
    }
    //check if hero moved on an item(not a coin), it has been added to the inventory(if inventory is not full)
    if (tileToOccupy instanceof Item && !(tileToOccupy instanceof Coin)
        && !preInv.isFull()) {
      if (preInv.countItem(i -> i.type() == tileToOccupy.type()) !=
          afterInv.countItem(i -> i.type() == tileToOccupy.type()) - 1) {
        throw new IllegalStateException("The item has not been added to the inventory");
      }
    }

    //check if hero moved on a coin, coin is added to inventory
    if (tileToOccupy instanceof Coin && preInv.coins() != afterInv.coins() - 1) {
      throw new IllegalStateException("The coin has not been added to the inventory");
    }

    //check previous tile is not item(picakble) tile, i.e check item disappears (if inventory is not full)
    Tile tileToLeave = afterMaze.getTileAt(
        getTile(preMaze, TileType.Hero).info().loc()); //tile left behind
    if (tileToLeave instanceof Item
        && !preInv.isFull()) {
      throw new IllegalStateException("Hero has left an item behind");
    }

    //check again for coin, since its not dependent on inventory being full
    if (tileToLeave instanceof Coin) {
      throw new IllegalStateException("Hero has left a coin behind");
    }


  }

  /**
   * helper method called by checkCurrentState() method, checks the integrity of the game if game is
   * claimed to be lost.
   *
   * @param domain domain to check on
   * 
   * @throws IllegalStateException in its check methods, to inform if the game is in an invalid state,
   * it must also include string telling which rule has been broken
   */
  private static void checkLose(Domain domain) {
    Maze maze = domain.getCurrentMaze();

    //check if one of the conditions that make the player lose is true
    //NOTE: must be extended if these conditions are extended
    //current conditions:
    //-1: hero moved on a damaging tile
    //-2: damaging tile moved on a hero
    //-3.. to be added later

    Tile h = getTile(maze, TileType.Hero); //will return Null tile if hero not maze
    boolean heroOnDangerTile = h instanceof Hero && ((Hero) h).tileOn().damagesHero(domain);
    boolean dangerTileOnHero = false;
    List<Tile> damagingTiles = maze.getAllTilesThat(t -> t.damagesHero(domain));
    for (Tile tile : damagingTiles) {
      //use reflection to find out the damaging tiles on maze, have a tileOn method
      try {
        Method m = tile.getClass().getMethod("tileOn");
        m.setAccessible(true);
        Tile tileOn = (Tile) m.invoke(tile);
        if (tileOn instanceof Hero) {
          dangerTileOnHero = true;
          break;
        }
      } catch (NoSuchMethodException | SecurityException | IllegalAccessException
               | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    // if a tile is on hero, then hero is not on maze, so need to check h is not an instance of hero
    dangerTileOnHero = dangerTileOnHero && !(h instanceof Hero);

    if (!heroOnDangerTile && !dangerTileOnHero) {
      throw new IllegalStateException("Hero has not moved on a damaging tile, or a damaging"
          + " tile has not moved on hero, but game is claimed to be lost");
    }


  }

  /**
   * helper method called by checkCurrentState() method, checks the integrity of the maze if game is
   * claimed to be won.
   *
   * @param domain domain to check on
   * 
   * @throws IllegalStateException in its check methods, to inform if the game is in an invalid state,
   * it must also include string telling which rule has been broken
   */
  private static void checkWin(Domain domain) {
    Maze maze = domain.getCurrentMaze();

    //check all conditions that player need to win is true
    //current conditions are:
    //-1: hero is on openExitDoor
    //-2: no coins are on maze(all in inventory)
    if (domain.getTreasuresLeft() != 0) {
      throw new IllegalStateException(
          "Not all coins are in inventory, but game is claimed to be won");
    }

    if (!((ExitDoorOpen) getTile(maze, TileType.ExitDoorOpen)).heroOn()) {
      throw new IllegalStateException(
          "Hero is not on open exit door, but game is claimed to be won");
    }
  }

  //UTILITY HELPERS:

  /**
   * returns the first tile with given type in the maze.
   *
   * @param maze maze to check for tile
   * @param type type of tile to find
   */
  public static Tile getTile(Maze maze, TileType type) {
    return maze.getTileThat(t -> t.type() == type);
  }

  /**
   * returns the list of all tiles with given type in the maze.
   *
   * @param maze maze to check for tiles
   * @param type type of tile to find
   */
  public static List<Tile> getAllTiles(Maze maze, TileType type) {
    return maze.getAllTilesThat(t -> t.type() == type);
  }
}
