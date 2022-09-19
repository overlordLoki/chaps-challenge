package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;


public class Maze {
    private Tile[][] tileArray;
    private int height; //height of tile array, how many rows (outer array) 
    private int width;  //width of tile array, how many columns (inner arrays)

    private Direction heroNextStep = Direction.None;
    

    public Maze(Tile[][] tileArray){
        this.tileArray = tileArray;
        this.height = tileArray.length;
        this.width = tileArray[0].length;
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

    //TILE GETTERS:
    /*
     * @return a copy of tile array 
     */
    public Tile[][] getTileArrayCopy() {
        Tile[][] copy = new Tile[width][height];
        IntStream.range(0, width)
        .forEach( x ->
            IntStream.range(0, height)
            .forEach(y -> copy[x][y] = tileArray[x][y].getType()
            .makeTileObject(new TileInfo(new Loc(x, y), tileArray[x][y].getInfo().consumer()))
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
        .filter(t -> t.getType() == type)
        .count();
    }

    /*
     * gets the tile at the given x and y co ordinates in the array
     * if location is out of bounds return null typed tile
     */
    public Tile getTileAt(int x, int y){
        if(Loc.checkInBound(new Loc(x, y), this) == false) return TileType.Null.makeTileObject(null);
        return tileArray[x - 1][y - 1];
    }

    /*
     * gets the tile at the given location in the array
     */
    public Tile getTileAt(Loc l){
        if(Loc.checkInBound(l, this) == false) return TileType.Null.makeTileObject(null);
        return tileArray[l.x() - 1][l.y() - 1];
    }

    //SETTERS and ACTIONS:
    /**
     * @return a new maze which is the current maze after a unit of time passing.
     */
    public Maze pingMaze(Domain d){
        Maze nextMaze = new Maze(this.getTileArrayCopy());
        Arrays.stream(nextMaze.tileArray).flatMap(Arrays::stream).forEach(t -> t.ping(d));
        return nextMaze;
    }

    /**
     * place a new tile object of desired tile type at a given location on maze.
     * 
     * 
     * @param x of tile (0 to max - 1)
     * @param y of tile (0 to max - 1)
     * @param type enum for the tile type to place
     */
    public void setTileAt(Loc loc, TileType type, Consumer<Domain> pingConsumer){
        //check in bound
        if(Loc.checkInBound(loc, this) == false) return;
            
        //make tile object from type enum and replace the tile at the location
        tileArray[loc.x() - 1][loc.y() - 1] = type.makeTileObject(new TileInfo(loc, pingConsumer));

    }

    /*
     * set tile at a given location
     */
    public void setTileAt(Loc loc, Tile tile){
        //check in bound
        if(Loc.checkInBound(loc, this) == false) return;

        //replace tile at location
        tileArray[loc.x() - 1][loc.y() - 1] = tile;
    }

    /*
     * sets the movement direction of hero, 
     * which the hero will try to move towards if possible in NEXT ping.
     */
    public void makeHeroStep(Direction d){
        this.heroNextStep = d;
    }
}
