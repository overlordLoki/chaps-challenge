package test.nz.ac.vuw.ecs.swen225.gp6.persistency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.dom4j.Document;
import org.junit.jupiter.api.Test;

import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
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

public class BaseTests {
    @Test
    public void testDomainSerialise() {
        Inventory inventory = new Inventory(1);
        inventory.addItem(TileType.makeTile(TileType.GreenKey, new TileInfo(new Loc(1, 1))));
        Maze maze = Helper.makeMaze();
        Domain domain = new Domain(List.of(maze), inventory, 1);
        System.out.println(domain);
        assertEquals(Persistency.serializeDomain(domain).asXML(),
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
        Document doc = Persistency.serializeMaze(maze, 0);
        System.out.println(doc.asXML());
        assertEquals(doc.asXML(),
                """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <level index="0" name="Level 1"><grid width="10" height="10"><cell x="0" y="0"><wall/></cell><cell x="0" y="1"><wall/></cell><cell x="0" y="2"><wall/></cell><cell x="0" y="3"><wall/></cell><cell x="0" y="4"><wall/></cell><cell x="0" y="5"><wall/></cell><cell x="0" y="6"><wall/></cell><cell x="0" y="7"><wall/></cell><cell x="0" y="8"><wall/></cell><cell x="0" y="9"><wall/></cell><cell x="1" y="0"><wall/></cell><cell x="1" y="1"><floor/></cell><cell x="1" y="2"><floor/></cell><cell x="1" y="3"><floor/></cell><cell x="1" y="4"><floor/></cell><cell x="1" y="5"><floor/></cell><cell x="1" y="6"><floor/></cell><cell x="1" y="7"><floor/></cell><cell x="1" y="8"><floor/></cell><cell x="1" y="9"><wall/></cell><cell x="2" y="0"><wall/></cell><cell x="2" y="1"><hero/></cell><cell x="2" y="2"><floor/></cell><cell x="2" y="3"><key color="blue"/></cell><cell x="2" y="4"><key color="yellow"/></cell><cell x="2" y="5"><key color="green"/></cell><cell x="2" y="6"><key color="orange"/></cell><cell x="2" y="7"><floor/></cell><cell x="2" y="8"><floor/></cell><cell x="2" y="9"><wall/></cell><cell x="3" y="0"><wall/></cell><cell x="3" y="1"><enemy/></cell><cell x="3" y="2"><floor/></cell><cell x="3" y="3"><blueLock/></cell><cell x="3" y="4"><yellowLock/></cell><cell x="3" y="5"><greenLock/></cell><cell x="3" y="6"><orangeLock/></cell><cell x="3" y="7"><floor/></cell><cell x="3" y="8"><floor/></cell><cell x="3" y="9"><wall/></cell><cell x="4" y="0"><wall/></cell><cell x="4" y="1"><floor/></cell><cell x="4" y="2"><floor/></cell><cell x="4" y="3"><floor/></cell><cell x="4" y="4"><floor/></cell><cell x="4" y="5"><floor/></cell><cell x="4" y="6"><exitDoor/></cell><cell x="4" y="7"><floor/></cell><cell x="4" y="8"><floor/></cell><cell x="4" y="9"><wall/></cell><cell x="5" y="0"><wall/></cell><cell x="5" y="1"><coin/></cell><cell x="5" y="2"><floor/></cell><cell x="5" y="3"><floor/></cell><cell x="5" y="4"><floor/></cell><cell x="5" y="5"><floor/></cell><cell x="5" y="6"><floor/></cell><cell x="5" y="7"><floor/></cell><cell x="5" y="8"><floor/></cell><cell x="5" y="9"><wall/></cell><cell x="6" y="0"><wall/></cell><cell x="6" y="1"><floor/></cell><cell x="6" y="2"><floor/></cell><cell x="6" y="3"><floor/></cell><cell x="6" y="4"><floor/></cell><cell x="6" y="5"><floor/></cell><cell x="6" y="6"><floor/></cell><cell x="6" y="7"><floor/></cell><cell x="6" y="8"><floor/></cell><cell x="6" y="9"><wall/></cell><cell x="7" y="0"><wall/></cell><cell x="7" y="1"><floor/></cell><cell x="7" y="2"><floor/></cell><cell x="7" y="3"><floor/></cell><cell x="7" y="4"><floor/></cell><cell x="7" y="5"><floor/></cell><cell x="7" y="6"><floor/></cell><cell x="7" y="7"><floor/></cell><cell x="7" y="8"><floor/></cell><cell x="7" y="9"><wall/></cell><cell x="8" y="0"><wall/></cell><cell x="8" y="1"><floor/></cell><cell x="8" y="2"><floor/></cell><cell x="8" y="3"><floor/></cell><cell x="8" y="4"><floor/></cell><cell x="8" y="5"><floor/></cell><cell x="8" y="6"><floor/></cell><cell x="8" y="7"><floor/></cell><cell x="8" y="8"><floor/></cell><cell x="8" y="9"><wall/></cell><cell x="9" y="0"><wall/></cell><cell x="9" y="1"><wall/></cell><cell x="9" y="2"><wall/></cell><cell x="9" y="3"><wall/></cell><cell x="9" y="4"><wall/></cell><cell x="9" y="5"><wall/></cell><cell x="9" y="6"><wall/></cell><cell x="9" y="7"><wall/></cell><cell x="9" y="8"><wall/></cell><cell x="9" y="9"><wall/></cell></grid></level>""");
    }

    @Test
    public void testMazeDeserialization() {
        Maze maze = Helper.makeMaze();
        Document doc = Persistency.serializeMaze(maze, 0);
        Maze maze2 = Persistency.deserializeMaze(doc.getRootElement());
        maze2.toString();
        assertEquals(maze.toString(), maze2.toString());
    }

    @Test
    public void testRecorderTimelineSerialization() {
        RecordTimeline<Actions> timeline = new RecordTimeline<Actions>();
        timeline.add(10l, Actions.MOVE_DOWN);
        timeline.add(20l, Actions.MOVE_LEFT);

        Document doc = Persistency.serializeRecordTimeline(timeline.getTimeline());

        assertEquals(doc.asXML(), """
                <?xml version="1.0" encoding="UTF-8"?>
                <recorder size="2"><MOVE_DOWN time="10"/><MOVE_LEFT time="20"/></recorder>""");
    }

    @Test
    public void testRecorderTimelineDeerialization() {
        RecordTimeline<Actions> timeline = new RecordTimeline<Actions>();
        timeline.add(10l, Actions.MOVE_DOWN);
        timeline.add(20l, Actions.MOVE_LEFT);

        Document doc = Persistency.serializeRecordTimeline(timeline.getTimeline());
        Stack<Pair<Long, Actions>> timeline2 = Persistency.deserializeRecordTimeline(doc);

        assertEquals(timeline.getTimeline().toString(), timeline2.toString());
    }

    @Test
    public void testDomainSerialisation() {
        Domain domain = Persistency.getInitialDomain();
        Document doc = Persistency.serializeDomain(domain);
        // assertEquals(doc.asXML(), "");
        Domain domain2 = Persistency.deserializeDomain(doc);

        assertEquals(domain.toString(), domain2.toString());
    }

    @Test
    public void deleteSave() throws IOException {
        Persistency.deleteSave(1);
    }

    // @Test
    // public void testEmptyUnserialise() {
    // assertEquals(Persistency.unserialize("<Domain/>").toString(), new
    // Domain().toString());
    // }

    // @Test
    // public void testEmptySave() {
    // try {
    // Persistency.save(new Domain(),
    // "/Users/benja/Documents/uni/swen225/chaps-challenge/test.xml");
    // } catch (IOException e1) {
    // fail("IOException thrown");
    // }

    // // Open file and check contents
    // try {
    // String content = new String(
    // Files.readAllBytes(Paths.get("/Users/benja/Documents/uni/swen225/chaps-challenge/test.xml")))
    // .strip();
    // assertEquals(content,
    // """
    // <?xml version="1.0" encoding="UTF-8"?>
    // <level index="0" name="Level 1"><grid width="10" height="10"><cell x="0"
    // y="0"/><cell x="0" y="1"/><cell x="0" y="2"/><cell x="0" y="3"/><cell x="0"
    // y="4"/><cell x="0" y="5"/><cell x="0" y="6"/><cell x="0" y="7"/><cell x="0"
    // y="8"/><cell x="0" y="9"/><cell x="1" y="0"/><cell x="1"
    // y="1"><wall/></cell><cell x="1" y="2"><wall/></cell><cell x="1"
    // y="3"><wall/></cell><cell x="1" y="4"><wall/></cell><cell x="1"
    // y="5"><wall/></cell><cell x="1" y="6"><wall/></cell><cell x="1"
    // y="7"><wall/></cell><cell x="1" y="8"><wall/></cell><cell x="1"
    // y="9"><wall/></cell><cell x="2" y="0"/><cell x="2" y="1"><wall/></cell><cell
    // x="2" y="2"><floor/></cell><cell x="2" y="3"><floor/></cell><cell x="2"
    // y="4"><floor/></cell><cell x="2" y="5"><floor/></cell><cell x="2"
    // y="6"><floor/></cell><cell x="2" y="7"><floor/></cell><cell x="2"
    // y="8"><floor/></cell><cell x="2" y="9"><floor/></cell><cell x="3"
    // y="0"/><cell x="3" y="1"><wall/></cell><cell x="3" y="2"><floor/></cell><cell
    // x="3" y="3"><floor/></cell><cell x="3" y="4"><floor/></cell><cell x="3"
    // y="5"><floor/></cell><cell x="3" y="6"><floor/></cell><cell x="3"
    // y="7"><floor/></cell><cell x="3" y="8"><floor/></cell><cell x="3"
    // y="9"><floor/></cell><cell x="4" y="0"/><cell x="4" y="1"><wall/></cell><cell
    // x="4" y="2"><floor/></cell><cell x="4" y="3"><floor/></cell><cell x="4"
    // y="4"><floor/></cell><cell x="4" y="5"><floor/></cell><cell x="4"
    // y="6"><floor/></cell><cell x="4" y="7"><floor/></cell><cell x="4"
    // y="8"><floor/></cell><cell x="4" y="9"><floor/></cell><cell x="5"
    // y="0"/><cell x="5" y="1"><wall/></cell><cell x="5" y="2"><floor/></cell><cell
    // x="5" y="3"><floor/></cell><cell x="5" y="4"><floor/></cell><cell x="5"
    // y="5"><floor/></cell><cell x="5" y="6"><floor/></cell><cell x="5"
    // y="7"><floor/></cell><cell x="5" y="8"><floor/></cell><cell x="5"
    // y="9"><floor/></cell><cell x="6" y="0"/><cell x="6" y="1"><wall/></cell><cell
    // x="6" y="2"><floor/></cell><cell x="6" y="3"><floor/></cell><cell x="6"
    // y="4"><floor/></cell><cell x="6" y="5"><floor/></cell><cell x="6"
    // y="6"><floor/></cell><cell x="6" y="7"><floor/></cell><cell x="6"
    // y="8"><floor/></cell><cell x="6" y="9"><floor/></cell><cell x="7"
    // y="0"/><cell x="7" y="1"><wall/></cell><cell x="7" y="2"><floor/></cell><cell
    // x="7" y="3"><floor/></cell><cell x="7" y="4"><floor/></cell><cell x="7"
    // y="5"><floor/></cell><cell x="7" y="6"><floor/></cell><cell x="7"
    // y="7"><floor/></cell><cell x="7" y="8"><floor/></cell><cell x="7"
    // y="9"><floor/></cell><cell x="8" y="0"/><cell x="8" y="1"><wall/></cell><cell
    // x="8" y="2"><floor/></cell><cell x="8" y="3"><floor/></cell><cell x="8"
    // y="4"><floor/></cell><cell x="8" y="5"><floor/></cell><cell x="8"
    // y="6"><floor/></cell><cell x="8" y="7"><floor/></cell><cell x="8"
    // y="8"><floor/></cell><cell x="8" y="9"><floor/></cell><cell x="9"
    // y="0"/><cell x="9" y="1"><wall/></cell><cell x="9" y="2"><floor/></cell><cell
    // x="9" y="3"><floor/></cell><cell x="9" y="4"><floor/></cell><cell x="9"
    // y="5"><floor/></cell><cell x="9" y="6"><floor/></cell><cell x="9"
    // y="7"><floor/></cell><cell x="9" y="8"><floor/></cell><cell x="9"
    // y="9"><floor/></cell></grid></level>""");
    // } catch (IOException e) {
    // e.printStackTrace();
    // fail();
    // }
    // }

    @Test
    public void testLogLine() throws IOException {
        Persistency.log("test");
    }
}
