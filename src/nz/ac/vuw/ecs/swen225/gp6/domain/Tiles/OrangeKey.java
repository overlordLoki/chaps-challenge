package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;

/**
 * A class representing orange key which when picked up by hero, can be used to unlock orange locks.
 */
public class OrangeKey extends Key{
    /**
     * Create a orange key
     * @param info tile information
     */
    public OrangeKey (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.OrangeKey;}
    @Override public KeyColor color(){return KeyColor.ORANGE;}
}
