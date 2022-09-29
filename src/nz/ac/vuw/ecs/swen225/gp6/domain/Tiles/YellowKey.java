package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;

public class YellowKey extends Item{

    public YellowKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.YellowKey;}
    @Override public char symbol(){return 'y';}
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().addItem(this);
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
