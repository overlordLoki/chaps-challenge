package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.*;

public class ExitDoorOpen extends AbstractTile{

    public ExitDoorOpen (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.ExitDoorOpen;}
    @Override public char symbol(){return 'Z';}

    @Override public void setOn(Tile t, Domain d){
        d.getEventListener(Domain.DomainEvent.onWin).forEach(r -> r.run());
    }// WIN
            
}
