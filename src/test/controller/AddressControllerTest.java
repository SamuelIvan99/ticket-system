package test.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import controller.AddressController;
import db.DataAccessException;
import model.Address;

public class AddressControllerTest {
    private static AddressController addressController;

    @BeforeAll
    static void initializeData() {
        addressController = new AddressController();
    }

    @Test
    void createUniqueAddressShouldCreate() {
        Address address = new Address("A country", "A zipcode", "A street name", 0);
        int genID = 0;
        try {
            boolean created = addressController.createAddress(address);
            genID = address.getAddressID();
            assertTrue(created);
        } catch (DataAccessException e) {
            fail(e);
        } finally {
            try {
                addressController.deleteAddress(genID);
            } catch (DataAccessException e) {
                fail(e);
            }
        }
    }

    @Test
    void createDuplicateAddressShouldNotCreate() {
        try {
            Address address = addressController.findAddressByID(1, false);
            boolean created = addressController.createAddress(address);
            assertFalse(created);
        } catch (DataAccessException e) {
            fail(e);
        }
    }

    @Test
    void deleteAddressShouldDelete() {
        Address address = new Address("A country", "A zipcode", "A street name", 0);
        try {
            addressController.createAddress(address);
            boolean deleted = addressController.deleteAddress(address.getAddressID());
            assertTrue(deleted);
        } catch (DataAccessException e) {
            fail(e);
        }
    }
}