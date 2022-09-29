package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;

public class GreenKey extends Item{

    public GreenKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.GreenKey;}
    @Override public char symbol(){return 'g';}
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().addItem(this);
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
