package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Pair;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * This utility class is responsible for saving and loading recorder timelines for the Recorder
 * package.
 *
 * @author Benjamin Hong - 300605520
 */
public final class RecorderPersistency {

  /**
   * A private constructor to prevent instantiation.
   */
  private RecorderPersistency() {
  }

  /**
   * serialise a record timeline object to an XML document.
   *
   * @param timeline The timeline to serialise
   * @return The serialised timeline
   */
  private static Element serialiseTimeline(Stack<Pair<Long, Actions>> timeline) {
    Element element = DocumentHelper.createElement("timeline");
    element.addAttribute("size", timeline.size() + "");
    for (Pair<Long, Actions> pair : timeline) {
      Element action = element.addElement(pair.value().toString());
      action.addAttribute("time", pair.key() + "");
    }
    return element;
  }

  /**
   * Deserialise a record timeline object from an XML document.
   *
   * @param element The XML element to deserialise
   * @return The deserialised timeline
   */
  private static Stack<Pair<Long, Actions>> deserialiseTimeline(Element element) {
    Stack<Pair<Long, Actions>> timeline = new Stack<Pair<Long, Actions>>();
    for (Element action : element.elements()) {
      timeline.add(new Pair<Long, Actions>(Long.parseLong(action.attributeValue("time")),
          Actions.valueOf(action.getName())));
    }
    return timeline;
  }

  /**
   * Save a timeline to a slot.
   *
   * @param timeline The timeline to save
   * @param slot     The slot to save to
   * @throws IOException If the file cannot be written to
   */
  public static void saveTimeline(Stack<Pair<Long, Actions>> timeline, int slot)
      throws IOException {
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
    OutputStreamWriter out = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
    document.write(out);
    out.close();
  }

  /**
   * Load a timeline from a save slot.
   *
   * @param slot The slot to load from
   * @return The loaded timeline
   * @throws DocumentException If the XML document is malformed
   */
  public static Stack<Pair<Long, Actions>> loadTimeline(int slot) throws DocumentException {
    SAXReader reader = new SAXReader();
    Document document = reader.read(new File("res/recordings/" + slot + ".xml"));
    return deserialiseTimeline(document.getRootElement());
  }

}
