package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.FileWriter;
import java.io.IOException;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import App.*;

public class Persistency {
    /*
     * Serialise a domain to an XML document
     * 
     * @param domain The domain to serialise
     * 
     * @return The serialised domain as an XML document
     */
    public static Document serialize(Domain domain) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("Domain");
        return document;
    }

    /*
     * Unserialise a domain to a file
     * 
     * @param xml The XML document to unserialise
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
    public static void save(Domain domain, String path) throws IOException {
        Document document = serialize(domain);

        FileWriter out = new FileWriter(path);
        document.write(out);
        out.close();
    }
}
