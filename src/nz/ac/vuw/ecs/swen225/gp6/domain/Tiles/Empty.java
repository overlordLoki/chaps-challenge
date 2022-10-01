package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;


public class Empty extends AbstractTile{ //empty INVENTORY tile

    public Empty (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Empty;}
    @Override public char symbol(){return ' ';}
}
