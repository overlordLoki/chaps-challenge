package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.CheckGame.GameState;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

public class DomainTestsIndividualMethods {
    //mock maze and domain for testing (DO NOT ALTER)
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

    //INVENTORY
    @Test
    public void testInventoryToString(){
        Inventory inv = new Inventory(8);
        inv.addItem(new OrangeKey(new TileInfo(null)));
        inv.addItem(new BlueKey(new TileInfo(null))); 
        inv.addItem(new GreenKey(new TileInfo(null)));
        assertEquals("Inv(8): o, b, g", 
        inv.toString());
    }


    //TILES:
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
    public void testExitDoors(){
        Hero hero = new Hero(new TileInfo(new Loc(0, 0)));
        Wall w = new Wall(new TileInfo(null));
        ExitDoor door = new ExitDoor(new TileInfo(new Loc(1, 1)));
        ExitDoorOpen doorOpen = new ExitDoorOpen(new TileInfo(new Loc(1, 1)));
        ExitDoorOpen doorOpenTwo = new ExitDoorOpen(new TileInfo(new Loc(1, 1)));

        assertEquals(true, w.obstructsEnemy(mockDomain));
        assertEquals(true, door.obstructsEnemy(mockDomain));
        assertEquals(true, doorOpen.obstructsEnemy(mockDomain));
        assertEquals(KeyColor.NONE, door.color());
        assertEquals(KeyColor.NONE, doorOpen.color());

        assertThrows(NullPointerException.class,()->{doorOpen.setOn(null, mockDomain);});
        assertThrows(NullPointerException.class,()->{doorOpen.setOn(hero, null);});
        assertThrows(NullPointerException.class,()->{doorOpen.setOn(null, null);});
        
        assertThrows(IllegalArgumentException.class, () ->{doorOpen.setOn(new Wall(new TileInfo(null)), mockDomain);});

        doorOpen.setOn(new Hero(new TileInfo(new Loc(0, 0))), mockDomain);
        assertEquals(true, doorOpen.heroOn());

        //to test winning with another level left
        mockDomain = new Domain(List.of(mockMaze, mockMaze), new Inventory(8), 1); 
        mockDomain.addEventListener(DomainEvent.onWin, ()->{}); //to test calling win listeners
        doorOpenTwo.setOn(hero, mockDomain);
        assertEquals(true, doorOpenTwo.heroOn());
        mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1); //mockDomain back to normal
    }

    @Test
    public void testInfoAndPeriphery(){
        Tile p = new Periphery(new TileInfo(new Loc(0, 0)));
        Info i = new Info(new TileInfo(new Loc(0, 0)));

        assertEquals(p, p.replaceWith());

        assertEquals(i, i.replaceWith());
        assertEquals(true, i.obstructsEnemy(mockDomain));
        assertEquals("", i.message());
    }

    @Test
    public void testMethodsInHero(){
        Hero hero = new Hero(new TileInfo(new Loc(2, 2)));

        mockDomain.getLevels().get(0).makeHeroStep(Direction.Up);
        hero.ping(mockDomain);
        assertEquals(Direction.Up, hero.dir());
        assertEquals(true, hero.tileOn() instanceof Floor);

        assertThrows(NullPointerException.class, ()->{hero.setOn(null, mockDomain);});
        assertThrows(NullPointerException.class, ()->{hero.setOn(new Wall(new TileInfo(null)), null);});
        assertThrows(NullPointerException.class, ()->{hero.ping(null);});

        mockDomain.addEventListener(DomainEvent.onLose, ()->{}); // to test calling lose listeners
        hero.setOn(new Wall(new TileInfo(new Loc(1,1))){
            @Override public boolean damagesHero(Domain d){return true;}
        }, mockDomain); //hero we put on hero a dangerous wall
    }

    //TILE GROUPS:
    @Test
    public void testDoor(){
        Door door = new GreenLock(new TileInfo(new Loc(0, 0)));
        Hero hero = new Hero(new TileInfo(new Loc(0, 1)));

        assertEquals(true, door.obstructsEnemy(mockDomain));
        assertEquals(true, door.obstructsHero(mockDomain));
        assertEquals(KeyColor.GREEN, door.color());


        Inventory invWithKey = new Inventory(8);
        invWithKey.addItem(new GreenKey(new TileInfo(null)));
        mockDomain = new Domain(List.of(mockMaze), invWithKey, 1); // inv with key
        assertEquals(false, door.obstructsHero(mockDomain));
        door.setOn(hero , mockDomain); //check no exception is thrown
        mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1); // mockDomain back to normal 
                  
        assertThrows(AssertionError.class,
        () ->{door.setOn(hero, mockDomain);});//testing error if no key
        assertThrows(IllegalArgumentException.class, 
        ()->{door.setOn(new Wall(new TileInfo(new Loc(0, 0))), mockDomain);});
        assertThrows(NullPointerException.class, 
        ()->{door.obstructsHero(null);});
        assertThrows(NullPointerException.class, 
        ()->{door.setOn(hero, null);});
    }

    @Test 
    public void testItem(){
        Hero hero = new Hero(new TileInfo(new Loc(1, 0)));
        Item item = new GreenKey(new TileInfo(new Loc(0, 0)));
        assertEquals(true, item.obstructsEnemy(mockDomain));
        
        Inventory invFull = new Inventory(2);
        invFull.addItem(new GreenKey(new TileInfo(null)));
        invFull.addItem(new GreenKey(new TileInfo(null)));
        mockDomain = new Domain(List.of(mockMaze), invFull, 1); // full inv
        assertEquals(false, item.obstructsHero(mockDomain));
        item.setOn(hero, mockDomain);
        assertEquals(item , item.replaceWith());
        mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1); // mockDomain back to normal 

        assertThrows(NullPointerException.class, ()->{item.setOn(hero, null);});
        assertThrows(NullPointerException.class, ()->{item.setOn(null, mockDomain);});
        assertThrows(IllegalArgumentException.class, ()->{item.setOn(new Wall(new TileInfo(null)), mockDomain);});
    }

    //TILE ANATOMY:
    @Test 
    public void testClassesInTileAnatomy(){
        //Abstract tile
        assertThrows(NullPointerException.class, () ->{
        Tile t = new AbstractTile(null) {
            @Override
            public TileType type() {
                return TileType.Other;
            }
        };});

        //Tile interface
        Tile t = new Null(new TileInfo(null));
        assertEquals(false, t.obstructsEnemy(mockDomain));
        assertThrows(NullPointerException.class, () -> {t.setOn(null, mockDomain);});
        assertThrows(NullPointerException.class, () -> {t.setOn(new Wall(new TileInfo(null)), null);});

        
        //TileInfo
        Tile t2 = new Wall(new TileInfo(null, "WALLLLL"));
        assertEquals("WALLLLL", t2.info().getImageName());
        t2.info().pingStep();
        assertEquals(1, t2.info().ping());

        //TileType
        assertThrows(IllegalArgumentException.class, 
        ()->{TileType.makeTile(TileType.Other, new TileInfo(null));});
        assertThrows(IllegalArgumentException.class, 
        ()->{TileType.makeTileFromSymbol('E', new TileInfo(null));});
        assertThrows(NullPointerException.class, 
        ()->{TileType.makeTile(null, new TileInfo(null));});
        assertThrows(NullPointerException.class, 
        ()->{TileType.makeTile(null, new TileInfo(null));});
        assertThrows(NullPointerException.class, 
        ()->{TileType.makeTile(TileType.Wall, null);});

    }

    //INTEGRITY CHECK CLASSES:
    @Test
    public void testCheckCurrentStateLoseWin(){
        CheckGame.state = GameState.PLAYING;
        mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1);
        CheckGame.checkCurrentState(mockDomain); //must not throw any exceptions
        
        CheckGame.state = GameState.WON;
        assertThrows(IllegalStateException.class, 
        ()->{CheckGame.checkCurrentState(mockDomain);});

        Maze mazeNoCoins = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|Z|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|_|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

        CheckGame.state = GameState.WON;
        Domain domainNoCoins = new Domain(List.of(mazeNoCoins), new Inventory(8), 1);
        assertThrows(IllegalStateException.class, //all coins collected but not on exit door
        ()->{CheckGame.checkCurrentState(domainNoCoins);});

        CheckGame.state = GameState.PLAYING;
        DomainTestsThruMoves.doMoves(domainNoCoins, "UUUUUU");
        CheckGame.state = GameState.WON;

        CheckGame.checkCurrentState(domainNoCoins); //must not throw any exceptions since hero is on exit door
                                                    //all coins are collected

        Maze mazeNoHero = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|Z|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|_|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

        Domain domainCheckLose = new Domain(List.of(mazeNoHero), new Inventory(8), 1);

        //make a dangerous wall that has swallowed enemy
        class dangerousWall extends Wall{
            public dangerousWall(TileInfo info){
                super(info);
            }
            @Override
            public boolean damagesHero(Domain d){
                return true;
            }
            public Tile tileOn(){
                return new Hero(new TileInfo(null));
            }
        }

        mazeNoHero.setTileAt(new Loc(0,0), new dangerousWall(new TileInfo(null)));

        CheckGame.state = GameState.LOST;
        CheckGame.checkCurrentState(domainCheckLose); //must not throw any exceptions since a damaging tile is on hero

    }

    //UTILITY CLASSES:
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
    public void testMethodsInLoc(){
    
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
}
