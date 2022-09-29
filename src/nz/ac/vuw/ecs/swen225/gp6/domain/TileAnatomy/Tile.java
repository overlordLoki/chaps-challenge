package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

public interface Tile {
    //WONT CHANGE DOMAIN:
    /* 
     * Each tile object will hold a reference to one TileState enum, 
     * which determines the behaviour of a number of the tiles important methods.
     * 
     * this returns the enum.
     */
    public TileType type();
    /*
     * Each tile  will hold an info object, which stores all "field-like" info that the tile may have.
     * Eg location of tile, consumer that may overwrite ping method, etc.
     * 
     * this returns that object.
     */
    public TileInfo info();
    /**
     * Each tile type should have a symbol, this is for testing purposes
     */
    public char symbol();
    /**
     * Checks wether this tile is an obstruction for another given tile t, in a given domain.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public default boolean isObstruction(Tile t, Domain d){return t.type() != TileType.Hero;}

    //MAY CHANGE DOMAIN:
    /**
     * Sets the given tile t instead this tile on maze, changing the domain to do so.
     * NOTE: does not check wether it's possible for tile t to move on this tile!
     */
    public default void setOn(Tile t, Domain d){}
    /**
     * Calculates the next state of the tile in the domain(maze/inventory).
     * Based on the tile and domain state, this method may alter the state of the tile and given domain object.
     */
    public default void ping(Domain d){}
}
