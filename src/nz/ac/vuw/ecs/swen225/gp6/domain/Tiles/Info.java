package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

/**
 * This class represents a info tile which when only hero can move on, and will display hints 
 * for the current level. Each level currently atmost can have one of these tiles.
 */
public class Info extends AbstractTile{//future idea: not disappear after once usage
    /**
     * Create a info tile.
     * @param info tile information
     */
    public Info (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Info;}

    @Override public boolean obstructsEnemy(Domain d){return true;}
    @Override public Tile replaceWith(){return this;} //the info tile is permanent
    
    @Override public void ping(Domain d){}//TODO: display info if heros on it
}
