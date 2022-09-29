package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class DomainTests {

    //INITIAL TESTS:

    @Test
    public void testMazeToString() {
        Maze maze = new Maze(new Tile[][] {
            {new Wall(new TileInfo(null, null)), new Wall( new TileInfo(null, null)), new Wall(new TileInfo(null, null))},
            {new Wall(new TileInfo(null, null)), new Floor(new TileInfo(null, null)), new Wall(new TileInfo(null, null))},
            {new Wall(new TileInfo(null, null)), new Wall(new TileInfo(null, null)), new Wall(new TileInfo(null, null))}
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
        inv.addItem(new OrangeKey(new TileInfo(null)));
        inv.addItem(new BlueKey(new TileInfo(null))); 
        inv.addItem(new GreenKey(new TileInfo(null)));
        assertEquals("Inv(8): o, b, g", 
        inv.toString());
    }

    @Test
    public void testMazeCreation(){
        // no Moves to check maze initialisation
        String input = """
                0|/|/|/|/|/|/|/|/|/|/|
                1|/|_|_|_|_|_|_|_|_|/|
                2|/|_|_|_|_|_|_|_|_|/|
                3|/|_|o|O|X|_|_|_|_|/|
                4|/|_|g|G|_|_|_|_|_|/|
                5|/|_|y|Y|_|_|_|_|_|/|
                6|/|_|b|B|_|_|_|_|_|/|
                7|/|_|_|_|_|_|_|_|_|/|
                8|/|_|H|E|_|C|_|_|_|/|
                9|/|/|/|/|/|/|/|/|/|/|
                  0 1 2 3 4 5 6 7 8 9
                  """;
        String moves = "";
        String output = """
                0|/|/|/|/|/|/|/|/|/|/|
                1|/|_|_|_|_|_|_|_|_|/|
                2|/|_|_|_|_|_|_|_|_|/|
                3|/|_|o|O|X|_|_|_|_|/|
                4|/|_|g|G|_|_|_|_|_|/|
                5|/|_|y|Y|_|_|_|_|_|/|
                6|/|_|b|B|_|_|_|_|_|/|
                7|/|_|_|_|_|_|_|_|_|/|
                8|/|_|H|E|_|C|_|_|_|/|
                9|/|/|/|/|/|/|/|/|/|/|
                  0 1 2 3 4 5 6 7 8 9""";
        testHarnessValid(input, moves, output);
    }


    //HELPER METHODS:


    //=====================================//
    //========= Testing harnesses =========//
    //=====================================//

    public void testHarnessValid(String input, String moves, String output){
        Maze maze = mazeParser(input);
        doMoves(new Domain(List.of(maze), new Inventory(8), 1), moves);
        assertEquals(output, maze.toString());
    }

    public void testHarnessInvalid(String input, String moves, String output, Class<? extends Throwable> exception){
        Maze maze = mazeParser(input);
        assertThrows(exception, ()-> doMoves(new Domain(List.of(maze), new Inventory(8), 1), moves));
        assertEquals(output, maze.toString());
    }

    //==================================================//
    //========= Testing harness Helper Methods =========//
    //==================================================//

    /*
     * makes tile object from given character, 
     * with given info (e.g location x and y)
     */
    public Tile makeTile(char c, int x, int y){
        TileType  type = Arrays.stream(TileType.values())
        .filter(t -> TileType.makeTile(t, null).symbol() == c).findFirst().get();

        TileInfo info = new TileInfo(new Loc(x,y));
        return TileType.makeTile(type, info);
    }

    /*
     * does a sequece of moves on a given maze
     * 
     * moves are given as a string of characters (U, D, L, R)
     * separated by space.
     */
    public void doMoves(Domain domain, String sequence){
        Maze maze = domain.getCurrentMaze();
        // for each char in sequence
        for (char c : sequence.toCharArray()) {
            switch (c) {
                case 'U' -> maze.makeHeroStep(Direction.Up);
                case 'D' -> maze.makeHeroStep(Direction.Down);
                case 'L' -> maze.makeHeroStep(Direction.Left);
                case 'R' -> maze.makeHeroStep(Direction.Right);
                case ' ' -> maze.makeHeroStep(Direction.None);
                default -> throw new IllegalArgumentException("Invalid move: " + c);
            }
            domain.pingDomain();
            System.out.println(domain.getCurrentMaze().toString());
        }
    }

    /*
     * parses a specifc format of string into a maze object
     */
    public Maze mazeParser(String maze){
        //split into lines
        String[] rows = maze.split("\n");
        String[][] StringTiles = new String[rows.length-1][];
        //split each line into tokens
        for(int i = 0; i < rows.length-1; i++){
            StringTiles[i] = rows[i].split("\\|");
        }
        //make tiles from tokens
        Tile[][] tiles = new Tile[StringTiles.length][StringTiles[0].length-1];
        for(int y = 0; y < StringTiles.length; y++){
            for(int x = 0; x < StringTiles[0].length-1; x++){               
                tiles[x][y] = makeTile(StringTiles[y][x+1].charAt(0), x, y);
            }
        }

        return new Maze(tiles);
    }
}
