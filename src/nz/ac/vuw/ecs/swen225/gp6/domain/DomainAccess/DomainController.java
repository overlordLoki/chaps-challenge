package nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess;

import java.util.ArrayList;
import java.util.List;


import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/*
 * This class is for app to wrap on a domain object to use its functionalities,
 * without accessing any other methods inside the domain package.
 */
public class DomainController {
    private Domain domain;

    public DomainController(Domain domain){
        this.domain = domain;
    }

    //ACTIONS ON DOMAIN:
    /*
     * move hero up in next ping, if possible
     */
    public void moveUp(){
        domain.getCurrentMaze().makeHeroStep(Direction.Up);
    }

    /*
     * move hero down in next ping, if possible
     */
    public void moveDown(){
        domain.getCurrentMaze().makeHeroStep(Direction.Down);
    }

    /*
     * move hero left in next ping, if possible
     */
    public void moveLeft(){
        domain.getCurrentMaze().makeHeroStep(Direction.Left);
    }

    /*
     * move hero right in next ping, if possible
     */
    public void moveRight(){
        domain.getCurrentMaze().makeHeroStep(Direction.Right);
    }

    /*
     * ping the maze
     */
    public void pingAll(){
        domain.pingDomain();
    }


    //INFO FROM DOMAIN:
    /*
     * returns true when player reaches this level's final tile (player on the open exit door)
     */
    public boolean playerOnExitDoor(){
        return false;
    }

    /*
     * gets current level
     */
    public int getCurrentLevel() {
        return 1;
        //return domain.getLvl();
    }

    /*
     * gets number of treasures left to collect
     */
    public int getTreasuresLeft() {
        return 10;
        //return domain.getTreasuresLeft();
    }

    /*
     * returns the inventory, any empty slot is represented by an Null typed tile
     */
    public List<Tile> getInventory() {
        return List.of(new Tile(TileType.BlueKey, null),new Tile(TileType.GreenKey, null), new Tile(TileType.OrangeKey, null));
        //return domain.getInv().getItems();
    }

    /*
     * returns a copy of current levels mazes game array
     */
    public Tile[][] getGameArray(){
        return domain.getCurrentMaze().getTileArrayCopy();
    }
}
