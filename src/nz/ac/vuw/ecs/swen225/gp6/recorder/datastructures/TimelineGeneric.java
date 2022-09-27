package nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures;

import java.util.ArrayDeque;
import java.util.Queue;

public class TimelineGeneric<E> {
    private Queue<Pair<Long, E>> timeline;

    /**
     * Creates a new Timeline
     */
    public TimelineGeneric() {
        this.timeline = new ArrayDeque<>();
    }

    /**
     * Creates a new Timeline from a saved timeline.
     * @param timeQueue
     */
    public TimelineGeneric(Queue<Pair<Long, E>> timeline) {
        this.timeline = timeline;
    }

    /**
     * Add actions to the timeline
     */
    public void add(Long time, E actions) {
        this.timeline.add(new Pair<Long, E>(time, actions));
    }

    /**
     * Returns the next action to be executed
     * in the timeline.
     * @return returns the list of actions to be executed
     */
    public Pair<Long, E> next(){
        return timeline.poll();
    }

    /**
     * Checks if there are any more actions in the timeline
     * @return
     */
    public boolean hasNext(){
        return !this.timeline.isEmpty();
    }

    @Override
    public String toString() {
        return "Timeline: " + this.timeline.toString();
    }
}
