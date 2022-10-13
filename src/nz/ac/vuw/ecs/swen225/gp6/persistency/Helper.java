package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.dom4j.Element;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

public class Helper {
    public static Map<TileType, String> typeToString = new EnumMap<TileType, String>(TileType.class) {
        {
            put(TileType.Hero, "hero");
            put(TileType.Empty, "empty");
            put(TileType.Floor, "floor");
            put(TileType.Wall, "wall");
            put(TileType.ExitDoor, "exitDoor");
            put(TileType.ExitDoorOpen, "exitDoorOpen");
            put(TileType.BlueLock, "blueLock");
            put(TileType.GreenLock, "greenLock");
            put(TileType.OrangeLock, "orangeLock");
            put(TileType.YellowLock, "yellowLock");
            put(TileType.BlueKey, "blueKey");
            put(TileType.GreenKey, "greenKey");
            put(TileType.OrangeKey, "orangeKey");
            put(TileType.YellowKey, "yellowKey");
            put(TileType.Coin, "coin");
            put(TileType.Info, "info");
            put(TileType.Null, "null");
        }
    };

    public static Map<String, TileType> stringToType = new HashMap<String, TileType>() {
        {
            put("hero", TileType.Hero);
            put("empty", TileType.Empty);
            put("floor", TileType.Floor);
            put("wall", TileType.Wall);
            put("exitDoor", TileType.ExitDoor);
            put("exitDoorOpen", TileType.ExitDoorOpen);
            put("blueLock", TileType.BlueLock);
            put("greenLock", TileType.GreenLock);
            put("orangeLock", TileType.OrangeLock);
            put("yellowLock", TileType.YellowLock);
            put("blueKey", TileType.BlueKey);
            put("greenKey", TileType.GreenKey);
            put("orangeKey", TileType.OrangeKey);
            put("yellowKey", TileType.YellowKey);
            put("coin", TileType.Coin);
            put("info", TileType.Info);
            put("null", TileType.Null);
        }
    };

    protected static Tile defaultTiler(Element element, Loc loc) {
        String name = element.getName();
        return TileType.makeTile(stringToType.get(name), new TileInfo(loc));
    }

    private static Tile makeTile(String name, Loc loc) {
        return TileType.makeTile(stringToType.get(name), new TileInfo(loc));
    }

    public static HashMap<String, BiFunction<Element, Loc, Tile>> tagToTiler = new HashMap<>() {
        {
            put("hero", Helper::defaultTiler);
            put("empty", Helper::defaultTiler);
            put("floor", Helper::defaultTiler);
            put("wall", Helper::defaultTiler);
            put("exitDoor", Helper::defaultTiler);
            put("key", (element, loc) -> {
                return makeTile(element.attributeValue("color") + "Key", loc);
            });
            put("lock", (element, loc) -> {
                return makeTile(element.attributeValue("color") + "Lock", loc);
            });
            put("coin", Helper::defaultTiler);
            put("info", Helper::defaultTiler);
            put("custom", (element, loc) -> {
                String customTile = element.attributeValue("class");
                String source = element.attributeValue("source");

                try {
                    File jar = new File("res/levels/" + source);

                    URLClassLoader child = new URLClassLoader(
                            new URL[] { jar.toURI().toURL() },
                            DomainPersistency.class.getClassLoader());

                    Class<?> clazz = Class.forName("custom.tiles." + customTile, true, child);
                    Constructor<?> ctor = clazz.getConstructor(TileInfo.class);
                    return (Tile) ctor.newInstance(new TileInfo(loc, 0,
                            Character.toLowerCase(customTile.charAt(0)) + customTile.substring(1), source));
                } catch (Exception e) {
                    e.printStackTrace();
                    return TileType.makeTile(TileType.Null, new TileInfo(loc));
                }
            });
            put("info", (element, loc) -> {
                String message = element.attributeValue("message");
                return TileType.makeTile(TileType.Info, new TileInfo(loc, 0, "", message));
            });
            put("null", Helper::defaultTiler);
        }
    };
}
