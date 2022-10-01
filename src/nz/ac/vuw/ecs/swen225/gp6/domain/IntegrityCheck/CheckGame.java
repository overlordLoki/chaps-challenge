package nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;

import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;


/*
 * This class contains a set of static method to check the integrity of 
 * the game:
 *  - before a ping step is successfully completed (by comparing the previous maze and inv to next ones)
 *  - after a ping (by looking at the new altered domain, and making sure certain rules are always followed)
 */
public class CheckGame {
    public static void checkStateChange(Maze preMaze, Inventory preInv, Maze afterMaze, Inventory afterInv){
        //make a mock pre and after domain //TODO: make sure this works
        Domain preDomain = new Domain(List.of(preMaze), preInv, 1);
        Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);


        //check if hero is moved on an obstruction
        Hero h = (Hero) afterMaze.getTileWithType(TileType.Hero);
        Loc heroNewLoc = h.info().loc();
        if(preMaze.getTileAt(heroNewLoc).obstructsHero(preDomain)){
            throw new IllegalStateException("Hero has moved on an obstruction: " 
            + preMaze.getTileAt(heroNewLoc).type().name());
        }

        //check if en
        
    }

    public static void checkCurrentState(Maze maze, Inventory inv){
        //check if hero is on maze
        if(maze.getTileWithType(TileType.Hero) instanceof Hero){
            throw new IllegalStateException("Hero is not on maze");
        } 
    }
}
