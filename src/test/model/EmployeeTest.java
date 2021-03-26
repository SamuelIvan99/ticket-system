package test.model;

import model.Employee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {

    static Employee employee;

    /**
     * Make sure Employee is not null to begin with
     */
    @BeforeAll
    static void initializeEmployee() {
        employee = new Employee();
    }

    /**
     * Test the constructor and the get methods
     */
    @Test
    void createEmployee() {
        employee = new Employee("Texanu", "Eufrosin", "01010101", "texanu@gmail.com", "texanueufrosin", "MANAGER");
        assertEquals("Texanu", employee.getFirstName());
        assertEquals("Eufrosin", employee.getLastName());
        assertEquals("01010101", employee.getPhoneNumber());
        assertEquals("texanu@gmail.com", employee.getEmail());
        assertEquals("texanueufrosin", employee.getPassword());
        assertEquals("Manager", employee.getEmployeeType().getLabel());
    }

    /**
     * Test the set methods and the get methods
     */
    @Test
    void updateEmployee() {
        employee
                .setFirstName("firstName")
                .setLastName("lastName")
                .setPhoneNumber("phoneNumber")
                .setEmail("email");

        employee
                .setPassword("password")
                .setEmployeeType("SALES_ASSISTANT");

        assertEquals("firstName", employee.getFirstName());
        assertEquals("lastName", employee.getLastName());
        assertEquals("phoneNumber", employee.getPhoneNumber());
        assertEquals("email", employee.getEmail());
        assertEquals("password", employee.getPassword());
        assertEquals("Sales_Assistant", employee.getEmployeeType().getLabel());
    }

}
