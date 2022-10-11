package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;

/**
 * A class representing a green lock that can be unlocked by a green key.
 */
public class GreenLock extends Door {
    /**
     * Create a green lock
     * @param info tile information
     */
    public GreenLock (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.GreenLock;}
    @Override public KeyColor color(){return KeyColor.GREEN;}

}
