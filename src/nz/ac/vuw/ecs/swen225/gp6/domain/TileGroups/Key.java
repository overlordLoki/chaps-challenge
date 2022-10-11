package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

/**
 * This is an abstract class for all kinds of keys, and each key must have a KeyColor enum associated with it.
 */
public abstract class Key extends Item{

    /**
     * enum for matching doors and keys, 
     * to avoid for checking for types.
     */
    public enum KeyColor{
        RED, BLUE, GREEN, YELLOW, NONE, ORANGE
    }
    
    /**
     * constructor for a key
     * 
     * @param info instance of tileInfo for a key
     */
    public Key(TileInfo info) {super(info);}

    /**
     * return the colour of the key
     * 
     * @return the KeyColor of the key
     */
    public abstract KeyColor color();
}
