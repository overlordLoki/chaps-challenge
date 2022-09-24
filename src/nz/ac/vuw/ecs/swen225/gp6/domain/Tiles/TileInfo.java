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
    private int ping; //to be used later perhaps to keep count of ping cycles

    public TileInfo(Loc loc, Consumer<Domain> consumer){
        this.loc = loc;
        this.consumer = consumer;
    }

    //GETTERS:
    /*
     * returns consumer given for some tiles, if null returns a -> {}
     */
    public Consumer<Domain> consumer(){return consumer == null? a ->{}: consumer;}
    /*
     * returns location of the associated tile
     */
    public Loc loc(){return loc;}
    /*
     * returns static direction of the associated tile (mainly applicable to hero/enemies)
     */
    public Direction dir(){return staticDirection;}
    /*
     * returns ping of the associated tile 
     */
    public int ping(){return ping;}

    //SETTERS
    /*
     * sets location of the associated tile
     */
    public void loc(Loc loc){this.loc = loc;}
    /*
     * sets the static direction of the associated tile (mainly applicable to hero/enemies)
     */
    public void dir(Direction d){this.staticDirection = d;}
    /*
     * increments the ping of the associated tile
     */
    public void pingStep(){ping++;}
}