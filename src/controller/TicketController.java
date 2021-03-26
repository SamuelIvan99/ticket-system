package controller;

import db.DBConnection;
import db.DataAccessException;
import db.TicketDB;
import db.TicketIF;
import model.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TicketController {
    private final TicketIF ticketDB;

    private InquiryController inquiryController;
    private ResponseController responseController;

    public TicketController() {
        ticketDB = new TicketDB();
        inquiryController = new InquiryController();
        responseController = new ResponseController();
    }

    public boolean createTicket(Ticket ticket) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = ticketDB.insert(ticket);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while creating a ticket.", e);
        }
    }

    public Ticket findTicketByID(int id, boolean fullAssociation) throws DataAccessException {
        return ticketDB.findByID(id, fullAssociation);
    }

    public List<Ticket> findTicketsByEmployee(Employee employee, boolean fullAssociation) throws DataAccessException {
        return ticketDB.findTicketsByEmployee(employee, fullAssociation);
    }

    public List<Ticket> findTicketsByCustomer(Customer customer, boolean fullAssociation) throws DataAccessException {
        return ticketDB.findTicketsByCustomer(customer, fullAssociation);
    }

    public List<Ticket> getAllTickets(boolean fullAssociation) throws DataAccessException {
        return ticketDB.getAll(fullAssociation);
    }

    public boolean updateVersion(Ticket ticketToUpdate) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();

            if (checkForUpdates(ticketToUpdate, false)) {
                DBConnection.getInstance().rollbackTransaction();
                throw new DataAccessException("There is a newer version in the system. Please update and try again.", new SQLException());
            }

            result = ticketDB.updateVersion(ticketToUpdate);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DataAccessException("Transaction error while updating a ticket.", e);
        }
    }

    public boolean updateTicket(Ticket ticketToUpdate, String newComplaintStatus, String newPriority,
                                LocalDateTime newStartDate, LocalDateTime newEndDate, Employee newEmployee, Customer newCustomer)
            throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            //Ticket ticket = new Ticket();

            if (checkForUpdates(ticketToUpdate, true)) {
                DBConnection.getInstance().rollbackTransaction();
                throw new DataAccessException("There is a newer version in the system. Please update and try again.", new SQLException());
            }

            ticketToUpdate.setComplaintStatus(newComplaintStatus)
                    .setPriority(newPriority)
                    .setStartDate(newStartDate)
                    .setEndDate(newEndDate)
                    .setEmployee(newEmployee)
                    .setCustomer(newCustomer)
                    .setVersion(ticketToUpdate.getVersion());
            result = ticketDB.update(ticketToUpdate);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while updating a ticket.", e);
        }
    }

    public boolean deleteTicket(int ID) throws DataAccessException {
        try {
            boolean result = false;
            DBConnection.getInstance().startTransaction();
            result = ticketDB.delete(ID);
            DBConnection.getInstance().commitTransaction();
            return result;
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new DataAccessException("Transaction error while deleting a ticket.", e);
        }
    }

    public boolean createInquiry(Inquiry inquiry, Ticket ticketToUpdate) throws DataAccessException {
        boolean result = false;

        try {
            DBConnection.getInstance().startTransaction();

            if (checkForUpdates(ticketToUpdate, true)) {
                DBConnection.getInstance().rollbackTransaction();
                throw new DataAccessException("There is a newer version in the system. Please update and try again.", new SQLException());
            }

            ticketToUpdate.incrementVersion();
            ticketDB.updateVersion(ticketToUpdate);
            result = inquiryController.createInquiry(inquiry, ticketToUpdate.getTicketID());

            DBConnection.getInstance().commitTransaction();
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DataAccessException("Transaction error while creating an inquiry.", e);
        }

        return result;
    }

    public boolean createResponse(Response response, Inquiry inquiry, Ticket ticketToUpdate) throws DataAccessException {
        boolean result = false;

        try {
            DBConnection.getInstance().startTransaction();

            if (checkForUpdates(ticketToUpdate, true)) {
                DBConnection.getInstance().rollbackTransaction();
                throw new DataAccessException("There is a newer version in the system. Please update and try again.", new SQLException());
            }

            ticketToUpdate.incrementVersion();
            ticketDB.updateVersion(ticketToUpdate);
            result = responseController.createResponse(response, inquiry.getInquiryID());

            DBConnection.getInstance().commitTransaction();
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new DataAccessException("Transaction error while creating a response.", e);
        }

        return result;
    }


    /**
     * Check for updates boolean.
     *
     * @param currentTicket the current ticket
     * @param updateCheck   whether the check is for the updateChecker thread (true)
     * @return the boolean
     */
    public boolean checkForUpdates(Ticket currentTicket, boolean updateCheck) {
        boolean currentTicketIsDifferent = false;
        try {
            int version = findTicketByID(currentTicket.getTicketID(), false).getVersion();
            if (updateCheck && version > currentTicket.getVersion()) {
                currentTicketIsDifferent = true;
            } else if (!updateCheck && version + 1 > currentTicket.getVersion()) {
                currentTicketIsDifferent = true;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return currentTicketIsDifferent;
    }
}