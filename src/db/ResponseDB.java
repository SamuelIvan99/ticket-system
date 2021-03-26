package db;

import model.Response;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group-One
 * @implNote class implements insert, find, update, delete of a response into database
 */
public class ResponseDB implements ResponseIF {
    /**
     * Reference Variables
     */

    /**
     * Constructor
     */
    public ResponseDB() {
    }

    /**
     * Inserts a response into the database.
     *
     * @param record    to be inserted
     * @param inquiryID
     * @return boolean whether it was inserted successfully
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Response record, int inquiryID) throws DataAccessException {
        String INSERT_RESPONSE = SqlUtil.buildInsert("Response", "Title", "Description", "Timestamp", "InquiryID", "EmployeeID");
        int id = DBConnection.executeInsertWithIdentity(INSERT_RESPONSE,
                record.getTitle(),
                record.getDescription(),
                record.getTimestamp(),
                inquiryID,
                record.getEmployee() != null ? record.getEmployee().getPersonID() : null);
        record.setResponseID(id);
        return findByID(id, false) != null;
    }

    /**
     * Finds a response in the database based on ID provided and builds it into an object.
     *
     * @param ID              of a response to be found
     * @param fullAssociation whether to find reference fields of a response as well
     * @return Response object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public Response findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_RESPONSE = "SELECT * FROM Response WHERE ResponseID = ?";
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_RESPONSE, ID);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds responses in the database based on title specified.
     *
     * @param title
     * @param fullAssociation whether to find reference variables of responses as well
     * @return List<Response> found responses
     * @throws DataAccessException
     */
    @Override
    public List<Response> findResponsesByTitle(String title, boolean fullAssociation) throws DataAccessException {
        String SELECT_RESPONSES = "SELECT * FROM Response WHERE Title = ?";
        List<Response> responses = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_RESPONSES, title);
            while (rs.next()) {
                responses.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responses;
    }

    /**
     * Finds responses in the database based on inquiryID specified.
     *
     * @param inquiryID
     * @param fullAssociation whether to find reference variables of responses as well
     * @return List<Response> found responses
     * @throws DataAccessException
     */
    @Override
    public List<Response> findResponsesByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException {
        String SELECT_RESPONSES = "SELECT * FROM Response WHERE InquiryID = ?";
        List<Response> responses = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_RESPONSES, inquiryID);
            while (rs.next()) {
                responses.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responses;
    }

    /**
     * Finds responses in the database based on employeeID specified.
     *
     * @param employeeID
     * @param fullAssociation whether to find reference variables of responses as well
     * @return List<Response> found responses
     * @throws DataAccessException
     */
    @Override
    public List<Response> findResponsesByEmployeeID(int employeeID, boolean fullAssociation) throws DataAccessException {
        String SELECT_RESPONSES = "SELECT * FROM Response WHERE EmployeeID = ?";
        List<Response> responses = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_RESPONSES, employeeID);
            while (rs.next()) {
                responses.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responses;
    }

    /**
     * Updates attributes of a response in the database.
     *
     * @param record response to be updated.
     * @return boolean whether a response was updated successfully.
     * @throws DataAccessException
     */
    @Override
    public boolean update(Response record) throws DataAccessException {
        String UPDATE_RESPONSE = SqlUtil.buildUpdate("Response", "ResponseID", "Title", "Description", "Timestamp", "EmployeeID");
        return DBConnection.executeUpdate(UPDATE_RESPONSE,
                record.getTitle(),
                record.getDescription(),
                record.getTimestamp(),
                record.getEmployee() != null ? record.getEmployee().getPersonID() : null,
                record.getResponseID()) > 0;
    }

    /**
     * Deletes a response form the database with ID specified.
     *
     * @param ID of a response
     * @return boolean whether the response was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_RESPONSE = "DELETE FROM Response WHERE ResponseID = ?";
        return DBConnection.executeUpdate(DELETE_RESPONSE, ID) > 0;
    }

    /**
     * Finds all responses in the database and builds them into a list.
     *
     * @param fullAssociation whether to include reference variables in each response
     * @return List<Response> List of all responses in the database
     * @throws DataAccessException
     */
    @Override
    public List<Response> getAll(boolean fullAssociation) throws DataAccessException {
        String SELECT_ALL = "SELECT * FROM Response";
        List<Response> responses = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                responses.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    /**
     * Builds a response using data from database.
     *
     * @param resultSet       response data from database
     * @param fullAssociation boolean whether to include reference variable in a response (employee, inquiry) and therefore find and build them as well.
     * @return Response object
     * @throws DataAccessException
     */
    public Response buildObject(ResultSet resultSet, boolean fullAssociation) throws DataAccessException {
        try {
            Response response = new Response();
            response.setResponseID(resultSet.getInt("ResponseID"))
                    .setTitle(resultSet.getString("Title"))
                    .setDescription(resultSet.getString("Description"))
                    .setTimestamp(resultSet.getTimestamp("Timestamp").toLocalDateTime());

            if (fullAssociation) {
                response.setEmployee(new EmployeeDB().findByID(resultSet.getInt("EmployeeID"), false));
            }

            return response;
        } catch (SQLException ex) {
            throw new DataAccessException("Could not build response object", ex);
        }
    }
}
