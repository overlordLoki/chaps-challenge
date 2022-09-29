package nz.ac.vuw.ecs.swen225.gp6.persistency.logging;

import nz.ac.vuw.ecs.swen225.gp6.app.gui.GUI;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;

import javax.swing.JOptionPane;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Package-private class for the App. Handles the output streams and redirects
 * all outputs to:
 * <P>
 * 1. Normal output
 * </P>
 * <P>
 * 2. In-game log panel display
 * </P>
 * <P>
 * 3. Log file in /res
 * </P>
 *
 * @author Ben Hong
 */
public class Interceptor extends PrintStream {
    private final NonBlockingLog logger;

    public Interceptor(OutputStream out) {
        super(out, true);
        logger = new NonBlockingLog();
        logger.start();
    }

    @Override
    public void print(String s) {
        // super.print(s); // this line enables output to stdout
        logger.add(s); // this line enables output to log file
        GUI.getLogPanel().print(s); // this line enables output to log panel
    }

    @Override
    public void println(String s) {
        print(s + "\n");
    }

    @Override
    public PrintStream printf(String format, Object... args) {
        print(String.format(format, args));
        return this;
    }
}

class NonBlockingLog extends Thread {
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public void add(String s) {
        queue.add(s);
    }

    public void run() {
        try {
            while (true) {
                String msg;
                while ((msg = queue.poll()) != null) {
                    Persistency.log(msg);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to log file");
        }
    }
}