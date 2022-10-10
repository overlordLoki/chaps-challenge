package test.nz.ac.vuw.ecs.swen225.gp6.persistency;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.awt.event.InputEvent;

import org.dom4j.Document;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.Helper;
import nz.ac.vuw.ecs.swen225.gp6.domain.Inventory;
import nz.ac.vuw.ecs.swen225.gp6.domain.Maze;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.Pair;
import nz.ac.vuw.ecs.swen225.gp6.recorder.datastructures.RecordTimeline;

import static java.awt.event.KeyEvent.*;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.*;
import static org.junit.jupiter.api.Assertions.*;

public class BaseTests {
    @Test
    public void testDomainSerialise() {
        Inventory inventory = new Inventory(1);
        inventory.addItem(TileType.makeTile(TileType.GreenKey, new TileInfo(new Loc(1, 1))));
        Maze maze = Helper.makeMaze();
        Domain domain = new Domain(List.of(maze), inventory, 1);
        System.out.println(domain);
        assertEquals(Persistency.serialiseDomain(domain).asXML(),
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <domain><levels><level index="0" name="Level 1"><grid width="10" height="10"><cell x="0" y="0"><wall/></cell><cell x="0" y="1"><wall/></cell><cell x="0" y="2"><wall/></cell><cell x="0" y="3"><wall/></cell><cell x="0" y="4"><wall/></cell><cell x="0" y="5"><wall/></cell><cell x="0" y="6"><wall/></cell><cell x="0" y="7"><wall/></cell><cell x="0" y="8"><wall/></cell><cell x="0" y="9"><wall/></cell><cell x="1" y="0"><wall/></cell><cell x="1" y="1"><floor/></cell><cell x="1" y="2"><floor/></cell><cell x="1" y="3"><floor/></cell><cell x="1" y="4"><floor/></cell><cell x="1" y="5"><floor/></cell><cell x="1" y="6"><floor/></cell><cell x="1" y="7"><floor/></cell><cell x="1" y="8"><floor/></cell><cell x="1" y="9"><wall/></cell><cell x="2" y="0"><wall/></cell><cell x="2" y="1"><hero/></cell><cell x="2" y="2"><floor/></cell><cell x="2" y="3"><key color="blue"/></cell><cell x="2" y="4"><key color="yellow"/></cell><cell x="2" y="5"><key color="green"/></cell><cell x="2" y="6"><key color="orange"/></cell><cell x="2" y="7"><floor/></cell><cell x="2" y="8"><floor/></cell><cell x="2" y="9"><wall/></cell><cell x="3" y="0"><wall/></cell><cell x="3" y="1"><enemy/></cell><cell x="3" y="2"><floor/></cell><cell x="3" y="3"><blueLock/></cell><cell x="3" y="4"><yellowLock/></cell><cell x="3" y="5"><greenLock/></cell><cell x="3" y="6"><orangeLock/></cell><cell x="3" y="7"><floor/></cell><cell x="3" y="8"><floor/></cell><cell x="3" y="9"><wall/></cell><cell x="4" y="0"><wall/></cell><cell x="4" y="1"><floor/></cell><cell x="4" y="2"><floor/></cell><cell x="4" y="3"><floor/></cell><cell x="4" y="4"><floor/></cell><cell x="4" y="5"><floor/></cell><cell x="4" y="6"><exitDoor/></cell><cell x="4" y="7"><floor/></cell><cell x="4" y="8"><floor/></cell><cell x="4" y="9"><wall/></cell><cell x="5" y="0"><wall/></cell><cell x="5" y="1"><coin/></cell><cell x="5" y="2"><floor/></cell><cell x="5" y="3"><floor/></cell><cell x="5" y="4"><floor/></cell><cell x="5" y="5"><floor/></cell><cell x="5" y="6"><floor/></cell><cell x="5" y="7"><floor/></cell><cell x="5" y="8"><floor/></cell><cell x="5" y="9"><wall/></cell><cell x="6" y="0"><wall/></cell><cell x="6" y="1"><floor/></cell><cell x="6" y="2"><floor/></cell><cell x="6" y="3"><floor/></cell><cell x="6" y="4"><floor/></cell><cell x="6" y="5"><floor/></cell><cell x="6" y="6"><floor/></cell><cell x="6" y="7"><floor/></cell><cell x="6" y="8"><floor/></cell><cell x="6" y="9"><wall/></cell><cell x="7" y="0"><wall/></cell><cell x="7" y="1"><floor/></cell><cell x="7" y="2"><floor/></cell><cell x="7" y="3"><floor/></cell><cell x="7" y="4"><floor/></cell><cell x="7" y="5"><floor/></cell><cell x="7" y="6"><floor/></cell><cell x="7" y="7"><floor/></cell><cell x="7" y="8"><floor/></cell><cell x="7" y="9"><wall/></cell><cell x="8" y="0"><wall/></cell><cell x="8" y="1"><floor/></cell><cell x="8" y="2"><floor/></cell><cell x="8" y="3"><floor/></cell><cell x="8" y="4"><floor/></cell><cell x="8" y="5"><floor/></cell><cell x="8" y="6"><floor/></cell><cell x="8" y="7"><floor/></cell><cell x="8" y="8"><floor/></cell><cell x="8" y="9"><wall/></cell><cell x="9" y="0"><wall/></cell><cell x="9" y="1"><wall/></cell><cell x="9" y="2"><wall/></cell><cell x="9" y="3"><wall/></cell><cell x="9" y="4"><wall/></cell><cell x="9" y="5"><wall/></cell><cell x="9" y="6"><wall/></cell><cell x="9" y="7"><wall/></cell><cell x="9" y="8"><wall/></cell><cell x="9" y="9"><wall/></cell></grid></level></levels><inventory size="1"><key color="green"/></inventory></domain>""");
    }

    @Test
    void testDomainSave() throws IOException {
        Inventory inventory = new Inventory(1);
        inventory.addItem(TileType.makeTile(TileType.GreenKey, new TileInfo(new Loc(1, 1))));
        Maze maze = Helper.makeMaze();
        Domain domain = new Domain(List.of(maze), inventory, 1);
        System.out.println(domain);
        Persistency.saveDomain(domain, 1);
    }

    @Test
    public void testMazeSerialization() {
        Maze maze = Helper.makeMaze();
        Document doc = Persistency.serialiseMaze(maze, 0);
        System.out.println(doc.asXML());
        assertEquals(doc.asXML(),
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <level index="0" name="Level 1"><grid width="10" height="10"><cell x="0" y="0"><wall/></cell><cell x="0" y="1"><wall/></cell><cell x="0" y="2"><wall/></cell><cell x="0" y="3"><wall/></cell><cell x="0" y="4"><wall/></cell><cell x="0" y="5"><wall/></cell><cell x="0" y="6"><wall/></cell><cell x="0" y="7"><wall/></cell><cell x="0" y="8"><wall/></cell><cell x="0" y="9"><wall/></cell><cell x="1" y="0"><wall/></cell><cell x="1" y="1"><floor/></cell><cell x="1" y="2"><floor/></cell><cell x="1" y="3"><floor/></cell><cell x="1" y="4"><floor/></cell><cell x="1" y="5"><floor/></cell><cell x="1" y="6"><floor/></cell><cell x="1" y="7"><floor/></cell><cell x="1" y="8"><floor/></cell><cell x="1" y="9"><wall/></cell><cell x="2" y="0"><wall/></cell><cell x="2" y="1"><hero/></cell><cell x="2" y="2"><floor/></cell><cell x="2" y="3"><key color="blue"/></cell><cell x="2" y="4"><key color="yellow"/></cell><cell x="2" y="5"><key color="green"/></cell><cell x="2" y="6"><key color="orange"/></cell><cell x="2" y="7"><floor/></cell><cell x="2" y="8"><floor/></cell><cell x="2" y="9"><wall/></cell><cell x="3" y="0"><wall/></cell><cell x="3" y="1"><enemy/></cell><cell x="3" y="2"><floor/></cell><cell x="3" y="3"><blueLock/></cell><cell x="3" y="4"><yellowLock/></cell><cell x="3" y="5"><greenLock/></cell><cell x="3" y="6"><orangeLock/></cell><cell x="3" y="7"><floor/></cell><cell x="3" y="8"><floor/></cell><cell x="3" y="9"><wall/></cell><cell x="4" y="0"><wall/></cell><cell x="4" y="1"><floor/></cell><cell x="4" y="2"><floor/></cell><cell x="4" y="3"><floor/></cell><cell x="4" y="4"><floor/></cell><cell x="4" y="5"><floor/></cell><cell x="4" y="6"><exitDoor/></cell><cell x="4" y="7"><floor/></cell><cell x="4" y="8"><floor/></cell><cell x="4" y="9"><wall/></cell><cell x="5" y="0"><wall/></cell><cell x="5" y="1"><coin/></cell><cell x="5" y="2"><floor/></cell><cell x="5" y="3"><floor/></cell><cell x="5" y="4"><floor/></cell><cell x="5" y="5"><floor/></cell><cell x="5" y="6"><floor/></cell><cell x="5" y="7"><floor/></cell><cell x="5" y="8"><floor/></cell><cell x="5" y="9"><wall/></cell><cell x="6" y="0"><wall/></cell><cell x="6" y="1"><floor/></cell><cell x="6" y="2"><floor/></cell><cell x="6" y="3"><floor/></cell><cell x="6" y="4"><floor/></cell><cell x="6" y="5"><floor/></cell><cell x="6" y="6"><floor/></cell><cell x="6" y="7"><floor/></cell><cell x="6" y="8"><floor/></cell><cell x="6" y="9"><wall/></cell><cell x="7" y="0"><wall/></cell><cell x="7" y="1"><floor/></cell><cell x="7" y="2"><floor/></cell><cell x="7" y="3"><floor/></cell><cell x="7" y="4"><floor/></cell><cell x="7" y="5"><floor/></cell><cell x="7" y="6"><floor/></cell><cell x="7" y="7"><floor/></cell><cell x="7" y="8"><floor/></cell><cell x="7" y="9"><wall/></cell><cell x="8" y="0"><wall/></cell><cell x="8" y="1"><floor/></cell><cell x="8" y="2"><floor/></cell><cell x="8" y="3"><floor/></cell><cell x="8" y="4"><floor/></cell><cell x="8" y="5"><floor/></cell><cell x="8" y="6"><floor/></cell><cell x="8" y="7"><floor/></cell><cell x="8" y="8"><floor/></cell><cell x="8" y="9"><wall/></cell><cell x="9" y="0"><wall/></cell><cell x="9" y="1"><wall/></cell><cell x="9" y="2"><wall/></cell><cell x="9" y="3"><wall/></cell><cell x="9" y="4"><wall/></cell><cell x="9" y="5"><wall/></cell><cell x="9" y="6"><wall/></cell><cell x="9" y="7"><wall/></cell><cell x="9" y="8"><wall/></cell><cell x="9" y="9"><wall/></cell></grid></level>""");
    }

    @Test
    public void testMazeDeserialization() {
        Maze maze = Helper.makeMaze();
        Document doc = Persistency.serialiseMaze(maze, 0);
        Maze maze2 = Persistency.deserialiseMaze(doc.getRootElement());
        maze2.toString();
        assertEquals(maze.toString(), maze2.toString());
    }

    @Test
    public void testRecorderTimelineSerialization() {
        RecordTimeline<Actions> timeline = new RecordTimeline<Actions>();
        timeline.add(10l, Actions.MOVE_DOWN);
        timeline.add(20l, Actions.MOVE_LEFT);

        Document doc = Persistency.serialiseRecordTimeline(timeline.getTimeline());

        assertEquals(doc.asXML(), """
                <?xml version="1.0" encoding="UTF-8"?>
                <recorder size="2"><MOVE_DOWN time="10"/><MOVE_LEFT time="20"/></recorder>""");
    }

    @Test
    public void testRecorderTimelineDeerialization() {
        RecordTimeline<Actions> timeline = new RecordTimeline<Actions>();
        timeline.add(10l, Actions.MOVE_DOWN);
        timeline.add(20l, Actions.MOVE_LEFT);

        Document doc = Persistency.serialiseRecordTimeline(timeline.getTimeline());
        Stack<Pair<Long, Actions>> timeline2 = Persistency.deserialiseRecordTimeline(doc);

        assertEquals(timeline.getTimeline().toString(), timeline2.toString());
    }

    @Test
    public void testDomainSerialisation() {
        Domain domain = Persistency.getInitialDomain();
        Document doc = Persistency.serialiseDomain(domain);
        // assertEquals(doc.asXML(), "");
        Domain domain2 = Persistency.deserialiseDomain(doc);

        assertEquals(domain.toString(), domain2.toString());
    }

    @Test
    public void deleteSave() {
        try {
            Persistency.deleteSave(1);
        } catch (Exception e) {
        }
    }

    @Test
    public void testLogLine() throws IOException {
        Persistency.log("test");
    }

    @Test
    public void testSerialisingConfiguration() {
        Configuration config = new Configuration(true, new EnumMap<>(Map.ofEntries(
                Map.entry(MOVE_UP, new Controller.Key(0, VK_UP)),
                Map.entry(MOVE_DOWN, new Controller.Key(0, VK_DOWN)),
                Map.entry(MOVE_LEFT, new Controller.Key(0, VK_LEFT)),
                Map.entry(MOVE_RIGHT, new Controller.Key(0, VK_RIGHT)),
                Map.entry(PAUSE_GAME, new Controller.Key(0, VK_SPACE)),
                Map.entry(RESUME_GAME, new Controller.Key(0, VK_ESCAPE)),
                Map.entry(TO_LEVEL_1, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_1)),
                Map.entry(TO_LEVEL_2, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_2)),
                Map.entry(QUIT_TO_MENU, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_X)),
                Map.entry(SAVE_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_S)),
                Map.entry(LOAD_GAME, new Controller.Key(InputEvent.CTRL_DOWN_MASK, VK_R)))));

        Element el = Persistency.serialiseConfiguration(config);
        String xml = el.asXML();
        Configuration config2 = Persistency.deserialiseConfiguration(el);

        assertEquals(config.toString(), config2.toString());
    }

    @Test
    public void testLoadingConfiguration() {
        Configuration config = Persistency.loadConfiguration();
        assertNotNull(config);
    }
}
