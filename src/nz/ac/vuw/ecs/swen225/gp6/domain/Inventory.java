package nz.ac.vuw.ecs.swen225.gp6.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.Tile;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Null;

/**
 * The Inventory class represents the space that a player holds items that they have picked up. Each
 * level has a separate inventory.
 *
 * @author Name: Mahdi Najafi ID: 300606634
 */
public class Inventory {

  private final Tile[] items;
  private final int size;
  private int coins;

  /**
   * constructor for making an inventory with specified size, items are all Null tile type, and the
   * coins are 0.
   *
   * @param size - size of inventory. The number of items the user can carry at once.
   * @throws IllegalArgumentException - if size is less than 0
   */
  public Inventory(int size) {
    if (size < 1) {
      throw new IllegalArgumentException("Inventory must include atleast one item.");
    }

    this.size = size;
    this.items = new Tile[size];
    this.coins = 0;

    //fill inventory with null typed tiles
    IntStream.range(0, size).forEach(i -> items[i] = new Null(new TileInfo(null)));
  }

  /**
   * constructor for making an inventory with specified size and items, and the number of coins.
   *
   * @param size  - size of inventory. The number of items the user can carry at once.
   * @param coins - the number of coins the inventory
   * @param items - the items that are in inventory (all empty spots will be filled with Null type
   *              tiles)
   * @throws IllegalArgumentException if size is less than 1, or coins are negative, or list of
   *                                  items is null or bigger than size
   */
  public Inventory(int size, int coins, List<Tile> items) {
    if (size < 1) {
      throw new IllegalArgumentException("Inventory must include atleast one item.");
    }
    if (coins < 0) {
      throw new IllegalArgumentException("Inventory's coin number is cannot be negative.");
    }
    if (items == null || items.size() > size || items.contains(null)) {
      throw new IllegalArgumentException("wrong input for list of items in Inventory");
    }

    this.size = size;
    this.coins = coins;
    this.items = new Tile[size];

    //fill inventory with null typed tiles
    IntStream.range(0, size).forEach(i -> this.items[i] = new Null(new TileInfo(null)));

    //add items to inventory
    items.forEach(this::addItem);
  }

  /**
   * gets the items (as an umodifiable list).
   *
   * @return list of tiles in inventory (it will not include the Null type tiles)
   */
  public List<Tile> getItems() {
    return Arrays.stream(items).filter(t -> t.type() != TileType.Null).toList();
  }

  /**
   * if the inventory is full, returns true.
   *
   * @return true if inventory is full, false otherwise
   */
  public boolean isFull() {
    return Arrays.stream(items).allMatch(t -> t.type() != TileType.Null);
  }

  /**
   * gets the size of the inventory.
   *
   * @return the size of the inventory
   */
  public int size() {
    return size;
  }

  /**
   * gets number of coins.
   *
   * @return number of coins
   */
  public int coins() {
    return coins;
  }

  /**
   * increments number of coins.
   */
  public void addCoin() {
    coins++;
  }

  /**
   * adds a tile to first empty place in inventory.
   *
   * @return true if item was added, false if it wasn't (since its full)
   */
  public boolean addItem(Tile tile) {
    int index = IntStream.range(0, size)
        .filter(i -> items[i].type() == TileType.Null)
        .findFirst()
        .orElse(-1);

    if (index == -1) {
      return false; //if no empty space found return false
    }
    items[index] = tile;
    return true;
  }

  /**
   * counts the number of a certain items tile in the inventory that satisfy the given predicate.
   *
   * @param p - the predicate
   * @return the number of items in the inventory that satisfy p
   */
  public int countItem(Predicate<Tile> p) {
    return (int) Arrays.stream(items).filter(p).count();
  }

  /**
   * finds out if the inventory has a certain typed item.
   *
   * @return true if a tile type found in inv, otherwise false
   */
  public boolean hasItem(TileType itemName) {
    return Arrays.stream(items).anyMatch(t -> t.type() == itemName);
  }

  /**
   * finds first instance of a given item type and removes it. Returns true if an item was found and
   * removed otherwise false.
   *
   * @param itemName - the item type to remove.
   * @return true if item type was found and removed, otherwise false
   */
  public boolean removeItem(TileType itemName) {
    int index = IntStream.range(0, size)
        .filter(i -> items[i].type() == itemName)
        .findFirst()
        .orElse(-1);

    if (index == -1) {
      return false; //if tile type not found return false
    }
    items[index] = new Null(new TileInfo(null)); //set to null tile type
    return true;
  }

  /**
   * returns a string representation of the inventory. the size, and the tiles contained using their
   * symbol character.
   *
   * @return a string representation of inventory.
   */
  public String toString() {
    String r = "Inv(" + size + "): ";
    for (Tile item : items) {
      if (item.type() != TileType.Null) {
        r += item.symbol() + ", ";
      }
    }
    return r.substring(0, r.length() - 2);
  }

}
