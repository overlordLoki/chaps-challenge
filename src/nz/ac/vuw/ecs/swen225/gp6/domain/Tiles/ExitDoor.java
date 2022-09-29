package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class ExitDoor extends AbstractTile{
    
    public ExitDoor(TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.ExitDoor;}
    @Override public char symbol(){return 'X';}
    @Override public boolean isObstruction(Tile t, Domain d) { return true;}

    @Override public void ping(Domain d) {
        //if all treasures collected replace exitdoor with open exit door
        if(d.getTreasuresLeft() == 0){
            d.getCurrentMaze().setTileAt(info.loc(), TileType.ExitDoorOpen); 
        }
    }
}
