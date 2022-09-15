package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.stream.IntStream;

public class Maze {
    private Tile[][] tileArray;
    private int height; //height of tile array, how many rows (outer array) 
    private int width;  //width of tile array, how many columns (inner arrays)

    private Inventory inv;

    private int lvl;

    private Direction heroNextStep = Direction.None;
    

    public Maze(Tile[][] tileArray, Inventory inv, int lvl){
        this.tileArray = tileArray;
        this.inv = inv;
        this.height = tileArray.length;
        this.width = tileArray[0].length;
        this.lvl = lvl;
    }

    /*
     * @return a copy of tile array 
     */
    public Tile[][] getTileArray() {
        Tile[][] copy = new Tile[width][height];
        IntStream.range(0, width)
        .forEach( x ->
            IntStream.range(0, height)
            .forEach(y -> copy[x][y] = tileArray[x][y].getType().getTileObject(new TileInfo(new Loc(x, y))))
        );
        return copy;
    }

    /*
     * get height of tile array
     */
    public int height(){return height;}

    /*
     * get width of tile array
     */
    public int width(){ return width;}

    /**
     * @return the inventory of the player
     */
    public Inventory getInv(){ return inv;}

    /**
     * @return the current level 
     */
    public int getLvl(){return lvl;}

    /**
     * @return direction of heros next step
     */
    public Direction getDirection(){return heroNextStep;}

    /**
     * @return a new maze which is the current maze after a unit of time passing.
     */
    public Maze ping(){
        // TODO
        return null;
    }

    /**
     * finds location of a given tile (exactly the same object)
     * 
     * @param t tile object to find
     * @return loc of tile object if found, else null
     */
    public Loc findTileLoc(Tile t){
        for(int x = 0; x < width - 1; x++){
            for(int y = 0; y < height - 1; y++){
                if(t == tileArray[x][y]) return new Loc(x, y);
            }
        }
        return null;
    }

    /*
     * gets the tile at the given x and y co ordinates in the array
     */
    public Tile getTileAt(int x, int y){
        return tileArray[x - 1][y - 1];
    }

    /*
     * gets the tile at the given location in the array
     */
    public Tile getTileAt(Loc l){
        return tileArray[l.x() - 1][l.y() - 1];
    }

    /**
     * places a desired tile at a given location on maze.
     * 
     * //TODO CHECK IF WORKS
     * 
     * @param x of tile (0 to max - 1)
     * @param y of tile (0 to max - 1)
     * @param type enum for the tile type to place
     */
    public void setTileAt(int x, int y, TileType type){
        //check in bound
        if(x < 1 || y < 1 || y > height|| x > width) return;

        //make tile object from type enum
        Tile tile = type.getTileObject(new TileInfo(new Loc(x, y)));

        //if item or actor given put it on an empty tile if there is one, else return 
        if(tileArray[x][y].setOn(tile) == false) return;
            
        //replace the tile at the location
        tileArray[x - 1][y - 1] = type.getTileObject(new TileInfo(new Loc(x, y)));

    }

    public void makeHeroStep(Direction d){
        this.heroNextStep = d;
    }
}
