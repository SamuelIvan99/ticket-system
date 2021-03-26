package test.db;

import db.DBConnection;
import db.DataAccessException;
import db.EmployeeDB;
import model.Department;
import model.Employee;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDBTest {
    static EmployeeDB employeeDB;
    static Employee employee;
    static Department department;

    @BeforeAll
    static void initializeData() {
        DBConnection.connect();

        employeeDB = new EmployeeDB();
        department = new Department();
        department.setDepartmentID(1);
    }

    /**
     * Integration test Create employee with the database
     */
    @Test
    @Order(1)
    void createEmployee() throws DataAccessException {
        // Arrange
        boolean inserted = false;
        employee = new Employee(
                "firstName",
                "lastName",
                "phoneNumber",
                "email",
                "password",
                "New_Employee"
        );
        employee.setDepartment(department);

        // Act
        inserted = employeeDB.insert(employee);

        // Assert
        assertTrue(inserted);
    }

    @Test
    @Order(2)
    void getAllEmployeesTest() throws DataAccessException {
        // Arrange
        List<Employee> employees;

        // Act
        employees = employeeDB.getAll(false);

        // Assert
        assertTrue(!employees.isEmpty());
    }

    /**
     * Integration test Find the inserted employee by email to check for existence
     */
    @Test
    @Order(3)
    void findCreatedEmployeeByEmail() throws DataAccessException {
        // Arrange
        Employee foundEmployee;

        // Act
        foundEmployee = employeeDB.findByEmail("email", false);

        // Assert
        assertNotNull(foundEmployee);
    }

    @Test
    @Order(4)
    void updateCreatedEmployee() throws DataAccessException {
        // Arrange
        boolean updated = false;
        Employee employeeToUpdate = employeeDB.findByEmail("email", true);
        employeeToUpdate.setFirstName("newFirstName");

        // Act
        updated = employeeDB.update(employeeToUpdate);

        // Assert
        Employee updatedEmployee = employeeDB.findByEmail("email", false);
        assertTrue(updated);
        assertEquals("newFirstName", employeeToUpdate.getFirstName());
    }

    /**
     * Integration test Delete the created employee
     */
    @Test
    @Order(5)
    void deleteCreatedEmployee() throws DataAccessException {
        // Arrange
        boolean deleted = false;
        Employee employeeToDelete = employeeDB.findByEmail("email", false);

        // Act
        deleted = employeeDB.delete(employeeToDelete.getPersonID());

        // Assert
        assertTrue(deleted);
    }

    /**
     * Integration test check for a nonexistent employee
     */
    @Test
    @Order(6)
    void findNonExistentEmployeeByEmail() throws DataAccessException {
        // Arrange
        Employee foundEmployee;

        // Act
        foundEmployee = employeeDB.findByEmail("nonExistentEmail", false);

        // Assert
        assertNull(foundEmployee);
    }

    /**
     * Integration test check for an existent employee
     */
    @Test
    @Order(7)
    void findExistentEmployeeByID() throws DataAccessException {
        // Arrange
        Employee foundEmployee;

        // Act
        foundEmployee = employeeDB.findByID(1, false);

        // Assert
        assertNotNull(foundEmployee);
    }

    /**
     * Integration test check for a nonexistent employee
     */
    @Test
    @Order(8)
    void findNonExistentEmployeeByID() throws DataAccessException {
        // Arrange
        Employee foundEmployee;

        // Act
        foundEmployee = employeeDB.findByID(0, false);

        // Assert
        assertNull(foundEmployee);
    }
}
