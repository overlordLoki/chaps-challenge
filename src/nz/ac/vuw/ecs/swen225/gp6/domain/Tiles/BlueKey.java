package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.*;

public class BlueKey extends Item{
    
    public BlueKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.BlueKey;}
    @Override public char symbol(){return 'b';}

    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().addItem(this);
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
