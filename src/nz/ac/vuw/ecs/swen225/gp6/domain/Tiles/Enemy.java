package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

public class Enemy extends Actor{
    Direction staticDirection = Direction.Up; //should never be None
    Tile tileOn; //tile the enemy will replace when moved
    
    public Enemy(TileInfo info){
        super(info);
        tileOn = new Floor((new TileInfo(info.loc()))); //set the tile initially is on to a floor
    }

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
            CheckGame.state = CheckGame.GameState.LOST; //let the integrity checker know the game is LOST

        }
    }
    
    @Override public void ping(Domain d){
        info.pingStep(); //step the ping counter
        if(info.ping() % 10 != 0) return; //only move every 20 pings

        Level lvl = d.getCurrentLevelObject();
        Loc loc1 = info.loc();
        Direction dir = Direction.values()[(int)(Math.random()*4)]; //random direction
        Loc loc2 = dir.transformLoc(loc1); //new loc 
        Tile tileToOccupy = lvl.maze.getTileAt(loc2); //tile enemy is to move on to

        //if enemy hasnt moved or tile at new location is obstruction return
        if(dir == Direction.None || tileToOccupy.obstructsEnemy(d)) return;

        lvl.maze.getTileAt(loc1).setOn(tileOn, d); //set previous location to tileOn
        lvl.maze.getTileAt(loc2).setOn(this, d);   //set new location to enemy, NOTE: Order matters here!

        this.tileOn = tileToOccupy.replaceWith(); // set tile heros to replace when moving off
        staticDirection = lvl.getDirection(); //set enemys direction of facing 
    } 
    
    //TODO: figure out how to make ben override/make all this

    
}
