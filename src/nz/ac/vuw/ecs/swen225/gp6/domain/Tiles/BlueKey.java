package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;

public class BlueKey extends Key{
    
    public BlueKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.BlueKey;}
    @Override public char symbol(){return 'b';}
    @Override public KeyColor color(){return KeyColor.BLUE;}

}
