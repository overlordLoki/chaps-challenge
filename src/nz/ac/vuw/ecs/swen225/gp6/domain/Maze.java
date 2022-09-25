package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;


public class Maze {
    private Tile[][] tileArray;
    private int height; //height of tile array, how many rows (outer array) 
    private int width;  //width of tile array, how many columns (inner arrays)

    private Direction heroNextStep;
    

    public Maze(Tile[][] tileArray, Direction heroNextStep){
        this.tileArray = tileArray;
        this.height = tileArray.length;
        this.width = tileArray[0].length;

        this.heroNextStep = heroNextStep;
    }

    public Maze(Tile[][] tileArray){
        this(tileArray, Direction.None);
    }

    //GETTERS:
    /*
     * get height of tile array
     */
    public int height(){return height;}

    /*
     * get width of tile array
     */
    public int width(){ return width;}

    /**
     * @return direction of heros next step
     */
    public Direction getDirection(){return heroNextStep;}

    /*
     * toString method which creates the board with each tile's given symbol
     */
    public String toString(){
        Tile[][] tileArray = this.getTileArrayCopy();
        String r = "";

        for(int y = 0; y < height; y++){
            r += y + "|";
            for(int x = 0; x < width; x++){
                r += tileArray[x][y].getSymbol() + "|";
            }
            r += "\n";
        }

        return IntStream.range(0, this.width()).mapToObj(i -> " " + i).reduce( r + " ", (a, b) -> a + b);
    }

    //TILE GETTERS:
    /*
     * @return a copy of tile array 
     */
    public Tile[][] getTileArrayCopy() {
        Tile[][] copy = new Tile[width][height];
        IntStream.range(0, width)
        .forEach( x ->
            IntStream.range(0, height)
            .forEach(
                y -> copy[x][y] = new Tile(tileArray[x][y].type(), 
                new TileInfo(new Loc(x, y), tileArray[x][y].info().consumer())
            )
        ));
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
    
    /*
     * finds the number tiles with this tile type on this maze
     */
    public int getTileCount(TileType type){
        return (int)Arrays
        .stream(tileArray)
        .flatMap(Arrays::stream)
        .filter(t -> t.type() == type)
        .count();
    }

    /*
     * gets the tile at the given x and y co ordinates in the array
     * if location is out of bounds return null typed tile
     */
    public Tile getTileAt(int x, int y){
        if(Loc.checkInBound(new Loc(x, y), this) == false) return new Tile(TileType.Null, null);
        return tileArray[x][y];
    }

    /*
     * gets the tile at the given location in the array
     */
    public Tile getTileAt(Loc l){
        if(Loc.checkInBound(l, this) == false) return new Tile(TileType.Null, null);
        return tileArray[l.x()][l.y()];
    }

    //SETTERS and ACTIONS:
    /**
     * pings all tiles in the maze
     */
    public void pingMaze(Domain d){
        Arrays.stream(d.getCurrentMaze().tileArray).flatMap(Arrays::stream).forEach(t -> t.ping(d));
    }

    /**
     * place a new tile object of desired tile type at a given location on maze.
     * 
     * 
     * @param x of tile (0 to max - 1)
     * @param y of tile (0 to max - 1)
     * @param type enum for the tile type to place
     */
    public void setTileAt(Loc loc, TileType type, BiConsumer<Tile, Domain> pingConsumer){
        //check in bound
        if(Loc.checkInBound(loc, this) == false) return;
            
        //make tile object from type enum and replace the tile at the location
        tileArray[loc.x()][loc.y()] = new Tile(type, new TileInfo(loc, pingConsumer));

    }

    /*
     * set tile at a given location
     */
    public void setTileAt(Loc loc, Tile tile){
        //check in bound
        if(Loc.checkInBound(loc, this) == false) return;

        //replace tile at location
        tileArray[loc.x()][loc.y()] = tile;

        //update tile info
        tile.info().loc(loc);
    }

    /*
     * sets the movement direction of hero, 
     * which the hero will try to move towards if possible in NEXT ping.
     */
    public void makeHeroStep(Direction d){
        this.heroNextStep = d;
    }


}
