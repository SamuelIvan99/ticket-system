package db;

import model.Customer;
import model.Employee;
import model.Inquiry;
import model.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Group-One
 * @implNote class implements insert, find, update, delete of a ticket into
 * database
 */
public class TicketDB implements TicketIF {
    /**
     * Reference Variables
     */

    /**
     * Constructor
     */
    public TicketDB() {
    }

    /**
     * Insert a ticket into database.
     *
     * @param record ticket to insert into database
     * @return boolean whether it was inserted successfully
     * @throws DataAccessException
     */
    @Override
    public boolean insert(Ticket record) throws DataAccessException {

        String INSERT_TICKET = SqlUtil.buildInsert("Ticket", "StartDate", "ComplaintStatus", "Priority", "CustomerID");
        int generatedKey = DBConnection.executeInsertWithIdentity(INSERT_TICKET, record.getStartDate(),
                record.getComplaintStatus().getTitle(), record.getPriority().getTitle(),
                record.getCustomer() != null ? record.getCustomer().getPersonID() : null);
        record.setTicketID(generatedKey);

        return generatedKey > 0;
    }

    /**
     * Finds a ticket in the database based on ID provided and builds it into an
     * object.
     *
     * @param ID              of a ticket to be found
     * @param fullAssociation whether to find reference fields of a ticket as well
     * @return Ticket object or null if no reference was found in the database
     * @throws DataAccessException
     */
    @Override
    public Ticket findByID(int ID, boolean fullAssociation) throws DataAccessException {
        String SELECT_TICKET = "SELECT * FROM Ticket WHERE TicketID = ?";
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_TICKET, ID);
            if (rs.next())
                return buildObject(rs, fullAssociation);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Finds employee's tickets in the database.
     *
     * @param employee
     * @param fullAssociation boolean whether to include reference variables in
     *                        ticket object
     * @return List<Ticket> employee's tickets found in the database
     * @throws DataAccessException
     */
    @Override
    public List<Ticket> findTicketsByEmployee(Employee employee, boolean fullAssociation) throws DataAccessException {
        String SELECT_TICKETS = "SELECT * FROM Ticket WHERE EmployeeID = ?";
        List<Ticket> tickets = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_TICKETS, employee.getPersonID());
            while (rs.next()) {
                tickets.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    /**
     * Finds customer's tickets in the database.
     *
     * @param customer
     * @param fullAssociation boolean whether to include reference variables in
     *                        ticket object
     * @return List<Ticket> customer's tickets found in the database
     * @throws DataAccessException
     */
    @Override
    public List<Ticket> findTicketsByCustomer(Customer customer, boolean fullAssociation) throws DataAccessException {
        String SELECT_TICKETS = "SELECT * FROM Ticket WHERE CustomerID = ?";
        List<Ticket> tickets = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_TICKETS, customer.getPersonID());
            while (rs.next()) {
                tickets.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    /**
     * Updates attributes of a ticket in the database.
     *
     * @param record ticket to be updated.
     * @return boolean whether a ticket was updated successfully.
     * @throws DataAccessException
     */
    @Override
    public boolean update(Ticket record) throws DataAccessException {
        ArrayList<String> fieldsToUpdate = new ArrayList<>(Arrays.asList(
                "StartDate",
                "ComplaintStatus",
                "Priority",
                "VersionNo"
        ));

        ArrayList<Object> valuesToUpdate = new ArrayList<>(Arrays.asList(
                record.getStartDate(),
                record.getComplaintStatus().getTitle(),
                record.getPriority().getTitle(),
                record.getVersion()
        ));

        if (record.getEndDate() != null) {
            fieldsToUpdate.add("EndDate");
            valuesToUpdate.add(record.getEndDate());
        }
        if (record.getEmployee() != null) {
            fieldsToUpdate.add("EmployeeID");
            valuesToUpdate.add(record.getEmployee().getPersonID());
        }
        if (record.getCustomer() != null) {
            fieldsToUpdate.add("CustomerID");
            valuesToUpdate.add(record.getCustomer().getPersonID());
        }
        valuesToUpdate.add(record.getTicketID());

        String UPDATE_TICKET = SqlUtil.buildUpdate(
                "Ticket",
                "TicketID",
                fieldsToUpdate.toArray(new String[fieldsToUpdate.size()]));

        return DBConnection.executeUpdate(UPDATE_TICKET,
                valuesToUpdate.toArray(new Object[valuesToUpdate.size()])) > 0;
    }

    public boolean updateVersion(Ticket record) throws DataAccessException {
        String UPDATE_TICKET_VERSION = SqlUtil.buildUpdate("Ticket", "TicketID", "VersionNo");

        return DBConnection.executeUpdate(UPDATE_TICKET_VERSION, record.getVersion(), record.getTicketID()) > 0;
    }

    /**
     * Deletes a ticket form a database with ID specified.
     *
     * @param ID of a ticket
     * @return boolean whether the ticket was successfully deleted
     * @throws DataAccessException
     */
    @Override
    public boolean delete(int ID) throws DataAccessException {
        String DELETE_TICKET = "DELETE FROM Ticket WHERE TicketID = ?";
        return DBConnection.executeUpdate(DELETE_TICKET, ID) > 0;
    }

    /**
     * Finds all tickets in the database and builds them into a list.
     *
     * @param fullAssociation whether to include reference variables in each ticket
     * @return List<Ticket> List of all tickets in the database
     * @throws DataAccessException
     */
    @Override
    public List<Ticket> getAll(boolean fullAssociation) throws DataAccessException {
        String SELECT_ALL = "SELECT * FROM Ticket";
        List<Ticket> tickets = new ArrayList<>();
        try {
            ResultSet rs = DBConnection.executeQuery(SELECT_ALL);
            while (!rs.isClosed() && rs.next()) {
                tickets.add(buildObject(rs, fullAssociation));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    /**
     * Builds a ticket using data from database.
     *
     * @param resultSet       ticket data from database
     * @param fullAssociation boolean whether to include reference variable in a
     *                        ticket (employee, customer, inquiries) and therefore
     *                        find and build them as well.
     * @return Ticket object
     * @throws DataAccessException
     */
    private Ticket buildObject(ResultSet resultSet, boolean fullAssociation) throws DataAccessException {
        try {
            Ticket ticket = new Ticket();
            ticket.setTicketID(resultSet.getInt("TicketID")).setComplaintStatus(resultSet.getString("ComplaintStatus"))
                    .setStartDate(resultSet.getTimestamp("StartDate").toLocalDateTime())
                    .setEndDate(resultSet.getTimestamp("EndDate") != null
                            ? resultSet.getTimestamp("EndDate").toLocalDateTime()
                            : null)
                    .setPriority(resultSet.getString("Priority"))
                    .setVersion(resultSet.getInt("VersionNo"));

            if (fullAssociation) {
                ticket.setEmployee(new EmployeeDB().findByID(resultSet.getInt("EmployeeID"), false))
                        .setCustomer(new CustomerDB().findByID(resultSet.getInt("CustomerID"), false))
                        .setInquiries((ArrayList<Inquiry>) new InquiryDB().findInquiriesByTicketID(resultSet.getInt("TicketID"), false));
            }
            return ticket;
        } catch (SQLException ex) {
            throw new DataAccessException("Could not build ticket object", ex);
        }
    }
}