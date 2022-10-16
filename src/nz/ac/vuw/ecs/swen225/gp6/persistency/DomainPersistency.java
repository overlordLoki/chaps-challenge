package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain.GameState;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.Level;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Floor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Info;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Wall;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * This utility class is responsible for saving and loading classes in the Domain package.
 *
 * @author Benjamin Hong - 300605520
 */
public final class DomainPersistency {
  /**
   * A map that holds the converter from an XML element to a tile.
   */
  private static final Map<String, BiFunction<Element, Loc, Tile>> tagToTiler =
      new HashMap<>() {
        {
          put(
              "key",
              (element, loc) -> {
                return makeTile(
                    TileType.valueOf(capitalise(element.attributeValue("color")) + "Key"), loc);
              });
          put(
              "lock",
              (element, loc) -> {
                return makeTile(
                    TileType.valueOf(capitalise(element.attributeValue("color")) + "Lock"), loc);
              });
          put(
              "custom",
              (element, loc) -> {
                String customTile = element.attributeValue("class");
                String source = element.attributeValue("source");

                try {
                  File jar = new File("res/levels/" + source);

                  URLClassLoader child =
                      new URLClassLoader(
                          new URL[]{jar.toURI().toURL()},
                          DomainPersistency.class.getClassLoader());

                  Class<?> clazz = Class.forName("custom.tiles." + customTile, true, child);
                  Constructor<?> ctor = clazz.getConstructor(TileInfo.class);
                  return (Tile)
                      ctor.newInstance(
                          new TileInfo(
                              loc,
                              0,
                              Character.toLowerCase(customTile.charAt(0)) + customTile.substring(1),
                              source));
                } catch (Exception e) {
                  e.printStackTrace();
                  return makeTile(TileType.Null, loc);
                }
              });
          put(
              "info",
              (element, loc) -> {
                String message = element.attributeValue("message");
                return TileType.makeTile(TileType.Info, new TileInfo(loc, 0, "", message));
              });
        }
      };

  /**
   * A private constructor to prevent instantiation.
   */
  private DomainPersistency() {
  }

  /**
   * Capitalizes the first letter of the string.
   *
   * @param s the string to capitalize
   * @return the capitalized string
   */
  private static String capitalise(String s) {
    return s.substring(0, 1).toUpperCase(Locale.ENGLISH) + s.substring(1);
  }

  /**
   * Un-capitalizes the first letter of the string.
   *
   * @param s the string to un-capitalize
   * @return the un-capitalized string
   */
  private static String uncapitalise(String s) {
    return s.substring(0, 1).toLowerCase(Locale.ENGLISH) + s.substring(1);
  }

  /**
   * A shortcut method to make a tile.
   *
   * @param tileTupe the type of the tile
   * @param loc      the location of the tile
   * @return the tile
   */
  private static Tile makeTile(TileType tileType, Loc loc) {
    return TileType.makeTile(tileType, new TileInfo(loc));
  }

  /**
   * The default converter from an XML element to a tile.
   *
   * @param element the XML element
   * @param loc     the location of the tile
   * @return the tile
   */
  private static Tile defaultTiler(Element element, Loc loc) {
    String name = element.getName();
    return makeTile(TileType.valueOf(capitalise(name)), loc);
  }

  /**
   * Serialise a domain to an XML document.
   *
   * @param domain The domain to serialise
   * @return The serialised domain as an XML document
   */
  private static Element serialiseDomain(Domain domain) {
    Element root = DocumentHelper.createElement("domain");
    Element levels = root.addElement("levels");
    for (int i = 0; i < domain.getMazes().size(); i++) {
      Level level = domain.getLevels().get(i);
      levels.add(serialiseLevel(level));
    }
    levels.addAttribute("current", Integer.toString(domain.getCurrentLevel()));
    root.addAttribute("state", domain.getGameState().toString());
    return root;
  }

  /**
   * Deserialise a domain from an XML document.
   *
   * @param root The XML element to deserialise
   * @return The deserialised domain
   */
  private static Domain deserialiseDomain(Element root) {
    Element levelEls = root.element("levels");
    int currentLevel = Integer.parseInt(levelEls.attributeValue("current"));
    String state = root.attributeValue("state");
    List<Level> levels = new ArrayList<Level>();
    for (Element level : levelEls.elements()) {
      levels.add(deserialiseLevel(level));
    }
    Domain domain = new Domain(levels, currentLevel);
    domain.setGameState(GameState.valueOf(state));
    return domain;
  }

  /**
   * Serialise a level to an XML element.
   *
   * @param level The maze to serialise
   * @return The serialised level as an XML element
   */
  private static Element serialiseLevel(Level level) {
    Element root = DocumentHelper.createElement("level");
    root.addAttribute("index", Integer.toString(level.lvl - 1));
    root.addAttribute("name", "Level " + (level.lvl));
    root.addAttribute("timeLimit", "" + level.timeLimit);
    root.addAttribute("timeCurrent", "" + level.getCurrentTime());
    root.addAttribute("direction", "" + level.getHeroNextStep().name());
    Element maze = serialiseMaze(level.maze);
    root.add(maze);
    Element inventory = serialiseInventory(level.inv);
    root.add(inventory);
    return root;
  }

  /**
   * Deserialise a level from an XML element.
   *
   * @param level The XML element to deserialise
   * @return The deserialised level
   */
  private static Level deserialiseLevel(Element level) {
    int index = Integer.parseInt(level.attributeValue("index"));
    int timeLimit =
        Integer.parseInt(
            level.attributeValue("timeLimit") == null ? "60" : level.attributeValue("timeLimit"));
    long timeCurrent =
        Long.parseLong(
            level.attributeValue("timeCurrent") == null
                ? "0"
                : level.attributeValue("timeCurrent"));
    String direction =
        level.attributeValue("direction") == null ? "None" : level.attributeValue("direction");
    Direction dir = Direction.valueOf(direction);

    Maze maze = deserialiseMaze(level.element("grid"));
    Element inventory = level.element("inventory");
    Inventory inv = inventory == null ? new Inventory(8) : deserialiseInventory(inventory);

    return new Level(maze, inv, index + 1, timeLimit, timeCurrent, dir);
  }

  /**
   * Serialise a maze to an XML document.
   *
   * @param maze The maze to serialise
   */
  private static Element serialiseMaze(Maze maze) {
    Element grid = DocumentHelper.createElement("grid");
    grid.addAttribute("width", Integer.toString(maze.width()));
    grid.addAttribute("height", Integer.toString(maze.height()));
    for (int x = 0; x < maze.width(); x++) {
      for (int y = 0; y < maze.height(); y++) {
        Tile tile = maze.getTileAt(x, y);
        if (tile != null && tile.type() != TileType.Null && tile.type() != TileType.Floor) {
          Element cell = grid.addElement("cell");
          cell.addAttribute("x", Integer.toString(x));
          cell.addAttribute("y", Integer.toString(y));
          cell.add(serialiseTile(tile));
        }
      }
    }

    return grid;
  }

  /**
   * Deserialise a maze from an XML document.
   *
   * @param grid The XML element to deserialise
   * @return The unserialised maze
   */
  private static Maze deserialiseMaze(Element grid) {
    int width = Integer.parseInt(grid.attributeValue("width"));
    int height = Integer.parseInt(grid.attributeValue("height"));
    Maze maze = new Maze(new Tile[width][height]);
    // fill maze with null tiles
    for (int x = 0; x < width; ++x) {
      for (int y = 0; y < height; ++y) {
        maze.setTileAt(new Loc(x, y), TileType.Floor);
      }
    }
    for (Element cell : grid.elements("cell")) {
      int x = Integer.parseInt(cell.attributeValue("x"));
      int y = Integer.parseInt(cell.attributeValue("y"));
      Element tile = cell.elements().get(0);
      if (tile != null) {
        Tile t = deserialiseTile(tile, new Loc(x, y));
        maze.setTileAt(new Loc(x, y), t);
      }
    }
    return maze;
  }

  /**
   * Serialise a tile to an XML element.
   *
   * @param tile The tile to serialise
   * @return The serialised tile as an XML element
   */
  private static Element serialiseTile(Tile tile) {
    String name = uncapitalise(tile.type().toString());

    if ("other".equals(name)) {
      // it's a custom tile
      Element custom = DocumentHelper.createElement("custom");
      custom.addAttribute("class", tile.getClass().getSimpleName());
      custom.addAttribute("source", tile.info().message());
      return custom;
    } else if (name.contains("Key") || name.contains("Lock") && "exitLock".equals(name)) {
      Element element = DocumentHelper.createElement(name.contains("Key") ? "key" : "lock");
      element.addAttribute(
          "color", name.replace("Key", "").replace("Lock", "").toLowerCase(Locale.ENGLISH));
      return element;
    } else if ("info".equals(name)) {
      Element element = DocumentHelper.createElement("info");
      element.addAttribute("message", ((Info) tile).message());
      return element;
    } else {
      return DocumentHelper.createElement(name);
    }
  }

  /**
   * Deserialise a tile from an XML element.
   *
   * @param element The XML element to deserialise
   * @return The deserialised tile
   */
  private static Tile deserialiseTile(Element element, Loc loc) {
    BiFunction<Element, Loc, Tile> tiler = tagToTiler.get(element.getName());

    if (tiler == null) {
      tiler = DomainPersistency::defaultTiler;
    }

    return tiler.apply(element, loc);
  }

  /**
   * Serialise an inventory to an XML document.
   *
   * @param inventory The inventory to serialise
   * @return The serialised inventory as an XML document
   */
  private static Element serialiseInventory(Inventory inventory) {
    Element root = DocumentHelper.createElement("inventory");
    for (Tile item : inventory.getItems()) {
      root.add(serialiseTile(item));
    }
    root.addAttribute("size", inventory.size() + "");
    root.addAttribute("coins", inventory.coins() + "");
    return root;
  }

  /**
   * Deserialise inventory from an XML document.
   *
   * @param root The XML element to deserialise
   * @return The deserialised inventory
   */
  private static Inventory deserialiseInventory(Element root) {
    int size = Integer.parseInt(root.attributeValue("size"));
    List<Tile> items = new ArrayList<>();
    for (Element item : root.elements()) {
      items.add(deserialiseTile(item, new Loc(0, 0)));
    }
    int coins = Integer.parseInt(root.attributeValue("coins"));
    return new Inventory(size, coins, items);
  }

  /**
   * Create a fallback maze to be used when the level files cannot be read. As this maze is more
   * basic than the default maze, it should only be used as a fallback.
   *
   * @return The fallback maze
   */
  public static Maze fallbackMaze() {
    int width = 10;
    int height = 10;
    Tile[][] gameArray = new Tile[height][width];

    // initialize the maze with Empty_tile. cant have null tiles
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        gameArray[i][j] = new Floor(new TileInfo(new Loc(i, j)));
      }
    }
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        // make the walls
        if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
          gameArray[i][j] = new Wall(new TileInfo(new Loc(i, j)));
        }
      }
    }

    Maze m = new Maze(gameArray);

    // display different tile types
    m.setTileAt(new Loc(2, 1), TileType.Hero);
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

  /**
   * Load a saved game from a slot. The associated file exists in res/saves/[slot].xml
   *
   * @param slot The slot to load from
   * @return The loaded domain
   * @throws DocumentException If the XML document is malformed
   */
  public static Domain loadSave(int slot) throws DocumentException {
    SAXReader reader = new SAXReader();
    try {
      InputStream in = new FileInputStream("res/saves/" + slot + ".xml");
      Document document = reader.read(in);
      return deserialiseDomain(document.getRootElement());
    } catch (FileNotFoundException e) {
      return getInitial();
    }
  }

  /**
   * Delete a saved game from a slot. The associated file in res/saves/[slot].xml is deleted.
   *
   * @param slot The slot to delete
   * @return True if the save was deleted, false otherwise
   * @throws IOException If the file could not be deleted
   */
  public static boolean delete(int slot) throws IOException {
    File file = new File("res/saves/" + slot + ".xml");
    return file.delete();
  }

  /**
   * Get the initial domain object. It contains all levels found in res/levels.
   *
   * @return The initial domain
   */
  public static Domain getInitial() {
    try {
      SAXReader reader = new SAXReader();
      // list files in res/levels
      File dir = new File("res/levels");
      File[] filesArr = dir.listFiles();
      if (filesArr == null) {
        throw new DocumentException("No levels found");
      }
      List<File> files = Arrays.asList(filesArr);
      files.sort(
          new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
              return o1.getName().compareTo(o2.getName());
            }
          });
      List<Level> levels = new ArrayList<Level>();
      for (File file : files) {
        if (file.getName().endsWith(".xml")) {
          Document document = reader.read(file);
          levels.add(deserialiseLevel(document.getRootElement()));
        }
      }
      return new Domain(levels, 1);
    } catch (DocumentException e) {
      e.printStackTrace();
      return new Domain(List.of(fallbackMaze()), new Inventory(8), 1);
    }
  }

  /**
   * Save a domain object to a save slot. The associated file exists in res/saves/[slot].xml
   *
   * @param domain The domain to save
   * @param slot   The save slot to save to
   * @throws IOException If the file could not be written
   */
  public static void save(Domain domain, int slot) throws IOException {
    Element root = serialiseDomain(domain);
    Document document = DocumentHelper.createDocument(root);

    File dir = new File("res/saves");
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        throw new IOException("Could not create save directory");
      }
    }

    FileOutputStream fileStream = new FileOutputStream("res/saves/" + slot + ".xml");
    OutputStreamWriter out = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
    document.write(out);
    out.close();
  }
}
