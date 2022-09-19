package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping.Tile;
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
     * pings the game one step, and replaces the current maze with a new one
     */
    public void pingMaze(){
        this.mazes.set(currentLvl - 1, this.mazes.get(currentLvl - 1).pingMaze(this));
    }
}

class DomainController {
    private Domain domain;

    public DomainController(List<Maze> mazes, Inventory inv, int lvl){
        this.domain = new Domain(mazes, inv, lvl);
    }

    //ACTIONS ON DOMAIN:
    /*
     * move hero up in next ping, if possible
     */
    public void moveUp(){
        System.out.println("Player moved up");
        //domain.getMaze().makeHeroStep(Direction.Up);
    }

    /*
     * move hero down in next ping, if possible
     */
    public void moveDown(){
        System.out.println("Player moved down");
        //domain.getMaze().makeHeroStep(Direction.Down);
    }

    /*
     * move hero left in next ping, if possible
     */
    public void moveLeft(){
        System.out.println("Player moved left");
        //domain.getMaze().makeHeroStep(Direction.Left);
    }

    /*
     * move hero right in next ping, if possible
     */
    public void moveRight(){
        System.out.println("Player moved right");
        //domain.getMaze().makeHeroStep(Direction.Right);
    }

    /*
     * ping the maze
     */
    public void pingAll(){
        System.out.println("PingAll");
        //domain.getMaze().pingMaze(domain);
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
        //return domain.getMaze().getTileCount(TileType.Coin) - domain.getInv().coins();
    }

    /*
     * returns the inventory, any empty slot is represented by an Null typed tile
     */
    public List<Tile> getInventory() {
        return List.of();
        //return domain.getInv().getItems();
    }

    /*
     * returns a copy of current mazes game array
     */
    public Tile[][] getGameArray(){
        return domain.getCurrentMaze().getTileArrayCopy();
    }
}