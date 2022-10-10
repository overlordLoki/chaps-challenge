package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

public class Domain {
    private List<Level> levels; //each corresponds to a level (in order)

    private int currentLvlIndex; //Note: first level should be 1


    public enum DomainEvent {
        onWin,
        onLose,
        onInfo
    }
    //domain events whose behaviour is dictated by app
    private EnumMap<DomainEvent, List<Runnable>>  eventListeners 
    = new EnumMap<DomainEvent, List<Runnable>>(DomainEvent.class);


    //CONSTRUCTORS:
    public Domain(List<Level> levels, int currentLvl){
        this.levels = levels;
        this.currentLvlIndex = currentLvl;
        
        //initialise event listeners to empty lists (every domain event should always have an associated list)
        for(DomainEvent e : DomainEvent.values()){eventListeners.put(e, new ArrayList<Runnable>());}
    }

    public Domain(List<Maze> mazes, Inventory inv, int currentLvl){
        this(new ArrayList<>(), currentLvl);

        IntStream.range(0, mazes.size()).forEach(i ->{
            levels.add( currentLvl == i + 1? //initialise all inventories to empty ones except current levels to given one
                new Level(mazes.get(i), inv, i + 1, 120, 0, Direction.None):
                new Level(mazes.get(i), i + 1));
        });
    }

    public Domain(List<Maze> mazes, List<Inventory> invs,  List<Integer> levelTimeLimits, List<Integer> currentTimes,
     int currentLvl){
        this(new ArrayList<>(), currentLvl);

        if(mazes.size() != invs.size() || invs.size()!= levelTimeLimits.size()){ //ensure there is a 1:1 relationship
            throw new IllegalArgumentException("inconsistency in domain inputs");
        }

        IntStream.range(0, mazes.size()).forEach(i -> {
            levels.add(new Level(mazes.get(i), invs.get(i), i+1, 
            levelTimeLimits.get(i), currentTimes.get(i), Direction.None));
        });
    }


    //GETTERS:
    /*
     * returns list of mazes
     */
    public List<Maze> getMazes(){ return levels.stream().map(l -> l.maze).toList();}

    /*
     * returns current maze
     */
    public Maze getCurrentMaze(){ return getMazes().get(currentLvlIndex - 1);}

    /*
     * returns the inventory of the current level
     */
    public Inventory getInv(){return getCurrentLevelObject().inv;}

    /*
     * returns current level object
     */
    public Level getCurrentLevelObject(){return levels.get(currentLvlIndex - 1);}
        
    /**
     * returns the list of level time limits, where each element corresponds to a level,
     * and the level is restarted if time runs out.
     * 
     * @return the list of level time limits (in order)
     */
    public List<Integer> getLevelTimeLimits(){return levels.stream().map(l -> l.timeLimit).toList();}

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
    public void moveUp(){ getCurrentLevelObject().makeHeroStep(Direction.Up);}

    /**
     * move hero down in next ping, if possible
     */
    public void moveDown(){getCurrentLevelObject().makeHeroStep(Direction.Down);}

    /**
     * move hero left in next ping, if possible
     */
    public void moveLeft(){getCurrentLevelObject().makeHeroStep(Direction.Left);}

    /**
     * move hero right in next ping, if possible
     */
    public void moveRight(){getCurrentLevelObject().makeHeroStep(Direction.Right);}

    /*
     * pings the game one step, and replaces the current maze and inventory with a new one
     */
    public void pingDomain(){
        CheckGame.checkCurrentState(this); //check current state integrity

        //copy current maze
        Maze currentMaze = getCurrentMaze();
        Maze nextMaze = new Maze(currentMaze.getTileArrayCopy());
        
        //copy current inventory
        Inventory preInv = levels.get(currentLvlIndex - 1).inv;
        Inventory nextInv = new Inventory(preInv.size(), preInv.coins(), preInv.getItems());

        //copy levels 
        Level currentLvl = getCurrentLevelObject();
        List<Level> nextLevels = new ArrayList<Level>(this.levels);
        nextLevels.set(this.currentLvlIndex - 1, 
        new Level(nextMaze, nextInv, this.currentLvlIndex, currentLvl.timeLimit, 
        currentLvl.getCurrentTime(), currentLvl.getDirection()));

        //copy domain 
        Domain nextDomain = new Domain(nextLevels, this.currentLvlIndex);
        nextDomain.eventListeners = this.eventListeners;

        //ping
        nextMaze.pingMaze(nextDomain); //ping the new domain

        CheckGame.checkStateChange(this, nextDomain); //check state change integrity 

        levels.set(this.currentLvlIndex - 1, nextLevels.get(this.currentLvlIndex - 1));//replace ONLY the current level
    
    }
    
    //GETTERS:
    /*
     * gets the index of current lvl
     */
    public int getCurrentLevel(){return currentLvlIndex;}

    /*
     * gets current levels time limit
     */
    public int getCurrentTimeLimit(){return getLevelTimeLimits().get(currentLvlIndex - 1);}

    /**
     * gets number of treasures left on current maze
     */
    public int getTreasuresLeft(){return getCurrentMaze().getTileCount(TileType.Coin);}

    /**
     * gets a list of current items in the inventory 
     */
    public List<Tile> getInventory(){return getInv().getItems();}

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
        String s = "Current Level: " + currentLvlIndex + "\n";
        s += getInv().toString() + "\n";
        s += getCurrentMaze().toString();

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
    public void setCurrentLevel(int lvl) {this.currentLvlIndex = lvl;}
    
    /**
     * If there is another level increments the current level and returns true, 
     * else return false
     * 
     * @return true if there is another level
     */
    public boolean nextLvl(){
        if(currentLvlIndex < levels.size()){
            setCurrentLevel(currentLvlIndex + 1);
            return true;
        }
        return false;
    }
    
}

