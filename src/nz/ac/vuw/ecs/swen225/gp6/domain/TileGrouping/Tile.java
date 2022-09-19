package nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

/*
 * This is the tile interface all tiles must extend. 
 * This file also includes other tile interfaces/abstract classes that aim to group certain tiles.
 */
public interface Tile {
    //INFO METHODS: (do not change maze and tile)
    /**
     * returns the enum name of this tile
     */
    public TileType getType();

    /**
     * Each tile type should have a symbol, this is for testing purposes
     */
    public char getSymbol();

    /*
     * Each tile should have a info object, which stores all "field-like" info that the tile may have.
     * Eg location of tile, consumer that may overwrite ping method, etc.
     */
    public TileInfo getInfo();

    /**
     * Checks wether it's possible for an actor, in a given maze, to move on a tile.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean canMoveOn(Actor a, Domain d);

    //ACTION METHODS: (may change maze and tile)
    /**
     * Calculates the next state of the tile in the maze/inventory.
     * Based on the tile and maze state, this method may alter the state of the tile and given maze object.
     * Does nothing unless implemented.
     */
    public default void ping(Domain d){};

    /**
     * Sets this tile on maze to the given actor, 
     * NOTE: does not check wether it's possible for the actor to move on it!
     * Does nothing unless implemented.
     */
    public default void setOn(Actor a, Domain d){}
    
}







