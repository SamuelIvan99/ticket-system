package db;

import model.Customer;

/**
 * @author Group 1
 */
public interface CustomerIF extends TableIF<Customer> {
    boolean insert(Customer record) throws DataAccessException;

    Customer findByEmail(String email, boolean fullAssociation) throws DataAccessException;
}