package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Level;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.CheckGame;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Actor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

public class Hero extends Actor{
    private static Direction staticDirection = Direction.Up; //should never be None(can be static atm since we have only one player - also has to be)
    private static Tile tileOn; //tile the hero will replace when moved 

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
            CheckGame.state = CheckGame.GameState.LOST; //let the integrity checker know the game is LOST
        }
    }

    @Override public void ping(Domain d) {
        Level lvl = d.getCurrentLevelObject();
        Loc loc1 = info.loc();
        Direction dir = lvl.getDirection();
        Loc loc2 = dir.transformLoc(loc1); //new loc 
        Tile tileToOccupy = lvl.maze.getTileAt(loc2); //tile hero is to move on to

        //if hero hasnt moved or tile at new location is obstruction return
        if(dir == Direction.None || tileToOccupy.obstructsHero(d)) return;

        lvl.maze.getTileAt(loc1).setOn(tileOn, d); //set previous location to tileOn
        lvl.maze.getTileAt(loc2).setOn(this, d); //set new location to hero, NOTE: Order matters here!

        //TODO remove after testing
        //System.out.println( "Location x: " + self.info().loc().x() + " y: " + self.info().loc().y());
        //System.out.println( d.getCurrentMaze().toString());

        this.tileOn = tileToOccupy.replaceWith(); // set tile heros to replace when moving off
        staticDirection = dir; //set heros direction of facing 
        lvl.makeHeroStep(Direction.None); //make hero stop moving
    }
}
