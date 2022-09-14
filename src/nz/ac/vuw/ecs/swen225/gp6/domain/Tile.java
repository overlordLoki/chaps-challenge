package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.lang.reflect.Field;
import java.util.Arrays;

public interface Tile {
    /**
     * returns the enum name of this tile
     */
    public TileType getName();

    /**
     * Checks wether its possible for an actor, in a given maze, to move on a tile.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean canMoveOn(Actor a, Maze m);

    /*
     * Calculates the next state of the tile in the maze/inventory.
     * Based on the tile and maze state, this method may alter the state of the tile and given maze object.
     */
    public default void ping(Maze m){};
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


//actor groups
abstract class Actor implements Tile{
    Direction staticDirection = Direction.None;
    Direction movementDirection = Direction.None;
}

//item groups
abstract class Item implements Tile{
    public boolean inInv = false;
    public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileType.Hero;} //hero can move on any item
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
     * Takes a tile, if it is an actor, sets the actorOn this tile to that tile.
     * 
     * returns true if the actorOn is reset (i.e tile is instance of actor), else false
     */
    public boolean setOn(Tile tile) {
        boolean[] setSuccessful = new boolean[1];
        setSuccessful[0] = false;

        Arrays
        .stream(EmptyTile.class.getFields())
        .map(field -> field.getType().getClass())
        .forEach(type -> {if(type.isAssignableFrom(tile.getClass())){
            try{
                Field f = EmptyTile.class.getField(type.getName().toLowerCase() + "On");
                f.setAccessible(true);
                f.set(f.get(this), tile);
            } catch(Exception e){
                System.out.println("No field with this name in empty tile: " + type.getName().toLowerCase() + "On");
            }
    
        }})
        .findFirst();

        

    }

    /*
     * Takes a tile, if it is an item, sets the itemOn this tile to that tile.
     * 
     * returns true if the itemOn is reset (i.e tile is instance of item), else false
     */
    public boolean setItemOn(Tile itemOn) {
        if(itemOn instanceof Item) {
            this.itemOn = (Item)itemOn;
            return true;
        }
        return false;
    }

    public Actor getActorOn() {return actorOn;}
    public Item getItemOn() {return itemOn;}

}


