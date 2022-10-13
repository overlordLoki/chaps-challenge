package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Floor;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/*
 * A class with some information that all tiles need to recieve to be initiated.
 * It includes fields that are common to most tiles.
 * This is to avoid adding more and more parameters to each tile class, 
 * and being able to pass larger chunks of information with one parameter.
 */
public class TileInfo{
    private String imageName;
    private String message; //only used for info tile currently but can be used to store messages in tiles
    private Loc loc;
    private int ping; //to be used later perhaps to keep count of ping cycles
    
    //FOR ACTORS ONLY ATM:
    private Direction facing;
    private Tile tileOn;

    /**
     * Create a tileInfo object, everything can be null if its for testing purposes and
     * the tile associated does not need the information in this object.
     * 
     * @param loc location of tile
     * @param pingCount ping count recorded by the tile
     * @param imageName the name of the image file
     * @param message the message associated with the tile
     */
    public TileInfo(Loc loc, int pingCount, String imageName, String message){
        this.loc = loc;
        this.ping = pingCount;
        this.imageName = imageName;
        this.message = message;

        //these are values that inform us this is first time the actor is created(no pings have been done yet)
        //they must be set to some value very quickly
        this.facing = Direction.None;
        this.tileOn = null;
    }

    /**
     * Create a tileInfo object only with loc, 
     * pingCount defaults to 0, the imageName to ""
     * 
     * @param loc location of tile
     */
    public TileInfo(Loc loc){this(loc, 0, "", "");}

    /**
     * Create a tileInfo object only with loc and imageName,
     * pingCount defaults to 0
     * 
     * @param 
     */
    public TileInfo(Loc loc, String imageName){this(loc, 0, imageName, "");}


    //GETTERS:
    /**
     * gets image name
     * 
     * @return image name
     */
    public String getImageName(){return imageName;}
    /**
     * gets location of the associated tile
     * 
     * @return location of tile
     */
    public Loc loc(){return loc;}
    /**
     * gets ping of the associated tile 
     * 
     * @return number of pings that the tile has recorded
     */
    public int ping(){return ping;}
    /**
     * gets message on the tile
     * 
     * @return message
     */
    public String message(){return message;}
    /**
     * gets the tile that is to be replaced when this tile moves, in
     * its spot.
     * 
     * @return tile to replace with this tile when moved
     */
    public Tile tileOn(){return tileOn;}
    
    /**
     * gets the direction the tile is facing
     * 
     * @return direction of facing
     */
    public Direction facing(){return facing;}

    //SETTERS
    /**
     * sets location of the associated tile
     * 
     * @param loc location of this tile
     */
    public void loc(Loc loc){this.loc = loc;}
    /**
     * increments the ping of the associated tile
     */
    public void pingStep(){ping++;}
    /**
     * sets the tile that this tile replaces under it when moved
     * 
     * @param tile to replace with this tile when moved
     */
    public void tileOn(Tile tile){this.tileOn = tile;}
    /**
     * sets the direction of the facing of associated tile
     * 
     * @param d direction to set facing of this tile
     */
    public void facing(Direction d){this.facing = d;}
}