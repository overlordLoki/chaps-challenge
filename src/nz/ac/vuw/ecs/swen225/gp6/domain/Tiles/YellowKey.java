package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;

public class YellowKey extends Key{

    public YellowKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.YellowKey;}
    @Override public KeyColor color(){return KeyColor.YELLOW;}
}
