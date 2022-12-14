package custom.tiles;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.GameState;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

public class Enemy extends Actor {
    public Enemy(TileInfo info) {
        super(info);
    }

    @Override
    public TileType type() {
        return TileType.Other;
    }

    @Override
    public Tile tileOn() {
        return info.tileOn();
    } // returns the tile the hero will replace when moved

    public Direction dir() {
        return info.facing();
    } // returns the direction the hero is facing

    @Override
    public boolean damagesHero(Domain d) {
        return true;
    }

    @Override
    public Tile replaceWith() {
        return this;
    }

    @Override
    public char symbol() {
        return 'E';
    }

    @Override
    public void setOn(Tile t, Domain d) {
        d.getCurrentMaze().setTileAt(info.loc(), t);

        // if the tile is hero, LOSE
        if (t.type() == TileType.Hero) {
            d.getEventListener(Domain.DomainEvent.onLose).forEach(r -> r.run());
            d.setGameState(GameState.LOST); // let the integrity checker know the game is LOST

        }
    }

    @Override
    public void ping(Domain d) {
        info.pingStep(); // step the ping counter
        if(info.ping() % 10 != 0)
            return; // only move every 20 pings

        Level lvl = d.getCurrentLevelObject();
        Loc loc1 = info.loc();
        Direction dir = info.facing(); // previous direction
        Loc loc2 = dir.transformLoc(loc1); // new loc
        Tile tileToOccupy = lvl.maze.getTileAt(loc2); // tile enemy is to move on to

        // if enemy hasnt moved return 
        if (dir == Direction.None)return;
            
        //if tile at new location is obstruction change direction, in a determinisitic way, then return
        if(tileToOccupy.obstructsEnemy(d)){
            info.facing(Direction.values()[(info.ping()/10)% 4]);
            return;
        }

        lvl.maze.getTileAt(loc1).setOn(info.tileOn(), d); // set previous location to tileOn
        lvl.maze.getTileAt(loc2).setOn(this, d); // set new location to enemy, NOTE: Order matters here!

        info.tileOn(tileToOccupy.replaceWith()); // set tile heros to replace when moving off
    }

}