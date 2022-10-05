package nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess;

import java.util.List;


import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.CheckGame;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/*
 * This class is for app to wrap on a domain object to use its functionalities,
 * without accessing any other methods inside the domain package.
 */
public class DomainController {
    private Domain domain;

    public DomainController(Domain domain){
        this.domain = domain;
        CheckGame.gameHasEnded = false; //reset the gameHasEnded flag
    }

    //ACTIONS ON DOMAIN:
    /**
     * move hero up in next ping, if possible
     */
    public void moveUp(){ domain.getCurrentMaze().makeHeroStep(Direction.Up);}

    /**
     * move hero down in next ping, if possible
     */
    public void moveDown(){domain.getCurrentMaze().makeHeroStep(Direction.Down);}

    /**
     * move hero left in next ping, if possible
     */
    public void moveLeft(){domain.getCurrentMaze().makeHeroStep(Direction.Left);}

    /**
     * move hero right in next ping, if possible
     */
    public void moveRight(){domain.getCurrentMaze().makeHeroStep(Direction.Right);}

    /**
     * ping the maze
     */
    public void pingAll(){ domain.pingDomain();}


    //INFO FROM DOMAIN:
    /**
     * gets current level
     * 
     * @return current level
     */
    public int getCurrentLevel() {return domain.getLvl();}

    /**
     * gets current levels time limit
     */
    public int getCurrentLevelTimeLimit() {return domain.getLevelTimeLimits().get(domain.getLvl() - 1);}

    /**
     * gets number of treasures left to collect
     * 
     * @return number of treasures left to collect
     */
    public int getTreasuresLeft() {return domain.getTreasuresLeft();}

    /**
     * returns the inventory, any empty slot is represented by an Null typed tile
     * 
     * @return a list of tiles representing the inventory
     */
    public List<Tile> getInventory() {return domain.getInv().getItems();}

    /**
     * returns a copy of current levels mazes game array
     * 
     * @return a copy of current levels mazes game array
     */
    public Tile[][] getGameArray(){return domain.getCurrentMaze().getTileArrayCopy();}

    /**
     * get's domain (should be used for only serialization)
     */
    public Domain getDomain(){return domain;}

    //SETTERS:
    /**
     * add an event listener to the domain
     * 
     * @param event the event to listen to
     * @param toRun the listener to add
     */
    public void addEventListener(Domain.DomainEvent event, Runnable toRun) {domain.addEventListener(event, toRun);}

    /**
     * sets current level to specified level index
     * 
     * @param lvl the level index to set the game to
     */
    public void setCurrentLevel(int lvl) {domain.setCurrentLvl(lvl);}

    /**
     * If there is another level increments the current level and returns true, 
     * else return false
     * 
     * @return true if there is another level
     */
    public boolean nextLvl(){
        if(domain.hasNextLvl()){
            this.domain.setCurrentLvl(domain.getLvl() + 1);
            return true;
        }
        return false;
    }
    
}


