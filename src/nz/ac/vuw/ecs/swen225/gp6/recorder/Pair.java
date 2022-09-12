package nz.ac.vuw.ecs.swen225.gp6.recorder;

import java.util.List;


/**
 * This Pair class is currently hard coded to suit the
 * needs of the project. It is not a generic class.
 */
public class Pair {
    private Integer time;
    private List<String> actions;

    /**
     * Construct a new Pair of time to actions
     * @param time the time of the actions
     * @param actions the recorded actions executed
     */
    public Pair(Integer time, List<String> actions) {
        this.time = time;
        this.actions = actions;
    }

    /**
     * Get the time portion of this pair.
     * @return
     */
    public Integer getTime() {
        return time;
    }

    /**
     * Get the actions portion of this pair.
     * @return
     */
    public List<String> getActions() {
        return this.actions;
    }

    @Override
    public String toString() {
        return "[time=" + time + ", actions=" + actions + "]";
    }
}
