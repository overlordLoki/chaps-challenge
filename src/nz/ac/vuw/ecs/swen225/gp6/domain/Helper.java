package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileGrouping.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

//TODO: temp class REMOVE
public class Helper {
    public static Maze makeMaze(){
        int width = 10;
        int height = 10;
        TileInfo info = new TileInfo(new Loc(0, 0), a ->{});
        Tile [][] gameArray = new Tile[height][width];

        //initialize the maze with Empty_tile. cant have null tiles
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameArray[i][j] = TileType.Floor.makeTileObject(info);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //make the walls
                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    gameArray[i][j] = TileType.Wall.makeTileObject(info);
                }
            }
        }


        //display different tile types
        gameArray[2][2] = TileType.Hero.makeTileObject(info);
        gameArray[4][2] = TileType.Enemy.makeTileObject(info);
        gameArray[3][6] = TileType.GreenKey.makeTileObject(info);
        gameArray[4][6] = TileType.GreenLock.makeTileObject(info);
        //gameArray[3][5] = TileType.YellowKey.makeTileObject(info);
        //gameArray[4][5] = new yellowLock();
        gameArray[3][4] = TileType.BlueKey.makeTileObject(info);
        gameArray[4][4] = TileType.BlueLock.makeTileObject(info);
        gameArray[3][7] = TileType.OrangeKey.makeTileObject(info);
        gameArray[4][7] = TileType.OrangeLock.makeTileObject(info);
        gameArray[6][2] = TileType.Coin.makeTileObject(info);
        gameArray[5][7] = TileType.ExitDoor.makeTileObject(info);
        
        
        Maze m = new Maze(gameArray);

        return m;
    }
}
