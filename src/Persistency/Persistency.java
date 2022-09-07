package Persistency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import App.*;
import Domain.*;

public class Persistency {
    /*
     * Serialise a domain to a string
     * 
     * @param domain The domain to serialise
     * 
     * @return The serialised domain as an XML string
     */
    public static String serialize(Domain domain) {
        return "<Domain/>";
    }

    /*
     * Unserialise a domain to a file
     * 
     * @param xml The XML string to unserialise
     * 
     * @param domain The domain to unserialise to
     */
    public static Domain unserialize(String xml) {
        return new Domain();
    }

    /*
     * Save a domain to a file
     * 
     * @param domain The domain to save
     * 
     * @param path The file path to save to
     */
    public static void save(Domain domain, String path) {
        String xml = serialize(domain);

        // save xml to file
        File file = new File(path);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            writer.println(xml);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
