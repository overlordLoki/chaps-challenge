package Domain;

import java.util.List;

public interface Tile {
    public TileName getName();
    public boolean canMoveOn(Actor a, Maze m);
}


//actor groups
abstract class Actor implements Tile{
}

//item groups
interface Item extends Tile{

}
    
abstract class Key implements Item{
    public boolean canMoveOn(Actor a, Maze m) { return a.getName() == TileName.Hero;} //hero can move on key
}

//terrain groups
abstract class Lock implements Tile{
    public boolean canMoveOn(Actor a, Maze m) { }
}


