package gui.employee;

import controller.EmployeeController;
import db.DataAccessException;
import gui.InvalidTextException;
import gui.MenuUtil;
import model.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static gui.MenuUtil.getCreateUpdateGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class EmployeeUpdateMenu extends JFrame {

    private EmployeeController employeeController;
    private Employee employee;
    private ArrayList<Runnable> runGetInfo;

    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel titleLabel;
    private ArrayList<Field> employeeFields;
    private ArrayList<Component> menuComponents;

    private EmployeeUpdateMenu(Employee employee) throws DataAccessException, IllegalAccessException {
        this.employee = employee;
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        employeeController = new EmployeeController();

        mainPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        submitButton = new JButton("Update Employee");
        titleLabel = new JLabel("Update Employee Menu");
        employeeFields = new ArrayList<>();
        menuComponents = new ArrayList<>();
        runGetInfo = new ArrayList<>();

        getCreateUpdateGUIComponentsFromObject(menuComponents, runGetInfo, employeeFields, employee);

        /**
         * When the submit button is clicked, the runGetInfo Functions will pull and map the data to the Employee provided to getCreateUpdateGUIComponentsFromObject
         * The Employee will then get verified for input errors and be pushed to the database if there are no errors within the input
         */
        submitButton.addActionListener(actionEvent -> {
            runGetInfo.forEach(Runnable::run);
            try {
                submitUpdateEmployee();
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
        setTitle("Update Employee");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    private void submitUpdateEmployee() throws DataAccessException {
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

        boolean updated = employeeController.updateEmployee(employee,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getPhoneNumber(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getEmployeeType().getLabel(),
                employee.getDepartment());
        if (updated) {
            JOptionPane.showMessageDialog(rootPane, "Employee has been updated!", "Employee Updated", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            EmployeeMenu.start();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Employee with that email already exists!", "The validation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void start(Employee employee) {
        EventQueue.invokeLater(() -> {
            try {
                new EmployeeUpdateMenu(employee).setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
