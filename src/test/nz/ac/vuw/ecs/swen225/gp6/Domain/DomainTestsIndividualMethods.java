package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

import org.junit.jupiter.api.Test;

import junit.framework.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

public class DomainTestsIndividualMethods {
    //mock maze and domain for testing
    public static Maze mockMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|$|_|/|X|_|/|_|$|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|$|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    public static Domain mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1);

    @Test
    public void testInventoryToString(){
        Inventory inv = new Inventory(8);
        inv.addItem(new OrangeKey(new TileInfo(null)));
        inv.addItem(new BlueKey(new TileInfo(null))); 
        inv.addItem(new GreenKey(new TileInfo(null)));
        assertEquals("Inv(8): o, b, g", 
        inv.toString());
    }

    @Test 
    public void testCoin(){
        Coin c = new Coin(new TileInfo(new Loc(0, 0)));
        assertThrows(IllegalArgumentException.class,
        ()->{c.setOn(new Wall(new TileInfo(new Loc(0, 0))), this.mockDomain);});
        assertThrows(NullPointerException.class,
        ()->{c.setOn(new Hero(new TileInfo(new Loc(0, 0))), null);});
        assertThrows(NullPointerException.class,
        ()->{c.setOn(null, this.mockDomain);});

        Inventory invBroken = new Inventory(8){ //doesn't add coin
            @Override
            public void addCoin(){}
        };
        Domain domainBroken = new Domain(mockDomain.getMazes(), invBroken, 1);
        assertThrows(AssertionError.class, ()->{c.setOn(new Hero(new TileInfo(new Loc(0, 0))), domainBroken);});
    }

    @Test
    public void testExitDoor(){

        Wall w = new Wall(new TileInfo(null));
        ExitDoor d = new ExitDoor(new TileInfo(new Loc(1, 1)));
        ExitDoorOpen doorOpen = new ExitDoorOpen(new TileInfo(new Loc(1, 1)));

        assertEquals(true, w.obstructsEnemy(mockDomain));
        assertEquals(true, d.obstructsEnemy(mockDomain));
        assertEquals(true, doorOpen.obstructsEnemy(mockDomain));
        assertEquals(KeyColor.NONE, d.color());
        assertEquals(KeyColor.NONE, doorOpen.color());
    }

    @Test
    public void testDirectionTransformLoc(){
        assertEquals( new Loc(0,2), Direction.Down.transformLoc(new Loc(0, 1)) );
        assertEquals( new Loc(0, 0), Direction.Up.transformLoc(new Loc(0,1)) );
        assertEquals( new Loc(0,0), Direction.Left.transformLoc(new Loc(1,0)) );
        assertEquals( new Loc(1,0), Direction.Right.transformLoc( new Loc(0,0)) );
    }

    @Test
    public void testDirectionFromSymbol(){
        assertEquals(Direction.Down, Direction.getDirFromSymbol('D'));
        assertEquals(Direction.Left, Direction.getDirFromSymbol('L') );
        assertEquals(Direction.Right, Direction.getDirFromSymbol('R') );
        assertEquals(Direction.Up, Direction.getDirFromSymbol('U') );
    }

    @Test 
    public void checkAllMethodsInLoc(){
    
        Loc loc = new Loc(1,1);
        Loc loc2 = new Loc(1,1); // the same thing as loc
        Loc loc3 = new Loc(2,2); // different to loc
        Loc loc4 = new Loc(1, 2); 

        assertEquals(1, loc.x() );
        assertEquals(1, loc.y() );

        assertEquals(true, loc.equals(loc2) );
        assertEquals(false, loc.equals(loc3) );
        assertEquals(false, loc.equals(loc4) );
        assertEquals(false, loc.equals(new Wall(new TileInfo(loc)) ) ); // loc is not a wall!
        assertEquals(true, loc.hashCode() == loc2.hashCode() );

        loc.x(2);
        loc.y(2);
        assertEquals(true, loc.equals(loc3));

        assertThrows(IllegalArgumentException.class, ()->{loc.x(-1);});
        assertThrows(IllegalArgumentException.class, ()->{loc.y(-1);});
        assertThrows(IllegalArgumentException.class, ()->{Loc l = new Loc(0, -1);});
        assertThrows(IllegalArgumentException.class, ()->{Loc l = new Loc(-1, 0);});

        assertEquals(false, Loc.checkInBound(new Loc(0, 100), new Maze(new Tile[4][4])));
        assertEquals(false, Loc.checkInBound(new Loc(100, 0), new Maze(new Tile[4][4])));
        assertEquals(true, Loc.checkInBound(new Loc(1, 1), new Maze(new Tile[4][4]) ));


    }

    @Test 
    public void checkAllClassesInTileAnatomy(){
        //Abstract tile
        assertThrows(NullPointerException.class, () ->{
        Tile t = new AbstractTile(null) {
            @Override
            public TileType type() {
                return TileType.Other;
            }
        };});

        //Tile interface


    }
}
