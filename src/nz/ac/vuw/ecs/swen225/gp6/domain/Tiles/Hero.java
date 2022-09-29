package nz.ac.vuw.ecs.swen225.gp6.domain.Tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

public class Hero extends Actor{
    Direction staticDirection; //TODO implement this

    public Hero (TileInfo info){super(info);}

    @Override public TileType type(){ return TileType.Hero;}
    @Override public char symbol(){return 'A';}
    @Override public boolean isObstruction(Tile t, Domain d) { return false;} 
    
    @Override public void setOn(Tile t, Domain d){
        d.getCurrentMaze().setTileAt(info.loc(), t);
        if(t.type() != TileType.Enemy) return; //TODO: temporary solution, make more future proof
        d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run()); //LOSE (if enemy on hero)
    }
    @Override public void ping(Domain d) {
        Maze m = d.getCurrentMaze();
        Loc l1 = info.loc();
        //find new location of hero if it moves
        Direction dir = d.getCurrentMaze().getDirection();
        Loc l2 = dir.transformLoc(l1);

        //if hero hasnt moved or tile at new location is obstruction return
        if(dir == Direction.None || m.getTileAt(l2).isObstruction(this, d)) return;
        
        //otherwise set previous location to empty and move self to new location (order matters here) 
        m.getTileAt(l1).setOn(new Floor(new TileInfo(l1)), d);
        m.getTileAt(l2).setOn(this, d);

        //TODO remove
        //System.out.println( "Location x: " + self.info().loc().x() + " y: " + self.info().loc().y());
        //System.out.println( d.getCurrentMaze().toString());
        
        staticDirection = m.getDirection(); //set heros direction of facing 
        m.makeHeroStep(Direction.None); //make hero stop moving
    }
}
