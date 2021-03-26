package db;

import model.Employee;

/**
 * @author Group 1
 */
public interface EmployeeIF extends TableIF<Employee> {
    boolean insert(Employee record) throws DataAccessException;

    public Employee findByEmail(String email, boolean fullAssociation) throws DataAccessException;
}
