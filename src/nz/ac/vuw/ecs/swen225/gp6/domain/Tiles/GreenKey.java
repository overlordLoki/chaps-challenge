package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;

public class GreenKey extends Key{

    public GreenKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.GreenKey;}
    @Override public char symbol(){return 'g';}
    @Override public KeyColor color(){return KeyColor.GREEN;}
    
}
