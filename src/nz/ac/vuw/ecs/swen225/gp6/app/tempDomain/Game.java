package nz.ac.vuw.ecs.swen225.gp6.app.tempDomain;

import nz.ac.vuw.ecs.swen225.gp6.renderer.tempDomain.Tiles.*;

import java.util.List;


public class Game {

    public void moveUp(){
        System.out.println("Player moved up");
    }

    public void moveDown(){
        System.out.println("Player moved down");
    }

    public void moveLeft(){
        System.out.println("Player moved left");
    }

    public void moveRight(){
        System.out.println("Player moved right");
    }

    public int getCurrentLevel() {
        return 1;
    }

    public int getTreasuresLeft() {
        return 12;
    }

    public List<Tile> getInventory() {
        // currently just a fixed list of 4 different keys
        return List.of(new yellowKey(), new greenKey(), new blueKey(), new orangeKey());
    }
}
