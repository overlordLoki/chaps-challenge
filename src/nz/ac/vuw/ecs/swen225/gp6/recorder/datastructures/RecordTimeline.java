package nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures;

import java.util.Stack;

/**
 * A generic record timeline class that stores a series of events in chronological order.
 * Events can only be added to the end of the timeline.
 * @param <E> the type of the events
 * 
 * @author: Jayden Hooper
 */
public class RecordTimeline<E> {
    private Stack<Pair<Long, E>> timeline;

    /**
     * Creates a new Timeline
     */
    public RecordTimeline() {
        this.timeline = new Stack<>();
    }

    /**
     * Add actions to the timeline
     */
    public void add(Long time, E actions) {
        if(time < timeline.peek().getKey()){
            throw new IllegalArgumentException("Time recordered must be chronological.");
        }
        this.timeline.add(new Pair<Long, E>(time, actions));
    }

    /**
     * Checks if there are any more actions in the timeline
     * @return boolean if there are more actions
     */
    public boolean hasNext(){
        return !this.timeline.isEmpty();
    }

    /**
     * @return the timeline stack
     */
    public Stack<Pair<Long, E>> getTimeline() {
        return timeline;
    }

    @Override
    public String toString() {
        return "Timeline: " + this.timeline.toString();
    }
}
