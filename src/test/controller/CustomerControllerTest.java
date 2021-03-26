package test.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.AddressController;
import controller.CustomerController;
import db.DBConnection;
import db.DataAccessException;
import model.Address;
import model.Customer;

public class CustomerControllerTest {
    private static CustomerController customerController;
    private static AddressController addressController;

    @BeforeAll
    static void initializeData() {
        DBConnection.connect();
        customerController = new CustomerController();
        addressController = new AddressController();
    }

    @Test
    void createUniqueCustomerWithUniqueAddressShouldCreate() {
        Address address = new Address("A country", "A zipcode", "A street name", 0);
        Customer customer = new Customer("A name", "A name", "A phone number", "An email", "Old_Customer",
                "Some company").setAddress(address);
        boolean createdAddress = false;
        boolean createdCustomer = false;
        try {
            createdAddress = addressController.createAddress(address);
            createdCustomer = customerController.createCustomer(customer);
        } catch (DataAccessException e) {
            fail(e);
        } finally {
            try {
                customerController.deleteCustomer(customer.getPersonID());
                addressController.deleteAddress(address.getAddressID());
            } catch (DataAccessException e) {
                fail(e);
            }
        }
        assertTrue(createdAddress);
        assertTrue(createdCustomer);
    }

    @Test
    void createUniqueCustomerWithDuplicateAddressShouldCreateOnlyCustomer() {
        int genCustomer = 0;
        try {
            Address address = addressController.findAddressByID(1, false);

            Customer customer = new Customer("A name", "A name", "A phone number", "An email", "Old_Customer",
                    "Some company").setAddress(address);
            boolean createdCustomer = customerController.createCustomer(customer);
            genCustomer = customer.getPersonID();
            assertTrue(customer.getAddress().getAddressID() == address.getAddressID());
            assertTrue(createdCustomer);
        } catch (DataAccessException e) {
            fail(e);
        } finally {
            try {
                customerController.deleteCustomer(genCustomer);
            } catch (DataAccessException e) {
                fail(e);
            }
        }
    }

    @Test
    void createDuplicateCustomerShouldNotCreate() {
        try {
            Address address = addressController.findAddressByID(1, false);
            Customer customer = customerController.findCustomerByID(1, true).setAddress(address);
            boolean createdCustomer = customerController.createCustomer(customer);
            assertFalse(createdCustomer);
        } catch (DataAccessException e) {
            fail(e);
        }
    }

    @Test
    void deleteCustomerShouldDelete() {
        Address address = new Address("A country", "A zipcode", "A street name", 0);
        Customer customer = new Customer("A name", "A name", "A phone number", "An email", "Old_Customer",
                "Some company").setAddress(address);
        try {
            addressController.createAddress(address);
            customerController.createCustomer(customer);

            boolean deleted = customerController.deleteCustomer(customer.getPersonID());
            assertTrue(deleted);
        } catch (DataAccessException e) {
            fail(e);
        } finally {
            try {
                addressController.deleteAddress(address.getAddressID());
                customerController.deleteCustomer(customer.getPersonID());
            } catch (DataAccessException e) {
                fail(e);
            }
        }
    }
}
