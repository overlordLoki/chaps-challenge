package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class Info extends AbstractTile{//future idea: not disappear after once usage
    
    public Info (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Info;}
    @Override public char symbol(){return 'i';}
    
    @Override public void setOn(Tile t, Domain d){d.getCurrentMaze().setTileAt(info.loc(), t);} 
    @Override public void ping(Domain d){}//TODO: display info
}
