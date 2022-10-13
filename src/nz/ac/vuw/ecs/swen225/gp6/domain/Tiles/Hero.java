package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.GameState;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

/**
 * A class that represents the hero/player in the game.
 */
public class Hero extends Actor{
    private static String moveString = "";//TODO remove
                            
    /**
     * Create a Hero actor
     * @param info tile information
     */
    public Hero (TileInfo info){
        super(info);
    }
    
    //INFO:
    @Override public TileType type(){ return TileType.Hero;}
    /**
     * This is at the moment only for testing purposes, MUST NOT be used in game.
     * @param t sets the tile the hero will replace when moved
     */
    public void setTileOn(Tile t){ info.tileOn(t);}
    
    /**
     * @return direction that hero is facing (not necessarily will move to)
     */
    public Direction dir(){return info.facing();} //returns the direction the hero is facing
    
    //MOVEMENT AND PING:
    @Override public Tile replaceWith(){return this;} //to know if a hero is taken by other tiles such as enemy
    @Override public void setOn(Tile t, Domain d){
        if(t == null || d == null) throw new NullPointerException("Domain d and Tile t must not be null(Hero.setOn)");

        d.getCurrentMaze().setTileAt(info.loc(), t);

        //if the tile damages the hero, LOSE
        if(t.damagesHero(d)){
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run());
            d.setGameState(GameState.LOST); //let the domain know the game is LOST
        }
    }

    @Override public void ping(Domain d) {
        if(d == null) throw new NullPointerException("Domain d can not be null(Hero.ping)");
        
        Level lvl = d.getCurrentLevelObject();
        Loc loc1 = info.loc();
        Direction dir = lvl.getHeroNextStep();
        Loc loc2 = dir.transformLoc(loc1); //new loc 
        Tile tileToOccupy = lvl.maze.getTileAt(loc2); //tile hero is to move on to

        //if hero hasnt moved or tile at new location is obstruction return
        if(dir == Direction.None || tileToOccupy.obstructsHero(d)) return;

        lvl.maze.getTileAt(loc1).setOn(tileOn(), d); //set previous location to tileOn
        lvl.maze.getTileAt(loc2).setOn(this, d); //set new location to hero, NOTE: Order matters here!

        this.info.tileOn(tileToOccupy.replaceWith()); // set tile heros to replace when moving off
        this.info.facing(dir); //set heros direction of facing 
        lvl.makeHeroStep(Direction.None); //make hero stop moving

        assert this.info.loc() == null || this.info.loc() != loc2: 
            "Hero didn't move properly or location is null";
    }
}
