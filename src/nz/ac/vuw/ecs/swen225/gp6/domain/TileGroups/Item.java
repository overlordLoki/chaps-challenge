package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public abstract class Item extends AbstractTile {
    
    public Item(TileInfo info) {super(info);}
    
    @Override public boolean obstructsEnemy(Domain d){return true;}
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().addItem(this); //put this item in the inventory(doesn't remove from maze)
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
