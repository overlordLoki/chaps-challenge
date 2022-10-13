package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;

import java.util.Arrays;
import java.util.List;


/**
 * Each tile object will have a type field, which helps to determine what the tile is.
 * 
 * Note1: Any tile class that to be implemented in game(in compile time), must be in tiles folder 
 * and the class file must have the same name as the its enum type.
 * Note2: Any tiles that are added to the game at run time will have the the type "Other".
 */
public enum TileType{
    //ACTORS:
    Hero('H'),

    //STATIC TERRAINS:
    Floor('_'), 

    Wall('/'),

    //INTERACTIVE TERRAINS:
    Info('i'),

    ExitDoor('X'),

    ExitDoorOpen('Z'),

    BlueLock('B'),

    GreenLock('G'),

    OrangeLock('O'),

    YellowLock('Y'),

    //PICKABLES(ITEMS):
    BlueKey('b'),

    GreenKey('g'),

    OrangeKey('o'),

    YellowKey('y'),

    Coin('$'), 

    //SPECIAL:
    Empty(' '), //used ONLY in inventory

    Periphery('*'), //used to draw the peripheries of the maze(out of bound areas)

    Other('?'), //used for tiles that are implemented at run time and such
                        //feeding this into makeTile method will cause an IllegalArgumentException

    Null(Character.MIN_VALUE); //used to represent null tiles

    //FIELDS:
    private char symbol;

    //CONSTRUCTOR:
    /**
     * Each tile will have a char symbol for debugging.
     * 
     * @param symbol
     */
    TileType(char symbol){this.symbol = symbol;}

    //METHODS:
    /**
     * this is to make the associated tile class instance for the given tile type.
     * NOTE: The enum name and Tile class name must be the same.
     * 
     * @param t tile type to make
     * @param info an instance of the class TileInfo that has all the necessary information that tile needs
     * @return an instance of the desired tile class
     * 
     * @throws IllegalArgumentException if a file in tiles with same name as tile type given is not found
     * @throws NullPointerException if t or info are null
     */
    public static Tile makeTile(TileType t, TileInfo info){
        if(t == null || info == null) throw new NullPointerException("tile type or tile info cannot be null (TileType.makeTile)");
        try{
            Class<?> c = Class.forName("nz.ac.vuw.ecs.swen225.gp6.domain.Tiles." + t.name());
            return (Tile) c.getDeclaredConstructor(TileInfo.class).newInstance(info);
        }catch(Exception e){
            throw new IllegalArgumentException("There isn't a class for tile type: " + t.name());
        }
    }

    /**
     * this method will return a tile type based on its symbol.
     * 
     * @param symbol symbol of desired tile class
     * @param info an instance of the TileInfo object that has all the necessary
     *  intial information that tile needs
     * 
     * @return an instance of the desired tile class
     * 
     * @throws IllegalArgumentException if a tile type with same symbol as given is not found
     */
    public static Tile makeTileFromSymbol(char symbol, TileInfo info) {
        //search in preset tile types
        TileType type = Arrays.stream(TileType.values())
            .filter(t -> {
                try{return TileType.makeTile(t, new TileInfo(null)).symbol() == symbol;}
                catch(Exception e){return false;}
            }).
            findFirst()
            .orElse(TileType.Other);
        
        if(type != TileType.Other) return TileType.makeTile(type, info);
        
        throw new IllegalArgumentException("Tiles class with symbol " + symbol 
            + " not defined in preset tiles, it may be in custom tiles!");
        
    }
    
    /**
     * gets the symbol of the tile type
     * 
     * @return char symbol of the tile
     */
    public char symbol(){return symbol;}
}


