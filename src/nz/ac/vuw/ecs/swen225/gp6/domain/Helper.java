package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

//TODO: temp class REMOVE
public class Helper {
    public static Maze makeMaze(){
        int width = 10;
        int height = 10;
        Tile [][] gameArray = new Tile[height][width];

        //initialize the maze with Empty_tile. cant have null tiles
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gameArray[i][j] = new Floor(new TileInfo(new Loc(i, j)));
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //make the walls
                if(i == 0 || j == 0 || i == width - 1 || j == height - 1){
                    gameArray[i][j] = new Wall(new TileInfo(new Loc(i, j)));
                }
            }
        }
        
        Maze m = new Maze(gameArray);

        //display different tile types
        m.setTileAt(new Loc(2, 1), TileType.Hero);
        m.setTileAt(new Loc(3, 1), TileType.Enemy);
        m.setTileAt(new Loc(2, 5), TileType.GreenKey);
        m.setTileAt(new Loc(3, 5), TileType.GreenLock);
        m.setTileAt(new Loc(2, 4), TileType.YellowKey);
        m.setTileAt(new Loc(3, 4), TileType.YellowLock);
        m.setTileAt(new Loc(2, 3), TileType.BlueKey);
        m.setTileAt(new Loc(3, 3), TileType.BlueLock);
        m.setTileAt(new Loc(2, 6), TileType.OrangeKey);
        m.setTileAt(new Loc(3, 6), TileType.OrangeLock);
        m.setTileAt(new Loc(5, 1), TileType.Coin);
        m.setTileAt(new Loc(4, 6), TileType.ExitDoor);
        
    

        return m;
    }
}
