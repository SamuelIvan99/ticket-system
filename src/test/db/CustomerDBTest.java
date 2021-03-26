package test.db;

import db.CustomerDB;
import db.DBConnection;
import db.DataAccessException;
import model.Address;
import model.Customer;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerDBTest {
    static CustomerDB customerDB;
    static Customer customer;
    static Address address;

    @BeforeAll
    static void initializeData() {
        DBConnection.connect();

        customerDB = new CustomerDB();
        address = new Address();
        address.setAddressID(1);
    }

    /**
     * Integration test Create customer with the database
     */
    @Test
    @Order(1)
    void createCustomer() throws DataAccessException {
        // Arrange
        boolean inserted = false;
        customer = new Customer(
                "firstName",
                "lastName",
                "phoneNumber",
                "email",
                "New_Customer",
                "companyName"
        );
        customer.setAddress(address);

        // Act
        inserted = customerDB.insert(customer);

        // Assert
        assertTrue(inserted);
    }

    @Test
    @Order(2)
    void getAllCustomersTest() throws DataAccessException {
        // Arrange
        List<Customer> customers;

        // Act
        customers = customerDB.getAll(false);

        // Assert
        assertTrue(!customers.isEmpty());
    }

    /**
     * Integration test Find the inserted customer by email to check for existence
     */
    @Test
    @Order(3)
    void findCreatedCustomerByEmail() throws DataAccessException {
        // Arrange
        Customer foundCustomer;

        // Act
        foundCustomer = customerDB.findByEmail("email", false);

        // Assert
        assertNotNull(foundCustomer);
    }

    @Test
    @Order(4)
    void updateCreatedCustomer() throws DataAccessException {
        // Arrange
        boolean updated = false;
        Customer customerToDelete = customerDB.findByEmail("email", true);

        // Act
        updated = customerDB.update(customerToDelete);

        // Assert
        assertTrue(updated);
    }

    /**
     * Integration test Delete the created customer
     */
    @Test
    @Order(5)
    void deleteCreatedCustomer() throws DataAccessException {
        // Arrange
        boolean deleted = false;
        Customer customerToDelete = customerDB.findByEmail("email", false);

        // Act
        deleted = customerDB.delete(customerToDelete.getPersonID());

        // Assert
        assertTrue(deleted);
    }

    /**
     * Integration test check for a nonexistent customer
     */
    @Test
    @Order(6)
    void findNonExistentCustomerByEmail() throws DataAccessException {
        // Arrange
        Customer foundCustomer;

        // Act
        foundCustomer = customerDB.findByEmail("nonExistentEmail", false);

        // Assert
        assertNull(foundCustomer);
    }

    /**
     * Integration test check for an existent customer
     */
    @Test
    @Order(7)
    void findExistentCustomerByID() throws DataAccessException {
        // Arrange
        Customer foundCustomer;

        // Act
        foundCustomer = customerDB.findByID(1, false);

        // Assert
        assertNotNull(foundCustomer);
    }

    /**
     * Integration test check for a nonexistent customer
     */
    @Test
    @Order(8)
    void findNonExistentCustomerByID() throws DataAccessException {
        // Arrange
        Customer foundCustomer;

        // Act
        foundCustomer = customerDB.findByID(0, false);

        // Assert
        assertNull(foundCustomer);
    }
}
