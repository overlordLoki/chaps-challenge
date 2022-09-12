package nz.ac.vuw.ecs.swen225.gp6.renderer.tempDomain;

import nz.ac.vuw.ecs.swen225.gp6.renderer.tempDomain.Tiles.*;

public class Maze {
    
    private Tile[][] gameArray;

    public Tile[][] getGameArray() {
        return gameArray;
    }

        /*
         *  |w|w|w|w|w|w|w|w|w|w|
         *  |w| | | | | | | | |w|
         *  |w| |H| |E| |C| | |w|
         *  |w| | | | | | | | |w|
         *  |w| |BK|BL| | | | | |w|
         *  |w| |YK|YL| | | | | |w|
         *  |w| |GK|GL| | | | | |w|
         *  |w| |OK|OL| |X| | | |w|
         *  |w| | | | | | | | |w|
         *  |w|w|w|w|w|w|w|w|w|w|
         */

    public Maze() {
        int width = 10;
        int height = 10;
        gameArray = new Tile[width][height];
        //initialize the maze with Empty_tile. cant have null tiles
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameArray[i][j] = new Floor();
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //make the walls
                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    gameArray[i][j] = new wall_tile();
                }
            }
        }
        gameArray[2][2] = new hero();
        gameArray[3][6] = new greenKey();
        gameArray[4][6] = new greenLock();
        gameArray[3][5] = new yellowKey();
        gameArray[4][5] = new yellowLock();
        gameArray[3][4] = new blueKey();
        gameArray[4][4] = new blueLock();
        gameArray[3][7] = new orangeKey();
        gameArray[4][7] = new orangeLock();
        gameArray[4][2] = new enemy();
        gameArray[6][2] = new coin();
        gameArray[5][7] = new exitDoor();
    }

}
