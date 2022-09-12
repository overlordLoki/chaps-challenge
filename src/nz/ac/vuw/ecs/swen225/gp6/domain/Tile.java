package nz.ac.vuw.ecs.swen225.gp6.domain;

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
    Actor actorOn = null;
    Item itemOn = null;

    public void setActorOn(Actor actorOn) {this.actorOn = actorOn;}
    public void setItemOn(Item itemOn) {this.itemOn = itemOn;}

    public Actor getActorOn() {return actorOn;}
    public Item getItemOn() {return itemOn;}

}


