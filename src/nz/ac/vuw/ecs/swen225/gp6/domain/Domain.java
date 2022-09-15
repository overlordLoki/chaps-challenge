package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.persistency.*;

public class Domain {
    public static Maze makeMaze(){
        int width = 10;
        int height = 10;
        TileInfo info = new TileInfo(new Loc(0, 0));
        Tile [][] gameArray = new Tile[height][width];

        //initialize the maze with Empty_tile. cant have null tiles
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameArray[i][j] = TileType.Floor.getTileObject(info);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //make the walls
                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    gameArray[i][j] = TileType.Wall.getTileObject(info);
                }
            }
        }


        //display different tile types
        gameArray[2][2] = TileType.Hero.getTileObject(info);
        gameArray[4][2] = TileType.Enemy.getTileObject(info);
        gameArray[3][6] = TileType.GreenKey.getTileObject(info);
        gameArray[4][6] = TileType.GreenLock.getTileObject(info);
        //gameArray[3][5] = TileType.YellowKey.getTileObject(info);
        //gameArray[4][5] = new yellowLock();
        gameArray[3][4] = TileType.BlueKey.getTileObject(info);
        gameArray[4][4] = TileType.BlueLock.getTileObject(info);
        gameArray[3][7] = TileType.OrangeKey.getTileObject(info);
        gameArray[4][7] = TileType.OrangeLock.getTileObject(info);
        gameArray[6][2] = TileType.Coin.getTileObject(info);
        gameArray[5][7] = TileType.ExitDoor.getTileObject(info);
        
        
        Maze m = new Maze(gameArray, new Inventory(8), 1);

        return m;
    }
}