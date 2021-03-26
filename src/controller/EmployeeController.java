package controller;

import db.DBConnection;
import db.DataAccessException;
import db.EmployeeDB;
import db.EmployeeIF;
import model.Department;
import model.Employee;

import java.sql.SQLException;
import java.util.List;

public class EmployeeController {
    private final EmployeeIF employeeDB;

    /**
     * Initialize the employee database thru the interface
     */
    public EmployeeController() {
        employeeDB = new EmployeeDB();
    }

    /**
     * Create and insert new employee into database
     * 
     * @param employee
     */
    public boolean createEmployee(Employee employee) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = employeeDB.insert(employee);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while creating an employee.", e);
        }
    }

    /**
     * Find an employee by ID
     * 
     * @param ID
     */
    public Employee findEmployeeByID(int ID, boolean fullAssociation) throws DataAccessException {
        return employeeDB.findByID(ID, fullAssociation);
    }

    public Employee findEmployeeByEmail(String email, boolean fullAssociation) throws DataAccessException {
        return employeeDB.findByEmail(email, fullAssociation);
    }

    /**
     * Get all employees from the database as list
     */
    public List<Employee> getAllEmployees(boolean fullAssociation) throws DataAccessException {
        return employeeDB.getAll(fullAssociation);
    }

    /**
     * Update an employee with all the fields
     */
    public boolean updateEmployee(Employee employeeToUpdate, String newFirstName, String newLastName,
            String newPhoneNumber, String newEmail, String newPassword, String newEmployeeType,
            Department newDepartment) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            if (employeeDB.findByEmail(newEmail, false) == null) {
                employeeToUpdate.setFirstName(newFirstName).setLastName(newLastName).setPhoneNumber(newPhoneNumber)
                        .setEmail(newEmail);

                employeeToUpdate.setPassword(newPassword).setEmployeeType(newEmployeeType).setDepartment(newDepartment);

                result = employeeDB.update(employeeToUpdate);
            }

            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while updating an employee.", e);
        }
    }

    /**
     * Delete an employee from the database by ID
     */
    public boolean deleteEmployee(int ID) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = employeeDB.delete(ID);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while deleting an employee.", e);
        }
    }
}
