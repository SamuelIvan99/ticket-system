package test.db;

import db.DBConnection;
import db.DataAccessException;
import db.DepartmentDB;
import model.Department;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DepartmentDBTest {

    static DepartmentDB departmentDB;
    static Department department;
    static int createdDepartmentIndex;

    @BeforeAll
    static void initializeData() {
        DBConnection.connect();

        departmentDB = new DepartmentDB();
        department = new Department();
    }

    @Test
    @Order(1)
    void createDepartmentTest() throws DataAccessException {
        // Arrange
        boolean inserted = false;
        department = new Department(
                "name"
        );

        // Act
        inserted = departmentDB.insert(department);
        createdDepartmentIndex = department.getDepartmentID();

        // Assert
        assertTrue(inserted);
    }

    @Test
    @Order(2)
    void getAllDepartmentsTest() throws DataAccessException {
        // Arrange
        List<Department> departments;

        // Act
        departments = departmentDB.getAll(false);

        // Assert
        assertTrue(!departments.isEmpty());
    }

    @Test
    @Order(3)
    void findCreatedDepartmentByIDTest() throws DataAccessException {
        // Arrange
        boolean found = false;

        // Act
        department = departmentDB.findByID(createdDepartmentIndex, false);

        // Assert
        assertEquals("name", department.getName());
    }

    @Test
    @Order(4)
    void updateCreatedDepartmentTest() throws DataAccessException {
        // Arrange
        boolean updated = false;

        // Act
        department = departmentDB.findByID(createdDepartmentIndex, false);
        department.setName("newName");
        updated = departmentDB.update(department);

        // Assert
        assertTrue(updated);
    }

    @Test
    @Order(5)
    void deleteCreatedDepartmentTest() throws DataAccessException {
        // Arrange
        boolean deleted;

        // Act
        deleted = departmentDB.delete(createdDepartmentIndex);

        // Assert
        assertTrue(deleted);
    }
}
