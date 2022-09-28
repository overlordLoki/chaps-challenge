package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

/*
 * Having a tileTypeInterface separately allows new Tile Types to be added 
 * through dynamic dispatching (and polymorphism) in run time and without having to change 
 * the TileType enum.
 */
public interface TileTypeInterface {
    /**
     * returns character symbol of tiletype associated with enum.
     */
    public char getSymbol();
    /**
     * Checks wether the associated tile to this type is an obstruction for another given tile t, 
     * in a given domain.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public default boolean isObstruction(Tile t, Domain d){return t.type() != TileType.Hero;} //only hero can move on 
    /**
     * Sets the given tile t instead of the associated tile to this type on maze, changing the domain to do so.
     * NOTE: should not check wether it's possible for tile t to move on this tile!
     */
    public default void setOn(Tile self, Tile t, Domain d){}
    /**
     * Calculates the next state of the tile in the domain(maze/inventory).
     * Based on the tile and domain state, this method may alter the state of the tile and given domain object.
     */
    public default void ping(Tile self, Domain d){}
}