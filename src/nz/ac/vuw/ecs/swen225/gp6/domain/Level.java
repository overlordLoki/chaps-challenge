package nz.ac.vuw.ecs.swen225.gp6.domain;

import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Direction;

/**
 * The level class is used to group all information related to a level together. Such as the 
 * maze, inventory, direction of players next movement, etc.
 */
public class Level {
    public final Maze maze;
    public final Inventory inv;
    public final int lvl;
    public final int timeLimit;

    private long timeCurrent;
    private Direction heroNextStep;

    /**
     * constructor for a level which takes, maze, inventory, level number, time limit of level,
     * current time of level, and heros next steps direction.
     * 
     * @param maze - the maze
     * @param inv - the inventory
     * @param lvl - the level number 1,2, ...
     * @param timeLimit - in seconds, the limit for how long the player has to finish the level
     * @param timeCurrent - the current time in seconds (should start at 0)
     * @param heroNextStep - the hero's direction of stepping in next ping
     */
    public Level(Maze maze, Inventory inv, int lvl, int timeLimit, long timeCurrent, Direction heroNextStep){
        this.maze = maze;
        this.inv = inv;
        this.lvl = lvl;
        this.timeLimit = timeLimit;

        this.timeCurrent = timeCurrent;
        this.heroNextStep = heroNextStep;
    }

    /**
     * constructor for a level, which takes the maze, and level number.
     * It default the time limit to 120 s, and the current time to 0 s, 
     * inventory to an empty inventory of size 8 and heros direction to None.
     * 
     * @param maze
     * @param lvl
     */
    public Level(Maze maze, int lvl){
        this(maze, new Inventory(8), lvl, 120, 0, Direction.None);
    }

    //GETTERS:
    /**
     * @return current time on level
     */
    public long getCurrentTime(){return timeCurrent;}
    /**
     * @return direction enum for next step of hero
     */
    public Direction getHeroNextStep(){return heroNextStep;}

    //SETTERS:
    /**
     * sets the current time of the level
     * 
     * @param timeCurrent - the time to set the level's time to
     */
    public void setCurrentTime(long timeCurrent){this.timeCurrent = timeCurrent;}
    /**
     * sets the movement direction of hero, 
     * which the hero will try to move towards if possible in NEXT ping.
     * 
     * @param direction - the direction for hero to step in next unit of time
     */
    public void makeHeroStep(Direction d){ this.heroNextStep = d;}

}
