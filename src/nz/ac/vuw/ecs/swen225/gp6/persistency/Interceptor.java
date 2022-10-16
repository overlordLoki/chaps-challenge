package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.JOptionPane;
import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;

/**
 * Class for intercepting System.out calls. Handles the output streams and redirects all outputs to:
 *
 * <p>1. Normal output
 *
 * <p>2. In-game log panel display
 *
 * <p>3. Log file in /res
 *
 * @author Benjamin Hong - 300605520
 */
public class Interceptor extends PrintStream {

  private final NonBlockingLog logger;

  /**
   * Constructor for the Interceptor class.
   *
   * @param out The output stream
   */
  public Interceptor(OutputStream out) {
    super(out, true);
    logger = new NonBlockingLog();
    logger.start();
  }

  /**
   * Print a string to the output stream.
   *
   * @param s The string to print
   */
  @Override
  public void print(String s) {
    // super.print(s); // this line enables output to stdout
    logger.add(s); // this line enables output to log file
    GUI.getLogPanel().print(s); // this line enables output to log panel
  }

  /**
   * Print a line to the output stream.
   *
   * @param s The string to print
   */
  @Override
  public void println(String s) {
    print(s + "\n");
  }

  /**
   * Print a format string to the output stream.
   *
   * @param format The format string
   * @param args The arguments
   */
  @Override
  public PrintStream printf(String format, Object... args) {
    print(String.format(format, args));
    return this;
  }

  /**
   * The NonBlockingLog class is a thread that handles the logging in a non-blocking way.
   *
   * @author Benjamin Hong - 300605520
   */
  private static class NonBlockingLog extends Thread {

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    /**
     * Add a string to the queue for logging.
     *
     * @param s The string to add
     */
    void add(String s) {
      queue.add(s);
    }

    /** Run the thread. This waits for a string to be added to the queue and then logs it. */
    public void run() {
      try {
        while (true) {
          String msg;
          while ((msg = queue.poll()) != null) {
            Logging.log(msg);
          }
        }
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error writing to log file");
      }
    }
  }
}
