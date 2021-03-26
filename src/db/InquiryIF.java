package db;

import model.Inquiry;

import java.util.List;

/**
 * @author Group 1
 */
public interface InquiryIF extends TableIF<Inquiry> {
    boolean insert(Inquiry inquiry, int ticketID) throws DataAccessException;

    List<Inquiry> findInquiriesByTitle(String title, boolean fullAssociation) throws DataAccessException;

    List<Inquiry> findInquiriesByTicketID(int ticketID, boolean fullAssociation) throws DataAccessException;

    List<Inquiry> findInquiriesByDepartmentID(int departmentID, boolean fullAssociation) throws DataAccessException;
}
