package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.CheckGame;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;

public class Domain {
    private List<Maze> mazes; //each corresponds to a level (in order)
    private List<Integer> levelTimeLimits; //each corresponds to a level (in order), the level restarts if time runs out
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

        //initialise default level times to 0
        levelTimeLimits = new ArrayList<Integer>();
        mazes.stream().forEach(a ->  levelTimeLimits.add(120));

        //initialise event listeners to empty lists (every domain event should always have an associated list)
        for(DomainEvent e : DomainEvent.values()){eventListeners.put(e, new ArrayList<Runnable>());}
    }

    public Domain(List<Maze> mazes, Inventory inv, int lvl, List<Integer> levelTimeLimits){
        this(mazes, inv, lvl);
        this.levelTimeLimits = levelTimeLimits;
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
    
    /**
     * returns the list of level time limits, where each element corresponds to a level,
     * and the level is restarted if time runs out.
     * 
     * @return the list of level time limits (in order)
     */
    public List<Integer> getLevelTimeLimits(){return levelTimeLimits;}

    /*
     * returns the inventory of the current maze
     */
    public Inventory getInv(){return inv;}

    /**
     * returns number of treasures left on current maze
     */
    public int getTreasuresLeft(){return this.getCurrentMaze().getTileCount(TileType.Coin);}

    /**
     * checks if there is a next level
     * 
     * @return true if there is a next level
     */
    public boolean hasNextLvl(){return currentLvl < mazes.size();}
   
    /**
     * gets the list of event listeners for a given event
     * 
     * @param e the event to get the listeners for
     * @return the list of listeners for the given event
     */
    public List<Runnable> getEventListener(DomainEvent event){
        return eventListeners.get(event);
    }
   
    /**
     * a specific toString method that uses the toString methods in 
     * maze and inventory as well as displaying the current level
     * TODO check if works
     */
    public String toString(){
        String s = "Current Level: " + currentLvl + "\n";
        s += inv.toString() + "\n";
        s += mazes.get(currentLvl - 1).toString();

        return s;
    }

    //SETTERS:
    /**
     * add an event listener to the domain
     * 
     * @param event the event to listen for
     * @param listener the listener to add
     */
    public void addEventListener(DomainEvent event, Runnable toRun) {
        List<Runnable> listeners = eventListeners.get(event); //get list of listeners
        listeners.add(toRun); //add new listener
        eventListeners.put(event, listeners); 
    }

    /*
     * sets current level to specified level index
     */
    public void setCurrentLvl(int lvl) {
        this.currentLvl = lvl;
        this.inv = new Inventory(this.inv.size()); //replace inventory
    }
    
    //PING:
    /*
     * pings the game one step, and replaces the current maze with a new one
     */
    public void pingDomain(){
        CheckGame.checkCurrentState(this); //check current state integrity

        //copy mazes
        Maze currentMaze = getCurrentMaze();
        Maze nextMaze = new Maze(currentMaze.getTileArrayCopy(), currentMaze.getDirection());
        List<Maze> newMazes = new ArrayList<Maze>(mazes);
        newMazes.set(currentLvl - 1, nextMaze);
        
        //copy current inventory
        Inventory nextInv = new Inventory(inv.size(), inv.coins(), inv.getItems());

        //copy domain 
        Domain nextDomain = new Domain(newMazes, nextInv, currentLvl);
        nextDomain.eventListeners = this.eventListeners;

        //ping
        nextMaze.pingMaze(nextDomain); //ping the new domain

        CheckGame.checkStateChange(this, nextDomain); //check state change integrity 

        this.mazes = newMazes;//replace current domain's field with next 
        this.inv = nextInv;
        
        //the nextDomain level should not be assigned to this domain, 
        //since the level may change in the ping and
        //the level changes will happen directly in this.domain
    }

}

