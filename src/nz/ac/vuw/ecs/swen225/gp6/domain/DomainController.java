package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Tile;

/*
 * This class is for app to wrap on a domain object to use its functionalities,
 * without accessing any other methods inside the domain package.
 */
public class DomainController {
    private Domain domain;

    //TODO delete later
    public DomainController(){
        List<Maze> mazes = new ArrayList<>();
        mazes.add(Helper.makeMaze());
        this.domain = new Domain(mazes, new Inventory(8), 1);
    }

    public DomainController(Domain domain){
        this.domain = domain;
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
     * returns a copy of current levels mazes game array
     */
    public Tile[][] getGameArray(){
        return domain.getCurrentMaze().getTileArrayCopy();
    }
}
