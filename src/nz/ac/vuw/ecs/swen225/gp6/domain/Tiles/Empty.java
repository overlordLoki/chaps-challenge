package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;


public class Empty extends AbstractTile{ //EMPTY INVENTORY TILE...

    public Empty (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Empty;}
    @Override public char symbol(){return ' ';}
    @Override public boolean isObstruction(Tile t, Domain d) { return false;} //anything can be put on empty tile 
}
