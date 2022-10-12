package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/*
 * A class with some information that all tiles need to recieve to be initiated.
 * It includes fields that are common to most tiles.
 * This is to avoid adding more and more parameters to each tile class, 
 * and being able to pass larger chunks of information with one parameter.
 */
public class TileInfo{
    private String imageName;
    private Loc loc;
    private int ping; //to be used later perhaps to keep count of ping cycles

    /**
     * Create a tileInfo object, everything can be null if its for testing purposes and
     * the tile associated does not need the information in this object.
     * 
     * @param loc location of tile
     * @param pingCount ping count recorded by the tile
     * @param imageName the name of the image file
     */
    public TileInfo(Loc loc, int pingCount, String imageName){
        this.loc = loc;
        this.ping = pingCount;
        this.imageName = imageName;
    }

    /**
     * Create a tileInfo object only with loc, 
     * pingCount defaults to 0, the imageName to ""
     * 
     * @param loc location of tile
     */
    public TileInfo(Loc loc){this(loc, 0, "");}

    /**
     * Create a tileInfo object only with loc and imageName,
     * pingCount defaults to 0
     * 
     * @param 
     */
    public TileInfo(Loc loc, String imageName){this(loc, 0, imageName);}


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
}