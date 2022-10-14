package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;

/**
 * Represents a complete game state, with levels.
 * Each level includes an inventory, maze, level number
 * direction of heros next move, time limit, current time, etc.
 */
public class Domain {
    private List<Level> levels; //each corresponds to a level (in order)
    private int currentLvlIndex; //Note: first level should be 1

    /**
     * enum to inform us of the current state of the game
     * integrity check uses this frequently to check rules of the game accordingly
     */
    public enum GameState{
        WON, 
        LOST, 
        PLAYING, 
        BETWEENLEVELS
    };
    private GameState state;

    /**
     * enum for domainEvents that the app will be informed of when triggered.
     */
    public enum DomainEvent {
        onWin,
        onLose,
        onInfo
    }
    //domain events whose behaviour is dictated by app
    private EnumMap<DomainEvent, List<Runnable>>  eventListeners 
    = new EnumMap<DomainEvent, List<Runnable>>(DomainEvent.class);


    //CONSTRUCTORS:
    /**
     * constructor with a list of levels. 
     * 
     * @param levels - must be in order.
     * @param currentLvl - the index of the current level starting at 1
     * 
     * @throws IndexOutOfBoundsException if currentLvl is < 1 or > levels.size()
     * @throws NullPointerException if levels is null or contains null.
     */
    public Domain(List<Level> levels, int currentLvl){
        if(levels == null)throw new NullPointerException("list of levels cannot be null.");
        if(currentLvl < 1 || currentLvl > levels.size())
            throw new IndexOutOfBoundsException("currentLvl must be greater than 0(Domain), and smaller or equal to levels.size()");

        this.levels = levels;
        this.currentLvlIndex = currentLvl;
        this.state = GameState.PLAYING; //set initial state to playing
        
        //initialise event listeners to empty lists (every domain event should always have an associated list)
        for(DomainEvent e : DomainEvent.values()){eventListeners.put(e, new ArrayList<Runnable>());}
    }

    /**
     * constructor with a list of mazes, and an inventory for current level (all other levels get an empty inv).
     * Levels are created internally from mazes in order. Also timelimits default to 120 seconds, and current 
     * times to 0 for each level.
     * 
     * @param domain - must be in order.
     * @param inventory - inventory for current level
     * @param currentLvl - the index of the current level starting at 1
     */
    public Domain(List<Maze> mazes, Inventory inv, int currentLvl){
        //make a mock list of levels to make sure current lvl is in bounds
        this(IntStream.range(0, mazes.size()).mapToObj(i -> new Level(mazes.get(i), i + 1)).toList(),
        currentLvl);

        levels = new ArrayList<Level>();
        IntStream.range(0, mazes.size()).forEach(i ->{
            levels.add( currentLvl == i + 1? //initialise all inventories to empty ones except current levels to given one
                new Level(mazes.get(i), inv, i + 1, 120, 0, Direction.None):
                new Level(mazes.get(i), i + 1));
        });
    }

    //GETTERS:
    /**
     * gets a list of mazes in order of the levels they are in.
     * 
     * @return - list of mazes 
     */
    public List<Maze> getMazes(){ return levels.stream().map(l -> l.maze).toList();}

    /**
     * gets the maze of current level
     * 
     * @return - maze of current level
     */
    public Maze getCurrentMaze(){ return getCurrentLevelObject().maze;}

    /**
     * gets the inventory of the current level
     * 
     * @return - inventory of the current level
     */
    public Inventory getInv(){return getCurrentLevelObject().inv;}

    /**
     * returns current level object
     * 
     * @return - current level object
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

    /**
     * pings the game one step, and replaces the current level object with a new level.
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
        currentLvl.getCurrentTime(), currentLvl.getHeroNextStep()));

        //copy domain 
        Domain nextDomain = new Domain(nextLevels, this.currentLvlIndex);
        nextDomain.eventListeners = this.eventListeners;

        //ping
        nextMaze.pingMaze(nextDomain); //ping the new domain

        CheckGame.checkStateChange(this, nextDomain); //check state change integrity 

        this.state = nextDomain.state; //update domain
        levels.set(this.currentLvlIndex - 1, nextLevels.get(this.currentLvlIndex - 1));//replace ONLY the current level
    
    }
    
    //GETTERS:
    /**
     * gets the list of levels 
     * 
     * @return list of levels 
     */
    public List<Level> getLevels(){ return new ArrayList<Level>(this.levels);}
    
    /**
     * gets the index of current lvl
     * 
     * @return index of current lvl
     */
    public int getCurrentLevel(){return currentLvlIndex;}

    /**
     * gets current levels time limit
     * 
     * @return time limit of current level
     */
    public int getCurrentTimeLimit(){return getLevelTimeLimits().get(currentLvlIndex - 1);}

    /**
     * gets the current time of the current level.
     * 
     * @return current time
     */
    public long getCurrentTime(){return levels.get(currentLvlIndex - 1).getCurrentTime();}

    /**
     * gets number of treasures left on current level's maze
     * 
     * @return number of treasures left 
     */
    public int getTreasuresLeft(){return getCurrentMaze().getTileCount(TileType.Coin);}

    /**
     * gets state of the game(won, lost, inbetween levels, playing)
     * @return GameState enum
     */
    public GameState getGameState(){return state;}

    /**
     * @return true if game is on last level, else false
     */
    public boolean isLastLevel(){return currentLvlIndex == levels.size();}

    /**
     * @return true if the hero is on the info tile of the level, else false
     */
    public boolean heroIsOnInfo(){
        return ((Hero)(getCurrentMaze().getTileThat(t -> t.type() == TileType.Hero))).tileOn().type() == TileType.Info;
    }
    /** 
     * gets the message stored in the info tile of this level (note every level can have one info tile at max), 
     * must ONLY BE CALLED when hero is on tile info.
     * 
     * @return the message stored in the info tile of this level
     * 
     * @throws RuntimeException if hero is not on the info tile
     */
    public String getInfoHint(){
        if(heroIsOnInfo() == false) throw new RuntimeException("hero is not on info");
        return ((Info)(((Hero)getCurrentMaze().getTileThat(t -> t.type() == TileType.Hero)).tileOn())).message();
    }
    
    /**
     * gets a list of current items in the inventory
     * 
     * @return list of items in inventory 
     */
    public List<Tile> getInventory(){return getInv().getItems();}

    /**
     * gets a copy of current level's maze's game array (shallow copy)
     * 
     * @return a copy of current level's maze's game array
     */
    public Tile[][] getGameArray(){return getCurrentMaze().getTileArrayCopy();}

    /**
     * a specific toString method that uses the toString methods in 
     * maze and inventory as well as displaying the current level
     * 
     * @return string representation of the Maze and Inventory at the moment
     */
    public String toString(){
        String s = "Current Level: " + currentLvlIndex + "\n";
        s += getInv().toString() + "\n";
        s += getCurrentMaze().toString();

        return s;
    }

    //SETTERS:
    /**
     * sets current time of current level 
     * 
     * @param time to set the current time to
     */
    public void setCurrentTime(long time){getCurrentLevelObject().setCurrentTime(time);}

    /**
     * add an event listener to the domain
     * 
     * @param event the event to listen for
     * @param listener the listener to add
     * 
     * @throws NullPointerException if the event or listener is null
     */
    public void addEventListener(DomainEvent event, Runnable toRun) {
        if(event == null || toRun == null)
            throw new NullPointerException("DomainEvent or Runnable associated cannot be null (Domain.addEventListener)");

        List<Runnable> listeners = eventListeners.get(event); //get list of listeners
        listeners.add(toRun); //add new listener
        eventListeners.put(event, listeners); 
    }

    /**
     * sets current level to specified level index
     * 
     * @param lvl the level index to switch to
     * 
     * @throws IndexOutOfBoundsException if lvl is not at least 1 or is bigger than the number of levels
     */
    public void setCurrentLevel(int lvl) {
        if(lvl < 1 || lvl > levels.size()) 
            throw new IndexOutOfBoundsException("invalid level index (Domain.setCurrentLevelIndex)");
        this.currentLvlIndex = lvl;}

    /**
     * sets state of the game(won, lost, inbetween levels, playing)
     * @param GameState enum
     */
    public void setGameState(GameState state){this.state = state;}
    
    /**
     * If there is another level increments the current level and returns true, 
     * else return false
     * 
     * @return true if there is another level
     */
    public boolean nextLvl(){
        if(isLastLevel() == false){
            setCurrentLevel(currentLvlIndex + 1);
            return true;
        }
        return false;
    }
    
}

