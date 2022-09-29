package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.util.EnumMap;
import java.util.HashMap;

import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;

public class Helper {
    public static EnumMap<TileType, String> typeToString = new EnumMap<>(TileType.class);
    static {
        typeToString.put(TileType.Hero, "hero");
        typeToString.put(TileType.Enemy, "enemy");
        typeToString.put(TileType.Empty, "empty");
        typeToString.put(TileType.Floor, "floor");
        typeToString.put(TileType.Wall, "wall");
        typeToString.put(TileType.ExitDoor, "exitDoor");
        typeToString.put(TileType.ExitDoorOpen, "exitDoorOpen");
        typeToString.put(TileType.BlueLock, "blueLock");
        typeToString.put(TileType.GreenLock, "greenLock");
        typeToString.put(TileType.OrangeLock, "orangeLock");
        typeToString.put(TileType.YellowLock, "yellowLock");
        typeToString.put(TileType.BlueKey, "blueKey");
        typeToString.put(TileType.GreenKey, "greenKey");
        typeToString.put(TileType.OrangeKey, "orangeKey");
        typeToString.put(TileType.YellowKey, "yellowKey");
        typeToString.put(TileType.Coin, "coin");
        typeToString.put(TileType.Null, "null");
    }

    public static HashMap<String, TileType> stringToType = new HashMap<>();
    static {
        stringToType.put("hero", TileType.Hero);
        stringToType.put("enemy", TileType.Enemy);
        stringToType.put("empty", TileType.Empty);
        stringToType.put("floor", TileType.Floor);
        stringToType.put("wall", TileType.Wall);
        stringToType.put("exitDoor", TileType.ExitDoor);
        stringToType.put("exitDoorOpen", TileType.ExitDoorOpen);
        stringToType.put("blueLock", TileType.BlueLock);
        stringToType.put("greenLock", TileType.GreenLock);
        stringToType.put("orangeLock", TileType.OrangeLock);
        stringToType.put("yellowLock", TileType.YellowLock);
        stringToType.put("blueKey", TileType.BlueKey);
        stringToType.put("greenKey", TileType.GreenKey);
        stringToType.put("orangeKey", TileType.OrangeKey);
        stringToType.put("yellowKey", TileType.YellowKey);
        stringToType.put("coin", TileType.Coin);
        stringToType.put("null", TileType.Null);
    }
}
