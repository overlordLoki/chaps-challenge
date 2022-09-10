package Domain;

public class Maze {
    private Tile[][] tileArray;
    private Inventory inv;

    /*
     * @return the tile array (DO: perhaps it can be made unmodifeable for future)
     */
    public Tile[][] getTileArray() {
        return tileArray;
    }

    /**
     * 
     * @return the inventory of the player
     */
    public Inventory getInv(){
        return inv;
    }

    /**
     * places a desired tile at a given location on maze.
     * 
     * @param x of tile (0 to max - 1)
     * @param y of tile (0 to max - 1)
     * @param name enum for the tile type to place
     */
    public void setTileArray(int x, int y, TileName name){
        //check in bound
        if(x < 0 || y < 0 || y > tileArray.length || x > tileArray[0].length) return;

        //replace the tile at the location
        tileArray[x][y] = name.getTileObject();
    }
}
