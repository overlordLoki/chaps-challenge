package nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

/**
 * an actor abstract class that all moving creatures must inherit, including hero.
 */
public abstract class Actor extends AbstractTile{

    /**
     * constructor for an actor
     * 
     * @param info instance of tileInfo for an actor
     */
    public Actor(TileInfo info) {super(info);}
}
