package nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping;

import java.util.function.Consumer;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/*
 * an interface for information tiles need to recieve to be initiated, 
 * to avoid adding more and more parameters to each tile class.
 */
public class TileInfo{
    Consumer<Domain> consumer;
    Loc loc;

    public TileInfo(Loc loc, Consumer<Domain> consumer){
        this.loc = loc;
        this.consumer = consumer;
    }

    public Consumer<Domain> consumer(){return consumer;}
    public Loc loc(){return loc;}
}