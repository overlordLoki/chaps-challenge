package test.nz.ac.vuw.ecs.swen225.gp6.persistency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import nz.ac.vuw.ecs.swen225.gp6.domain.Domain;
import nz.ac.vuw.ecs.swen225.gp6.persistency.Persistency;

public class BaseTests {
    @Test
    public void testEmptySerialise() {
        assertEquals(Persistency.serialize(new Domain()).asXML(), """
                <?xml version="1.0" encoding="UTF-8"?>
                <Domain/>""");
    }

    @Test
    public void testEmptyUnserialise() {
        assertEquals(Persistency.unserialize("<Domain/>").toString(), new Domain().toString());
    }

    @Test
    public void testEmptySave() {
        try {
            Persistency.save(new Domain(), "/Users/benja/Documents/uni/swen225/chaps-challenge/test.xml");
        } catch (IOException e1) {
            fail("IOException thrown");
        }

        // Open file and check contents
        try {
            String content = new String(
                    Files.readAllBytes(Paths.get("/Users/benja/Documents/uni/swen225/chaps-challenge/test.xml")))
                    .strip();
            assertEquals(content, """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <Domain/>""");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
