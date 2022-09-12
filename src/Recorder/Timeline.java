package Recorder;

import java.util.ArrayDeque;
import java.util.Queue;

public class Timeline {
    private Queue<Pair> timeline;

    /**
     * Creates a new Timeline
     */
    public Timeline() {
        this.timeline = new ArrayDeque<>();
    }

    /**
     * Creates a new Timeline from a saved timeline.
     * @param timeQueue
     */
    public Timeline(Queue<Pair> timeline) {
        this.timeline = timeline;
    }

    /**
     * Add actions to the timeline
     */
    public void add(Pair timeActions) {
        this.timeline.add(timeActions);
    }

    /**
     * Returns the next action to be executed
     * in the timeline.
     * @return returns the list of actions to be executed
     */
    public Pair next(){
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
