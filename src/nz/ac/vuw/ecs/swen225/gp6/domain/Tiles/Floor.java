package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.*;

public class Floor extends AbstractTile{

    public Floor (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Floor;}
    @Override public char symbol(){return '_';}
    @Override public boolean isObstruction(Tile t, Domain d) { return false;} //anyone can move on floor
    
    @Override public void setOn(Tile t, Domain d){d.getCurrentMaze().setTileAt(info.loc(), t);}
}
