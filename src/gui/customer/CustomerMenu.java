package gui.customer;

import controller.CustomerController;
import db.DBConnection;
import db.DataAccessException;
import gui.MenuBar;
import gui.MenuUtil;
import model.Customer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CustomerMenu extends JFrame {

    private CustomerController customerController;
    private ArrayList<Customer> customers;

    private DefaultTableModel customerTableModel;
    private JTable customersTable;
    private JScrollPane customerScrollPane;
    private JPanel mainPanel;
    private JButton createCustomerButton;
    private JButton customerInfoButton;
    private JButton updateCustomerButton;
    private JButton deleteCustomerButton;
    private JLabel loadingLabel;

    private CustomerMenu() throws DataAccessException {
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
        readDataFromDatabase();
    }

    private void readDataFromDatabase() {
        SwingWorker loadCustomerData = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                customers = new ArrayList<>(customerController.getAllCustomers(true));
                return null;
            }

            @Override
            protected void done() {
                mainPanel.remove(loadingLabel);
                mainPanel.add(customerScrollPane, "grow, spany");

                initCustomerTable();
                initializeMainFrame();
            }
        };

        loadCustomerData.execute();
    }

    private void initializeMainFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Customer Menu");

        MenuUtil.setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    private void initializeVariables() {
        DBConnection.connect();

        customerController = new CustomerController();

        mainPanel = new JPanel();
        loadingLabel = new JLabel("Loading Data...");
        loadingLabel.setFont(new Font(loadingLabel.getFont().getName(), Font.ITALIC, 50));

        /**
         * Create customer button logic
         */
        createCustomerButton = new JButton("Create Customer");
        createCustomerButton.addActionListener(actionEvent -> {
            this.dispose();
            CustomerCreateMenu.start();
        });

        /**
         * Customer info button logic
         * @NOTE When starting the info menu, the customer data does not load again from the database
         * This is done by passing the setVisible method to the other menu
         */
        customerInfoButton = new JButton("Customer Information");
        customerInfoButton.addActionListener(actionEvent -> {
            Customer selectedCustomer = null;
            if (!customersTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = customersTable.convertRowIndexToModel(customersTable.getSelectedRow());
                selectedCustomer = customers.get(selectedRow);
                this.setVisible(false);
                CustomerInfoMenu.start(() -> this.setVisible(true), selectedCustomer);
            }
        });

        /**
         * Update Customer button logic
         */
        updateCustomerButton = new JButton("Update Customer");
        updateCustomerButton.addActionListener(actionEvent -> {
            Customer selectedCustomer = null;
            if (!customersTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = customersTable.convertRowIndexToModel(customersTable.getSelectedRow());
                selectedCustomer = customers.get(selectedRow);
                this.dispose();
                CustomerUpdateMenu.start(selectedCustomer);
            }
        });

        /**
         * Delete Customer button logic
         */
        deleteCustomerButton = new JButton("Delete Customer");
        deleteCustomerButton.addActionListener(actionEvent -> {
            Customer selectedCustomer = null;
            if (!customersTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = customersTable.convertRowIndexToModel(customersTable.getSelectedRow());
                selectedCustomer = customers.get(selectedRow);

                int reply = JOptionPane.showConfirmDialog(
                        rootPane,
                        "Are you sure you want to delete " + selectedCustomer.getFullName(),
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        customerController.deleteCustomer(selectedCustomer.getPersonID());
                        this.dispose();
                        CustomerMenu.start();
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        customerTableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "CustomerID", "First Name", "Last Name", "Email", "Phone Number", "Customer Type", "Company"}) {

            private static final long serialVersionUID = 1L;
            final boolean[] canEdit = { false, false, false, false, false, false, false };

            @Override
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        };

        customersTable = new JTable(customerTableModel);
        customerScrollPane = new JScrollPane(customersTable);
    }

    private void initializeMainPanel() {
        mainPanel.setLayout(new MigLayout(
            "flowy",
            "[25%][grow]",
            "[50][grow]"
        ));

        mainPanel.add(new MenuBar().getMainPanel(), "span, grow");
        mainPanel.add(createCustomerButton, "grow");
        mainPanel.add(customerInfoButton, "grow");
        mainPanel.add(updateCustomerButton, "grow");
        mainPanel.add(deleteCustomerButton, "grow, wrap");
        mainPanel.add(loadingLabel, "center, spany");
    }

    private void initCustomerTable() {
        customerTableModel.setRowCount(0);
        customers.forEach((customer) -> customerTableModel.addRow(new String[] {
            Integer.toString(customer.getPersonID()),
            customer.getFirstName(),
            customer.getLastName(),
            customer.getEmail(),
            customer.getPhoneNumber(),
            customer.getCustomerType().getLabel(),
            customer.getCompanyName()
        }));
    }

    public static void start() {
        DBConnection.connect();
        EventQueue.invokeLater(() -> {
            try {
                new CustomerMenu().setVisible(true);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //TODO get removed later
    public static void main(String[] args) throws DataAccessException {
        CustomerMenu customerMenu = new CustomerMenu();
        customerMenu.start();
    }
}