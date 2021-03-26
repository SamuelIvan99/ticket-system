package db;

import model.Department;
import model.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group 1
 * @implNote class implements insert, find, update, delete of an employee into database
 */
public class EmployeeDB implements EmployeeIF {

    /**
     * inserts an employee into database
     *
     * @param record object to insert into database
     * @return boolean whether it was successfully inserted
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Employee record) throws DataAccessException {
        String INSERT_EMPLOYEE = SqlUtil.buildInsert("Employee",
                "FirstName", "LastName",
                "Email", "Type", "Password", "Phone", "DepartmentID");

        int generatedKey = DBConnection.executeInsertWithIdentity(INSERT_EMPLOYEE,
                record.getFirstName(), record.getLastName(), record.getEmail(),
                record.getEmployeeType().getLabel(), record.getPassword(),
                record.getPhoneNumber(), record.getDepartment().getDepartmentID());

        record.setPersonID(generatedKey);

        return generatedKey > 0;
    }

    /**
     * finds an employee in the database based on ID attribute
     *
     * @param ID
     * @param fullAssociation whether to include reference variable of an employee class
     * @return Employee object
     * @throws DataAccessException
     */
    @Override
    public Employee findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_EMPLOYEE =
                "SELECT * " +
                        "FROM [dbo].[Employee] " +
                        "WHERE EmployeeID = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_EMPLOYEE, ID);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the employee with ID " + ID, e);
        }
        return null;
    }

    /**
     * finds an employee in the database based on email attribute
     *
     * @param email
     * @param fullAssociation whether to include reference variable of an employee class
     * @return Employee object
     * @throws DataAccessException
     */
    @Override
    public Employee findByEmail(String email, boolean fullAssociation) throws DataAccessException {
        String SELECT_EMPLOYEE =
                "SELECT * " +
                        "FROM [dbo].[Employee] " +
                        "WHERE Email = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_EMPLOYEE, email);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the employee with email " + email, e);
        }
        return null;
    }

    /**
     * updates an employee in the database
     *
     * @param record to be updated
     * @return boolean whether it was successfully updated
     * @throws DataAccessException
     */
    @Override
    public boolean update(Employee record) throws DataAccessException {
        int rowsChanged = 0;

        String UPDATE_EMPLOYEE = SqlUtil.buildUpdate(
                "Employee",
                "EmployeeID",
                "FirstName", "LastName", "Email", "Type", "Password", "Phone", "DepartmentID");

        if (UPDATE_EMPLOYEE != null) {
            rowsChanged = DBConnection.executeUpdate(UPDATE_EMPLOYEE,
                    record.getFirstName(), record.getLastName(), record.getEmail(),
                    record.getEmployeeType().getLabel(), record.getPassword(),
                    record.getPhoneNumber(), record.getDepartment().getDepartmentID(),
                    record.getPersonID());
        }
        return rowsChanged > 0;
    }

    /**
     * deletes an employee from the database based on ID attribute
     *
     * @param ID
     * @return boolean whether it was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_EMPLOYEE =
                "DELETE FROM [dbo].[Employee] " +
                        "WHERE EmployeeID = ?";

        return DBConnection.executeUpdate(DELETE_EMPLOYEE, ID) > 0;
    }

    /**
     * finds all employees in the database
     *
     * @param fullAssociation boolean whether to include reference variables of an employee class
     * @return List<Employee>
     * @throws DataAccessException
     */
    @Override
    public List<Employee> getAll(boolean fullAssociation) throws DataAccessException {
        List<Employee> employees = new ArrayList<>();
        String SELECT_ALL =
                "SELECT * " +
                        "FROM [dbo].[Employee]";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                employees.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not get all Employees", e);
        }
        return employees;
    }

    /**
     * builds an employee based on data from database
     *
     * @param resultSet       data from database
     * @param fullAssociation whether to build it with reference variables
     * @return Employee object
     * @throws DataAccessException
     */
    public Employee buildObject(ResultSet resultSet, boolean fullAssociation) throws DataAccessException {
        Employee employee = new Employee();

        try {
            employee.
                    setPersonID(resultSet.getInt("EmployeeID")).
                    setFirstName(resultSet.getString("FirstName")).
                    setLastName(resultSet.getString("LastName")).
                    setPhoneNumber(resultSet.getString("Phone")).
                    setEmail(resultSet.getString("Email")).
                    setFullName();
            employee.
                    setEmployeeType(resultSet.getString("Type")).
                    setPassword(resultSet.getString("Password"));

            if (fullAssociation) {
                DepartmentIF departmentDB = new DepartmentDB();
                Department department = departmentDB.findByID(resultSet.getInt("DepartmentID"), false);

                if (department != null) {
                    employee.setDepartment(department);
                } else {
                    System.out.println("Could not find department with ID " + resultSet.getInt("DepartmentID"));
                }
            }
            return employee;
        } catch (SQLException e) {
            throw new DataAccessException("Could not build employee object.", e);
        }
    }
}
