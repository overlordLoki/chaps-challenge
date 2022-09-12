package Domain;
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
     * checks wether a given x and y is in bounds. (DO: use this to check more often)
     * If not throws illegalStateException. 
     
    public static void checkInBound(int x, int y, Maze m) throws Throwable{
        if( x < 0 || y < 0 || x > m.width() - 1 || y > m.height() - 1){
            throw new ChapGameException("Location not in bounds.");
        }
    }*/
    
}
