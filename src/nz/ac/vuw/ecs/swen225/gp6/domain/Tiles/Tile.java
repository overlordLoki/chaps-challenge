package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;

/*
 * This is the tile interface all tiles must extend. 
 * This file also includes other tile interfaces/abstract classes that aim to group certain tiles.
 */
public class Tile {
    
    private TileType type; //TODO : chabnge to tileTypeInterface talk to loki and ben about this
    private TileInfo info;

    public Tile(TileType state, TileInfo info){
        this.type = state;
        this.info = info;
    }

    //TILE METHODS:
    /* 
     * Each tile object will hold a reference to one TileState enum, 
     * which determines the behaviour of a number of the tiles important methods.
     * 
     * this returns the enum.
     */
    public TileType type(){return type;}
    /*
     * Each tile  will hold an info object, which stores all "field-like" info that the tile may have.
     * Eg location of tile, consumer that may overwrite ping method, etc.
     * 
     * this returns that object.
     */
    public TileInfo info(){return info;}

    //STATE DEPENDANT METHODS:
    /**
     * Each tile type should have a symbol, this is for testing purposes
     */
    public char getSymbol(){return type.getSymbol();}
    /**
     * Checks wether this tile is an obstruction for another given tile t, in a given domain.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean isObstruction(Tile t, Domain d){return type.isObstruction(t, d);}
    /**
     * Sets the given tile t instead this tile on maze, changing the domain to do so.
     * NOTE: does not check wether it's possible for tile t to move on this tile!
     */
    public void setOn(Tile t, Domain d){ type.setOn(this, t, d);}
    /**
     * Calculates the next state of the tile in the domain(maze/inventory).
     * Based on the tile and domain state, this method may alter the state of the tile and given domain object.
     */
    public void ping(Domain d){type.ping(this, d);}
}

//TILE GROUPS:(classes that extend tile to help with grouping of tiles)
//non currently 



