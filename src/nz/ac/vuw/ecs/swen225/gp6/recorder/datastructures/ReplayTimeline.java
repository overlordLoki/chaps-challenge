package nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures;

import java.util.Stack;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Pair;

/**
 * A generic timeline class that replays a series of events in chronological order. Events can be
 * retrieved in both forward and reverse order.
 *
 * @param <E> the type of the events
 * @author Jayden Hooper
 */
public class ReplayTimeline<E> {

  private final Stack<Pair<Long, E>> forward;
  private final Stack<Pair<Long, E>> backward;

  /**
   * Creates a new Timeline.
   *
   * @param recording the recording to replay from a RecordTimeline object.
   */
  public ReplayTimeline(RecordTimeline<E> recording) {
    if (recording == null) {
      throw new IllegalArgumentException("Timeline cannot be null");
    }
    this.forward = new Stack<>();
    this.backward = new Stack<>();
    Stack<Pair<Long, E>> temp = recording.getTimeline();
    while (!temp.isEmpty()) {
      forward.push(temp.pop());    // reverse stack
    }
  }

  /**
   * Creates a new Timeline.
   *
   * @param recording the recording to replay from a stack of pairs.
   */
  public ReplayTimeline(Stack<Pair<Long, E>> recording) {
    if (recording == null) {
      throw new IllegalArgumentException("Timeline cannot be null");
    }
    this.forward = new Stack<>();
    this.backward = new Stack<>();
    while (!recording.isEmpty()) {
      forward.push(recording.pop());    // reverse stack
    }
  }

  /**
   * Returns the next action in the timeline.
   *
   * @return returns the time and event of the next action.
   */
  public Pair<Long, E> next() {
    Pair<Long, E> action = forward.pop();
    backward.push(action);
    return action;
  }

  /**
   * Checks if there are any more actions in the timeline.
   *
   * @return Whether there are more actions in the timeline.
   */
  public boolean hasNext() {
    return !this.forward.isEmpty();
  }

  /**
   * Returns the next action in the timeline without removing it.
   *
   * @return returns the time and event of the next action.
   */
  public Pair<Long, E> peek() {
    return forward.peek();
  }

  @Override
  public String toString() {
    return "Timeline {Previous actions: " + this.backward.toString()
        + ", Next actions: " + this.forward.toString() + "}";
  }
}
