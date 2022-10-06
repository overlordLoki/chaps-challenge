package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

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

    /**
     * gets the list of event listeners for a given event
     * 
     * @param e the event to get the listeners for
     * @return the list of listeners for the given event
     */
    public List<Runnable> getEventListener(DomainEvent event){ return eventListeners.get(event);}
   


    //=====================================================================//
    //========= METHODS ACCESSED BY EXTERNAL PACKAGES(APP MAINLY) =========//
    //=====================================================================//
    //NOTE: may also be used internally in domain package

    //ACTIONS ON DOMAIN:
    /**
     * move hero up in next ping, if possible
     */
    public void moveUp(){ getCurrentMaze().makeHeroStep(Direction.Up);}

    /**
     * move hero down in next ping, if possible
     */
    public void moveDown(){getCurrentMaze().makeHeroStep(Direction.Down);}

    /**
     * move hero left in next ping, if possible
     */
    public void moveLeft(){getCurrentMaze().makeHeroStep(Direction.Left);}

    /**
     * move hero right in next ping, if possible
     */
    public void moveRight(){getCurrentMaze().makeHeroStep(Direction.Right);}

    /*
     * pings the game one step, and replaces the current maze and inventory with a new one
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
    
    //GETTERS:
    /*
     * gets current lvl
     */
    public int getCurrentLevel(){return currentLvl;}

    /*
     * gets current levels time limit
     */
    public int getCurrentTimeLimit(){return levelTimeLimits.get(currentLvl - 1);}

    /**
     * returns number of treasures left on current maze
     */
    public int getTreasuresLeft(){return getCurrentMaze().getTileCount(TileType.Coin);}

    /*
     * returns the inventory of the current maze
     */
    public Inventory getInventory(){return inv;}

    /**
     * gets a copy of current level's maze's game array
     * 
     * @return a copy of current level's maze's game array
     */
    public Tile[][] getGameArray(){return getCurrentMaze().getTileArrayCopy();}

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
    public void setCurrentLevel(int lvl) {
        this.currentLvl = lvl;
        this.inv = new Inventory(this.inv.size()); //replace inventory TODO should this be here?
    }
    
    /**
     * If there is another level increments the current level and returns true, 
     * else return false
     * 
     * @return true if there is another level
     */
    public boolean nextLvl(){
        if(currentLvl < mazes.size()){
            setCurrentLevel(currentLvl + 1);
            return true;
        }
        return false;
    }
    
}

