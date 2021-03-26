package gui.employee;

import controller.EmployeeController;
import db.DBConnection;
import db.DataAccessException;
import gui.MenuBar;
import gui.MenuUtil;
import model.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class EmployeeMenu extends JFrame {

    private EmployeeController employeeController;
    private ArrayList<Employee> employees;

    private DefaultTableModel employeeTableModel;
    private JTable employeesTable;
    private JScrollPane employeeScrollPane;
    private JPanel mainPanel;
    private JButton createEmployeeButton;
    private JButton employeeInfoButton;
    private JButton updateEmployeeButton;
    private JButton deleteEmployeeButton;
    private JLabel loadingLabel;

    private EmployeeMenu() throws DataAccessException {
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
        readDataFromDatabase();
    }

    private void readDataFromDatabase() {
        SwingWorker loadEmployeeData = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                employees = new ArrayList<>(employeeController.getAllEmployees(true));
                return null;
            }

            @Override
            protected void done() {
                mainPanel.remove(loadingLabel);
                mainPanel.add(employeeScrollPane, "grow, spany");

                initEmployeeTable();
                initializeMainFrame();
            }
        };

        loadEmployeeData.execute();
    }

    private void initializeMainFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Employee Menu");

        MenuUtil.setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    private void initializeVariables() {
        DBConnection.connect();

        employeeController = new EmployeeController();

        mainPanel = new JPanel();
        loadingLabel = new JLabel("Loading Data...");
        loadingLabel.setFont(new Font(loadingLabel.getFont().getName(), Font.ITALIC, 50));

        /**
         * Create employee button logic
         */
        createEmployeeButton = new JButton("Create Employee");
        createEmployeeButton.addActionListener(actionEvent -> {
            this.dispose();
            EmployeeCreateMenu.start();
        });

        /**
         * Employee info button logic
         * @NOTE When starting the info menu, the employee data does not load again from the database
         * This is done by passing the setVisible method to the other menu
         */
        employeeInfoButton = new JButton("Employee Information");
        employeeInfoButton.addActionListener(actionEvent -> {
            Employee selectedEmployee = null;
            if (!employeesTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = employeesTable.convertRowIndexToModel(employeesTable.getSelectedRow());
                selectedEmployee = employees.get(selectedRow);
                this.setVisible(false);
                EmployeeInfoMenu.start(() -> this.setVisible(true), selectedEmployee);
            }
        });

        /**
         * Update Employee button logic
         */
        updateEmployeeButton = new JButton("Update Employee");
        updateEmployeeButton.addActionListener(actionEvent -> {
            Employee selectedEmployee = null;
            if (!employeesTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = employeesTable.convertRowIndexToModel(employeesTable.getSelectedRow());
                selectedEmployee = employees.get(selectedRow);
                this.dispose();
                EmployeeUpdateMenu.start(selectedEmployee);
            }
        });

        /**
         * Delete Employee button logic
         */
        deleteEmployeeButton = new JButton("Delete Employee");
        deleteEmployeeButton.addActionListener(actionEvent -> {
            Employee selectedEmployee = null;
            if (!employeesTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = employeesTable.convertRowIndexToModel(employeesTable.getSelectedRow());
                selectedEmployee = employees.get(selectedRow);

                int reply = JOptionPane.showConfirmDialog(
                        rootPane,
                        "Are you sure you want to delete " + selectedEmployee.getFullName(),
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        employeeController.deleteEmployee(selectedEmployee.getPersonID());
                        this.dispose();
                        EmployeeMenu.start();
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        employeeTableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "EmployeeID", "First Name", "Last Name", "Email", "Phone Number", "Employee Type", "Department"}) {

            private static final long serialVersionUID = 1L;
            final boolean[] canEdit = { false, false, false, false, false, false };

            @Override
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        };

        employeesTable = new JTable(employeeTableModel);
        employeeScrollPane = new JScrollPane(employeesTable);
    }

    private void initializeMainPanel() {
        mainPanel.setLayout(new MigLayout(
            "flowy",
            "[25%][grow]",
            "[50][grow]"
        ));

        mainPanel.add(new MenuBar().getMainPanel(), "span, grow");
        mainPanel.add(createEmployeeButton, "grow");
        mainPanel.add(employeeInfoButton, "grow");
        mainPanel.add(updateEmployeeButton, "grow");
        mainPanel.add(deleteEmployeeButton, "grow, wrap");
        mainPanel.add(loadingLabel, "center, spany");
    }

    private void initEmployeeTable() {
        employeeTableModel.setRowCount(0);
        employees.forEach((employee) -> employeeTableModel.addRow(new String[] {
            Integer.toString(employee.getPersonID()),
            employee.getFirstName(),
            employee.getLastName(),
            employee.getEmail(),
            employee.getPhoneNumber(),
            employee.getEmployeeType().getLabel(),
            employee.getDepartment().getName()
        }));
    }

    public static void start() {
        DBConnection.connect();
        EventQueue.invokeLater(() -> {
            try {
                new EmployeeMenu().setVisible(true);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //TODO get removed later
    public static void main(String[] args) throws DataAccessException {
        EmployeeMenu employeeMenu = new EmployeeMenu();
        employeeMenu.start();
    }
}