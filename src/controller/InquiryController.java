package controller;

import db.DataAccessException;
import db.InquiryDB;
import db.InquiryIF;
import model.Department;
import model.Inquiry;

import java.time.LocalDateTime;
import java.util.List;

public class InquiryController {
    private final InquiryIF inquiryDB;

    public InquiryController() {
        inquiryDB = new InquiryDB();
    }

    public boolean createInquiry(Inquiry inquiry, int ticketID) throws DataAccessException {
        boolean result = false;
        //DBConnection.getInstance().startTransaction();
        result = inquiryDB.insert(inquiry, ticketID);
        //DBConnection.getInstance().commitTransaction();
        return result;

    }

    public Inquiry findInquiryByID(int id, boolean fullAssociation) throws DataAccessException {
        return inquiryDB.findByID(id, fullAssociation);
    }

    public List<Inquiry> findInquiriesByTitle(String title, boolean fullAssociation) throws DataAccessException {
        return inquiryDB.findInquiriesByTitle(title, fullAssociation);
    }

    public List<Inquiry> findInquiriesByTicketID(int ticketID, boolean fullAssociation) throws DataAccessException {
        return inquiryDB.findInquiriesByTicketID(ticketID, fullAssociation);
    }

    public List<Inquiry> findInquiriesByDepartmentID(int departmentID, boolean fullAssociation)
            throws DataAccessException {
        return inquiryDB.findInquiriesByDepartmentID(departmentID, fullAssociation);
    }

    public List<Inquiry> getAllInquiries(boolean fullAssociation) throws DataAccessException {
        return inquiryDB.getAll(fullAssociation);
    }

    public boolean updateInquiry(Inquiry inquiryToUpdate, String newTitle, String newDescription,
                                 LocalDateTime newTimestamp, Department newDepartment) throws DataAccessException {
        boolean result = false;
        //DBConnection.getInstance().startTransaction();
        Inquiry inquiry = new Inquiry();
        inquiry.setTitle(newTitle).setDescription(newDescription).setTimestamp(newTimestamp)
                .setDepartment(newDepartment);

        result = inquiryDB.update(inquiryToUpdate);

        //DBConnection.getInstance().commitTransaction();
        return result;

    }

    public boolean deleteInquiry(int id) throws DataAccessException {
        boolean result = false;
        //DBConnection.getInstance().startTransaction();
        result = inquiryDB.delete(id);
        //DBConnection.getInstance().commitTransaction();
        return result;

    }
}
