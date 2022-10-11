package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

/**
 * A class that represents the hero/player in the game.
 */
public class Hero extends Actor{
    private static Direction staticDirection = Direction.Up; //should never be None(can be static atm since we have only one player - also has to be)
    private static Tile tileOn; //tile the hero will replace when moved 
    
    /**
     * Create a Hero actor
     * @param info tile information
     */
    public Hero (TileInfo info){
        super(info);
        tileOn = new Floor((new TileInfo(info.loc()))); //set the tile initially is on to a floor
    }
    
    //INFO:
    @Override public TileType type(){ return TileType.Hero;}
    /**
     * @return tile that hero will replace when moved 
     */
    public Tile tileOn(){return tileOn;} //returns the tile the hero will replace when moved
    /**
     * @return direction that hero is facing (not necessarily will move to)
     */
    public Direction dir(){return staticDirection;} //returns the direction the hero is facing
    
    //MOVEMENT AND PING:
    @Override public void setOn(Tile t, Domain d){
        if(t == null || d == null) throw new NullPointerException("Domain d and Tile t must not be null(Hero.setOn)");

        d.getCurrentMaze().setTileAt(info.loc(), t);

        //if the tile damages the hero, LOSE
        if(t.damagesHero(d)){
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run());
            CheckGame.state = CheckGame.GameState.LOST; //let the integrity checker know the game is LOST
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

        lvl.maze.getTileAt(loc1).setOn(tileOn, d); //set previous location to tileOn
        lvl.maze.getTileAt(loc2).setOn(this, d); //set new location to hero, NOTE: Order matters here!

        //TODO remove after testing
        //System.out.println( "Location x: " + self.info().loc().x() + " y: " + self.info().loc().y());
        //System.out.println( d.getCurrentMaze().toString());

        this.tileOn = tileToOccupy.replaceWith(); // set tile heros to replace when moving off
        staticDirection = dir; //set heros direction of facing 
        lvl.makeHeroStep(Direction.None); //make hero stop moving

        assert this.info.loc() == null || this.info.loc() != loc2: 
            "Hero didn't move properly or location is null";
    }
}
