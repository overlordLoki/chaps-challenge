package nz.ac.vuw.ecs.swen225.gp6.app;

import javax.swing.SwingUtilities;

/**
 * Main method of the application.
 *
 * @author Jeff Lin
 */
public class Main {
    /**
     * Main method of the application.
     *
     * @param args No arguments required for this application
     */
    public static void main(String... args){
        SwingUtilities.invokeLater(App::new);
    }
}
