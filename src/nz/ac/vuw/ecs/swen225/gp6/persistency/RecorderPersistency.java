package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.*;
import java.util.Stack;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Pair;

public class RecorderPersistency {

    /**
     * serialise a record timeline object to an XML document
     * 
     * @param timeline The timeline to serialise
     * @return The serialised timeline
     */
    public static Element serialiseTimeline(Stack<Pair<Long, Actions>> timeline) {
        Element root = DocumentHelper.createElement("timeline");
        root.addAttribute("size", timeline.size() + "");
        for (Pair<Long, Actions> pair : timeline) {
            Element action = root.addElement(pair.value().toString());
            action.addAttribute("time", pair.key() + "");
        }
        return root;
    }

    /**
     * Deserialise a record timeline object from an XML document
     * 
     * @param document The XML document to deserialise
     * @return The deserialised timeline
     */
    public static Stack<Pair<Long, Actions>> deserialiseTimeline(Element root) {
        Stack<Pair<Long, Actions>> timeline = new Stack<Pair<Long, Actions>>();
        for (Element action : root.elements()) {
            timeline.add(new Pair<Long, Actions>(Long.parseLong(action.attributeValue("time")),
                    Actions.valueOf(action.getName())));
        }
        return timeline;
    }

    /**
     * Save a timeline to a file
     * 
     * @param timeline The timeline to save
     * @param slot     The slot to save to
     */
    public static void saveTimeline(Stack<Pair<Long, Actions>> timeline, int slot) throws IOException {
        Element element = serialiseTimeline(timeline);
        Document document = DocumentHelper.createDocument();
        document.add(element);

        File dir = new File("res/recordings");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("Could not create directory");
            }
        }

        FileOutputStream fileStream = new FileOutputStream("res/recordings/" + slot + ".xml");
        OutputStreamWriter out = new OutputStreamWriter(fileStream, "UTF-8");
        document.write(out);
        out.close();
    }

    /**
     * Load a timeline from a file
     * 
     * @param slot The slot to load from
     * @return The loaded timeline
     */
    public static Stack<Pair<Long, Actions>> loadTimeline(int slot) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File("res/recordings/" + slot + ".xml"));
        return deserialiseTimeline(document.getRootElement());
    }

}
