package nz.ac.vuw.ecs.swen225.gp6.domain.Utility;

public enum Direction {
    Up(0, 1),
    Down(0, -1),
    Right(1, 0),
    Left(-1, 0),
    None(0,0);

    public final int x;
    public final int y;
    /*
     * x and y difference from location of hero
     */
    private Direction(int x, int y){
        this.x = x;
        this.y = y;
    }

    /*
     * transforms a given location with the 
     */
    public Loc transformLoc(Loc l){return new Loc(l.x() + x, l.y() + y);}

}
