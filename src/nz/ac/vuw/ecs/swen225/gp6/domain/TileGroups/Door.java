package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * An abstract class that all door tiles must inherit.
 */
public abstract class Door extends AbstractTile {
    
    /**
     * constructor for a Door
     * 
     * @param info instance of tileInfo for a Door
     */
    public Door(TileInfo info) {super(info);}
    
    /**
     * gets the colour of the key of the door (none if not a coloured door, e.g exit door)
     * 
     * @return the colour of the key of the door (or none)
     */
    public abstract KeyColor color();

    @Override public boolean obstructsEnemy(Domain d){return true;} // doors obstruct enemies
    @Override public boolean obstructsHero(Domain d){
        return d.getInv().getItems().stream()
        .anyMatch(t -> t instanceof Key && ((Key) t).color() == color()) == false;
    } // if hero has key with correct color, then it does not obstruct

    @Override public void setOn(Tile t, Domain d){ 
        Inventory inv = d.getInv();

        inv.getItems().stream().filter(tile -> tile instanceof Key && ((Key) tile).color() == color())
        .findFirst().ifPresent(k -> {
            inv.removeItem(k.type());
            d.getCurrentMaze().setTileAt(info.loc(), t);
        });//if item in inventory is a key with the correct color, remove it 
           //note that this method will move the hero to the tile re
        
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
