package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;



public class DomainTests {

    //INITIAL TESTS:
	@Test
    public void testMazeToString() {
        Maze maze = new Maze(new Tile[][] {
            {new Tile(TileType.Wall, new TileInfo(null, null)), new Tile(TileType.Wall, new TileInfo(null, null)), new Tile(TileType.Wall, new TileInfo(null, null))},
            {new Tile(TileType.Wall, new TileInfo(null, null)), new Tile(TileType.Floor, new TileInfo(null, null)), new Tile(TileType.Wall, new TileInfo(null, null))},
            {new Tile(TileType.Wall, new TileInfo(null, null)), new Tile(TileType.Wall, new TileInfo(null, null)), new Tile(TileType.Wall, new TileInfo(null, null))}
        });
        assertEquals(
            "0|||||||\n" + 
            "1|||_|||\n" +
            "2|||||||\n" +
            "  0 1 2" + 
            "",
            maze.toString()
        );
    }

    @Test
    public void testInventoryToString(){
        Inventory inv = new Inventory(8);
        inv.addItem(tile(TileType.OrangeKey));
        inv.addItem(tile(TileType.BlueKey)); 
        inv.addItem(tile(TileType.GreenKey));
        assertEquals("Inv(8): o, b, g", 
        inv.toString());
    }



    //HELPER METHODS:
    /*
     * returns a tile with no consumer and location
     */
    public Tile tile(TileType type){
        return new Tile(type, new TileInfo(null, null));
    }

}
