package nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures;

import java.util.Stack;

public class RecordTimeline<E> {
    private Stack<Pair<Long, E>> timeline;

    /**
     * Creates a new Timeline
     */
    public RecordTimeline() {
        this.timeline = new Stack<>();
    }

    /**
     * Creates a new Timeline from a saved timeline.
     * @param timeQueue
     */
    public RecordTimeline(Stack<Pair<Long, E>> timeline) {
        this.timeline = timeline;
    }

    /**
     * Add actions to the timeline
     */
    public void add(Long time, E actions) {
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
