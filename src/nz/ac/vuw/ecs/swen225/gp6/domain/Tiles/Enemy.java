package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

public class Enemy extends Actor{
    Direction staticDirection = Direction.Up; //should never be None
    Tile tileOn; //tile the enemy will replace when moved
    
    public Enemy(TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Enemy;}
    public Tile tileOn(){return tileOn;} //returns the tile the hero will replace when moved
    public Direction dir(){return staticDirection;} //returns the direction the hero is facing
    
    @Override public boolean damagesHero(Domain d){return true;}
    @Override public Tile replaceWith(){return this;} 
    @Override public void setOn(Tile t, Domain d){
        d.getCurrentMaze().setTileAt(info.loc(), t);

        //if the tile is hero, LOSE
        if(t.type() == TileType.Hero){
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run());
            CheckGame.gameHasEnded = true; //let the integrity checker know the game has ended
            CheckGame.won = false;
        }
    }
    
    @Override public void ping(Domain d){
        Maze m = d.getCurrentMaze();
        Loc l1 = info.loc();
        Direction dir = Direction.values()[(int)(Math.random()*4)]; //random direction
        Loc l2 = dir.transformLoc(l1); //new loc 
        Tile tileToOccupy = m.getTileAt(l2); //tile enemy is to move on to

        //if enemy hasnt moved or tile at new location is obstruction return
        if(dir == Direction.None || tileToOccupy.obstructsEnemy(d)) return;

        m.getTileAt(l1).setOn(tileOn, d); //set previous location to tileOn
        m.getTileAt(l2).setOn(this, d);   //set new location to enemy, NOTE: Order matters here!

        this.tileOn = tileToOccupy.replaceWith(); // set tile heros to replace when moving off
        staticDirection = m.getDirection(); //set heros direction of facing 
    } 
    
    //TODO: figure out how to make ben override/make all this

    
}
