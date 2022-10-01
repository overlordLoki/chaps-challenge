package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

/*
 * This class is the parent class of all tiles in the game.
 * It contains the information of the tile, and the methods that are common to all tiles.
 */
public abstract class AbstractTile implements Tile{
    protected TileInfo info;

    public AbstractTile(TileInfo info) { 
        //tile info cannot be null
        if(info == null)throw new IllegalArgumentException("TileInfo cannot be null");
        
        this.info = info;
    }

    @Override public TileInfo info() {return info;}
    @Override public char symbol() {return type().symbol();} // gets the symbol of the tile type
                                                            //(can be overriden in runtime to custom symbols)
}
