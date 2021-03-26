package db;

import model.Address;
import model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group-One
 * @implNote class implements insert, find, update, delete of a customer into
 * database
 */
public class CustomerDB implements CustomerIF {

    /**
     * Insert a customer into database.
     *
     * @param record customer to insert into database
     * @return boolean whether it was inserted successfully
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Customer record) throws DataAccessException {

        String INSERT_CUSTOMER = SqlUtil.buildInsert("Customer", "FirstName", "LastName", "Email", "Type",
                "CompanyName", "Phone", "AddressID");

        int generatedKey = DBConnection.executeInsertWithIdentity(INSERT_CUSTOMER, record.getFirstName(),
                record.getLastName(), record.getEmail(), record.getCustomerType().getLabel(), record.getCompanyName(),
                record.getPhoneNumber(), record.getAddress().getAddressID());

        record.setPersonID(generatedKey);

        return generatedKey > 0;
    }

    /**
     * Finds an customer in the database based on ID provided and builds it into an
     * object.
     *
     * @param ID              of an customer to be found
     * @param fullAssociation whether to find reference fields of an customer as
     *                        well
     * @return customer object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public Customer findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_CUSTOMER = "SELECT * " + "FROM [dbo].[Customer] " + "WHERE CustomerID = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_CUSTOMER, ID);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the customer with ID " + ID, e);
        }
        return null;
    }

    /**
     * Finds an customer in the database based on email provided and builds it into
     * an object.
     *
     * @param email           of an customer to be found
     * @param fullAssociation whether to find reference fields of an customer as
     *                        well
     * @return customer object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public Customer findByEmail(String email, boolean fullAssociation) throws DataAccessException {
        String SELECT_CUSTOMER = "SELECT * " + "FROM [dbo].[Customer] " + "WHERE Email = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_CUSTOMER, email);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the customer with email " + email, e);
        }
        return null;
    }

    /**
     * Updates attributes of an customer in the database.
     *
     * @param record customer to be updated.
     * @return boolean whether an customer was updated successfully.
     * @throws DataAccessException
     */
    @Override
    public boolean update(Customer record) throws DataAccessException {
        int rowsChanged = 0;

        String UPDATE_CUSTOMER = SqlUtil.buildUpdate("Customer", "CustomerID", "FirstName", "LastName", "Email", "Type",
                "CompanyName", "Phone", "AddressID");

        if (UPDATE_CUSTOMER != null) {
            rowsChanged = DBConnection.executeUpdate(UPDATE_CUSTOMER, record.getFirstName(), record.getLastName(),
                    record.getEmail(), record.getCustomerType().getLabel(), record.getCompanyName(),
                    record.getPhoneNumber(), record.getAddress().getAddressID(), record.getPersonID());
        }
        return rowsChanged > 0;
    }

    /**
     * Deletes an customer form the database with ID specified.
     *
     * @param ID of an customer
     * @return boolean whether the customer was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_CUSTOMER = "DELETE FROM [dbo].[Customer] " + "WHERE CustomerID = ?";

        return DBConnection.executeUpdate(DELETE_CUSTOMER, ID) > 0;
    }

    /**
     * Finds all customer in the database and builds them into a list.
     *
     * @param fullAssociation whether to include reference variables in each
     *                        customer
     * @return List<Customer> List of all customers in the database
     * @throws DataAccessException
     */
    @Override
    public List<Customer> getAll(boolean fullAssociation) throws DataAccessException {
        List<Customer> customers = new ArrayList<>();
        String SELECT_ALL = "SELECT * " + "FROM [dbo].[Customer]";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                customers.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not get all Customers", e);
        }
        return customers;
    }

    /**
     * Builds an Customer using data from database.
     *
     * @param resultSet Customer data from database
     * @return Customer object
     * @throws DataAccessException
     */
    public Customer buildObject(ResultSet resultSet, boolean fullAssociation) throws DataAccessException {
        Customer customer = new Customer();

        try {
            customer.setPersonID(resultSet.getInt("CustomerID")).setFirstName(resultSet.getString("FirstName"))
                    .setLastName(resultSet.getString("LastName")).setPhoneNumber(resultSet.getString("Phone"))
                    .setEmail(resultSet.getString("Email")).setFullName();
            customer.setCustomerType(resultSet.getString("Type")).setCompanyName(resultSet.getString("CompanyName"));

            if (fullAssociation) {
                AddressIF addressDB = new AddressDB();
                Address address = addressDB.findByID(resultSet.getInt("AddressID"), false);

                if (address != null) {
                    customer.setAddress(address);
                } else {
                    System.out.println("Could not find address with ID " + resultSet.getInt("AddressID"));
                }
            }
            return customer;
        } catch (SQLException e) {
            throw new DataAccessException("Could not build customer object.", e);
        }
    }
}