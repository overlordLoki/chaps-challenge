package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Helper;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.Tiles.Tile;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import nz.ac.vuw.ecs.swen225.gp6.app.*;

public class Persistency {
    /*
     * Serialise a domain to an XML document
     *
     * @param domain The domain to serialise
     *
     * @return The serialised domain as an XML document
     */
    public static Document serializeDomain(Domain domain) {
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
        return new Domain(new ArrayList<Maze>(), new Inventory(1), 1);
    }

    /*
     * Save a domain to a file
     *
     * @param domain The domain to save
     *
     * @param path The file path to save to
     */
    public static void saveDomain(Domain domain, int slot) throws IOException {
        Document document = serializeDomain(domain);

        FileWriter out = new FileWriter("res/save" + slot + ".xml");
        document.write(out);
        out.close();
    }

    /**
     * List saved games in res/saves
     * 
     * @return List of games
     */
    public static List<Domain> loadSaves() {
        List<Domain> saves = new ArrayList<Domain>();
        saves.add(new Domain(new ArrayList<Maze>(), new Inventory(1), 1));
        saves.add(new Domain(new ArrayList<Maze>(), new Inventory(1), 1));
        saves.add(new Domain(new ArrayList<Maze>(), new Inventory(1), 1));
        return saves;
    }

    /**
     * Get the initial domain
     * 
     * @return The initial domain
     */
    public static Domain getInitialDomain() {
        return new Domain(List.of(Helper.makeMaze()), new Inventory(8), 1);
    }

    /**
     * Unserialise a maze from xml
     * 
     * @param xml The xml to unserialise
     * @return The unserialised maze
     */
    public static Maze unserializeMaze(String xml) {
        return new Maze(new Tile[1][1]);
    }

    /**
     * Load a maze from a file
     * 
     * @param path The file path to load from
     * @return The loaded maze
     */
    public static Maze load(String path) {
        // TODO: Implement
        return new Maze(new Tile[1][1]);
        // return new Maze(new Tile[1][1]);
    }

    /**
     * Get all mazes in res/mazes
     * 
     * @return A list of maze objects
     */
    public static List<Maze> getMazes() {
        return new ArrayList<Maze>();
    }

}
