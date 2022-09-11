package Domain;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Inventory {
    Tile[] items;

    public Inventory(int size){
        items = new Tile[size];
    }

    /**
     * adds a tile to first empty place in inventory
     * 
     * @return true if item was added, false if it wasn't (since its full)
     */
    public boolean addItem(Tile tile){
        for(int i = 0; i < items.length; i ++){
            if(items[i] == null){
                items[i] = tile;
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if a tile name found in inv, otherwise false
     */
    public boolean hasItem(TileName itemName){
        return Arrays.stream(items).anyMatch(t -> t.getName() == itemName);
    }

    /**
     * @return true if item was found and removed, otherwise false
     */
    public boolean removeItem(TileName itemName){
        for(int i = 0; i < items.length; i ++){
            if(items[i].getName() == itemName){
                items[i] = null;
                return true;
            }
        }
        return false;
    }
}
