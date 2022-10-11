package nz.ac.vuw.ecs.swen225.gp6.domain.Utility;

import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;

/**
 * location for maze tiles, index 0 to max - 1
 */
public class Loc{
    private int x;
    private int y;

    /**
     * constructs a new loc object
     * 
     * @param x co ordinate 0 to max - 1
     * @param y co ordinate 0 to max - 1
     * 
     * @throws IllegalArgumentException when either co ordinate argument is < 0 
     */
    public Loc(int x, int y){
        if(x < 0 || y < 0) throw new IllegalArgumentException("location cant be negative");
        this.x = x;
        this.y = y;
    }

    /**
     * @return x co ordinate of location
     */
    public int x(){return x;}
    /**
     * @return y co ordinate of location.
     */
    public int y(){return y;}

    /**
     * set the x co ordinate of location.
     * @param x co ordinate
     * 
     * @throws IllegalArgumentException when co ordinate argument is < 0
     */
    public void x(int x){
        if(x < 0) throw new IllegalArgumentException("location cant be negative");
        this.x = x;
    }

    /**
     * set the y co ordinate of location.
     * @param y co ordinate
     * 
     * @throws IllegalArgumentException when co ordinate argument is < 0
     */
    public void y(int y){
        if(y < 0) throw new IllegalArgumentException("location cant be negative");
        this.y = y;
    }


    /**
     * @return true if a given location is in bounds, else false.
     */
    public static boolean checkInBound(Loc l, Maze m) {
        if( l.x() < 0 || l.y() < 0 || l.x() > m.width() - 1 || l.y() > m.height() - 1)return false;
        return true;
    }

    /**
     * if x and y coordinates of this location is equal to a given object that is also a location, 
     * return true, else false
     * 
     * @param o object to compare to.
     * @return true if o is a location and shares same x and y values as this location
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Loc == false) return false; //if not loc object 
        Loc loc = (Loc)o;
        if(this.x() != loc.x() ||this.y() != loc.y() ) return false; //if x's or y's dont match
        return true;
    }

    @Override
    public int hashCode(){return x * 31 + y;}
        
    
}
