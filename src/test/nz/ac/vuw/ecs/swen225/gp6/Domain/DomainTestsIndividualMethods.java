package test.nz.ac.vuw.ecs.swen225.gp6.Domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.DomainEvent;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.GameState;
import nz.ac.vuw.ecs.swen225.gp6.domain.IntegrityCheck.CheckGame;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.Level;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.AbstractTile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Door;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Item;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileGroups.Key.KeyColor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.BlueKey;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Coin;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.ExitDoor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.ExitDoorOpen;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Floor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.GreenKey;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.GreenLock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Hero;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Info;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Null;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.OrangeKey;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Periphery;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Wall;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.YellowKey;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.YellowLock;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;
import org.junit.jupiter.api.Test;

/**
 * Tests that isolate individual methods in the Domain class.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
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
  public void testInventoryToString() {
    Inventory inv = new Inventory(8);
    inv.addItem(new OrangeKey(new TileInfo(null)));
    inv.addItem(new BlueKey(new TileInfo(null)));
    inv.addItem(new GreenKey(new TileInfo(null)));
    assertEquals("Inv(8): o, b, g",
        inv.toString());
  }


  //TILES:
  @Test
  public void testCoin() {
    Coin c = new Coin(new TileInfo(new Loc(0, 0)));
    assertThrows(IllegalArgumentException.class,
        () -> c.setOn(new Wall(new TileInfo(new Loc(0, 0))), mockDomain));
    assertThrows(NullPointerException.class,
        () -> c.setOn(new Hero(new TileInfo(new Loc(0, 0))), null));
    assertThrows(NullPointerException.class,
        () -> {
          c.setOn(null, mockDomain);
        });

    Inventory invBroken = new Inventory(8) { //doesn't add coin
      @Override
      public void addCoin() {
      }
    };
    Domain domainBroken = new Domain(mockDomain.getMazes(), invBroken, 1);
    assertThrows(AssertionError.class, () -> {
      c.setOn(new Hero(new TileInfo(new Loc(0, 0))), domainBroken);
    });
  }

  @Test
  public void testExitDoors() {
    final Hero hero = new Hero(new TileInfo(new Loc(0, 0)));
    final Wall w = new Wall(new TileInfo(null));
    final ExitDoor door = new ExitDoor(new TileInfo(new Loc(1, 1)));
    final ExitDoorOpen doorOpen = new ExitDoorOpen(new TileInfo(new Loc(1, 1)));
    final ExitDoorOpen doorOpenTwo = new ExitDoorOpen(new TileInfo(new Loc(1, 1)));

    assertTrue(w.obstructsEnemy(mockDomain));
    assertTrue(door.obstructsEnemy(mockDomain));
    assertTrue(doorOpen.obstructsEnemy(mockDomain));
    assertEquals(KeyColor.NONE, door.color());
    assertEquals(KeyColor.NONE, doorOpen.color());

    assertThrows(NullPointerException.class, () -> {
      doorOpen.setOn(null, mockDomain);
    });
    assertThrows(NullPointerException.class, () -> {
      doorOpen.setOn(hero, null);
    });
    assertThrows(NullPointerException.class, () -> {
      doorOpen.setOn(null, null);
    });

    assertThrows(IllegalArgumentException.class, () -> {
      doorOpen.setOn(new Wall(new TileInfo(null)), mockDomain);
    });

    doorOpen.setOn(new Hero(new TileInfo(new Loc(0, 0))), mockDomain);
    assertTrue(doorOpen.heroOn());

    //to test winning with another level left
    mockDomain = new Domain(List.of(mockMaze, mockMaze), new Inventory(8), 1);
    mockDomain.addEventListener(DomainEvent.onWin, () -> {
    }); //to test calling win listeners
    doorOpenTwo.setOn(hero, mockDomain);
    assertTrue(doorOpenTwo.heroOn());
    mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1); //mockDomain back to normal
  }

  @Test
  public void testInfoAndPeriphery() {
    Tile p = new Periphery(new TileInfo(new Loc(0, 0)));
    Info i = new Info(new TileInfo(new Loc(0, 0)));

    assertEquals(p, p.replaceWith());

    assertEquals(i, i.replaceWith());
    assertTrue(i.obstructsEnemy(mockDomain));
    assertEquals("", i.message());
  }

  @Test
  public void testMethodsInHero() {
    Hero hero = new Hero(new TileInfo(new Loc(2, 2)));

    mockDomain.getLevels().get(0).makeHeroStep(Direction.Up);
    hero.ping(mockDomain);
    assertEquals(Direction.Up, hero.dir());
    assertTrue(hero.tileOn() instanceof Floor);
    assertEquals(hero, hero.replaceWith());

    assertThrows(NullPointerException.class, () -> {
      hero.setOn(null, mockDomain);
    });
    assertThrows(NullPointerException.class, () -> {
      hero.setOn(new Wall(new TileInfo(null)), null);
    });
    assertThrows(NullPointerException.class, () -> {
      hero.ping(null);
    });

    mockDomain.addEventListener(DomainEvent.onLose, () -> {
    }); // to test calling lose listeners
    hero.setOn(new Wall(new TileInfo(new Loc(1, 1))) {
      @Override
      public boolean damagesHero(Domain d) {
        return true;
      }
    }, mockDomain); //hero we put on hero a dangerous wall
  }

  //TILE GROUPS:
  @Test
  public void testDoor() {
    final Door door = new GreenLock(new TileInfo(new Loc(0, 0)));
    final Hero hero = new Hero(new TileInfo(new Loc(0, 1)));

    assertTrue(door.obstructsEnemy(mockDomain));
    assertTrue(door.obstructsHero(mockDomain));
    assertEquals(KeyColor.GREEN, door.color());

    Inventory invWithKey = new Inventory(8);
    invWithKey.addItem(new GreenKey(new TileInfo(null)));
    mockDomain = new Domain(List.of(mockMaze), invWithKey, 1); // inv with key
    assertFalse(door.obstructsHero(mockDomain));
    door.setOn(hero, mockDomain); //check no exception is thrown
    mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1); // mockDomain back to normal

    assertThrows(AssertionError.class,
        () -> {
          door.setOn(hero, mockDomain);
        }); //testing error if no key
    assertThrows(IllegalArgumentException.class,
        () -> {
          door.setOn(new Wall(new TileInfo(new Loc(0, 0))), mockDomain);
        });
    assertThrows(NullPointerException.class,
        () -> {
          door.obstructsHero(null);
        });
    assertThrows(NullPointerException.class,
        () -> {
          door.setOn(hero, null);
        });
  }

  @Test
  public void testItem() {
    final Hero hero = new Hero(new TileInfo(new Loc(1, 0)));
    final Item item = new GreenKey(new TileInfo(new Loc(0, 0)));
    assertTrue(item.obstructsEnemy(mockDomain));

    Inventory invFull = new Inventory(2);
    invFull.addItem(new GreenKey(new TileInfo(null)));
    invFull.addItem(new GreenKey(new TileInfo(null)));
    mockDomain = new Domain(List.of(mockMaze), invFull, 1); // full inv
    assertFalse(item.obstructsHero(mockDomain));
    item.setOn(hero, mockDomain);
    assertEquals(item, item.replaceWith());
    mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1); // mockDomain back to normal

    assertThrows(NullPointerException.class, () -> {
      item.setOn(hero, null);
    });
    assertThrows(NullPointerException.class, () -> {
      item.setOn(null, mockDomain);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      item.setOn(new Wall(new TileInfo(null)), mockDomain);
    });
  }

  //TILE ANATOMY:
  @Test
  public void testClassesInTileAnatomy() {
    //Abstract tile
    assertThrows(NullPointerException.class, () -> {
      Tile t = new AbstractTile(null) {
        @Override
        public TileType type() {
          return TileType.Other;
        }
      };
    });

    //Tile interface
    Tile t = new Null(new TileInfo(null));
    assertFalse(t.obstructsEnemy(mockDomain));
    assertThrows(NullPointerException.class, () -> {
      t.setOn(null, mockDomain);
    });
    assertThrows(NullPointerException.class, () -> {
      t.setOn(new Wall(new TileInfo(null)), null);
    });

    //TileInfo
    Tile t2 = new Wall(new TileInfo(null, "WALLLLL"));
    assertEquals("WALLLLL", t2.info().getImageName());
    t2.info().pingStep();
    assertEquals(1, t2.info().ping());

    //TileType
    assertThrows(IllegalArgumentException.class,
        () -> {
          TileType.makeTile(TileType.Other, new TileInfo(null));
        });
    assertThrows(IllegalArgumentException.class,
        () -> {
          TileType.makeTileFromSymbol('E', new TileInfo(null));
        });
    assertThrows(NullPointerException.class,
        () -> {
          TileType.makeTile(null, new TileInfo(null));
        });
    assertThrows(NullPointerException.class,
        () -> {
          TileType.makeTile(null, new TileInfo(null));
        });
    assertThrows(NullPointerException.class,
        () -> {
          TileType.makeTile(TileType.Wall, null);
        });

  }

  //INTEGRITY CHECK CLASSES:
  @Test
  public void testCheckCurrentStateWin() {
    mockDomain = new Domain(List.of(mockMaze), new Inventory(8), 1);
    CheckGame.checkCurrentState(mockDomain); //must not throw any exceptions

    mockDomain.setGameState(GameState.WON);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(mockDomain);
        });

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
    Domain domainNoCoins = new Domain(List.of(mazeNoCoins), new Inventory(8), 1);

    domainNoCoins.setGameState(GameState.WON);
    assertThrows(IllegalStateException.class, //all coins collected but not on exit door
        () -> {
          CheckGame.checkCurrentState(domainNoCoins);
        });

    domainNoCoins.setGameState(GameState.PLAYING);
    DomainTestsThruMoves.doMoves(domainNoCoins, "UUUUUU");
    domainNoCoins.setGameState(GameState.WON);

    CheckGame.checkCurrentState(
        domainNoCoins); //must not throw any exceptions since hero is on exit door
    //all coins are collected
  }

  @Test
  public void testCheckCurrentStateLose() {
    //CHECK LOSE:
    mockDomain.setGameState(GameState.LOST);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(mockDomain);
        });

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
    domainCheckLose.setGameState(GameState.LOST);

    //make a dangerous wall that has swallowed enemy
    class DangerousWall extends Wall {

      public DangerousWall(TileInfo info) {
        super(info);
      }

      @Override
      public boolean damagesHero(Domain d) {
        return true;
      }

      public Tile tileOn() {
        return new Hero(new TileInfo(null));
      }
    }

    //dangerous wall with no onTile
    class DangerousWallBroken extends Wall {

      public DangerousWallBroken(TileInfo info) {
        super(info);
      }

      @Override
      public boolean damagesHero(Domain d) {
        return true;
      }
    }

    mazeNoHero.setTileAt(new Loc(0, 0), new DangerousWall(new TileInfo(null)));

    domainCheckLose.setGameState(GameState.LOST);
    CheckGame.checkCurrentState(
        domainCheckLose); //must not throw any exceptions since a damaging tile is on hero

    mazeNoHero.setTileAt(new Loc(0, 0), new DangerousWallBroken(new TileInfo(null)));

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainCheckLose);
        } //damaging tile but hero isn't on
    );

    mockDomain.setGameState(GameState.BETWEENLEVELS);
    CheckGame.checkCurrentState(mockDomain); //should change state to playing
    mockDomain.setGameState(GameState.PLAYING);
  }

  @Test
  public void testCheckCurrentStateNoHero() {
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
    Domain domainNoHero = new Domain(List.of(mazeNoHero), new Inventory(8), 1);

    domainNoHero.setGameState(GameState.PLAYING);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainNoHero);
        }); //damaging tile but hero isn't on
  }

  @Test
  public void testCheckCurrentStateNoCoin() {
    Maze mazeNoCoin = DomainTestsThruMoves.mazeParser("""
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
    Domain domainNoCoin = new Domain(List.of(mazeNoCoin), new Inventory(8), 1);

    domainNoCoin.setGameState(GameState.PLAYING);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainNoCoin);
        }); //damaging tile but hero isn't on
  }

  @Test
  public void testCheckCurrentStateDoors1() {
    Maze mazeNoCoin = DomainTestsThruMoves.mazeParser("""
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
    Inventory invOneCoin = new Inventory(8);
    invOneCoin.addCoin();
    Domain domainOpenDoor = new Domain(List.of(mazeNoCoin), invOneCoin, 1);

    domainOpenDoor.setGameState(GameState.PLAYING);
    CheckGame.checkCurrentState(domainOpenDoor); //should not throw exception
  }

  @Test
  public void testCheckCurrentStateDoors2() {

    Maze mazeNoCoinDoorClosed = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|_|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory invOneCoin = new Inventory(8);
    invOneCoin.addCoin();
    Domain domainDoorClosedBroken = new Domain(List.of(mazeNoCoinDoorClosed), invOneCoin, 1);

    domainDoorClosedBroken.setGameState(GameState.PLAYING);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainDoorClosedBroken);
        }); //all coins collected but door still close
  }

  @Test
  public void testCheckCurrentStateDoors3() {

    Maze mazeCoinDoorOpen = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|O|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory invOneCoin = new Inventory(8);
    invOneCoin.addCoin();
    Domain domainDoorOpenBroken = new Domain(List.of(mazeCoinDoorOpen), invOneCoin, 1);

    domainDoorOpenBroken.setGameState(GameState.PLAYING);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainDoorOpenBroken);
        }); //coins left but door is open
  }

  @Test
  public void testCheckCurrentStateDoors4() {

    Maze mazeCoinNoDoor = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory invOneCoin = new Inventory(8);
    invOneCoin.addCoin();
    Domain domainCoinNoDoor = new Domain(List.of(mazeCoinNoDoor), invOneCoin, 1);

    domainCoinNoDoor.setGameState(GameState.PLAYING);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainCoinNoDoor);
        }); //coins left but door is open
  }

  @Test
  public void testCheckCurrentStateDoors5() {

    Maze mazeNoCoinNoDoor = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|_|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory invOneCoin = new Inventory(8);
    invOneCoin.addCoin();
    Domain domainNoCoinNoDoor = new Domain(List.of(mazeNoCoinNoDoor), invOneCoin, 1);

    domainNoCoinNoDoor.setGameState(GameState.PLAYING);
    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkCurrentState(domainNoCoinNoDoor);
        }); //coins left but door is open
  }

  @Test
  public void testCheckHeroStateChange1() { //test legal move
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    CheckGame.checkStateChange(preDomain, afterDomain); //must not throw exceptions
  }

  @Test
  public void testCheckHeroStateChange2() { //test moving on obstruction
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|H|_|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  //test hero having out of bound location stored internally
  @Test
  public void testCheckHeroStateChange3() {
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    //mess up heros internal location memory
    afterMaze.getTileThat(t -> t.type() == TileType.Hero).info().loc(new Loc(100, 100));

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange4() { //test hero tileOn is messed up
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|H|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    //mess up on tile of hero
    Hero h = (Hero) afterMaze.getTileThat(t -> t.type() == TileType.Hero);
    h.setTileOn(new Wall(new TileInfo(null)));

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange5() { //test hero move on locks without correct key
    class NonObstructingLock extends YellowKey {

      public NonObstructingLock(TileInfo t) {
        super(t);
      }

      @Override
      public boolean obstructsHero(Domain d) {
        return false;
      }

    }

    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);
    preMaze.setTileAt(new Loc(3, 7), new NonObstructingLock(new TileInfo(new Loc(3, 7))));

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|H|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange6() { //test hero move on locks and key isnt removd

    class NonObstructingLock extends YellowLock {

      public NonObstructingLock(TileInfo t) {
        super(t);
      }

      @Override
      public boolean obstructsHero(Domain d) {
        return false;
      }

    }

    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    preInv.addItem(new YellowKey(new TileInfo(null)));
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);
    preMaze.setTileAt(new Loc(3, 7), new NonObstructingLock(new TileInfo(new Loc(3, 7))));

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|H|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    afterInv.addItem(new YellowKey(new TileInfo(null)));
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange7() { //test hero move on item but not picked up
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|H|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange8() { //test hero move on coin but not added to inventory
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|$|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|H|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|H|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange9() { //test hero leave item behind without full inventory
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|o|H|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckHeroStateChange10() { //test hero leave coin behind
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|$|H|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckStateChange1() { //test total number of coins changing in game
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|$|$|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    assertThrows(IllegalStateException.class,
        () -> {
          CheckGame.checkStateChange(preDomain, afterDomain);
        });
  }

  @Test
  public void testCheckStateChange2() { //test no error when game is finished(won/lost)
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    afterDomain.setGameState(GameState.LOST); //set default state
    CheckGame.checkStateChange(preDomain, afterDomain);

    afterDomain.setGameState(GameState.WON);  //set default state
    CheckGame.checkStateChange(preDomain, afterDomain);
  }

  @Test
  public void testCheckStateChange3() { //test no error when game is in between levels
    Maze preMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory preInv = new Inventory(8);
    Domain preDomain = new Domain(List.of(preMaze), preInv, 1);

    Maze afterMaze = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|_|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");
    Inventory afterInv = new Inventory(8);
    Domain afterDomain = new Domain(List.of(afterMaze), afterInv, 1);

    afterDomain.setGameState(GameState.BETWEENLEVELS); //set default state
    CheckGame.checkStateChange(preDomain, afterDomain);
  }

  //MAJOR CLASSES:
  @Test
  public void testDomainInitalisation() {
    Maze m = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|_|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

    assertThrows(IndexOutOfBoundsException.class,
        () -> {
          new Domain(List.of(new Level(m, 1)), 10);
        });
    assertThrows(IndexOutOfBoundsException.class,
        () -> {
          new Domain(List.of(new Level(m, 1)), 0);
        });
    assertThrows(NullPointerException.class,
        () -> {
          new Domain(null, 0);
        });

  }

  @Test
  public void testDomainMethods1() {
    Maze m = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

    Domain d = new Domain(List.of(new Level(m, 1), new Level(m, 2)), 1);

    assertEquals(60, d.getLevelTimeLimits().get(0));
    assertEquals(60, d.getCurrentTimeLimit());
    assertEquals(0, d.getCurrentTime());
    assertEquals(m.toString(), new Maze(d.getGameArray()).toString());
    assertEquals(1, d.getCurrentLevel());

    d.setCurrentTime(20);
    assertEquals(20, d.getCurrentTime());

    assertTrue(d.nextLvl());
    assertEquals(2, d.getCurrentLevel());

    assertFalse(d.nextLvl());
    assertEquals(2, d.getCurrentLevel());

    d.setCurrentLevel(1);
    assertEquals(1, d.getCurrentLevel());

    //doesn't matter what the domain string looks like as long as it's same
    assertEquals(d.toString(), d.toString());

    assertThrows(IndexOutOfBoundsException.class,
        () -> {
          d.setCurrentLevel(3);
        });
    assertThrows(IndexOutOfBoundsException.class,
        () -> {
          d.setCurrentLevel(0);
        });

    assertThrows(NullPointerException.class,
        () -> {
          d.addEventListener(null, () -> {
          });
        });
    assertThrows(NullPointerException.class,
        () -> {
          d.addEventListener(DomainEvent.onWin, null);
        });

  }

  @Test
  public void testDomainMethods2() {
    final Maze m = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

    final Maze m2 = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
            0 1 2 3 4 5 6 7 8 9""");

    Domain d = new Domain(new ArrayList<>(List.of(new Level(m, 1), new Level(m, 2))), 1);

    d.moveDown();
    d.pingDomain();
    d.moveUp();
    d.pingDomain();
    d.moveRight();
    d.pingDomain();
    d.moveLeft();
    d.pingDomain(); //should end up in same spot

    assertEquals(m.toString(), m2.toString());

  }

  @Test
  public void testDomainMethods3() {
    Maze m = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

    Domain d = new Domain(new ArrayList<>(List.of(new Level(m, 1), new Level(m, 2))), 1);

    assertFalse(d.heroIsOnInfo());
    assertThrows(RuntimeException.class, () -> {
      d.getInfoHint();
    });
    ((Hero) d.getCurrentMaze().getTileThat(t -> t.type() == TileType.Hero)).setTileOn(
        new Info(new TileInfo(null)));
    assertTrue(d.heroIsOnInfo());
    assertEquals("", d.getInfoHint());

  }

  @Test
  public void testMazeMethods() {
    Maze m = DomainTestsThruMoves.mazeParser("""
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

    assertEquals(m.toString(), """
        0|_|/|/|/|/|/|/|/|/|_|
        1|/|_|_|/|X|_|/|_|_|/|
        2|/|_|_|/|_|_|O|_|_|/|
        3|/|/|B|/|_|_|/|/|/|/|
        4|/|_|_|_|g|b|_|_|_|/|
        5|/|_|_|_|o|y|_|_|_|/|
        6|/|/|/|/|H|_|/|G|/|/|
        7|/|_|_|Y|_|_|/|_|_|/|
        8|/|_|_|/|_|_|/|_|$|/|
        9|_|/|/|/|/|/|/|/|/|_|
          0 1 2 3 4 5 6 7 8 9""");

    assertNull(m.getTileLoc(new Wall(new TileInfo(new Loc(0, 0)))));

    Tile t = m.getTileAt(new Loc(0, 0));
    assertEquals(new Loc(0, 0), m.getTileLoc(t));

    assertTrue(m.getTileAt(0, 7).type() == TileType.Wall);

    //Exceptions
    assertThrows(NullPointerException.class, () -> {
      m.getTileLoc(null);
    });

    assertThrows(IllegalArgumentException.class,
        () -> {
          m.checkLocationIntegrity(new Loc(100, 100));
        });

    assertThrows(NullPointerException.class,
        () -> {
          m.checkLocationIntegrity(null);
        });

    Maze mazeWithNullArray = new Maze(new Tile[3][3]);
    assertThrows(RuntimeException.class,
        () -> {
          mazeWithNullArray.getTileArrayCopy();
        });
  }

  //UTILITY CLASSES:
  @Test
  public void testDirectionTransformLoc() {
    assertEquals(new Loc(0, 2), Direction.Down.transformLoc(new Loc(0, 1)));
    assertEquals(new Loc(0, 0), Direction.Up.transformLoc(new Loc(0, 1)));
    assertEquals(new Loc(0, 0), Direction.Left.transformLoc(new Loc(1, 0)));
    assertEquals(new Loc(1, 0), Direction.Right.transformLoc(new Loc(0, 0)));
  }

  @Test
  public void testDirectionFromSymbol() {
    assertEquals(Direction.Down, Direction.getDirFromSymbol('D'));
    assertEquals(Direction.Left, Direction.getDirFromSymbol('L'));
    assertEquals(Direction.Right, Direction.getDirFromSymbol('R'));
    assertEquals(Direction.Up, Direction.getDirFromSymbol('U'));
  }

  @Test
  public void testMethodsInLoc() {

    final Loc loc = new Loc(1, 1);
    final Loc loc2 = new Loc(1, 1); // the same thing as loc
    final Loc loc3 = new Loc(2, 2); // different to loc
    final Loc loc4 = new Loc(1, 2);

    assertEquals(1, loc.x());
    assertEquals(1, loc.y());

    assertTrue(loc.equals(loc2));
    assertFalse(loc.equals(loc3));
    assertFalse(loc.equals(loc4));
    assertFalse(loc.equals(new Wall(new TileInfo(loc)))); // loc is not a wall!
    assertTrue(loc.hashCode() == loc2.hashCode());

    loc.x(2);
    loc.y(2);
    assertTrue(loc.equals(loc3));

    assertThrows(IllegalArgumentException.class, () -> {
      loc.x(-1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      loc.y(-1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Loc l = new Loc(0, -1);
    });
    assertThrows(IllegalArgumentException.class, () -> {
      Loc l = new Loc(-1, 0);
    });

    assertFalse(Loc.checkInBound(new Loc(0, 100), new Maze(new Tile[4][4])));
    assertFalse(Loc.checkInBound(new Loc(100, 0), new Maze(new Tile[4][4])));
    assertTrue(Loc.checkInBound(new Loc(1, 1), new Maze(new Tile[4][4])));


  }
}
