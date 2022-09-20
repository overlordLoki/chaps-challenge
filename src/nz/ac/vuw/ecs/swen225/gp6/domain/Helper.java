package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
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
                gameArray[i][j] = new Tile(TileType.Floor, info);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //make the walls
                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    gameArray[i][j] = new Tile(TileType.Wall, info);
                }
            }
        }
        
        Maze m = new Maze(gameArray);

        //display different tile types
        m.setTileAt(new Loc(2, 2), TileType.Hero, a ->{});
        m.setTileAt(new Loc(4, 2), TileType.Enemy, a ->{});
        m.setTileAt(new Loc(3, 6), TileType.GreenKey, a ->{});
        m.setTileAt(new Loc(4, 6), TileType.GreenLock, a ->{});
        m.setTileAt(new Loc(3, 5), TileType.YellowKey, a ->{});
        m.setTileAt(new Loc(4, 5), TileType.YellowLock, a ->{});
        m.setTileAt(new Loc(3, 4), TileType.BlueKey, a ->{});
        m.setTileAt(new Loc(4, 4), TileType.BlueLock, a -> {});
        m.setTileAt(new Loc(3, 7), TileType.OrangeKey, a ->{});
        m.setTileAt(new Loc(4, 7), TileType.OrangeLock, a -> {});
        m.setTileAt(new Loc(6, 2), TileType.Coin, a -> {});
        m.setTileAt(new Loc(5, 7), TileType.ExitDoor, a -> {});
        
    

        return m;
    }
}
