package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public abstract class Key extends Item{

    /*
     * enum for matching doors and keys without 
     * checking for types
     */
    public enum KeyColor{
        RED, BLUE, GREEN, YELLOW, NONE, ORANGE
    }
    
    public Key(TileInfo info) {super(info);}

    /*
     * return the colour of the key
     */
    public abstract KeyColor color();
}
