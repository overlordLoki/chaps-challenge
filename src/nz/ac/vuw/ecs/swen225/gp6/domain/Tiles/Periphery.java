package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class Periphery extends AbstractTile{

    
    public Periphery(TileInfo info) {super(info);}

    @Override public TileType type() {return TileType.Periphery;}
    
}
