package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

public class Null extends AbstractTile{

    public Null (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Null;}
    @Override public char symbol(){return Character.MIN_VALUE;}
    @Override public boolean isObstruction(Tile t, Domain d) { return true;}
}
