package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

public class Level {
    public final Maze maze;
    public final Inventory inv;
    public final int lvl;
    public final int timeLimit;

    private long timeCurrent;
    private Direction heroNextStep;

    public Level(Maze maze, Inventory inv, int lvl, int timeLimit, long timeCurrent, Direction heroNextStep){
        this.maze = maze;
        this.inv = inv;
        this.lvl = lvl;
        this.timeLimit = timeLimit;

        this.timeCurrent = timeCurrent;
        this.heroNextStep = heroNextStep;
    }

    public Level(Maze maze, int lvl){
        this(maze, new Inventory(8), lvl, 120, 0, Direction.None);
    }

    public long getCurrentTime(){return timeCurrent;}

    public Direction getDirection(){return heroNextStep;}

    public void setCurrentTime(long timeCurrent){this.timeCurrent = timeCurrent;}
    /*
     * sets the movement direction of hero, 
     * which the hero will try to move towards if possible in NEXT ping.
     */
    public void makeHeroStep(Direction d){ this.heroNextStep = d;}

}
