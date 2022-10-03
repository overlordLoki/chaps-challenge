package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class Info extends AbstractTile{//future idea: not disappear after once usage
    
    public Info (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Info;}

    @Override public Tile replaceWith(){return this;} //the info tile is permanent
    
    @Override public void ping(Domain d){}//TODO: display info
}
