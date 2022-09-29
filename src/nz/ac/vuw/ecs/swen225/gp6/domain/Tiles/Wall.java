package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class Wall extends AbstractTile{

    public Wall (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Wall;}
    @Override public char symbol(){return '|';}
    @Override public boolean isObstruction(Tile t, Domain d) { return true;} //no one can move on wall
    
}
