package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.Arrays;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.persistency.*;

public class Domain {
    private List<Maze> mazes; //each corresponds to a level (in order)
    private Inventory inv;
    private int currentLvl; //Note: first level should be 1

    public Domain(List<Maze> mazes, Inventory inv, int lvl){
        this.mazes = mazes;
        this.inv = inv;
        this.currentLvl = lvl;
    }

    //GETTERS:
    /*
     * returns current lvl
     */
    public int getLvl(){return currentLvl;}

    /*
     * returns current maze
     */
    public Maze getCurrentMaze(){ return mazes.get(currentLvl - 1);}

    /*
     * returns list of mazes
     */
    public List<Maze> getMazes(){ return mazes;}
    
    /*
     * returns the inventory of the current maze
     */
    public Inventory getInv(){return inv;}

    /*
     * returns number of treasures left on current maze
     */
    public int getTreasuresLeft(){return this.getCurrentMaze().getTileCount(TileType.Coin);}

   
    //PING:
    /*
     * pings the game one step, and replaces the current maze with a new one
     */
    public void pingDomain(){
        //copy current maze TODO: see if this is necessary, or what it is useful for
        Maze currentMaze = getCurrentMaze();
        Maze nextMaze = new Maze(currentMaze.getTileArrayCopy(), currentMaze.getDirection());
        currentMaze = nextMaze;
        
        //copy current inventory
        Inventory nextInv = new Inventory(inv.size(), inv.coins(), inv.getItems());
        inv = nextInv;

        //ping the maze
        nextMaze.pingMaze(this);
    }
}

