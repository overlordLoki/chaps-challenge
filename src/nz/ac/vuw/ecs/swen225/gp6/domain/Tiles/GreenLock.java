package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

public class GreenLock extends Door {

    public GreenLock (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.GreenLock;}
    @Override public KeyColor color(){return KeyColor.GREEN;}

}
