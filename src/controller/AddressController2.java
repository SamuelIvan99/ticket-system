package controller;

import db.AddressDB;
import db.AddressIF;
import db.DBConnection;
import db.DataAccessException;
import model.Address;
import model.Department;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressController2 {
    private final AddressIF addressDB;

    /**
     * Initialize the address database thru the interface
     */
    public AddressController2() {
        addressDB = new AddressDB();
    }

    private boolean withTransaction(Transactionable transact, Address address) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = transact.action(address);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {}
            throw new DataAccessException("Transaction error.", e);
        }
    }

    /**
     * Create and insert new address into database
     * 
     * @param address
     */
    public boolean createAddress(Address address) throws DataAccessException {
        Transactionable transact = a -> {
            boolean result = false;
            try {
                if (addressDB.findEquals(a) == null)
                    result = addressDB.insert(a);
            } catch (DataAccessException e) {
            }
            return result;
        };
        return withTransaction(transact, address);
    }

    /**
     * Find an address by ID
     * 
     * @param ID
     */
    public Address findAddressByID(int ID, boolean fullAssociation) throws DataAccessException {
        return addressDB.findByID(ID, fullAssociation);
    }

    /**
     * Get all addresses from the database as list
     */
    public List<Address> getAllAddresses(boolean fullAssociation) throws DataAccessException {
        return addressDB.getAll(fullAssociation);
    }

    /**
     * Update an address with all the fields
     */
    public boolean updateAddress(Address addressToUpdate, String country, String zipCode, String streetName,
            int streetNumber, ArrayList<Department> departments) throws DataAccessException {
        Transactionable transact = a -> {
            boolean result = false;
            a.setCountry(country).setZipCode(zipCode).setStreetName(streetName);
            a.setDepartments(departments);

            try {
                if (addressDB.findEquals(a) == null)
                    result = addressDB.update(a);
            } catch (DataAccessException e) {
            }
            return result;
        };
        return withTransaction(transact, addressToUpdate);
    }

    /**
     * Delete an address from the database by ID
     */
    public boolean deleteAddress(int ID) throws DataAccessException {
        Transactionable transact = a -> {
            boolean result = false;
            try {
                result = addressDB.delete(ID);
            } catch (DataAccessException e) {
            }
            return result;
        };
        return withTransaction(transact, new Address());
        // We have to do some changes in the method signatures
        // to make this work
    }

    public Address findEquals(Address address) throws DataAccessException {
        return addressDB.findEquals(address);
    }
}
