package db;

import model.FormField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group-One
 * @implNote class implements insert, find, update, delete of a field into database
 */
public class FieldDB implements FieldIF {
    /**
     * Reference Variables
     */

    /**
     * Constructor
     */
    public FieldDB() {
    }

    /**
     * Inserts a field into the database.
     *
     * @param field     to be inserted
     * @param inquiryID
     * @return boolean whether it was inserted successfully
     * @throws DataAccessException
     */
    @Override
    public boolean insert(FormField field, int inquiryID) throws DataAccessException {
        String INSERT_FIELD = SqlUtil.buildInsert("Field", "Name", "Content", "InquiryID");
        int id = DBConnection.executeInsertWithIdentity(INSERT_FIELD,
                field.getName(),
                field.getContent(),
                inquiryID);
        field.setFieldID(id);
        return findByID(id, false) != null;
    }

    /**
     * Finds a field in the database based on ID provided and builds it into an object.
     *
     * @param ID              of a field to be found
     * @param fullAssociation whether to find reference fields of a field as well
     * @return FormField object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public FormField findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_FIELD = "SELECT * FROM Field WHERE FieldID = ?";
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_FIELD, ID);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds fields in the database based on name specified.
     *
     * @param name
     * @param fullAssociation whether to find reference variables of fields as well
     * @return List<FormField> found fields
     * @throws DataAccessException
     */
    @Override
    public List<FormField> findFieldsByName(String name, boolean fullAssociation) throws DataAccessException {
        String SELECT_FIELD = "SELECT * FROM Field WHERE Name = ?";
        List<FormField> fields = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_FIELD, name);
            while (rs.next()) {
                fields.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }

    /**
     * Finds fields in the database based on inquiryID specified.
     *
     * @param inquiryID
     * @param fullAssociation whether to find reference variables of fields as well
     * @return List<FormField> found fields
     * @throws DataAccessException
     */
    @Override
    public List<FormField> findFieldsByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException {
        String SELECT_FIELD = "SELECT * FROM Field WHERE InquiryID = ?";
        List<FormField> fields = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_FIELD, inquiryID);
            while (rs.next()) {
                fields.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fields;
    }

    /**
     * Updates attributes of a field in the database.
     *
     * @param record field to be updated.
     * @return boolean whether a field was updated successfully.
     * @throws DataAccessException
     */
    @Override
    public boolean update(FormField record) throws DataAccessException {
        String UPDATE_FIELD = SqlUtil.buildUpdate("Field", "FieldID", "Name", "Content");

        return DBConnection.executeUpdate(UPDATE_FIELD,
                record.getName(),
                record.getContent(),
                record.getFieldID()) > 0;
    }

    /**
     * Deletes a field form the database with ID specified.
     *
     * @param ID of a field
     * @return boolean whether the field was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_FIELD = "DELETE FROM Field WHERE FieldID = ?";
        return DBConnection.executeUpdate(DELETE_FIELD, ID) > 0;
    }

    /**
     * Finds all fields in the database and builds them into a list.
     *
     * @param fullAssociation whether to include reference variables in each field
     * @return List<FormField> List of all fields in the database
     * @throws DataAccessException
     */
    @Override
    public List<FormField> getAll(boolean fullAssociation) throws DataAccessException {
        String SELECT_ALL = "SELECT * FROM Field";
        List<FormField> fields = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                fields.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fields;
    }

    /**
     * Builds a field using data from database.
     *
     * @param resultSet       field data from database
     * @param fullAssociation boolean whether to include reference variable in a field (but there are none now)
     * @return FormField object
     * @throws DataAccessException
     */
    public FormField buildObject(ResultSet resultSet, boolean fullAssociation) throws DataAccessException {
        try {
            FormField field = new FormField();
            field.setName(resultSet.getString("Name")).setContent(resultSet.getString("Content")).setFieldID(resultSet.getInt("FieldID"));

            if (fullAssociation) {
            }

            return field;
        } catch (SQLException ex) {
            throw new DataAccessException("Could not build field object", ex);
        }
    }
}
