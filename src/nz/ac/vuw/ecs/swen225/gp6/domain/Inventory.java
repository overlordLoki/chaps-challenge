package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Inventory {
    private Tile[] items;
    private int size;
    private int coins;

    public Inventory(int size){
        this.size = size;
        this.items = new Tile[size];
        this.coins = 0;
    }

    /*
     * gets the items (as an umodifiable list)
     */
    public List<Tile> getItems(){return Collections.unmodifiableList(List.of(items));}

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
        int index = IntStream.range(0, size)
        .filter(i -> items[i] != null)
        .findFirst()
        .orElse(-1);

        if(index == -1) return false; //if no empty space found return false
        items[index] = tile; 
        return true;
    }

    /**
     * @return true if a tile name found in inv, otherwise false
     */
    public boolean hasItem(TileType itemName){
        return Arrays.stream(items).anyMatch(t -> t.getType() == itemName);
    }

    /**
     * @return true if item was found and removed, otherwise false
     */
    public boolean removeItem(TileType itemName){
        int index = IntStream.range(0, size)
        .filter(i -> items[i].getType() == itemName)
        .findFirst()
        .orElse(-1);

        if(index == -1) return false; //if tile type not found return false
        items[index] = null;
        return true;
    }
}
