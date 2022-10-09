package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.concurrent.TimeoutException;

import custom.Tiles.Enemy;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Helper;
import nz.ac.vuw.ecs.swen225.gp6.recorder.Record;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.Pair;
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
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;

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
     * Deserialize the settings xml
     * 
     * @return Settings object
     */
    public static Settings deserializeSettings(Element root) {
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
    }

    /**
     * Serialize the settings xml
     * 
     * @param settings Settings object
     * @return XML document
     */
    public static Document serializeSettings(Settings settings) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("settings");

        // add texture pack
        root.addElement("texturePack").addText(settings.texturePack());

        // add key bindings
        Element keyBindings = root.addElement("keyBindings");
        for (Keys key : Keys.values()) {
            keyBindings.addElement(key.name()).addText(settings.keyBindings().get(key));
        }

        // add music enabled
        root.addElement("musicEnabled").addText(settings.musicEnabled().toString());

        return document;
    }

    /**
     * Save the settings to res/settings.xml
     * 
     * @param settings The settings to save
     */

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
        levels.addAttribute("current", Integer.toString(domain.getCurrentLevel()));
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
            mazes.add(deserializeMaze(level));
        }
        int currentLevel = Integer.parseInt(levels.attributeValue("current"));
        Inventory inv = deserializeInventory(root.element("inventory"));
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
     * @param root The XML element to deserialise
     * @return The deserialised inventory
     */
    public static Inventory deserializeInventory(Element root) {
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
    private static TileType deserializeTileType(Element element) {
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
    public static Maze deserializeMaze(Element root) {
        String thing = root.asXML();
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
                } else if (name.equals("custom")) {
                    String customTile = tile.attributeValue("name");
                    try {
                        Class<?> clazz = Class.forName("custom.Tiles." + customTile);
                        Constructor<?> ctor = clazz.getConstructor(TileInfo.class);
                        Tile object = (Tile) ctor.newInstance(new TileInfo(new Loc(x, y),
                                Character.toLowerCase(customTile.charAt(0)) + customTile.substring(1)));
                        // Tile object = new Enemy(new TileInfo(new Loc(x, y)));
                        maze.setTileAt(new Loc(x, y), object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    maze.setTileAt(new Loc(x, y), Helper.stringToType.get(name));
                }
            }
        }
        return maze;
    }

    /**
     * Serialize a record timeline object to an XML document
     * 
     * @param timeline The timeline to serialize
     * @return The serialized timeline
     */
    public static Document serializeRecordTimeline(Stack<Pair<Long, Actions>> timeline) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("recorder");
        root.addAttribute("size", timeline.size() + "");
        for (Pair<Long, Actions> pair : timeline) {
            Element action = root.addElement(pair.getValue().toString());
            action.addAttribute("time", pair.getKey() + "");
        }
        return document;
    }

    /**
     * Deserialize a record timeline object from an XML document
     * 
     * @param document The XML document to deserialize
     * @return The deserialized timeline
     */
    public static Stack<Pair<Long, Actions>> deserializeRecordTimeline(Document document) {
        Element root = document.getRootElement();
        Stack<Pair<Long, Actions>> timeline = new Stack<Pair<Long, Actions>>();
        for (Element action : root.elements()) {
            timeline.add(new Pair<Long, Actions>(Long.parseLong(action.attributeValue("time")),
                    Actions.valueOf(action.getName())));
        }
        return timeline;
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

        File dir = new File("res/saves");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileWriter out = new FileWriter("res/saves/" + slot + ".xml");
        document.write(out);
        out.close();
    }

    /**
     * Load settings from file
     * 
     * @return The settings
     */
    public static Settings loadSettings() throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("res/settings.xml"));
        return deserializeSettings(document.getRootElement());
    }

    /**
     * Load a maze from a file
     * 
     * @param path The file path to load from
     * @return The loaded maze
     */
    public static Domain loadSave(int slot) throws DocumentException {
        SAXReader reader = new SAXReader();
        try {
            InputStream in = new FileInputStream("res/saves/" + slot + ".xml");
            Document document = reader.read(in);
            return deserializeDomain(document);
        } catch (FileNotFoundException e) {
            return getInitialDomain();
        }
    }

    /**
     * Delete a save file
     */
    public static void deleteSave(int slot) throws IOException {
        File file = new File("res/save/" + slot + ".xml");
        if (!file.delete()) {
            throw new IOException("Could not delete file");
        }
    }

    /**
     * Load saves 1, 2, 3 to a list
     * 
     * @return The list of saves
     */
    @Deprecated
    public static List<Domain> loadSaves() throws DocumentException {
        List<Domain> saves = new ArrayList<Domain>();
        for (int i = 1; i <= 3; ++i) {
            saves.add(loadSave(i));
        }
        return saves;
    }

    /**
     * Get the initial domain
     * 
     * @return The initial domain
     */
    public static Domain getInitialDomain() {
        try {
            SAXReader reader = new SAXReader();
            // list files in res/levels
            File dir = new File("res/levels");
            List<File> files = Arrays.asList(dir.listFiles());
            files.sort(new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            List<Maze> mazes = new ArrayList<Maze>();
            for (File file : files) {
                if (file.getName().endsWith(".xml")) {
                    Document document = reader.read(file);
                    mazes.add(deserializeMaze(document.getRootElement()));
                }
            }
            return new Domain(mazes, new Inventory(8), 1);
        } catch (DocumentException e) {
            e.printStackTrace();
            return new Domain(List.of(nz.ac.vuw.ecs.swen225.gp6.domain.Helper.makeMaze(),
                    nz.ac.vuw.ecs.swen225.gp6.domain.Helper.makeMaze()), new Inventory(8), 1);
        }
    }

}
