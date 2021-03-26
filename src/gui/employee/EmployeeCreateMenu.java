package gui.employee;

import controller.EmployeeController;
import db.DataAccessException;
import gui.InvalidTextException;
import gui.MenuUtil;
import model.Address;
import model.Employee;
import model.Inquiry;
import model.Ticket;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static gui.MenuUtil.getCreateUpdateGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class EmployeeCreateMenu extends JFrame {

    private EmployeeController employeeController;
    private Employee employee;
    private ArrayList<Runnable> runGetInfo;

    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel titleLabel;
    private ArrayList<Field> employeeFields;
    private ArrayList<Component> menuComponents;

    private EmployeeCreateMenu() throws DataAccessException, IllegalAccessException {
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        employeeController = new EmployeeController();
        employee = new Employee();

        mainPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        submitButton = new JButton("Create Employee");
        titleLabel = new JLabel("Create Employee Menu");
        employeeFields = new ArrayList<>();
        menuComponents = new ArrayList<>();
        runGetInfo = new ArrayList<>();

        Address address = new Address();
        Inquiry inquiry = new Inquiry();
        Ticket ticket = new Ticket();
        Employee employee = new Employee();

        getCreateUpdateGUIComponentsFromObject(menuComponents, runGetInfo, employeeFields, employee);

        /**
         * When the submit button is clicked, the runGetInfo Functions will pull and map the data to the Employee provided to getCreateUpdateGUIComponentsFromObject
         * The Employee will then get verified for input errors and be pushed to the database if there are no errors within the input
         */
        submitButton.addActionListener(actionEvent -> {
            runGetInfo.forEach(Runnable::run);
            try {
                submitCreateEmployee();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });

        cancelButton.addActionListener(actionEvent -> {
            this.dispose();
            EmployeeMenu.start();
        });
    }

    private void initializeMainPanel() {
        mainPanel.setLayout(new MigLayout(
            "flowy, center",
            "",
            ""
        ));

        mainPanel.add(titleLabel, "center, height 60");
        menuComponents.forEach(component -> mainPanel.add(component));

        mainPanel.add(submitButton, "grow, span, height 40");
        mainPanel.add(cancelButton, "grow, span, height 40");
    }

    private void initializeMainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Create Employee");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }
    
    private void submitCreateEmployee() throws DataAccessException {
        try {
            MenuUtil.checkName(employee.getFirstName());
            MenuUtil.checkName(employee.getLastName());
            MenuUtil.checkPhone(employee.getPhoneNumber());
            MenuUtil.checkEmail(employee.getEmail());
            MenuUtil.checkText(employee.getPassword());
        } catch (InvalidTextException e) {
            JOptionPane.showMessageDialog(rootPane, e, "The validation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean created = employeeController.createEmployee(employee);
        if (created) {
            JOptionPane.showMessageDialog(rootPane, "Employee has been added to the database!", "Employee Added", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            EmployeeMenu.start();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Employee with that email already exists!", "The validation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void start() {
        EventQueue.invokeLater(() -> {
            try {
                new EmployeeCreateMenu().setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
