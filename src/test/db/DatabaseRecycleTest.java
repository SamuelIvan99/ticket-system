package test.db;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;

import org.junit.jupiter.api.Test;

import db.DatabaseRecycle;

public class DatabaseRecycleTest {

    @Test
    void recycleDatabase() {
        char s = File.separatorChar;
        String setup = String.format(".%cscripts%csetup%c", s, s, s);
        String clean = String.format(".%cscripts%ccleanup%c", s, s, s);
        String populate = String.format(".%cscripts%cpopulate%c", s, s, s);
        assertDoesNotThrow(() -> {
            DatabaseRecycle.recycleDatabase(clean, setup, populate);
        });
    }
}
