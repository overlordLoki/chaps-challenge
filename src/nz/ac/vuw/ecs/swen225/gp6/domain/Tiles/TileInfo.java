package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import java.util.function.Consumer;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/*
 * a class for information tiles need to recieve to be initiated, 
 * to avoid adding more and more parameters to each tile class, 
 * and being able to pass larger chunks of information with one parameter.
 */
public class TileInfo{
    private Direction staticDirection = Direction.None; //mainly applicable to hero/enemies
    private Consumer<Domain> consumer; //mainly applicable to enemies currently
    private Loc loc;

    public TileInfo(Loc loc, Consumer<Domain> consumer){
        this.loc = loc;
        this.consumer = consumer;
    }

    public Consumer<Domain> consumer(){return consumer;}
    public Loc loc(){return loc;}
    public Direction dir(){return staticDirection;}

    public void dir(Direction d){this.staticDirection = d;}
}