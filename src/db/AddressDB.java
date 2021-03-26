package db;

import model.Address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group-One
 * @implNote class implements insert, find, update, delete of an address into
 * database
 */
public class AddressDB implements AddressIF {

    /**
     * Insert a address into database.
     *
     * @param record address to insert into database
     * @return boolean whether it was inserted successfully
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Address record) throws DataAccessException {

        String INSERT_ADDRESS = SqlUtil.buildInsert("Address", "Country", "Zipcode", "StreetName", "StreetNumber");

        int generatedKey = DBConnection.executeInsertWithIdentity(INSERT_ADDRESS, record.getCountry(),
                record.getZipCode(), record.getStreetName(), record.getStreetNumber());

        record.setAddressID(generatedKey);

        return generatedKey > 0;
    }

    /**
     * Finds an address in the database based on ID provided and builds it into an
     * object.
     *
     * @param ID              of an address to be found
     * @param fullAssociation whether to find reference fields of an address as well
     * @return Address object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public Address findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_ADDRESS = "SELECT * " + "FROM [dbo].[Address] " + "WHERE AddressID = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ADDRESS, ID);
            if (rs.next())
                return buildObject(rs);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the address with ID " + ID, e);
        }
        return null;
    }

    /**
     * Updates attributes of an address in the database.
     *
     * @param record address to be updated.
     * @return boolean whether an address was updated successfully.
     * @throws DataAccessException
     */
    @Override
    public boolean update(Address record) throws DataAccessException {
        int rowsChanged = 0;

        String UPDATE_ADDRESS = SqlUtil.buildUpdate("Address", "AddressID", "Country", "Zipcode", "StreetName",
                "StreetNumber");

        if (UPDATE_ADDRESS != null) {
            rowsChanged = DBConnection.executeUpdate(UPDATE_ADDRESS, record.getCountry(), record.getZipCode(),
                    record.getStreetName(), record.getStreetNumber(), record.getAddressID());
        }
        return rowsChanged > 0;
    }

    /**
     * Deletes an address form the database with ID specified.
     *
     * @param ID of an address
     * @return boolean whether the address was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_ADDRESS = "DELETE FROM [dbo].[Address] " + "WHERE AddressID = ?";

        return DBConnection.executeUpdate(DELETE_ADDRESS, ID) > 0;
    }

    /**
     * Finds all address in the database and builds them into a list.
     *
     * @param fullAssociation whether to include reference variables in each address
     * @return List<Address> List of all addresses in the database
     * @throws DataAccessException
     */
    @Override
    public List<Address> getAll(boolean fullAssociation) throws DataAccessException {
        List<Address> addresses = new ArrayList<>();
        String SELECT_ALL = "SELECT * " + "FROM [dbo].[Address]";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                addresses.add(buildObject(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not get all Addresses.", e);
        }
        return addresses;
    }

    /**
     * Builds an address using data from database.
     *
     * @param rs address data from database
     * @return Address object
     * @throws DataAccessException
     */
    public Address buildObject(ResultSet rs) throws DataAccessException {
        Address address = new Address();

        try {
            address.setCountry(rs.getString("Country")).setZipCode(rs.getString("Zipcode"))
                    .setStreetName(rs.getString("StreetName")).setStreetNumber(rs.getInt("StreetNumber"))
                    .setAddressID(rs.getInt("AddressID"));
            return address;
        } catch (SQLException e) {
            throw new DataAccessException("Could not build address object.", e);
        }
    }

    /**
     * Finds an object in the database with the same attributes
     *
     * @param address object to be compared
     * @return Address null or an object if it was found
     * @throws DataAccessException
     */
    @Override
    public Address findEquals(Address address) throws DataAccessException {
        String SELECT_ADDRESS = "SELECT * " + "FROM Address "
                + "WHERE Country = ? AND Zipcode = ? AND StreetName = ? AND StreetNumber = ?";

        ResultSet rs = DBConnection.executeQuery(SELECT_ADDRESS, address.getCountry(), address.getZipCode(),
                address.getStreetName(), address.getStreetNumber());
        try {
            if (rs.next()) {
                return buildObject(rs);
            } else
                return null;
        } catch (SQLException e) {
            throw new DataAccessException("Could not find " + address, e);
        }
    }
}