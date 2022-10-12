package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;

/**
 * A class representing yellow key which when picked up by hero, can be used to unlock yellow locks.
 */
public class YellowKey extends Key{
    /**
     * Create a yellow key
     * @param info tile information
     */
    public YellowKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.YellowKey;}
    @Override public KeyColor color(){return KeyColor.YELLOW;}
}
