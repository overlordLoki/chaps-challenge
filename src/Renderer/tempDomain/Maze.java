package Renderer.tempDomain;

import Renderer.tempDomain.Tiles.*;

public class Maze {
    
    private Tile[][] gameArray;

    public Tile[][] getGameArray() {
        return gameArray;
    }

    public Maze() {
        int width = 10;
        int height = 10;
        gameArray = new Tile[width][height];
        //initialize the maze with Empty_tile. cant have null tiles
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameArray[i][j] = new Empty_tile();
            }
        }
        //added chap for testing
        gameArray[0][0] = new Chap();
    }
}
