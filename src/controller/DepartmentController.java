package controller;

import db.DBConnection;
import db.DataAccessException;
import db.DepartmentDB;
import db.DepartmentIF;
import model.Department;

import java.sql.SQLException;
import java.util.List;

public class DepartmentController {
    private final DepartmentIF departmentDB;

    /**
     * Initialize the department database thru the interface
     */
    public DepartmentController() {
        departmentDB = new DepartmentDB();
    }

    /**
     * Create and insert new department into database
     * 
     * @param department
     */
    public boolean createDepartment(Department department) throws DataAccessException {

        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            if (departmentDB.findByID(department.getDepartmentID(), false) == null)
                result = departmentDB.insert(department);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while creating a department.", e);
        }
    }

    /**
     * Find an department by ID
     * 
     * @param ID
     */
    public Department findDepartmentByID(int ID, boolean fullAssociation) throws DataAccessException {
        return departmentDB.findByID(ID, fullAssociation);
    }

    /**
     * Get all departmentes from the database as list
     */
    public List<Department> getAllDepartments(boolean fullAssociation) throws DataAccessException {
        return departmentDB.getAll(fullAssociation);
    }

    /**
     * Update an department with all the fields
     */
    public boolean updateDepartment(Department departmentToUpdate, String name) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            departmentToUpdate.setName(name);
            result = departmentDB.update(departmentToUpdate);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while updating a department.", e);
        }
    }

    /**
     * Delete an department from the database by ID
     */
    public boolean deleteDepartment(int ID) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = departmentDB.delete(ID);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while deleting a department.", e);
        }
    }
}
