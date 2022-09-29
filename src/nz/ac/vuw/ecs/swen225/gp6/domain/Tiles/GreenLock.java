package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class GreenLock extends AbstractTile {

    public GreenLock (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.GreenLock;}
    @Override public char symbol(){return 'G';}
    @Override public boolean isObstruction(Tile t, Domain d){ 
        return !(t.type() == TileType.Hero && d.getInv().hasItem(TileType.GreenKey));
    }
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().removeItem(TileType.GreenKey);//TODO future proofness
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
