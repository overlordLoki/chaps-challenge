package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

/**
 * Everything that the player can pick up, including all kinds of key, all kinds of treasure and tool, 
 * must extend this class.
 */
public abstract class Item extends AbstractTile {
    boolean invWasFull = false; //returns true if this item was attempted to be added to a full inventory
    
    /**
     * constructor for an item
     * 
     * @param info instance of tileInfo for an item
     */
    public Item(TileInfo info) {super(info);}
    
    @Override public boolean obstructsEnemy(Domain d){return true;}
    @Override public Tile replaceWith(){return invWasFull? this: super.replaceWith();} //if inv is full leave the item where it is
    @Override public void setOn(Tile t, Domain d){ 
        if(t.type() != TileType.Hero) throw new IllegalArgumentException("only hero can move on items");
        if(d == null) throw new NullPointerException("domain cannot be null (Item.setOn)");

        int oldInvSize = d.getInventory().size();
        invWasFull = !d.getInv().addItem(this); //try to put this item in the inventory if not full
        d.getCurrentMaze().setTileAt(info.loc(), t);//replace the item on maze

        if(invWasFull == false){assert oldInvSize == d.getInventory().size() - 1: 
        "Inventory size should have increased by 1 after adding item.";}
    }
}
