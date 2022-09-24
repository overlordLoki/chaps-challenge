package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.TileType;

public class Inventory {
    private Tile[] items;
    private int size;
    private int coins;

    public Inventory(int size){
        this.size = size;
        this.items = new Tile[size];
        this.coins = 0;

        //fill inventory with null typed tiles
        IntStream.range(0, size).forEach(i -> items[i] = new Tile(TileType.Null, null));
    }

    public Inventory(int size, int coins, List<Tile> items){
        this.size = size;
        this.coins = coins;
        this.items = new Tile[size];
        
        //fill inventory with null typed tiles
        IntStream.range(0, size).forEach(i -> this.items[i] = new Tile(TileType.Null, null));

        //add items to inventory
        items.forEach(this::addItem);
    }

    /*
     * gets the items (as an umodifiable list)
     */
    public List<Tile> getItems(){return Arrays.stream(items).filter(t -> t.type() != TileType.Null).toList();}
    
    /*
     * gets the size of the inventory
     */
    public int size()  {return size;}
    
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
        .filter(i -> items[i].type() == TileType.Null)
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
        return Arrays.stream(items).anyMatch(t -> t.type() == itemName);
    }

    /**
     * @return true if item was found and removed, otherwise false
     */
    public boolean removeItem(TileType itemName){
        int index = IntStream.range(0, size)
        .filter(i -> items[i].type() == itemName)
        .findFirst()
        .orElse(-1);

        if(index == -1) return false; //if tile type not found return false
        items[index] = new Tile(TileType.Null, null); //set to null tile type
        return true;
    }

    public String toString(){
        String r = "Inv(" + size + "): ";
        for(Tile item : items){
            if(item.type() != TileType.Null) r += item.getSymbol()+ ", ";
        }
        return r.substring(0, r.length() - 2);
    }

}
