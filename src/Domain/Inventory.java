package Domain;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Inventory {
    private Tile[] items;
    private int coins;

    public Inventory(int size){
        items = new Tile[size];
        coins = 0;
    }

    /**
     * gets number of coins
     */
    public int coins(){return coins;}

    /*
     * increments number of coins
     */
    public void addCoin(){coins += 1;}

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
    public boolean hasItem(TileType itemName){
        return Arrays.stream(items).anyMatch(t -> t.getName() == itemName);
    }

    /**
     * @return true if item was found and removed, otherwise false
     */
    public boolean removeItem(TileType itemName){
        for(int i = 0; i < items.length; i ++){
            if(items[i].getName() == itemName){
                items[i] = null;
                return true;
            }
        }
        return false;
    }
}
