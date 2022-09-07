package Persistency.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import Domain.Domain;
import Persistency.Persistency;

public class BaseTests {
    @Test
    public void testEmptySerialise() {
        assertEquals(Persistency.serialize(new Domain()), "<Domain/>");
    }

    @Test
    public void testEmptyUnserialise() {
        assertEquals(Persistency.unserialize("<Domain/>").toString(), new Domain().toString());
    }

    @Test
    public void testEmptySave() {
        Persistency.save(new Domain(), "test.xml");

        // Open file and check contents
        try {
            String content = new String(Files.readAllBytes(Paths.get("test.xml"))).strip();
            assertEquals(content, "<Domain/>");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
