package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class Floor extends AbstractTile{

    public Floor (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Floor;}
    @Override public char symbol(){return '_';}
}
