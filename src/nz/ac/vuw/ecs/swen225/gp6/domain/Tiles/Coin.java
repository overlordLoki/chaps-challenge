package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;

public class Coin extends Item{

    public Coin (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Coin;}
    @Override public char symbol(){return 'C';}
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().addCoin();
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
