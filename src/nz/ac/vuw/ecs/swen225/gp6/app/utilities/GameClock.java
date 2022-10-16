package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import nz.ac.vuw.ecs.swen225.gp6.app.App;

/**
 * This class is used to represent the timer for the game, it is also used to clock the time, and
 * define what happens with each ping during a game loop.
 *
 * @author Jeff Lin
 */
public class GameClock {

  private final int timeIntervalGame = 34; // time interval for each tick
  private final int timeIntervalReplay = 34; // time interval for each tick
  private App app;
  private Runnable replayObserver = () -> {
  }; // observer for the replay mode
  private long timeStart = 0;  // starting time of current pause
  private long timePlayed = 0; // total time played in a level
  private float replaySpeed = 1; // speed of replay
  private long timeLimit = 0; // time limit for the level
  private final Timer gameTimer = new Timer(timeIntervalGame, unused -> {
    assert SwingUtilities.isEventDispatchThread();
    app.getGame().pingDomain();
    long currentTime = System.nanoTime();
    timePlayed += currentTime - timeStart;
    if (timePlayed > timeLimit) {
      app.runLoseEvent();
    }
    timeStart = currentTime;
    app.repaint();
  });

  private final Timer replayTimer = new Timer(timeIntervalReplay, unused -> {
    assert SwingUtilities.isEventDispatchThread();
    app.getGame().pingDomain();
    long currentTime = System.nanoTime();
    timePlayed += (currentTime - timeStart) * replaySpeed;
    if (timePlayed > timeLimit) {
      app.runLoseEvent();
    }
    timeStart = currentTime;
    replayObserver.run();
    app.repaint();
  });
  private Timer timer = gameTimer;

  /**
   * Constructor for the GameTimer class.
   *
   * @param app The game object that the timer will be used on.
   */
  public GameClock(App app) {
    this.app = app;
  }

  /**
   * Starts the timer.
   */
  public void start() {
    timeStart = System.nanoTime();
    timer.start();
  }

  /**
   * Stops the timer.
   */
  public void stop() {
    timer.stop();
  }

  /**
   * Resets the timer to the initial state.
   */
  public void reset() {
    timer.stop();
    resetTime();
    timeLimit = app.getGame().getCurrentTimeLimit()
        * 1_000_000_000L; // converts time limit from seconds to nanoseconds
  }

  //==============================================================================================//
  //================================== Utility Method ============================================//
  //==============================================================================================//

  /**
   * Sets the observer to fire during a replay session.
   *
   * @param ob the observer to be set
   */
  public void setObserver(Runnable ob) {
    this.replayObserver = ob;
  }

  /**
   * Sets the delay between pings for the replay timer.
   *
   * @param speed the delay in milliseconds
   */
  public void setReplaySpeed(float speed) {
    this.replayTimer.setDelay((int) (timeIntervalReplay / speed));
    this.replaySpeed = speed;
  }

  /**
   * Resets the time left for the current level.
   */
  public void resetTime() {
    timeStart = System.nanoTime();
    timePlayed = 0;
  }

  //==============================================================================================//
  //=================================== Setter Method ============================================//
  //==============================================================================================//

  /**
   * Sets the timer used for the game loop.
   */
  public void useGameTimer() {
    this.timer = gameTimer;
  }

  /**
   * Sets the timer used for the replay loop.
   */
  public void useReplayTimer() {
    this.timer = replayTimer;
  }

  /**
   * Gets the time elapsed since the start of the game.
   *
   * @return the time elapsed since the start of the game
   */
  public long getTimePlayed() {
    return timePlayed;
  }

  /**
   * Sets the time played in the previous session.
   *
   * @param timePlayed the time played in the previous session
   */
  public void setTimePlayed(long timePlayed) {
    this.timePlayed = timePlayed;
  }

  //==============================================================================================//
  //=================================== Getter Method ============================================//
  //==============================================================================================//

  /**
   * Gets the time left for the current level.
   *
   * @return the time left for the current level in nanoseconds
   */
  public long getTimeLeft() {
    return timeLimit - timePlayed;
  }

  /**
   * Gets the time elapsed since the start of the game in Minutes and Seconds.
   *
   * @return the time elapsed since the start of the game
   */
  public String getTimeInMinutes() {
    long time = getTimeLeft();
    long millis = time / 1000000 % 1000 / 10;
    long seconds = time / 1000000000;
    long minutes = seconds / 60;
    seconds %= 60;
    return String.format("%02d:%02d:%02d", minutes, seconds, millis);
  }
}
