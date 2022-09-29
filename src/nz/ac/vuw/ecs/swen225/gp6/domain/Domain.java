package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileManaging.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;
import nz.ac.vuw.ecs.swen225.gp6.persistency.*;

public class Domain {
    private List<Maze> mazes; //each corresponds to a level (in order)
    private Inventory inv;
    private int currentLvl; //Note: first level should be 1


    public enum DomainEvent {
        onWin,
        onLose,
        onInfo
    }
    //domain events whose behaviour is dictated by app
    private EnumMap<DomainEvent, List<Runnable>>  eventListeners 
    = new EnumMap<DomainEvent, List<Runnable>>(DomainEvent.class);

    public Domain(List<Maze> mazes, Inventory inv, int lvl){
        this.mazes = mazes;
        this.inv = inv;
        this.currentLvl = lvl;
        
        //initialise event listeners to empty lists (every domain event should always have an associated list)
        for(DomainEvent e : DomainEvent.values()){eventListeners.put(e, new ArrayList<Runnable>());}
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

    /*
     * returns true if there is a next level
     */
    public boolean hasNextLvl(){return currentLvl < mazes.size();}
   
    /*
     * gets the list of event listeners for a given event
     */
    public List<Runnable> getEventListener(DomainEvent event){
        return eventListeners.get(event);
    }
    
    //SETTERS:
    /*
     * add an event listener to the domain
     */ 
    public void addEventListener(DomainEvent event, Runnable toRun) {
        List<Runnable> listeners = eventListeners.get(event); //get list of listeners
        listeners.add(toRun); //add new listener
        eventListeners.put(event, listeners); 
    }

    /*
     * sets current level to specified level index
     */
    public void setCurrentLvl(int lvl) {this.currentLvl = lvl;}
    
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

