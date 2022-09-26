package nz.ac.vuw.ecs.swen225.gp6.domain.Utility;

import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;

/*
 * location for maze tiles, index 0 to max - 1
 */
public class Loc{
    private int x;
    private int y;

    
    public Loc(int x, int y){
        this.x = x;
        this.y = y;
    }

    /*
     * get x co ordinate of location.
     */
    public int x(){return x;}
    /*
     * get y co ordinate of location.
     */
    public int y(){return y;}

    /*
     * set the x co ordinate of location.
     */
    public void x(int x){this.x = x;}

    /*
     * set the y co ordinate of location.
     */
    public void y(int y){this.y = y;}


    /*
     * returns true if a given location is in bounds, else false.
     */
    public static boolean checkInBound(Loc l, Maze m) {
        if( l.x() < 0 || l.y() < 0 || l.x() > m.width() - 1 || l.y() > m.height() - 1)return false;
        return true;
    }
    
}
