package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

/**
 * This class is only made to aid the renderer with drawing out of bound tiles,
 * no hero, enemy or any other tile should be able to move on this tile.  
 */
public class Periphery extends AbstractTile{
    /**
     * Create the periphery tile.
     * @param info tile information
     */
    public Periphery(TileInfo info) {super(info);}

    @Override public TileType type() {return TileType.Periphery;}

    @Override public Tile replaceWith(){return this;} //this should be rarely ever used
                                                      //only to check a hero hasn't moved on a periphery tile
}
