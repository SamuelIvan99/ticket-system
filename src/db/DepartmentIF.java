package db;

import model.Department;

/**
 * @author Group 1
 */
public interface DepartmentIF extends TableIF<Department> {
    boolean insert(Department record) throws DataAccessException;

    Department findDepartmentByName(String name, boolean fullAssociation) throws DataAccessException;
}
