package nz.ac.vuw.ecs.swen225.gp6.domain;


import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;



/**
 * A class that holds a 2d array of tiles representing the maze in the game.
 */
public class Maze {
    private Tile[][] tileArray;
    private int height; //height of tile array, how many rows (outer array) 
    private int width;  //width of tile array, how many columns (inner arrays)
    
    /**
     * Constructs a new 2d array of tiles based on a given 2d tile array.
     * 
     * @param tileArray a 2d array of type Tile
     */
    public Maze(Tile[][] tileArray){
        this.tileArray = tileArray;
        this.height = tileArray.length;
        this.width = tileArray[0].length;
    }


    //GETTERS:
    /**
     * get height of tile array
     * 
     * @return height of maze
     */
    public int height(){return height;}

    /**
     * get width of tile array
     * 
     * @return width of maze
     */
    public int width(){ return width;}
    
    /**
     * toString method which creates the board with each tile's given symbol.
     * 
     * @return the board in string format
     */
    public String toString(){
        String r = "";

        for(int y = 0; y < height; y++){
            r += y + "|";
            for(int x = 0; x < width; x++){
                r += tileArray[x][y].symbol() + "|";
            }
            r += "\n";
        }

        return IntStream.range(0, this.width()).mapToObj(i -> " " + i).reduce( r + " ", (a, b) -> a + b);
    }

    //TILE GETTERS:
    /**
     * TODO:change
     * @return a copy of tile array (SHALLOW COPY)
     * returns tile array
     * 
     * IMPORTANT NOTE: a deep copy here would have provided more encapsulation of the tile array,
     * however a shallow copy is chosen to allow for tile types to be added at run time. 
     */
    public Tile[][] getTileArrayCopy() {
        Tile[][] copy = new Tile[width][height];
        IntStream.range(0, width)
        .forEach( x ->
            IntStream.range(0, height)
            .forEach(y->{
                Tile t = tileArray[x][y];
                copy[x][y] = t;
            }
            )
        );
        return copy;
    }

    /**
     * finds location of a given tile (exactly the same object)
     * 
     * @param t tile object to find
     * @return loc of tile object if found, else null
     * 
     * @throws NullPointerException if tile t is null
     */
    public Loc getTileLoc(Tile t){
        if(t == null) throw new NullPointerException("tile t cannot be null Maze.getTileLoc(Tile)");
        for(int x = 0; x < width - 1; x++){
            for(int y = 0; y < height - 1; y++){
                if(t == tileArray[x][y]) return new Loc(x, y);
            }
        }
        return null;
    }
    
    /**
     * finds the number tiles with this tile type on this maze
     * 
     * @param type to count
     * @return number of tiles with give type
     */
    public int getTileCount(TileType type){
        return (int)Arrays
        .stream(tileArray)
        .flatMap(Arrays::stream)
        .filter(t -> t.type() == type)
        .count();
    }

    /**
     * gets the tile at the given x and y co ordinates in the array
     * if location is out of bounds return null typed tile
     * 
     * @param x x coord
     * @param y y coord
     * @return the tile at the coords or the Null typed tile if its out of bounds.
     *
     * @throws IndexOutOfBoundsException if loc out of maze
     */
    public Tile getTileAt(int x, int y){
        checkLocationIntegrity(new Loc(x, y));
        return tileArray[x][y];
    }

    /**
     * gets the tile at the given location in the array
     * 
     * @param l location of tile
     * @return the tile at the location or the Null typed tile if its out of bounds.
     * 
     * @throws NullPointerException if loc or tile is null
     * @throws IndexOutOfBoundsException if loc out of maze
     */
    public Tile getTileAt(Loc l){
        checkLocationIntegrity(l);
        return tileArray[l.x()][l.y()];
    }

    /**
     * gets the first tile that satisfies the given predicate.
     * 
     * @param p predicate
     * @return first tile that satisfies the predicate, or Null typed tile if none exist
     */
    public Tile getTileThat(Predicate<Tile> p){
        return Arrays
        .stream(tileArray)
        .flatMap(Arrays::stream)
        .filter(p)
        .findFirst()
        .orElse(new Null(new TileInfo(null)));
    }
    
    /**
     * get all the tiles that satisfy the given predicate
     * 
     * @param p predicate
     * @return list of all tiles that satisfy the predicate, otherwise an empty list
     */
    public List<Tile> getAllTilesThat(Predicate<Tile> p){
        return Arrays
        .stream(tileArray)
        .flatMap(Arrays::stream)
        .filter(p)
        .toList();
    }

    //SETTERS and ACTIONS:
    /**
     * pings all tiles in the maze
     * 
     * @param d - domain where the ping is taking place in 
     * (this is since the ping may affect inventory, level index, etc)
     * 
     * @throws NullPointerException if Domain is null
     */
    public void pingMaze(Domain d){
        if(d == null) throw new NullPointerException("domain cannot be null in maze.pingMaze()");

        Arrays.stream(d.getCurrentMaze().tileArray).flatMap(Arrays::stream).forEach(t -> t.ping(d));
    }

    /**
     * place a new tile object of desired tile type at a given location on maze.
     * 
     * 
     * @param loc - location of tile 
     * @param type enum for the tile type to place
     * 
     * @throws NullPointerException if loc is null
     * @throws IndexOutOfBoundsException if loc out of maze
     */
    public void setTileAt(Loc loc, TileType type){
        checkLocationIntegrity(loc); 
            
        //make tile object from type enum and replace the tile at the location
        tileArray[loc.x()][loc.y()] = TileType.makeTile(type, new TileInfo(loc));

    }

    /**
     * set tile at a given location
     * 
     * @param loc - location of tile
     * @param tile new tile to replace old tile
     * 
     * @throws NullPointerException if loc or tile is null
     * @throws IndexOutOfBoundsException if loc out of maze
     */
    public void setTileAt(Loc loc, Tile tile){
        //prechecks
        if(tile == null) throw new IllegalArgumentException("Tile cannot be null (Maze.setTileAt(Loc, Tile))");
        checkLocationIntegrity(loc);

        //replace tile at location
        tileArray[loc.x()][loc.y()] = tile;

        //update tile info
        tile.info().loc(loc);
    }

    //HELPERS 
    /**
     * helper class that throws an exception if the location is out of bounds or null.
     * 
     * @param loc - location to check
     * 
     * @throws NullPointerException if loc is null;
     * @throws IndexOutOfBoundsException if loc is not in the bounds of the maze;
     */
    public void checkLocationIntegrity(Loc loc){
        if(loc == null) throw new NullPointerException("location cannot be null in (Maze.checkLocationIntegrity(Loc))");
        if(Loc.checkInBound(loc, this) == false) 
            throw new IllegalArgumentException("Location out of bounds: x: " 
            + loc.x() + " y: " + loc.y() + " (Maze.checkLocationIntegrity(Loc))");
    }

}
