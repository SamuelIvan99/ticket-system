package db;

import model.Address;

/**
 * @author Group 1
 */
public interface AddressIF extends TableIF<Address> {
    boolean insert(Address record) throws DataAccessException;

    public Address findEquals(Address address) throws DataAccessException;
}