package Domain;

import java.util.stream.IntStream;

public class Maze {
    private Tile[][] tileArray;
    private int height; //height of tile array, how many rows (outer array) 
    private int width;  //width of tile array, how many columns (inner arrays)

    private Inventory inv;
    

    public Maze(Tile[][] tileArray, Inventory inv){
        this.tileArray = tileArray;
        this.inv = inv;
        this.height = tileArray.length;
        this.width = tileArray[0].length;
    }

    /*
     * @return a copy of tile array 
     */
    public Tile[][] getTileArray() {
        Tile[][] copy = new Tile[tileArray.length][tileArray[0].length];
        IntStream.range(0, height - 1)
        .forEach( y ->
            IntStream.range(0, width - 1)
            .forEach(x -> copy[y][x] = tileArray[y][x].getName().getTileObject(new TileInfo(new Loc(x, y))))
        );
        return copy;
    }

    /*
     * get height of tile array
     */
    public int height(){
        return height;
    }

    /*
     * get width of tile array
     */
    public int width(){
        return width;
    }

    /**
     * 
     * @return the inventory of the player
     */
    public Inventory getInv(){
        return inv;
    }

    /**
     * @return a new maze which is the current maze after a unit of time passing.
     */
    public Maze ping(){
        // TODO
        return null;
    }

    /**
     * finds location of a given tile (exactly the same object)
     * @param t tile object to find
     * @return loc of tile object if found, else null
     */
    public Loc findTileLoc(Tile t){
        for(int y = 0; y < height - 1; y ++){
            for(int x = 0; x < width - 1; x++){
                if(t == tileArray[y][x]) return new Loc(x, y);
            }
        }
        return null;
    }

    /**
     * places a desired tile at a given location on maze.
     * 
     * TODO make shorter ??
     * 
     * @param x of tile (0 to max - 1)
     * @param y of tile (0 to max - 1)
     * @param type enum for the tile type to place
     */
    public void setTileArray(int x, int y, TileType type){
        //check in bound
        if(x < 1 || y < 1 || y > height|| x > width) return;

        //make tile object from type enum
        Tile tile = type.getTileObject(new TileInfo(new Loc(x, y)));

        //if item or actor given put it on an empty tile if there is one, else return 
        if(tile instanceof Actor){
            if(tileArray[y][x] instanceof EmptyTile){
                ((EmptyTile)tileArray[y][x]).setActorOn((Actor)tile);
            }
            return;
        }

        if(tile instanceof Item){
            if(tileArray[y][x] instanceof EmptyTile){
                ((EmptyTile)tileArray[y][x]).setItemOn((Item)tile);
            }
        }

        //replace the tile at the location
        tileArray[x][y] = type.getTileObject(new TileInfo(new Loc(x, y)));

    }
}
