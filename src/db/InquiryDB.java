package db;

import model.Inquiry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Group-One
 * @implNote class implements insert, find, update, delete of a inquiry into
 * database
 */
public class InquiryDB implements InquiryIF {
    /**
     * Reference Variables
     */

    /**
     * Constructor
     */
    public InquiryDB() {
    }

    /**
     * Inserts an inquiry into the database.
     *
     * @param inquiry  to be inserted
     * @param ticketID
     * @return boolean whether it was inserted successfully
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Inquiry inquiry, int ticketID) throws DataAccessException {
        String INSERT_INQUIRY = SqlUtil.buildInsert("Inquiry", "Title", "Category", "Description", "Timestamp",
                "TicketID");
        int generatedKey = DBConnection.executeInsertWithIdentity(INSERT_INQUIRY, inquiry.getTitle(),
                inquiry.getCategory().getLabel(), inquiry.getDescription(), inquiry.getTimestamp(), ticketID);
        inquiry.setInquiryID(generatedKey);
        return generatedKey > 0;
    }

    /**
     * Finds an inquiry in the database based on ID provided and builds it into an
     * object.
     *
     * @param ID              of an inquiry to be found
     * @param fullAssociation whether to find reference fields of an inquiry as well
     * @return Inquiry object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public Inquiry findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_INQUIRY = "SELECT * FROM Inquiry WHERE InquiryID = ?";
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_INQUIRY, ID);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds inquiries in the database based on title specified.
     *
     * @param title
     * @param fullAssociation whether to find reference variables of inquiries as
     *                        well
     * @return List<Inquiry> found inquiries
     * @throws DataAccessException
     */
    @Override
    public List<Inquiry> findInquiriesByTitle(String title, boolean fullAssociation) throws DataAccessException {
        String SELECT_INQUIRIES = "SELECT * FROM Inquiry WHERE Title = ?";
        List<Inquiry> inquiries = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_INQUIRIES, title);
            while (rs.next()) {
                inquiries.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inquiries;
    }

    /**
     * Finds inquiries in the database based on ticketID specified.
     *
     * @param ticketID
     * @param fullAssociation whether to find reference variables of inquiries as
     *                        well
     * @return List<Inquiry> found inquiries
     * @throws DataAccessException
     */
    @Override
    public List<Inquiry> findInquiriesByTicketID(int ticketID, boolean fullAssociation) throws DataAccessException {
        String SELECT_INQUIRIES = "SELECT * FROM Inquiry WHERE TicketID = ?";
        List<Inquiry> inquiries = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_INQUIRIES, ticketID);
            while (rs.next()) {
                inquiries.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inquiries;
    }

    /**
     * Finds inquiries in the database based on departmentID specified.
     *
     * @param departmentID
     * @param fullAssociation whether to find reference variables of inquiries as
     *                        well
     * @return List<Inquiry> found inquiries
     * @throws DataAccessException
     */
    @Override
    public List<Inquiry> findInquiriesByDepartmentID(int departmentID, boolean fullAssociation)
            throws DataAccessException {
        String SELECT_INQUIRIES = "SELECT * FROM Inquiry WHERE DepartmentID = ?";
        List<Inquiry> inquiries = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_INQUIRIES, departmentID);
            while (rs.next()) {
                inquiries.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inquiries;
    }

    /**
     * Updates attributes of an inquiry in the database.
     *
     * @param record inquiry to be updated.
     * @return boolean whether an inquiry was updated successfully.
     * @throws DataAccessException
     */
    @Override
    public boolean update(Inquiry record) throws DataAccessException {
        String UPDATE_INQUIRY = SqlUtil.buildUpdate("Inquiry", "InquiryID", "Title", "Category", "Description",
                "Timestamp", "DepartmentID");

        return DBConnection.executeUpdate(UPDATE_INQUIRY, record.getTitle(), record.getCategory().getLabel(),
                record.getDescription(), record.getTimestamp(),
                record.getDepartment() != null ? record.getDepartment().getDepartmentID() : null,
                record.getInquiryID()) > 0;
    }

    /**
     * Deletes an inquiry form the database with ID specified.
     *
     * @param ID of an inquiry
     * @return boolean whether the inquiry was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_INQUIRY = "DELETE FROM Inquiry WHERE InquiryID = ?";
        return DBConnection.executeUpdate(DELETE_INQUIRY, ID) > 0;
    }

    /**
     * Finds all inquiries in the database and builds them into a list.
     *
     * @param fullAssociation whether to include reference variables in each inquiry
     * @return List<Inquiry> List of all inquiries in the database
     * @throws DataAccessException
     */
    @Override
    public List<Inquiry> getAll(boolean fullAssociation) throws DataAccessException {
        String SELECT_ALL = "SELECT * FROM Inquiry";
        List<Inquiry> inquiries = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (rs.next()) {
                inquiries.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inquiries;
    }

    /**
     * Builds an inquiry using data from database.
     *
     * @param resultSet       inquiry data from database
     * @param fullAssociation boolean whether to include reference variable in an
     *                        inquiry (department, fields) and therefore find and
     *                        build them as well.
     * @return Inquiry object
     * @throws DataAccessException
     */
    public Inquiry buildObject(ResultSet resultSet, boolean fullAssociation) throws DataAccessException {
        try {
            Inquiry inquiry = new Inquiry();
            inquiry.setInquiryID(resultSet.getInt("InquiryID")).setTitle(resultSet.getString("Title"))
                    .setCategory(resultSet.getString("Category")).setDescription(resultSet.getString("Description"))
                    .setTimestamp(resultSet.getTimestamp("Timestamp").toLocalDateTime());

            if (fullAssociation) {
                inquiry.setFormFields(new FieldDB().findFieldsByInquiryID(resultSet.getInt("InquiryID"), false))
                        .setResponses(new ResponseDB().findResponsesByInquiryID(resultSet.getInt("InquiryID"), true))
                        .setDepartment(new DepartmentDB().findByID(resultSet.getInt("DepartmentID"), false));
            }

            return inquiry;
        } catch (SQLException ex) {
            throw new DataAccessException("Could not build inquiry object", ex);
        }
    }
}
