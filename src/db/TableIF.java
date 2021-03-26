package db;

import java.util.List;

/**
 * @author Group 1
 */
public interface TableIF<T> {
    T findByID(int ID, boolean fullAssociation) throws DataAccessException;

    boolean update(T record) throws DataAccessException;

    boolean delete(int ID) throws DataAccessException;

    List<T> getAll(boolean fullAssociation) throws DataAccessException;
}
