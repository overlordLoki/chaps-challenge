package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface Tile {
    /**
     * returns the enum name of this tile
     */
    public TileType getType();

    /**
     * Checks wether its possible for an actor, in a given maze, to move on a tile.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean canMoveOn(Actor a, Maze m);

    /*
     * Calculates the next state of the tile in the maze/inventory.
     * Based on the tile and maze state, this method may alter the state of the tile and given maze object.
     * Does nothing unless implemented.
     */
    public default void ping(Maze m){};

    /**
     * If this tile can hold other tiles, and if given one of the tile types it can hold, it 
     * will store it in its relevant field and return true, otherwise false. 
     * Does nothing and returns false unless implemented
     */
    public default boolean setOn(Tile t){ return false;}
}

/*
 * an interface for information tile needs to recieve to be initiated, 
 * to avoid adding more and more parameters to each tile class.
 */
class TileInfo{
    Loc loc;
    int pingCount = 0;

    TileInfo(Loc loc){
        this.loc = loc;
    }
}

//interface that tiles that need to be placed on other tiles must implement
interface SecondLayerTile extends Tile{}


//actor groups
abstract class Actor implements SecondLayerTile{
    Direction staticDirection = Direction.None;
    Direction movementDirection = Direction.None;
}

//item groups
abstract class Item implements SecondLayerTile{
    public boolean inInv = false;
    public boolean canMoveOn(Actor a, Maze m) { return a.getType() == TileType.Hero;} //hero can move on any item
}
    
abstract class Key extends Item{
    
}

//terrain groups
abstract class Lock implements Tile{

}

abstract class EmptyTile implements Tile{
    private Actor actorOn = null;
    private Item itemOn = null;

    //TODO CHECK IF WORKS
    /*
     * Takes a tile, if it is an instance of an actor, item, or any other tile field empty tile may hold in future,
     *  sets that relevant tile to the given tile.
     * 
     * returns true any tile field is reset (e.g given tile is instance of actor), else false
     */
    public boolean setOn(Tile t) {
        boolean[] setSuccessful = new boolean[1];
        setSuccessful[0] = false;

        Arrays
        .stream(EmptyTile.class.getFields())
        .map(field -> field.getType().getClass())
        .takeWhile(e -> setSuccessful[0] == false)
        .forEach(type -> {if(type.isAssignableFrom(t.getClass())){
            try{
                Field f = EmptyTile.class.getField(type.getName().toLowerCase() + "On");
                f.setAccessible(true);
                f.set(f.get(this), t);
                setSuccessful[0] = true;
            } catch(Exception e){
                System.out.println("No field with this name in empty tile: " + type.getName().toLowerCase() + "On");
            }
    
        }});

        return setSuccessful[0];
    }

    public Actor getActorOn() {return actorOn;}
    public Item getItemOn() {return itemOn;}

}


