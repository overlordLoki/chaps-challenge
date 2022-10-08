package nz.ac.vuw.ecs.swen225.gp6.renderer;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Hero;

/*
 * change the array to be centered on the player
 * @Author Loki
 */
public class Viewport {

    //find the Hero in the Tile[][] and return its position
    /**
     * find the Hero in the Tile[][] and return its position
     * @param gameArray
     * @return int[]
     */
    public static int[] findHero(Tile[][] gameArray) {
        int[] heroPos = new int[2];
        for(int i = 0; i < gameArray.length; i++) {
            for(int j = 0; j < gameArray[i].length; j++) {
                if(gameArray[i][j] instanceof Hero) {
                    heroPos[0] = i;
                    heroPos[1] = j;
                    return heroPos;
                }
            }
        }
        return heroPos;
    }
    
    /**
     * get a viewport for the current game array
     * @param gameArray
     * @param renderSize
     * @return
     */
    public static Tile[][] getViewport(Tile[][] gameArray, int renderSize) {
        int[] heroPos = findHero(gameArray);
        int heroX = heroPos[0];
        int heroY = heroPos[1];
        int xStart = heroX - renderSize/2;
        int yStart = heroY - renderSize/2;
        Tile[][] viewport = new Tile[renderSize][renderSize];
        Tile periphery = TileType.makeTile(TileType.Periphery, new TileInfo(null));
        for(int i = 0; i < renderSize; i++) {
            for(int j = 0; j < renderSize; j++) {
                if(xStart + i < 0 || xStart + i >= gameArray.length || yStart + j < 0 || yStart + j >= gameArray[0].length) {
                    viewport[i][j] = periphery;
                } else {
                    viewport[i][j] = gameArray[xStart + i][yStart + j];
                }
            }
        }
        return viewport;
    }

}
