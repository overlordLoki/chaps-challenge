package Renderer.tempDomain;

import Renderer.tempDomain.Tiles.*;

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
                gameArray[i][j] = new Empty_tile();
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //make the walls
                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    gameArray[i][j] = new wall_tile();
                }
                //make hero at 2,2
                if(i == 2 && j == 2){
                    gameArray[i][j] = new hero();
                }
                //greenKey and GreenLock at 3,6 and 4,6
                if(i == 3 && j == 6){
                    gameArray[i][j] = new greenKey();
                }
                if(i == 4 && j == 6){
                    gameArray[i][j] = new greenLock();
                }
                //yellowKey and YellowLock at 3,5 and 4,5
                if(i == 3 && j == 5){
                    gameArray[i][j] = new yellowKey();
                }
                if(i == 4 && j == 5){
                    gameArray[i][j] = new yellowLock();
                }
                //blueKey and BlueLock at 3,4 and 4,4
                if(i == 3 && j == 4){
                    gameArray[i][j] = new blueKey();
                }
                if(i == 4 && j == 4){
                    gameArray[i][j] = new blueLock();
                }
                //orangeKey and orangeLock at 3,7 and 4,7
                if(i == 3 && j == 7){
                    gameArray[i][j] = new orangeKey();
                }
                if(i == 4 && j == 7){
                    gameArray[i][j] = new orangeLock();
                }
                //enemy at 4,2
                if(i == 4 && j == 2){
                    gameArray[i][j] = new enemy();
                }
                //coin at 6,2
                if(i == 6 && j == 2){
                    gameArray[i][j] = new coin();
                }
                //exitDoor at 5,7
                if(i == 5 && j == 7){
                    gameArray[i][j] = new exitDoor();
                }
            }
        }
    }
}
