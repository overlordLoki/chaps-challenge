package nz.ac.vuw.ecs.swen225.gp6.app.utilities;

import nz.ac.vuw.ecs.swen225.gp6.app.App;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This class is used to represent the timer for the game, it is also used to clock the time,
 * and define what happens with each ping during a game loop.
 *
 * @author Jeff Lin
 */
public class GameClock {
    private App app;
    private boolean inReplayMode = false;
    private Runnable replayObserver = ()->{}; // observer for the replay mode
    private long time = 0;       // used to store accumulated time from the previous pause
    private long timeStart = 0;  // starting time of current pause
    private long playedTime = 0; // total time played in a level
    private final Timer gameTimer = new Timer(34, unused -> {
        assert SwingUtilities.isEventDispatchThread();
        app.getGame().pingAll();
        playedTime = System.nanoTime() - timeStart + time;
        app.repaint();
    });
    private final Timer replayTimer = new Timer(34, unused -> {
        assert SwingUtilities.isEventDispatchThread();
        app.getGame().pingAll();
        playedTime = System.nanoTime() - timeStart + time;
        replayObserver.run();
        app.repaint();
    });
    private Timer timer = gameTimer;

    /**
     * Constructor for the GameTimer class.
     * @param app The game object that the timer will be used on.
     */
    public GameClock(App app) {
        this.app = app;
    }


    //================================================================================================================//
    //=========================================== Utility Method =====================================================//
    //================================================================================================================//

    /**
     * Starts the timer.
     */
    public void start() {timer.start();}

    /**
     * Stops the timer.
     */
    public void stop() {timer.stop();}

    /**
     * Resets the timer to the initial state
     */
    public void reset() {
        timer.stop();
        resetTime();
    }

    /**
     * Sets the replay mode
     *
     * @param useReplay true if the game is in replay mode, false otherwise
     */
    public void setReplayMode(boolean useReplay) {inReplayMode = useReplay;}


    //================================================================================================================//
    //============================================ Setter Method =====================================================//
    //================================================================================================================//

    /**
     * Sets the observer to fire during a replay session
     *
     * @param ob the observer to be set
     */
    public void setObserver(Runnable ob) {this.replayObserver = ob;}

    /**
     * Sets the delay between pings for the replay timer
     *
     * @param delay the delay in milliseconds
     */
    public void setReplayDelay(int delay) {this.replayTimer.setDelay(delay);}

    /**
     * Sets the timer and its action going to be used for the game loop
     *
     * @param timer the timer to use for the main loop
     */
    public void setTimer(Timer timer) {this.timer = timer;}

    /**
     * Sets the starting time for the game loop.
     *
     * @param nanoTime the starting time for the game loop
     */
    public void setStartingTime(long nanoTime) {this.timeStart = nanoTime;}

    /**
     * Sets the time left for the current level.
     *
     * @param time the time left for the current level
     */
    public void setTime(long time) {this.time = time;}

    /**
     * Resets the time left for the current level.
     */
    public void resetTime(){
        time = 0;
        timeStart = System.nanoTime();
        playedTime = 0;
    }


    //================================================================================================================//
    //============================================ Getter Method =====================================================//
    //================================================================================================================//

    /**
     * Gets the timer for the game loop.
     *
     * @return the timer for the game loop
     */
    public Timer getTimer() {return timer;}

    /**
     * Gets the time when the timer starts
     *
     * @return the starting time of the timer
     */
    public long getTimeStart() {return timeStart;}

    /**
     * Gets the time elapsed since the start of the game.
     *
     * @return the time elapsed since the start of the game
     */
    public long getTime() {return time;}

    /**
     * Gets the time elapsed since the start of the game in Minutes and Seconds.
     *
     * @return the time elapsed since the start of the game
     */
    public String getTimeInMinutes() {
        long time = this.playedTime;
        long seconds = time / 1000000000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
