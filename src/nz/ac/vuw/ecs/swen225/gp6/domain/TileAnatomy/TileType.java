package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.*;


//TODO: testing and ensuring some states wont be reached

/*
 * Each tile object will have a type field, which helps to determine what the tile is.
 * 
 * Note: Any tile class that to be implemented in game(in compile time), must be in tiles folder and have the same
 * name as the its enum type.
 * Note: Any tiles that are added to the game at run time will have the the type "Other".
 * 
 */
public enum TileType{
    //ACTORS:
    Hero,

    Enemy,

    //STATIC TERRAINS:
    Empty,

    Floor, 

    Wall,

    //INTERACTIVE TERRAINS:
    Info,

    ExitDoor,

    ExitDoorOpen,

    BlueLock,

    GreenLock,

    OrangeLock,

    YellowLock,

    //PICKABLES(ITEMS):
    BlueKey,

    GreenKey,

    OrangeKey,

    YellowKey,

    Coin, 

    //SPECIAL:
    Other, 

    Null;

    //METHODS:
    /*
     * this is to make the associated tile class instance for the given tile type.
     * NOTE: The enum name and Tile class name must be the same.
     */
    public static Tile makeTile(TileType t, TileInfo info){
        try{
            Class<?> c = Class.forName("nz.ac.vuw.ecs.swen225.gp6.domain.Tiles." + t.name());
            return (Tile) c.getDeclaredConstructor(TileInfo.class).newInstance(info);
        }catch(Exception e){
            System.out.println("There isn't a class for tile type: " + t.name());
            return new Null(null);//return null type tile if no tile is found
        }
    }
    
    //FIELDS://TODO: consider using this
    private char symbol;
}


