package nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy;

import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;

/*
 * a class for some information that all tiles need to recieve to be initiated, 
 * (fields that are common to all tiles)
 * to avoid adding more and more parameters to each tile class, 
 * and being able to pass larger chunks of information with one parameter.
 */
public class TileInfo{
    private String imageName;
    private Loc loc;
    private int ping; //to be used later perhaps to keep count of ping cycles

    

    public TileInfo(Loc loc, int pingCount, String imageName){
        this.loc = loc;
        this.ping = pingCount;
        this.imageName = imageName;
    }

    public TileInfo(Loc loc){this(loc, 0, "");}

    public TileInfo(Loc loc, String imageName){ this(loc, 0, imageName);}

    public TileInfo(Loc loc, int pingCount){ this(loc, pingCount, "");}



    //GETTERS:
    /*
     * returns image name
     */
    public String getImageName(){return imageName;}
    /*
     * returns location of the associated tile
     */
    public Loc loc(){return loc;}
    /*
     * returns ping of the associated tile 
     */
    public int ping(){return ping;}

    //SETTERS
    /*
     * sets location of the associated tile
     */
    public void loc(Loc loc){this.loc = loc;}
    /*
     * increments the ping of the associated tile
     */
    public void pingStep(){ping++;}
}