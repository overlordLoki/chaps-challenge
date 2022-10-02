package nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck;

import java.util.List;
import java.util.function.Predicate;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;


/*
 * This class contains a set of static method to check the integrity of 
 * the game:
 *  - before a ping step is successfully completed (by comparing the previous maze and inv to next ones)
 *  - after a ping (by looking at the new altered domain, and making sure certain rules are always followed)
 */
public final class CheckGame {
    public static boolean gameHasEnded;
    
    public static void checkStateChange(Maze preMaze, Inventory preInv, Maze afterMaze, Inventory afterInv){
        //make a mock pre and after domain //TODO: make sure this works
        Domain preDomain = new Domain(List.of(preMaze), preInv, 1);
        Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

        //if the game has ended, then do end game checks and return TODO BREAKS SOMETIMES
        if(gameHasEnded){
            return;
        }

        //HERO:
        checkHeroStateChange(preMaze, preInv, afterMaze, afterInv, preDomain);

        //ENEMY:
        checkEnemyStateChange(preMaze, afterMaze, preDomain);

        //COINS:
        //check there is the same amounts of coins in the maze and inventory combined before and after
        int numCoinsBefore = getAllTiles(preMaze, TileType.Coin).size() + preInv.coins();
        int numCoinsAfter = getAllTiles(afterMaze, TileType.Coin).size() + afterInv.coins();
        if(numCoinsBefore !=numCoinsAfter){
            throw new IllegalStateException("There were a total of " + numCoinsBefore
             + " coins in game before, but now there are " + numCoinsAfter);
        }
        
    }

    public static void checkCurrentState(Maze maze, Inventory inv){
        //if the game has ended, then do end game checks and return 
        if(gameHasEnded){
            return;
        }

        //HERO:
        //check if hero is on maze, and there is exactly one hero on the maze
        if(getTile(maze,TileType.Hero) instanceof Hero == false){
            throw new IllegalStateException("Hero is not on maze");
        } 

        //COIN:
        //check that there is atleast 1 coin in total in game
        if(getAllTiles(maze,TileType.Coin).size() + inv.coins() <= 1){
            throw new IllegalStateException("There are no(or negative) coins in game");
        }

        //check door is closed if coins left on maze, open otherwise
        Tile closedDoor = getTile(maze, TileType.ExitDoor);
        Tile openDoor = getTile(maze, TileType.ExitDoorOpen);
        if(getAllTiles(maze,TileType.Coin).size() == 0){
            if(closedDoor instanceof Null == false||openDoor instanceof Null){
                throw new IllegalStateException("The exit door is not open, but there are no coins on maze.");
            }
        }
        else{
            if(closedDoor instanceof Null ||openDoor instanceof Null == false){
                throw new IllegalStateException("The exit door is open, but there are coins left on maze.");
            }
        }
        
    }

    //CHECKER HELPERS:
    /*
     * checks the integrity of enemies as the state of game changes
     */
    private static void checkEnemyStateChange(Maze preMaze, Maze afterMaze, Domain preDomain) {
        //check if enemies havent moved on obstructions
        List<Tile> enemies = getAllTiles(afterMaze, TileType.Enemy);
        for(Tile e : enemies){
            Loc enemyNewLoc = e.info().loc();
            Tile tileToOccupyEnemy = preMaze.getTileAt(enemyNewLoc);
            if(tileToOccupyEnemy.obstructsEnemy(preDomain)){
                throw new IllegalStateException("Enemy has moved on an obstruction: " 
                + tileToOccupyEnemy.type().name());
            }
        }

        //check if enemies arent out of bounds
        for(Tile e : enemies){
            if( Loc.checkInBound(e.info().loc(), afterMaze) == false ||
                ((Enemy)e).tileOn().type() == TileType.Periphery){
                throw new IllegalStateException("Enemy has moved out of bounds");
            }
        }

        //check number of enemies is the same (NOTE: unless they can die, then this check must change- future feature perhaps)
        if(getAllTiles(preMaze,TileType.Enemy).size() != 
        getAllTiles(afterMaze, TileType.Enemy).size()){
            throw new IllegalStateException("Number of enemies has changed");
        }
    }

    /*
     * checks the integrity of hero as the state of game changes
     */
    private static void checkHeroStateChange(Maze preMaze, Inventory preInv, Maze afterMaze, Inventory afterInv,
            Domain preDomain) {
        
        Hero h = (Hero) getTile(afterMaze, TileType.Hero);
        Loc heroNewLoc = h.info().loc();
        Tile tileToOccupy = preMaze.getTileAt(heroNewLoc);
        if(tileToOccupy instanceof Hero)return; //if hero hasn't moved then no need to check

        //check if hero is moved on an obstruction
        if(tileToOccupy.obstructsHero(preDomain)){
            throw new IllegalStateException("Hero has moved on an obstruction: " 
            + tileToOccupy.type().name());
        }

        //check hero isn't out of bounds
        if( Loc.checkInBound(h.info().loc(), afterMaze) == false ||
            h.tileOn().type() == TileType.Periphery){
            throw new IllegalStateException("Hero has moved out of bounds");
        }

        //check onTile field is replaced with tile's replaceWith method that hero is on
        if(tileToOccupy.replaceWith().type() !=  h.tileOn().type()){
            throw new IllegalStateException("Hero has the wrong onTile field");
        }

        //check if hero in moved on a door that needs a key, correct key is in and removed from inventory
        if(tileToOccupy instanceof Door && ((Door)tileToOccupy).color() != KeyColor.NONE){
            KeyColor doorColor = ((Door) tileToOccupy).color();
            List<Tile> items = preInv.getItems();
            Predicate<Tile> p = i -> i instanceof Key && ((Key) i).color() == doorColor;

            if(items.stream().noneMatch(p)){
                throw new IllegalStateException("Hero has moved on a door without having the correctly colored key");
            }

            if(preInv.countItem(p) != afterInv.countItem(p) + 1){
                throw new IllegalStateException("The correct key has not been removed from the inventory");
            }
        }
        //check if hero moved on an item(not a coin), it has been added to the inventory(if inventory is not full)
        if(tileToOccupy instanceof Item && !(tileToOccupy instanceof Coin)){
            if(preInv.countItem(i -> i.type() == tileToOccupy.type()) != 
            afterInv.countItem(i -> i.type() == tileToOccupy.type()) - 1){
                throw new IllegalStateException("The item has not been added to the inventory");
            }
        }

        //check if hero moved on a coin, coin is added to inventory
        if(tileToOccupy instanceof Coin && preInv.coins()!= afterInv.coins() - 1){
            throw new IllegalStateException("The coin has not been added to the inventory");
        }

        //check previous tile is not item(picakble) tile, i.e check item disappears (if inventory is not full)
        Tile tileToLeave = afterMaze.getTileAt(getTile(preMaze,TileType.Hero).info().loc()); //tile left behind
        if(tileToLeave instanceof Item
        && preInv.isFull() == false){
            throw new IllegalStateException("Hero has left an item behind");
        }

        //check again for coin, since its not dependent on inventory being full
        if(tileToLeave instanceof Coin){
            throw new IllegalStateException("Hero has left a coin behind");
        }

        
    }

    //HELPER
    /*
     * returns the first tile with given type in the maze
     */
    public static Tile getTile(Maze maze, TileType type){
        return maze.getTileThat(t->t.type() == type);
    }

    /*
     * returns the list of all tiles with given type in the maze
     */
    public static List<Tile> getAllTiles(Maze maze, TileType type){
        return maze.getAllTilesThat(t->t.type() == type);
    }
}
