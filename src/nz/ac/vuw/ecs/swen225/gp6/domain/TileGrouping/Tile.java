package nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/*
 * This is the tile interface all tiles must extend. 
 * This file also includes other tile interfaces/abstract classes that aim to group certain tiles.
 */
public class Tile {
    
    private TileType state;
    private TileInfo info;

    public Tile(TileType state, TileInfo info){
        this.state = state;
        this.info = info;
    }

    //TILE METHODS
    /* 
     * Each tile object will hold a reference to one TileState enum, 
     * which determines the behaviour of a number of the tiles important methods.
     * 
     * this returns the enum.
     */
    public TileType type(){return state;}
    /*
     * Each tile  will hold an info object, which stores all "field-like" info that the tile may have.
     * Eg location of tile, consumer that may overwrite ping method, etc.
     * 
     * this returns that object.
     */
    public TileInfo info(){return info;}

    //STATE DEPENDANT
    /**
     * Each tile type should have a symbol, this is for testing purposes
     */
    public char getSymbol(){return state.getSymbol();}
    /**
     * Checks wether this tile is an obstruction for an actor, in a given maze,
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean isObstruction(Actor a, Domain d){return state.isObstruction(a, d);}
    /**
     * Sets this tile on maze to the given actor, 
     * NOTE: does not check wether it's possible for the actor to move on it!
     */
    public void setOn(Actor a, Domain d){ state.setOn(this, a, d);}
    /**
     * Calculates the next state of the tile in the maze/inventory.
     * Based on the tile and maze state, this method may alter the state of the tile and given maze object.
     */
    public void ping(Domain d){state.ping(this, d);}
}

//TILE GROUPS:(classes that extend tile to help with grouping of tiles)
class Actor extends Tile{
    protected Direction staticDirection = Direction.None;
    public Actor(TileType state, TileInfo info) {super(state, info);}
}