package controller;

import db.CustomerDB;
import db.CustomerIF;
import db.DBConnection;
import db.DataAccessException;
import model.Address;
import model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerController {
    private final CustomerIF customerDB;
    private final AddressController addressController;

    public CustomerController() {
        customerDB = new CustomerDB();
        addressController = new AddressController();
    }

    public boolean createCustomer(Customer customer) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            if (findCustomerByEmail(customer.getEmail(), false) == null) {
                addressController.createAddress(customer.getAddress());
                result = customerDB.insert(customer);
            }
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while creating a customer.", e);
        }
    }

    public Customer findCustomerByID(int ID, boolean fullAssociation) throws DataAccessException {
        return customerDB.findByID(ID, fullAssociation);
    }

    public Customer findCustomerByEmail(String email, boolean fullAssociation) throws DataAccessException {
        return customerDB.findByEmail(email, fullAssociation);
    }

    public List<Customer> getAllCustomers(boolean fullAssociation) throws DataAccessException {
        return customerDB.getAll(fullAssociation);
    }

    public boolean updateCustomer(Customer customerToUpdate, String newFirstName, String newLastName,
            String newPhoneNumber, String newEmail, String newCustomerType, String newCompanyName,
            Address addressToUpdate) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            if (customerDB.findByEmail(newEmail, false) == null) {
                customerToUpdate.setFirstName(newFirstName).setLastName(newLastName).setPhoneNumber(newPhoneNumber)
                        .setEmail(newEmail);

                customerToUpdate.setCustomerType(newCustomerType).setCompanyName(newCompanyName)
                        .setAddress(addressToUpdate);

                result = customerDB.update(customerToUpdate);
            }

            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while updating a customer.", e);
        }
    }

    public boolean deleteCustomer(int ID) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = customerDB.delete(ID);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while deleting a customer.", e);
        }
    }
}