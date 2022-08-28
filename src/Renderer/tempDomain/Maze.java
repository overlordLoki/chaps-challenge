package Renderer.tempDomain;

import Renderer.tempDomain.Tiles.*;

public class Maze {
    
    private Tile[][] gameArray;

    public Tile[][] getGameArray() {
        return gameArray;
    }

    public Maze(int width, int height) {
        gameArray = new Tile[width][height];
        
    }
}
