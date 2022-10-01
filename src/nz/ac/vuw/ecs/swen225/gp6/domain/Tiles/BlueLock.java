package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

public class BlueLock extends Door{

    public BlueLock (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.BlueLock;}
    @Override public KeyColor color(){return KeyColor.BLUE;}
}
