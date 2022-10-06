package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;

public class Coin extends Item{

    public Coin (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Coin;}
    
    @Override public void setOn(Tile t, Domain d){ 
        //add coin to inventory
        d.getInventory().addCoin();
        d.getCurrentMaze().setTileAt(info.loc(), t);

        //if all treasures collected replace exitdoor with open exit door
        if(d.getTreasuresLeft() == 0){
            //find exit door and replace it with open exit door
            Tile exitDoor = d.getCurrentMaze().getTileThat(tile -> tile.type() == TileType.ExitDoor);
            d.getCurrentMaze().setTileAt(exitDoor.info().loc(), TileType.ExitDoorOpen); 
        }
    }
}
