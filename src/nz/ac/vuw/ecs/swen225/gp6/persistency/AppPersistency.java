package nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EnumMap;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller.Key;

public class AppPersistency {

    /**
     * Serialise the configuration file
     * 
     * @param config The configuration object
     * @return The xml element
     */
    public static Element serialise(Configuration config) {
        Element root = DocumentHelper.createElement("configuration");

        // add texture pack
        Element texturePack = root.addElement("texturePack");
        texturePack.setText(config.getTexturePack());

        // add key bindings
        Element keyBindings = root.addElement("keyBindings");
        for (Actions action : Actions.values()) {
            Key key = config.getUserKeyBindings().get(action);
            if (key == null)
                continue;
            Element keyElement = keyBindings.addElement(action.name());
            keyElement.addAttribute("modifier", Integer.toString(key.modifier()));
            keyElement.addAttribute("key", Integer.toString(key.key()));
        }

        // add music enabled
        Element musicEnabled = root.addElement("musicEnabled");
        musicEnabled.setText(Boolean.toString(config.isMusicOn()));

        // add view distance
        Element viewDistance = root.addElement("viewDistance");
        viewDistance.setText(Integer.toString(config.getViewDistance()));

        return root;
    }

    /**
     * Deserialise the configuration file
     * 
     * @param element The xml element
     * @return Configuration object
     */
    public static Configuration deserialise(Element root) {
        // get texture pack
        String texturePack = root.element("texturePack").getText();

        // get key bindings
        EnumMap<Actions, Key> keyBindings = new EnumMap<>(Actions.class);
        for (Actions action : Actions.values()) {
            Element keyElement = root.element("keyBindings").element(action.name());
            if (keyElement == null)
                continue;
            int modifier = Integer.parseInt(keyElement.attributeValue("modifier"));
            int key = Integer.parseInt(keyElement.attributeValue("key"));
            keyBindings.put(action, new Key(modifier, key));
        }

        // get music enabled
        Boolean musicEnabled = Boolean.parseBoolean(root.element("musicEnabled").getText());

        // get view distance
        Integer viewDistance = Integer.parseInt(root.element("viewDistance").getText());

        return new Configuration(musicEnabled, texturePack, viewDistance, keyBindings);
    }

    /**
     * Load configuration from res/config.xml
     * 
     * @return Configuration object
     */
    public static Configuration load() {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read("res/config.xml");
            return deserialise(document.getRootElement());
        } catch (Throwable e) {
            try {
                System.out.println("Failed to load configuration: " + e.getMessage());
                Document document = reader.read("res/defaultConfig.xml");
                return deserialise(document.getRootElement());
            } catch (Throwable f) {
                f.printStackTrace();
                return Configuration.getDefaultConfiguration();
            }
        }
    }

    /**
     * Save configuration to res/config.xml
     * 
     * @param config The configuration object
     */
    public static void save(Configuration config) throws IOException {
        Element element = serialise(config);
        Document document = DocumentHelper.createDocument();
        document.add(element);

        File dir = new File("res");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        FileWriter out = new FileWriter("res/config.xml");
        document.write(out);
        out.close();
    }

}
