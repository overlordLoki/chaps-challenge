package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;

/**
 * Coin class is a collectible item in the game that the hero must collect to be able to 
 * finish the level. The term coin and treasure are used interchangeably throughout the code.
 */
public class Coin extends Item{

    /**
     * Create a coin
     * @param info tile information
     */
    public Coin (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Coin;}
    
    @Override public void setOn(Tile t, Domain d){ 
        if(d == null || t == null) throw new NullPointerException("Domain/Tile can not be null (Coin.setOn)");
        if(t.type() != TileType.Hero) throw new IllegalArgumentException("only hero can move on coin");
        
        //add coin to inventory
        int preNumCoins = d.getInv().coins();
        d.getInv().addCoin();
        d.getCurrentMaze().setTileAt(info.loc(), t);

        assert preNumCoins == d.getInv().coins() - 1: 
               "the number of coins inside the inventory should be increased by 1 after picking a coin.";

        //if all treasures collected replace exitdoor with open exit door
        if(d.getTreasuresLeft() == 0){
            //find exit door and replace it with open exit door
            Tile exitDoor = d.getCurrentMaze().getTileThat(tile -> tile.type() == TileType.ExitDoor);
            d.getCurrentMaze().setTileAt(exitDoor.info().loc(), TileType.ExitDoorOpen); 
        }
    }
}
