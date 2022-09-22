package nz.ac.vuw.ecs.swen225.gp6.domain.DomainAccess;

import java.util.List;
import java.util.function.Consumer;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/*
 * This class is for persistency to use to create a new domain object,
 * without accessing any other methods inside the domain package.
 */
public class DomainFactory {
    /*
     * returns a tile object with the given type and info
     * (wouldnt need to use this if adding tiles directly to the maze,
     * as there is another provided method)
     */
    public static Tile makeTile(TileType type, int x, int y, Consumer<Domain> consumer){
        return new Tile(type, new TileInfo(new Loc(x, y), consumer));
    }

    /*
     * returns a new maze with an empty game array.
     */
    public static Maze makeMaze(int height, int width){
        Tile[][] gameArray = new Tile[width][height];
        return new Maze(gameArray);
    }

    /*
     * sets the tile at the given column x and row y of game array on the given maze,
     * to a new object of the given type and an optional consumer<Domain> to overwrite ping
     * method of certain tiles.
     */
    public static void setTileOnMaze(int x, int y, Maze maze, TileType type, Consumer<Domain> consumer){
        maze.setTileAt(new Loc(x, y), type, consumer);
    }

    /*
     * sets a tile object on the given column x and row y of the game array om the given maze.
     */
    public static void setTileOnMaze(int x, int y, Maze maze, Tile tile){
        maze.setTileAt(new Loc(x, y), tile);
    }

    /*
     * makes a new empty inventory object with the given size.
     */
    public static Inventory makeInventory(int size){
        return new Inventory(size);
    }

    /*
     * make a new domain object with the given list of mazes, inventory object and a current level which domain is at.
     */
    public static Domain makeDomain(List<Maze> mazes, Inventory inv, int currentLvl){
        return new Domain(mazes, inv, currentLvl);
    }
}
