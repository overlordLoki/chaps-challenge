package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Actor;

public class Enemy extends Actor{
    
    public Enemy(TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Enemy;}
    @Override public char symbol(){return 'E';}
    
    @Override public void setOn(Tile t, Domain d){
        d.getCurrentMaze().setTileAt(info.loc(), t);
        d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run()); //LOSE (since only hero can move on enemy)
    }//TODO: LOSE
    
    @Override public void ping(Domain d){} //TODO: figure out how to make ben override/make all this

    
}
