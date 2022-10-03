package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

public interface Tile {
    //INFO ABOUT TILE:
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

    //MOVING ON AND OFF THE TILE:
    /*
     * This method checks wether a given tile can cause damage/kill the hero.
     * this is not a field, since tiles may behave different depending on the state of the game.
     * e.g having armor in inventory
     * 
     * if not implemented, defaults to false
     */
    public default boolean damagesHero(Domain d){return false;};
    /**
     * Checks wether this tile is an obstruction for hero tile ,in a given domain.
     * NOTE: does not alter the tile, maze or hero in anyway.
     * 
     * if not implemented, defaults to false.
     */
    public default boolean obstructsHero( Domain d){return false;}
    /**
     * Checks wether this tile is an obstruction for enemy tile ,in a given domain.
     * NOTE: does not alter the tile, maze or enemy in anyway.
     * 
     * if not implemented, defaults to false.
     */
    public default boolean obstructsEnemy( Domain d){return false;}
    /*
     * when an actor is moved onto this tile, then is to move off of it, 
     * replace it with the tile that this method returns.
     * 
     * if not implemented, defaults to Floor.
     */
    public default Tile replaceWith(){return TileType.makeTile(TileType.Floor, new TileInfo(info().loc(), info().ping()));} 
    /**
     * ALTERS DOMAIN.
     * 
     * Sets the given tile t instead of this tile on maze, changing the domain to do so.
     * NOTE: does not check wether it's possible for tile t to move on this tile!
     * 
     * if not implemented defaults to replacing this tile with given tile on maze.
     */
    public default void setOn(Tile t, Domain d){d.getCurrentMaze().setTileAt(info().loc(), t);}

    //PING
    /**
     * ALTERS DOMAIN.
     * 
     * Calculates the next state of the tile in the domain(maze/inventory).
     * Based on the tile and domain state, this method may alter the state of the tile and given domain object.
     */
    public default void ping(Domain d){}
}
