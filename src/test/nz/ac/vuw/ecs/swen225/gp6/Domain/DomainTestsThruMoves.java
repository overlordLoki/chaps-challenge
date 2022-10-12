package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.AbstractTile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import junit.framework.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class DomainTests {
    //mock maze and domain for testing
    public static Maze mockMaze = mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|$|_|/|X|_|/|_|$|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|$|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    public static Domain mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1);

    //INITIAL TESTS:

    @Test
    public void testMazeToString() {
        Maze maze = new Maze(new Tile[][] {
            {new Wall(new TileInfo(null)), new Wall( new TileInfo(null)), new Wall(new TileInfo(null))},
            {new Wall(new TileInfo(null)), new Floor(new TileInfo(null)), new Wall(new TileInfo(null))},
            {new Wall(new TileInfo(null)), new Wall(new TileInfo(null)), new Wall(new TileInfo(null))}
        });
        assertEquals(
            "0|/|/|/|\n" + 
            "1|/|_|/|\n" +
            "2|/|/|/|\n" +
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
        // no Moves to check maze creation from string form
        String input = """
                0|/|/|/|/|/|/|/|/|/|/|
                1|/|_|_|_|_|_|_|_|_|/|
                2|/|_|_|_|_|_|_|_|_|/|
                3|/|_|o|O|X|_|_|_|_|/|
                4|/|_|g|G|_|_|_|_|_|/|
                5|/|_|y|Y|_|_|_|_|_|/|
                6|/|_|b|B|_|_|_|_|_|/|
                7|/|_|_|_|_|_|_|_|_|/|
                8|/|_|H|E|_|$|_|_|_|/|
                9|/|/|/|/|/|/|/|/|/|/|
                  0 1 2 3 4 5 6 7 8 9""";
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
                8|/|_|H|E|_|$|_|_|_|/|
                9|/|/|/|/|/|/|/|/|/|/|
                  0 1 2 3 4 5 6 7 8 9""";
        testHarnessValid(input, moves, output);
    }

    @Test 
    public void testSimpleMovement(){
        String input = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|_|_|$|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|_|_|/|_|_|/|/|/|/|
            4|/|_|_|_|_|_|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|_|/|
            7|/|_|_|_|H|_|/|_|_|/|
            8|/|$|_|_|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";
        String moves = "DRUUUULL";
        String output = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|_|_|$|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|_|_|/|_|_|/|/|/|/|
            4|/|_|_|H|_|_|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|_|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|$|_|_|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";;
        testHarnessValid(input, moves, output);
    }

    @Test 
    public void testSimpleMovingEnemy(){
        //Tile t = new Enemy(new TileInfo(null));TODO OOOOOO
    }

    @Test 
    public void testAllKeysAndLocks(){
        String input = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|/|_|$|/|
            2|/|_|_|/|_|_|O|_|_|/|
            3|/|/|B|/|_|_|/|/|/|/|
            4|/|_|_|_|g|b|_|_|_|/|
            5|/|_|_|_|o|y|_|_|_|/|
            6|/|/|/|/|_|_|/|G|/|/|
            7|/|_|_|Y|H|_|/|_|_|/|
            8|/|$|_|/|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";
        String moves = "UUURDDDLLLRRRUURRDDUULLUUURRLLDDLLLUU";
        String output = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|/|_|$|/|
            2|/|_|H|/|_|_|_|_|_|/|
            3|/|/|_|/|_|_|/|/|/|/|
            4|/|_|_|_|_|_|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|$|_|/|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";

        testHarnessValid(input, moves, output);
    }

    @Test 
    public void testPickingCoins(){
        String input = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|/|_|$|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|/|B|/|H|_|/|/|/|/|
            4|/|_|_|_|_|b|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|G|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|$|_|/|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";
        String moves = "RURRRULDLLLDDDDDLLLDR";
        String output = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|/|_|_|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|/|B|/|_|_|/|/|/|/|
            4|/|_|_|_|_|b|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|G|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|_|H|/|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";

        testHarnessValid(input, moves, output); 

        Coin c = new Coin(new TileInfo(new Loc(0, 0)));
        assertThrows(IllegalArgumentException.class,
        ()->{c.setOn(new Wall(new TileInfo(new Loc(0, 0))), this.mockDomain);});
        assertThrows(NullPointerException.class,
        ()->{c.setOn(new Hero(new TileInfo(new Loc(0, 0))), null);});
        assertThrows(NullPointerException.class,
        ()->{c.setOn(null, this.mockDomain);});

        Inventory invBroken = new Inventory(8){ //doesn't add coin
            @Override
            public void addCoin(){}
        };
        Domain domainBroken = new Domain(mockDomain.getMazes(), invBroken, 1);
        assertThrows(AssertionError.class, ()->{c.setOn(new Hero(new TileInfo(new Loc(0, 0))), domainBroken);});
    }

    @Test
    public void testCollectAllCoins(){
        String input = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|/|_|$|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|/|_|/|_|_|/|/|/|/|
            4|/|_|_|_|_|_|_|_|_|/|
            5|/|_|_|_|H|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|$|_|/|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
            0 1 2 3 4 5 6 7 8 9""";
        String moves = "ULLUUULRDDDRRDDDLLLDURRRRUURRDDDRLUUULLLUUURRRRULDL";
        String output = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|_|_|/|Z|_|/|_|_|/|
            2|/|_|_|/|_|_|H|_|_|/|
            3|/|/|_|/|_|_|/|/|/|/|
            4|/|_|_|_|_|_|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|_|_|/|_|_|/|_|_|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";

        testHarnessValid(input, moves, output); 
    }

    @Test
    public void testExitDoorAndWalls(){
        String input = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|$|_|/|X|_|/|_|$|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|/|_|/|_|_|/|/|/|/|
            4|/|_|_|_|_|_|_|_|_|/|
            5|/|_|_|_|H|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|$|_|/|_|_|/|_|$|/|
            9|_|/|/|/|/|/|/|/|/|_|
            0 1 2 3 4 5 6 7 8 9""";
        String moves = "UUUUUUUUUUUUUUUUUUUUUUUUUUURULLLLLLLLLLLDRRRUUUUUUUUUUUULLLLLLLDLLDDDRR"+
        "DDDLLLLLLLLLLLLLRLUUULLLDDLLLDUUUUUUUUUUUUUURRRUUULLLLLLLLLLLLLLLRUUULRDDDRRUUU";
        String output = """
            0|_|/|/|/|/|/|/|/|/|_|
            1|/|_|_|/|Z|_|/|_|_|/|
            2|/|_|_|/|_|_|_|_|_|/|
            3|/|/|_|/|_|_|/|/|/|/|
            4|/|_|_|_|_|_|_|_|_|/|
            5|/|_|_|_|_|_|_|_|_|/|
            6|/|/|/|/|_|_|/|_|/|/|
            7|/|_|_|_|_|_|/|_|_|/|
            8|/|_|_|/|_|_|/|_|_|/|
            9|_|/|/|/|/|/|/|/|/|_|
              0 1 2 3 4 5 6 7 8 9""";

        testHarnessValid(input, moves, output); 
    }
    

    @Test
    public void testDirectionTransformLoc(){
        assertEquals( new Loc(0,2), Direction.Down.transformLoc(new Loc(0, 1)) );
        assertEquals( new Loc(0, 0), Direction.Up.transformLoc(new Loc(0,1)) );
        assertEquals( new Loc(0,0), Direction.Left.transformLoc(new Loc(1,0)) );
        assertEquals( new Loc(1,0), Direction.Right.transformLoc( new Loc(0,0)) );
    }

    @Test
    public void testDirectionFromSymbol(){
        assertEquals(Direction.Down, Direction.getDirFromSymbol('D'));
        assertEquals(Direction.Left, Direction.getDirFromSymbol('L') );
        assertEquals(Direction.Right, Direction.getDirFromSymbol('R') );
        assertEquals(Direction.Up, Direction.getDirFromSymbol('U') );
    }

    @Test 
    public void checkAllMethodsInLoc(){
    
        Loc loc = new Loc(1,1);
        Loc loc2 = new Loc(1,1); // the same thing as loc
        Loc loc3 = new Loc(2,2); // different to loc
        Loc loc4 = new Loc(1, 2); 

        assertEquals(1, loc.x() );
        assertEquals(1, loc.y() );

        assertEquals(true, loc.equals(loc2) );
        assertEquals(false, loc.equals(loc3) );
        assertEquals(false, loc.equals(loc4) );
        assertEquals(false, loc.equals(new Wall(new TileInfo(loc)) ) ); // loc is not a wall!
        assertEquals(true, loc.hashCode() == loc2.hashCode() );

        loc.x(2);
        loc.y(2);
        assertEquals(true, loc.equals(loc3));

        assertThrows(IllegalArgumentException.class, ()->{loc.x(-1);});
        assertThrows(IllegalArgumentException.class, ()->{loc.y(-1);});
        assertThrows(IllegalArgumentException.class, ()->{Loc l = new Loc(0, -1);});
        assertThrows(IllegalArgumentException.class, ()->{Loc l = new Loc(-1, 0);});

        assertEquals(false, Loc.checkInBound(new Loc(0, 100), new Maze(new Tile[4][4])));
        assertEquals(false, Loc.checkInBound(new Loc(100, 0), new Maze(new Tile[4][4])));
        assertEquals(true, Loc.checkInBound(new Loc(1, 1), new Maze(new Tile[4][4]) ));


    }

    @Test 
    public void checkAllClassesInTileAnatomy(){
        //Abstract tile
        assertThrows(NullPointerException.class, () ->{
        Tile t = new AbstractTile(null) {
            @Override
            public TileType type() {
                return TileType.Other;
            }
        };});

        //Tile interface


    }


    //HELPER METHODS:

    //=====================================//
    //========= Testing harnesses =========//
    //=====================================//

    /**
     * given a maze string as input, and another as output, and a sequence of moves,
     * it checks wether after applying these moves on the input maze the a maze equivalent to the output maze
     * is reached.
     * 
     * @param input a string representing initial maze
     * @param moves a string representing sequence of moves
     * @param output a string representing expected final maze
     */
    public static void testHarnessValid(String input, String moves, String output){
        Maze maze = mazeParser(input);
        doMoves(new Domain(List.of(maze), new Inventory(8), 1), moves);
        assertEquals(output, maze.toString());
    }

    /**
     * TODO 
     * @param input
     * @param moves
     * @param output
     * @param exception
     */
    public static void testHarnessInvalid(String input, String moves, String output, Class<? extends Throwable> exception){
        Maze maze = mazeParser(input);
        assertThrows(exception, ()-> doMoves(new Domain(List.of(maze), new Inventory(8), 1), moves));
        assertEquals(output, maze.toString());
    }

    //==================================================//
    //========= Testing harness Helper Methods =========//
    //==================================================//

    /**
     * makes tile object from given character, 
     * with given info (e.g location x and y) 
     * 
     * @param c the character to make a tile from
     * @param x co ord of tile
     * @param y co ord of tile
     */
    public static Tile makeTile(char c, int x, int y){
        try{
            TileType  type = Arrays.stream(TileType.values())
            .filter(t -> TileType.makeTile(t, new TileInfo(null)).symbol() == c).findFirst().get();
            TileInfo info = new TileInfo(new Loc(x,y));
            return TileType.makeTile(type, info);
        } catch (Exception e){
            System.out.println(e.getMessage() + c);
            return new Null(new TileInfo(null));
        }
    }

    /**
     * does a sequece of moves on a given maze
     * 
     * moves are given as a string of characters (U, D, L, R)
     * separated by space.
     * 
     * @param domain used to do moves on
     * @param sequence of string of moves in format (L, U, R, D) separated by space or nothing
     */
    public static void doMoves(Domain domain, String sequence){
        Level level = domain.getCurrentLevelObject();
        // for each char in sequence
        for (char c : sequence.toCharArray()) {
            level.makeHeroStep(Direction.getDirFromSymbol(c));
            domain.getCurrentMaze().pingMaze(domain);
        }
    }

    /**
     * parses a specifc format of string into a maze object
     * 
     * @param string format of the maze
     * @return maze object extracted from this string
     */
    public static Maze mazeParser(String maze){
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
                tiles[x][y] = TileType.makeTileFromSymbol
                (StringTiles[y][x+1].charAt(0), new TileInfo(new Loc(x, y)));
            }
        }

        return new Maze(tiles);
    }
}
