package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.CheckGame;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Actor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

public class Hero extends Actor{
    Direction staticDirection = Direction.Up; //should never be None
    Tile tileOn; //tile the hero will replace when moved 

    public Hero (TileInfo info){
        super(info);
        tileOn = new Floor((new TileInfo(info.loc()))); //set the tile initially is on to a floor
    }

    @Override public TileType type(){ return TileType.Hero;}
    public Tile tileOn(){return tileOn;} //returns the tile the hero will replace when moved
    public Direction dir(){return staticDirection;} //returns the direction the hero is facing
    
    @Override public void setOn(Tile t, Domain d){
        d.getCurrentMaze().setTileAt(info.loc(), t);

        //if the tile damages the hero, LOSE
        if(t.damagesHero(d)){
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run());
            CheckGame.gameHasEnded = true; //let the integrity checker know the game has ended
            CheckGame.won = false;
        }
    }

    @Override public void ping(Domain d) {
        Maze m = d.getCurrentMaze();
        Loc l1 = info.loc();
        Direction dir = d.getCurrentMaze().getDirection();
        Loc l2 = dir.transformLoc(l1); //new loc 
        Tile tileToOccupy = m.getTileAt(l2); //tile hero is to move on to

        //if hero hasnt moved or tile at new location is obstruction return
        if(dir == Direction.None || tileToOccupy.obstructsHero(d)) return;

        m.getTileAt(l1).setOn(tileOn, d); //set previous location to tileOn
        m.getTileAt(l2).setOn(this, d); //set new location to hero, NOTE: Order matters here!

        //TODO remove after testing
        //System.out.println( "Location x: " + self.info().loc().x() + " y: " + self.info().loc().y());
        //System.out.println( d.getCurrentMaze().toString());

        this.tileOn = tileToOccupy.replaceWith(); // set tile heros to replace when moving off
        staticDirection = m.getDirection(); //set heros direction of facing 
        m.makeHeroStep(Direction.None); //make hero stop moving
    }
}
