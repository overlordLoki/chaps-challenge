package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
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

public class DomainPersistency {
    /**
     * Serialise a domain to an XML document
     *
     * @param domain The domain to serialise
     *
     * @return The serialised domain as an XML document
     */
    public static Element serialiseDomain(Domain domain) {
        Element root = DocumentHelper.createElement("domain");
        Element levels = root.addElement("levels");
        for (int i = 0; i < domain.getMazes().size(); i++) {
            Level level = domain.getLevels().get(i);
            levels.add(serialiseLevel(level));
        }
        levels.addAttribute("current", Integer.toString(domain.getCurrentLevel()));
        return root;
    }

    /**
     * Deserialise a domain from an XML document
     * 
     * @param root The XML element to deserialise
     * @return The deserialised domain
     */
    public static Domain deserialiseDomain(Element root) {
        Element levelEls = root.element("levels");
        int currentLevel = Integer.parseInt(levelEls.attributeValue("current"));
        List<Level> levels = new ArrayList<Level>();
        for (Element level : levelEls.elements()) {
            levels.add(deserialiseLevel(level));
        }
        return new Domain(levels, currentLevel);
    }

    /**
     * Serialise a level to an XML element
     *
     * @param level The maze to serialise
     *
     * @return The serialised level as an XML element
     */
    public static Element serialiseLevel(Level level) {
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
     * Deserialise a level from an XML element
     * 
     * @param level The XML element to deserialise
     * @return The deserialised level
     */
    public static Level deserialiseLevel(Element level) {
        int index = Integer.parseInt(level.attributeValue("index"));
        int timeLimit = Integer
                .parseInt(level.attributeValue("timeLimit") == null ? "60" : level.attributeValue("timeLimit"));
        long timeCurrent = Long
                .parseLong(level.attributeValue("timeCurrent") == null ? "0" : level.attributeValue("timeCurrent"));
        String direction = level.attributeValue("direction") == null ? "None" : level.attributeValue("direction");
        Direction dir = Direction.valueOf(direction);

        Maze maze = deserialiseMaze(level.element("grid"));
        Element inventory = level.element("inventory");
        Inventory inv;

        if (inventory == null) {
            inv = new Inventory(8);
        } else {
            inv = deserialiseInventory(inventory);
        }

        return new Level(maze, inv, index + 1, timeLimit, timeCurrent, dir);
    }

    /**
     * Serialise a maze to an XML document
     * 
     * Example:
     * <level index="1" name="Level 1">
     * <grid width="10" height="10">
     * <cell x="5" y="0">
     * <wall />
     * </cell>
     * </grid>
     * </level>
     * 
     * @param maze The maze to serialise
     */
    public static Element serialiseMaze(Maze maze) {
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
     * Deserialise a maze from an XML document
     * 
     * @param grid The XML element to deserialise
     * @return The unserialised maze
     */
    public static Maze deserialiseMaze(Element grid) {
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
     * Serialise a tile to an XML element
     * 
     * @param tile The tile to serialise
     * 
     * @return The serialised tile as an XML element
     */
    public static Element serialiseTile(Tile tile) {
        String name = Helper.typeToString.get(tile.type());

        if (null == name) {
            // it's a custom tile
            Element custom = DocumentHelper.createElement("custom");
            custom.addAttribute("class", tile.getClass().getSimpleName());
            custom.addAttribute("source", tile.info().message());
            return custom;
        } else if (name.contains("Key") || name.contains("Lock") && name.equals("exitLock")) {
            Element element = DocumentHelper.createElement(name.contains("Key") ? "key" : "lock");
            element.addAttribute("color", name.replace("Key", "").replace("Lock", "").toLowerCase());
            return element;
        } else if (name.equals("info")) {
            Element element = DocumentHelper.createElement("info");
            element.addAttribute("message", ((Info) tile).message());
            return element;
        } else {
            return DocumentHelper.createElement(name);
        }
    }

    /**
     * Deserialise a tile from an XML element
     * 
     * @param element The XML element to deserialise
     * @return The deserialised tile
     */
    private static Tile deserialiseTile(Element element, Loc loc) {
        BiFunction<Element, Loc, Tile> tiler = Helper.tagToTiler.get(element.getName());

        if (tiler == null) {
            tiler = Helper::defaultTiler;
        }

        return tiler.apply(element, loc);
    }

    /**
     * Serialise an inventory to an XML document
     * 
     * Example:
     * <inventory>
     * <key color="green" />
     * </inventory>
     * 
     * @param inventory The inventory to serialise
     */
    public static Element serialiseInventory(Inventory inventory) {
        Element root = DocumentHelper.createElement("inventory");
        for (Tile item : inventory.getItems()) {
            root.add(serialiseTile(item));
        }
        root.addAttribute("size", inventory.size() + "");
        root.addAttribute("coins", inventory.coins() + "");
        return root;
    }

    /**
     * Deserialise inventory from an XML document
     * 
     * @param root The XML element to deserialise
     * @return The deserialised inventory
     */
    public static Inventory deserialiseInventory(Element root) {
        int size = Integer.parseInt(root.attributeValue("size"));
        List<Tile> items = new ArrayList<>();
        for (Element item : root.elements()) {
            items.add(deserialiseTile(item, new Loc(0, 0)));
        }
        int coins = Integer.parseInt(root.attributeValue("coins"));
        return new Inventory(size, coins, items);
    }

    /**
     * 
     * 
     * @return The loaded maze
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
     * Delete a save file
     */
    public static boolean delete(int slot) throws IOException {
        File file = new File("res/saves/" + slot + ".xml");
        return file.delete();
    }

    /**
     * Create a new test maze
     * 
     * @return The test maze
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
     * Get the initial domain
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
            files.sort(new Comparator<File>() {
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
     * Save a domain to a file
     *
     * @param domain The domain to save
     *
     * @param slot   The save slot to save to
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
        OutputStreamWriter out = new OutputStreamWriter(fileStream, "UTF-8");
        document.write(out);
        out.close();
    }

}
