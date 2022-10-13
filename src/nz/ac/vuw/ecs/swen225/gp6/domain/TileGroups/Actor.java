package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Floor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/**
 * an actor abstract class that all moving creatures must inherit, including hero.
 */
public abstract class Actor extends AbstractTile{

    /**
     * constructor for an actor,
     * set initial facing direction to Up
     * and initial tileOn to floor tile 
     * 
     * @param info instance of tileInfo for an actor
     */
    public Actor(TileInfo info) {
        super(info);

        //check wether its first time this actor is created (since it constantly copies at every ping)
        //and accodingly assign values
        Direction dir = info.facing() == Direction.None? Direction.Up:info.facing();
        Tile tileOn = info.tileOn() == null?new Floor(new TileInfo(info.loc())): info.tileOn();

        info.facing(dir);
        info.tileOn(tileOn);
    }

    /**
     * this gets the tile that the actor will replace when moved
     * 
     * @return tile under actor
     */
    public Tile tileOn(){return info.tileOn();}//returns the tile the actor will replace when moved
}
