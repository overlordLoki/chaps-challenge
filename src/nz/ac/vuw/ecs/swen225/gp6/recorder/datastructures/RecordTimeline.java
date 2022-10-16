package nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures;

import java.util.Stack;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Pair;

/**
 * A generic record timeline class that stores a series of events in chronological order. Events can
 * only be added to the end of the timeline.
 *
 * @param <E> the type of the events
 * @author Jayden Hooper
 */
public class RecordTimeline<E> {

  /**
   * The internal structure of the Timeline represented as a Stack of Pairs.
   */
  private final Stack<Pair<Long, E>> timeline;

  /**
   * Creates a new Timeline.
   */
  public RecordTimeline() {
    this.timeline = new Stack<>();
  }

  /**
   * Creates a new timeline from an existing timeline.
   *
   * @param timeline the timeline to start from.
   */
  public RecordTimeline(Stack<Pair<Long, E>> timeline) {
    this.timeline = (Stack) timeline.clone();
  }

  /**
   * Add actions to the timeline.
   *
   * @param time    the time the actions are performed.
   * @param actions the actions performed.
   */
  public void add(Long time, E actions) {
    this.timeline.add(new Pair<>(time, actions));
  }

  /**
   * Gets the internal representation of the timeline.
   *
   * @return The stack of pairs representing the timeline.
   */
  public Stack<Pair<Long, E>> getTimeline() {
    return (Stack) timeline.clone();
  }

  @Override
  public String toString() {
    return "Timeline: " + this.timeline.toString();
  }
}
