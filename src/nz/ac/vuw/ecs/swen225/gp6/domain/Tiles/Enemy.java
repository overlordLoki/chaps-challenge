package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

public class Enemy extends Actor{
    Direction staticDirection; //TODO implement this
    Tile tileOn; //tile the enemy will replace when moved
    
    public Enemy(TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Enemy;}
    public Tile tileOn(){return tileOn;} //returns the tile the hero will replace when moved
    public Direction dir(){return staticDirection;} //returns the direction the hero is facing
    
    @Override public boolean damagesHero(Domain d){return true;}
    @Override public void setOn(Tile t, Domain d){
        d.getCurrentMaze().setTileAt(info.loc(), t);

        //if the tile is hero, LOSE
        if(t.type() == TileType.Hero){
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run());
            CheckGame.gameHasEnded = true; //let the integrity checker know the game has ended
        }
    }
    
    @Override public void ping(Domain d){} //TODO: figure out how to make ben override/make all this

    
}
