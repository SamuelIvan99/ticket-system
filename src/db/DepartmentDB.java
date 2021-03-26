package db;

import model.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group 1
 * @implNote class implements insert, find, update, delete of a department into database
 */
public class DepartmentDB implements DepartmentIF {

    /**
     * Inserts a department object into database
     *
     * @param record object to insert into database
     * @return boolean whether it was successfully inserted
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Department record) throws DataAccessException {
        String INSERT_DEPARTMENT = SqlUtil.buildInsert("Department", "Name");

        int generatedKey = DBConnection.executeInsertWithIdentity(INSERT_DEPARTMENT, record.getName());

        record.setDepartmentID(generatedKey);

        return generatedKey > 0;
    }

    /**
     * Finds a department by ID attribute
     *
     * @param ID
     * @param fullAssociation whether to include reference objects of department class
     * @return Department object
     * @throws DataAccessException
     */
    @Override
    public Department findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_DEPARTMENT =
                "SELECT * " +
                        "FROM [dbo].[Department] " +
                        "WHERE DepartmentID = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_DEPARTMENT, ID);
            if (rs.next())
                return buildObject(rs);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the department with ID " + ID, e);
        }
        return null;
    }

    /**
     * Finds a department in the database based on name attribute
     *
     * @param name
     * @param fullAssociation boolean whether to include reference variables of department class
     * @return Department object
     * @throws DataAccessException
     */
    @Override
    public Department findDepartmentByName(String name, boolean fullAssociation) throws DataAccessException {
        String SELECT_DEPARTMENT =
                "SELECT * " +
                        "FROM [dbo].[Department] " +
                        "WHERE Name = ?";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_DEPARTMENT, name);
            if (rs.next())
                return buildObject(rs);
        } catch (SQLException e) {
            throw new DataAccessException("Could not find the department with name " + name, e);
        }
        return null;
    }

    /**
     * Updates a department in the database
     *
     * @param record to be updated
     * @return boolean whether it was successfully update
     * @throws DataAccessException
     */
    @Override
    public boolean update(Department record) throws DataAccessException {
        int rowsChanged = 0;

        String UPDATE_CUSTOMER = SqlUtil.buildUpdate("Department", "DepartmentID", "Name");

        if (UPDATE_CUSTOMER != null) {
            rowsChanged = DBConnection.executeUpdate(UPDATE_CUSTOMER, record.getName(), record.getDepartmentID());
        }
        return rowsChanged > 0;
    }

    /**
     * Deletes a department in the database based on ID attribute
     *
     * @param ID
     * @return boolean whether it was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_DEPARTMENT = "DELETE FROM [dbo].[Department] " + "WHERE DepartmentID = ?";

        return DBConnection.executeUpdate(DELETE_DEPARTMENT, ID) > 0;
    }

    /**
     * Gets all departments from the database
     *
     * @param fullAssociation whether to include reference objects in the department class
     * @return List<Department>
     * @throws DataAccessException
     */
    @Override
    public List<Department> getAll(boolean fullAssociation) throws DataAccessException {
        List<Department> departments = new ArrayList<>();
        String SELECT_ALL = "SELECT * " + "FROM [dbo].[Department]";

        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                departments.add(buildObject(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not get all Departments.", e);
        }
        return departments;
    }

    /**
     * Builds department object based on data from database
     *
     * @param resultSet data from database
     * @return Department object
     * @throws DataAccessException
     */
    public Department buildObject(ResultSet resultSet) throws DataAccessException {
        Department department = new Department();
        try {
            department.
                    setDepartmentID(resultSet.getInt("DepartmentID")).
                    setName(resultSet.getString("Name"));
            return department;
        } catch (SQLException e) {
            throw new DataAccessException("Could not build department object.", e);
        }
    }
}
