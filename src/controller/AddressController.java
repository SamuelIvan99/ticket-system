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

public class AddressController {
    private final AddressIF addressDB;

    /**
     * Initialize the address database thru the interface
     */
    public AddressController() {
        addressDB = new AddressDB();
    }

    /**
     * Create and insert new address into database
     * 
     * @param address
     */
    public boolean createAddress(Address address) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            if (addressDB.findEquals(address) == null)
                result = addressDB.insert(address);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while creating an address.", e);
        }
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
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            addressToUpdate.setCountry(country).setZipCode(zipCode).setStreetName(streetName);
            addressToUpdate.setDepartments(departments);

            if (addressDB.findEquals(addressToUpdate) == null)
                result = addressDB.update(addressToUpdate);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while updating an address.", e);
        }
    }

    /**
     * Delete an address from the database by ID
     */
    public boolean deleteAddress(int ID) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = addressDB.delete(ID);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while deleting an address.", e);
        }
    }

    public Address findEquals(Address address) throws DataAccessException {
        return addressDB.findEquals(address);
    }
}
