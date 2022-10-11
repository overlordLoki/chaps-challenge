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
        Tile[][] tileArray = this.getTileArrayCopy();
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
     */
    public Tile[][] getTileArrayCopy() {
        Tile[][] copy = new Tile[width][height];
        IntStream.range(0, width)
        .forEach( x ->
            IntStream.range(0, height)
            .forEach(y->{
                Tile t = tileArray[x][y];
                copy[x][y] = TileType.makeTile(t.type(),
                new TileInfo(new Loc(x, y), t.info().ping(), t.info().getImageName()));
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
     */
    public Loc getTileLoc(Tile t){
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
     */
    public Tile getTileAt(int x, int y){
        if(Loc.checkInBound(new Loc(x, y), this) == false) return new Null(new TileInfo(null));
        return tileArray[x][y];
    }

    /**
     * gets the tile at the given location in the array
     * 
     * @param l location of tile
     * @return the tile at the location or the Null typed tile if its out of bounds.
     */
    public Tile getTileAt(Loc l){
        if(Loc.checkInBound(l, this) == false) return new Null(new TileInfo(null));
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
     */
    public void pingMaze(Domain d){
        Arrays.stream(d.getCurrentMaze().tileArray).flatMap(Arrays::stream).forEach(t -> t.ping(d));
    }

    /**
     * place a new tile object of desired tile type at a given location on maze.
     * 
     * 
     * @param loc - location of tile 
     * @param type enum for the tile type to place
     */
    public void setTileAt(Loc loc, TileType type){
        //check in bound
        if(Loc.checkInBound(loc, this) == false) return;
            
        //make tile object from type enum and replace the tile at the location
        tileArray[loc.x()][loc.y()] = TileType.makeTile(type, new TileInfo(loc));

    }

    /**
     * set tile at a given location
     * 
     * @param loc - location of tile
     * @param tile new tile to replace old tile
     */
    public void setTileAt(Loc loc, Tile tile){
        //check in bound
        if(Loc.checkInBound(loc, this) == false) return;

        //replace tile at location
        tileArray[loc.x()][loc.y()] = tile;

        //update tile info
        tile.info().loc(loc);
    }

    

}
