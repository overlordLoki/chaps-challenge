package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Helper;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import nz.ac.vuw.ecs.swen225.gp6.app.*;

public class Persistency {
    public record Log(LocalDateTime date, String message) {
    }

    /**
     * Log the string to the log file
     * 
     * @param string The string to log
     */
    public static void log(String message) throws IOException {
        // get time and date string
        String time = LocalDateTime.now().toString();
        // write to file
        FileWriter out = null;
        out = new FileWriter("res/log.txt", true);
        out.write(time + ": " + message + "\n");
        out.close();
    }

    /**
     * Get the log file
     * 
     * @return List of log entries
     */
    public static List<Log> getLogs() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("res/log.txt"));

        return lines.stream().map(line -> {
            if (!line.contains(": ")) {
                return null;
            }
            String dateString = line.substring(0, line.indexOf(": "));
            LocalDateTime date = LocalDateTime.parse(dateString);
            String message = line.substring(line.indexOf(": ") + 1).strip();
            return new Log(date, message);
        }).filter(Objects::nonNull).toList();
    }

    enum Keys {
        UP, DOWN, LEFT, RIGHT, PAUSE, RESUME, JUMP1, JUMP2, QUIT, SAVE, RELOAD
    }

    record Settings(String texturePack, EnumMap<Keys, String> keyBindings, Boolean musicEnabled) {
    }

    /**
     * Get the settings from res/settings.xml
     * 
     * @return Settings object
     */
    public static Settings getSettings() throws IOException {
        // read file
        String content = new String(Files.readAllBytes(Paths.get("res/settings.xml"))).strip();

        // parse xml
        try {
            Document doc = DocumentHelper.parseText(content);
            Element root = doc.getRootElement();

            // get texture pack
            String texturePack = root.element("texturePack").getText();

            // get key bindings
            EnumMap<Keys, String> keyBindings = new EnumMap<>(Keys.class);
            for (Keys key : Keys.values()) {
                keyBindings.put(key, root.element("keyBindings").element(key.name()).getText());
            }

            // get music enabled
            Boolean musicEnabled = Boolean.parseBoolean(root.element("musicEnabled").getText());

            return new Settings(texturePack, keyBindings, musicEnabled);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Serialise a domain to an XML document
     *
     * @param domain The domain to serialise
     *
     * @return The serialised domain as an XML document
     */
    public static Document serializeDomain(Domain domain) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("domain");
        Element levels = root.addElement("levels");
        for (int i = 0; i < domain.getMazes().size(); i++) {
            Maze maze = domain.getMazes().get(i);
            Document mazeDoc = serializeMaze(maze, i);
            levels.add(mazeDoc.getRootElement());
        }
        levels.addAttribute("current", Integer.toString(domain.getLvl() - 1));
        root.add(serializeInventory(domain.getInv()).getRootElement());

        return document;
    }

    /**
     * Deserialise a domain from an XML document
     * 
     * @param document The XML document to deserialise
     * @return The deserialised domain
     */
    public static Domain deserializeDomain(Document document) {
        Element root = document.getRootElement();
        Element levels = root.element("levels");
        List<Maze> mazes = new ArrayList<>();
        for (Element level : levels.elements()) {
            mazes.add(deserializeMaze(level.getDocument()));
        }
        int currentLevel = Integer.parseInt(levels.attributeValue("current"));
        Inventory inv = deserializeInventory(root.element("inventory").getDocument());
        return new Domain(mazes, inv, currentLevel);
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
    public static Document serializeMaze(Maze maze, int i) {
        Document document = DocumentHelper.createDocument();
        Element level = document.addElement("level");
        level.addAttribute("index", Integer.toString(i));
        level.addAttribute("name", "Level " + (i + 1));
        Element grid = level.addElement("grid");
        grid.addAttribute("width", Integer.toString(maze.width()));
        grid.addAttribute("height", Integer.toString(maze.height()));
        for (int x = 0; x < maze.width(); x++) {
            for (int y = 0; y < maze.height(); y++) {
                Tile tile = maze.getTileAt(x, y);
                if (tile != null && tile.type() != TileType.Null) {
                    Element cell = grid.addElement("cell");
                    cell.addAttribute("x", Integer.toString(x));
                    cell.addAttribute("y", Integer.toString(y));
                    cell.add(serializeTile(tile).getRootElement());
                }
            }
        }

        return document;
    }

    /**
     * Serialise a tile to an XML element
     * 
     * @param tile The tile to serialise
     * 
     * @return The serialised tile as an XML element
     */
    public static Document serializeTile(Tile tile) {
        Document document = DocumentHelper.createDocument();
        String name = Helper.typeToString.get(tile.type());
        if (name.contains("Key") || name.contains("Lock") && name.equals("exitLock")) {
            Element element = document.addElement(name.contains("Key") ? "key" : "lock");
            element.addAttribute("color", name.replace("Key", "").replace("Lock", "").toLowerCase());
        } else {
            document.addElement(name);
        }
        return document;
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
    public static Document serializeInventory(Inventory inventory) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("inventory");
        for (Tile item : inventory.getItems()) {
            root.add(serializeTile(item).getRootElement());
        }
        root.addAttribute("size", inventory.size() + "");
        return document;
    }

    /**
     * Deserialise inventory from an XML document
     * 
     * @param document The XML document to deserialise
     * @return The deserialised inventory
     */
    public static Inventory deserializeInventory(Document document) {
        Element root = document.getRootElement();
        Inventory inv = new Inventory(Integer.parseInt(root.attributeValue("size")));
        for (Element item : root.elements()) {
            inv.addItem(TileType.makeTile(deserializeTileType(item), new TileInfo(new Loc(0, 0))));
        }
        return inv;
    }

    /**
     * Deserialise a tile type from an XML element
     * 
     * @param element The XML element to deserialise
     * @return The deserialised tile type
     */
    public static TileType deserializeTileType(Element element) {
        String name = element.getName();
        if (name.equals("key")) {
            String color = element.attributeValue("color");
            return Helper.stringToType.get(color + "Key");
        } else if (name.equals("lock")) {
            String color = element.attributeValue("color");
            return Helper.stringToType.get(color + "Lock");
        } else {
            return Helper.stringToType.get(name);
        }
    }

    /**
     * Deserialise a maze from an XML document
     * 
     * @param xml
     * @return The unserialised maze
     */
    public static Maze deserializeMaze(Document doc) {
        Element root = doc.getRootElement();
        Element grid = root.element("grid");
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
                String name = tile.getName();
                if (name.equals("key")) {
                    String color = tile.attributeValue("color");
                    maze.setTileAt(new Loc(x, y), Helper.stringToType.get(color + "Key"));
                } else if (name.equals("lock")) {
                    String color = tile.attributeValue("color");
                    maze.setTileAt(new Loc(x, y), Helper.stringToType.get(color + "Lock"));
                } else {
                    maze.setTileAt(new Loc(x, y), Helper.stringToType.get(name));
                }
            }
        }
        return maze;
    }

    /**
     * Save a domain to a file
     *
     * @param domain The domain to save
     *
     * @param path   The file path to save to
     */
    public static void saveDomain(Domain domain, int slot) throws IOException {
        Document document = serializeDomain(domain);

        FileWriter out = new FileWriter("res/save/" + slot + ".xml");
        document.write(out);
        out.close();
    }

    /**
     * List saved games in res/saves
     * 
     * @return List of games
     */
    public static List<Domain> loadSaves() {
        List<Domain> saves = new ArrayList<Domain>();
        saves.add(new Domain(List.of(nz.ac.vuw.ecs.swen225.gp6.domain.Helper.makeMaze()), new Inventory(10), 1));
        saves.add(new Domain(List.of(nz.ac.vuw.ecs.swen225.gp6.domain.Helper.makeMaze()), new Inventory(10), 1));
        saves.add(new Domain(List.of(nz.ac.vuw.ecs.swen225.gp6.domain.Helper.makeMaze()), new Inventory(10), 1));
        return saves;
    }

    /**
     * Get the initial domain
     * 
     * @return The initial domain
     */
    public static Domain getInitialDomain() {
        File file = new File("res/levels/level1.xml");
        try {
            Document document = new SAXReader().read(file);
            Maze maze = deserializeMaze(document);
            return new Domain(List.of(maze), new Inventory(8), 1);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new Domain(List.of(nz.ac.vuw.ecs.swen225.gp6.domain.Helper.makeMaze()), new Inventory(8), 1);
        }
    }

    /**
     * Get all mazes in res/mazes
     * 
     * @return A list of maze objects
     */
    public static List<Maze> getMazes() {
        return new ArrayList<Maze>();
    }

}
