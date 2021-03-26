package db;

import model.Customer;
import model.Employee;
import model.Ticket;

import java.util.List;

/**
 * @author Group 1
 */
public interface TicketIF extends TableIF<Ticket> {
    boolean insert(Ticket record) throws DataAccessException;

    boolean updateVersion(Ticket record) throws DataAccessException;

    List<Ticket> findTicketsByCustomer(Customer customer, boolean fullAssociation) throws DataAccessException;

    List<Ticket> findTicketsByEmployee(Employee employee, boolean fullAssociation) throws DataAccessException;
}