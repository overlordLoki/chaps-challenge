package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

public class OrangeLock extends AbstractTile{

    public OrangeLock (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.OrangeLock;}
    @Override public char symbol(){return 'O';}
    @Override public boolean isObstruction(Tile t, Domain d){ 
        return !(t.type() == TileType.Hero && d.getInv().hasItem(TileType.OrangeKey));
    }
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().removeItem(TileType.OrangeKey);
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
