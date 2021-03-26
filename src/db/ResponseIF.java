package db;

import model.Response;

import java.util.List;

/**
 * @author Group 1
 */
public interface ResponseIF extends TableIF<Response> {
    boolean insert(Response response, int inquiryID) throws DataAccessException;

    List<Response> findResponsesByTitle(String title, boolean fullAssociation) throws DataAccessException;

    List<Response> findResponsesByEmployeeID(int employeeID, boolean fullAssociation) throws DataAccessException;

    List<Response> findResponsesByInquiryID(int inquiryID, boolean fullAssociation) throws DataAccessException;
}
