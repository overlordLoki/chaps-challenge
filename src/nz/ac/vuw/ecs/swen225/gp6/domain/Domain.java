package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.persistency.*;

public class Domain {
    private List<Maze> mazes; //each corresponds to a level (in order)
    private Inventory inv;
    private int currentLvl;

    public Domain(List<Maze> mazes, Inventory inv, int lvl){
        this.mazes = mazes;
        this.inv = inv;
        this.currentLvl = lvl;
    }

    //GETTERS:
    /*
     * returns current lvl
     */
    public int getLvl(){
        return currentLvl;
    }

    /*
     * returns current maze
     */
    public Maze getCurrentMaze(){
        return mazes.get(currentLvl - 1);
    }

    /*
     * returns the inventory of the current maze
     */
    public Inventory getInv(){
        return inv;
    }

    /*
     * returns number of treasures left on current maze
     */
    public int getTreasuresLeft(){
        return this.getCurrentMaze().getTileCount(TileType.Coin) - inv.coins();
    }

    //PING:
    /*
     * pings the game one step, and replaces the current maze with a new one
     */
    public void pingMaze(){
        this.mazes.set(currentLvl - 1, this.mazes.get(currentLvl - 1).pingMaze(this));
    }
}

