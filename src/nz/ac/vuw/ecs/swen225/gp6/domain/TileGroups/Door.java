package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.AbstractTile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * An abstract class that all door tiles must inherit.
 */
public abstract class Door extends AbstractTile {

  /**
   * constructor for a Door.
   *
   * @param info instance of tileInfo for a Door
   */
  public Door(TileInfo info) {
    super(info);
  }

  /**
   * gets the colour of the key of the door (none if not a coloured door, e.g exit door)
   *
   * @return the colour of the key of the door (or none)
   */
  public abstract KeyColor color();

  @Override
  public boolean obstructsEnemy(Domain d) {
    return true;
  } // doors obstruct enemies

  @Override
  public boolean obstructsHero(Domain d) {
    if (d == null) {
      throw new NullPointerException("domain cannot be null (Door.obstructsHero)");
    }

    return !d.getInv().getItems().stream()
        .anyMatch(t -> t instanceof Key && ((Key) t).color() == color());
  } // if hero has key with correct color, then it does not obstruct

  @Override
  public void setOn(Tile t, Domain d) {
    //also throws null pointer exception if t is null, you will see this pattern of checking in few overriden setOn methods
    if (t.type() != TileType.Hero) {
      throw new IllegalArgumentException("only hero can move on door");
    }
    if (d == null) {
      throw new NullPointerException("domain cannot be null (Door.setOn)");
    }

    Inventory inv = d.getInv();
    int prevSize = inv.getItems().size();//to check later if size of inventory changed

    //if item in inventory is a key with the correct color, remove it
    inv.getItems().stream().filter(tile -> tile instanceof Key && ((Key) tile).color() == color())
        .findFirst().ifPresent(k -> {
          inv.removeItem(k.type());
          d.getCurrentMaze().setTileAt(info.loc(), t);
        });

    assert
        inv.getItems().size() == prevSize - 1 : "inventory has not decreased after removing a key";

    d.getCurrentMaze().setTileAt(info.loc(), t);
  }
}
