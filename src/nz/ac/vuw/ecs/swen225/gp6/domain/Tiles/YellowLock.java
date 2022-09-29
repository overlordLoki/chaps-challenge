package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class YellowLock extends AbstractTile{

    public YellowLock(TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.YellowLock;}
    @Override public char symbol(){return 'Y';}
    @Override public boolean isObstruction(Tile t, Domain d){ 
        return !(t.type() == TileType.Hero && d.getInv().hasItem(TileType.YellowKey));
    }
    
    @Override public void setOn(Tile t, Domain d){ 
        d.getInv().removeItem(TileType.YellowKey);
        d.getCurrentMaze().setTileAt(info.loc(), t);
    }
}
