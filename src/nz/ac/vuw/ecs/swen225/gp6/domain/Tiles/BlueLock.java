package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class BlueLock extends AbstractTile{

    public BlueLock (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.BlueLock;}
    @Override public char symbol(){return 'B';}
    @Override public boolean isObstruction(Tile t, Domain d){ 
        return !(t.type() == TileType.Hero && d.getInv().hasItem(TileType.BlueKey));
    }
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().removeItem(TileType.BlueKey);
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
