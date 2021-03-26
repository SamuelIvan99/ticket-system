package test.db;

import db.DBConnection;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBConnectionTest {

    @Test
    @Order(1)
    public void connect() {
        assertDoesNotThrow(DBConnection::connect);
    }

    @Test
    @Order(2)
    public void disconnect() {
        assertDoesNotThrow(DBConnection::disconnect);
    }
}