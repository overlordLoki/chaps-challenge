package Domain;

import java.util.List;

public interface Tile {
    /**
     * returns the enum name of this tile
     */
    public TileName getName();

    /**
     * Checks wether its possible for an actor, in a given maze, to move on a tile.
     * NOTE: does not alter the tile, maze or actor in anyway.
     */
    public boolean canMoveOn(Actor a, Maze m);
}


//actor groups
abstract class Actor implements Tile{
   Loc l = new Loc(1, 2, new Maze());
}

//item groups
abstract class Item implements Tile{
    public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileName.Hero;} //hero can move on any item
}
    
abstract class Key extends Item{
    
}

//terrain groups
abstract class Lock implements Tile{

}


