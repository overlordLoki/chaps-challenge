package nz.ac.vuw.ecs.swen225.gp6.recorder;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.persistency.RecorderPersistency;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.RecordTimeline;
import org.dom4j.DocumentException;

/**
 * Class for recording actions in a game.
 *
 * @author Jayden Hooper
 */
public final class Record {

  public static final Record INSTANCE = new Record();
  private RecordTimeline<Actions> timeline;

  /**
   * Create a new Record object.
   */
  private Record() {
    this.timeline = new RecordTimeline<>();
  }

  /**
   * Starts a new recording.
   */
  public void startRecording() {
    this.timeline = new RecordTimeline<>();
  }

  /**
   * Stitches a recording to the start of this recording.
   *
   * @param slot the slot to load the recording from.
   **/
  public void stitchRecording(int slot) {
    try {
      this.timeline = new RecordTimeline<Actions>(RecorderPersistency.loadTimeline(slot));
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a time and action to the timeline.
   *
   * @param time    the time the action is executed.
   * @param actions the action executed.
   */
  public void addActions(long time, Actions actions) {
    this.timeline.add(time, actions);
  }

  /**
   * Saves the recording to a file.
   *
   * @param slot the slot to save the recording to.
   */
  public void saveRecording(int slot) {
    try {
      RecorderPersistency.saveTimeline(timeline.getTimeline(), slot);
      System.out.println("Saved recording to file");
    } catch (Exception e) {
      System.out.println("Error saving recording");
    }
  }
}
