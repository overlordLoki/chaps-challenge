package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class DomainTestsThruMoves {
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
                8|/|_|H|_|_|$|_|_|_|/|
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
                8|/|_|H|_|_|$|_|_|_|/|
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

    //TEST TILES:
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
     * tests if a given input and a given sequence of moves results
     * in a generic exception, and a final maze equivalent to the
     * input.(CURRENTLY NOT USED but kept due to intense usefulness)
     * 
     * @param input a string representing initial maze
     * @param moves a string representing sequence of moves
     * @param output a string representing expected final maze
     * @param exception type of expected exception
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
        return TileType.makeTileFromSymbol(c, new TileInfo(new Loc(x,y)));
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
