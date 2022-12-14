package test.nz.ac.vuw.ecs.swen225.gp6.persistency;

import static java.awt.event.KeyEvent.VK_1;
import static java.awt.event.KeyEvent.VK_2;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_R;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_SPACE;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_X;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.LOAD_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_DOWN;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_LEFT;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_RIGHT;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.MOVE_UP;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.PAUSE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.QUIT_TO_MENU;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.RESUME_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.SAVE_GAME;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.TO_LEVEL_1;
import static nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions.TO_LEVEL_2;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.InputEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Actions;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Configuration;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Controller;
import nz.ac.vuw.ecs.swen225.gp6.app.utilities.Pair;
import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileInfo;
import nz.ac.vuw.ecs.swen225.gp6.domain.TileAnatomy.TileType;
import nz.ac.vuw.ecs.swen225.gp6.domain.Utility.Loc;
import nz.ac.vuw.ecs.swen225.gp6.persistency.AppPersistency;
import nz.ac.vuw.ecs.swen225.gp6.persistency.DomainPersistency;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Interceptor;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Logging;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Logging.Log;
import nz.ac.vuw.ecs.swen225.gp6.persistency.RecorderPersistency;
import org.dom4j.DocumentException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Tests for all classes in the persistency package.
 *
 * @author Benjamin Hong 300605520
 */
public class Tests {

  @Test
  void testDomainSaveAndOpen() throws IOException, DocumentException {
    File saves = new File("res/saves");
    System.out.println(saves.delete() ? "Deleted saves" : "Not deleted saves");
    Domain domain = DomainPersistency.getInitial();
    domain.getInv().addItem(TileType.makeTile(TileType.BlueKey, new TileInfo(new Loc(0, 0))));

    DomainPersistency.save(domain, 1);
    Domain domain2 = DomainPersistency.loadSave(1);

    assertEquals(domain.toString(), domain2.toString());
  }

  @Test
  public void deleteSave() throws IOException {
    DomainPersistency.delete(1);
  }

  @Test
  public void testMissingSave() throws IOException, DocumentException {
    Domain domain = DomainPersistency.getInitial();
    DomainPersistency.delete(1);
    Domain domain2 = DomainPersistency.loadSave(1);
    assertEquals(domain.toString(), domain2.toString());
  }

  @Test
  public void testCorruptLevel() throws IOException {
    try {
      FileOutputStream fileStream = new FileOutputStream("res/levels/level1.xml", true);
      OutputStreamWriter fw = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
      fw.write("corrupt adfhiufhoiwjlr");
      fw.close();

      System.out.println("Test that parsing fails and fallback level is used:");
      Domain domain = DomainPersistency.getInitial();
      assertEquals(domain.getCurrentMaze().toString(), DomainPersistency.fallbackMaze().toString());
    } finally {
      String data = Files.readString(new File("res/levels/level1.xml").toPath());
      data = data.replace("corrupt adfhiufhoiwjlr", "");
      FileOutputStream fileStream = new FileOutputStream("res/levels/level1.xml");
      OutputStreamWriter fw = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
      fw.write(data);
      fw.close();
    }
  }

  @Test
  public void testRecorderTimelineSavingLoading() throws IOException, DocumentException {
    Stack<Pair<Long, Actions>> timeline = new Stack<Pair<Long, Actions>>();
    timeline.add(new Pair<Long, Actions>(10L, Actions.MOVE_DOWN));
    timeline.add(new Pair<Long, Actions>(20L, Actions.MOVE_LEFT));

    RecorderPersistency.saveTimeline(timeline, 1);
    Stack<Pair<Long, Actions>> timeline2 = RecorderPersistency.loadTimeline(1);

    assertEquals(timeline.toString(), timeline2.toString());
  }

  @Test
  public void testConfiguration() throws IOException {
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

    AppPersistency.save(config);
    Configuration config2 = AppPersistency.load();

    assertEquals(config.toString(), config2.toString());
  }

  @Test
  public void testCorruptConfiguration() throws IOException, DocumentException {
    try {
      FileOutputStream fileStream = new FileOutputStream("res/config.xml", true);
      OutputStreamWriter fw = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
      fw.write("corrupt adfhiufhoiwjlr");
      fw.close();

      System.out.println("Test that parsing fails and fallback config is used:");
      AppPersistency.load();
    } finally {
      String data = Files.readString(new File("res/config.xml").toPath());
      data = data.replace("corrupt adfhiufhoiwjlr", "");
      FileOutputStream fileStream = new FileOutputStream("res/config.xml");
      OutputStreamWriter fw = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
      fw.write(data);
      fw.close();
    }
  }

  @Test
  public void testCorruptFallbackConfiguration() throws IOException, DocumentException {
    try {
      FileOutputStream fileStream = new FileOutputStream("res/config.xml", true);
      OutputStreamWriter fw = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
      fw.write("corrupt adfhiufhoiwjlr");
      fw.close();

      FileOutputStream fileStreamD = new FileOutputStream("res/defaultConfig.xml", true);
      OutputStreamWriter fwD = new OutputStreamWriter(fileStreamD, StandardCharsets.UTF_8);
      fwD.write("corrupt adfhiufhoiwjlr");
      fwD.close();

      System.out.println("Test that parsing fails and fallback config is used:");
      assertEquals(Configuration.getDefaultConfiguration().toString(),
          AppPersistency.load().toString());
    } finally {
      String data = Files.readString(new File("res/config.xml").toPath());
      data = data.replace("corrupt adfhiufhoiwjlr", "");
      FileOutputStream fileStream = new FileOutputStream("res/config.xml");
      OutputStreamWriter fw = new OutputStreamWriter(fileStream, StandardCharsets.UTF_8);
      fw.write(data);
      fw.close();

      String datad = Files.readString(new File("res/defaultConfig.xml").toPath());
      datad = datad.replace("corrupt adfhiufhoiwjlr", "");
      FileOutputStream fileStreamd = new FileOutputStream("res/defaultConfig.xml");
      OutputStreamWriter fwd = new OutputStreamWriter(fileStreamd, StandardCharsets.UTF_8);
      fwd.write(datad);
      fwd.close();
    }
  }

  @Test
  public void testEmptyLogFile() throws IOException {
    Logging.clearLogs();

    List<Log> logs = Logging.getLogs();

    Assert.assertEquals(0, logs.size());
  }

  @Test
  public void testLogging() throws IOException {
    Logging.clearLogs();
    Interceptor interceptor = new Interceptor(System.out);
    interceptor.print("test\n");
    interceptor.println("test2");
    interceptor.printf("test%d", 3);

    // Wait for logs to be written
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    List<Log> logs = Logging.getLogs();

    Assert.assertEquals(3, logs.size());
    Assert.assertEquals("test", logs.get(0).message());
    Assert.assertEquals("test2", logs.get(1).message());
    Assert.assertEquals("test3", logs.get(2).message());
  }
}
