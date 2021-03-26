package gui.department;

import controller.DepartmentController;
import db.DataAccessException;
import gui.InvalidTextException;
import gui.MenuUtil;
import model.Department;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static gui.MenuUtil.getCreateUpdateGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class DepartmentUpdateMenu extends JFrame{
    private DepartmentController departmentController;
    private Department department;
    private ArrayList<Runnable> runGetInfo;

    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel titleLabel;
    private ArrayList<Field> departmentFields;
    private ArrayList<Component> menuComponents;

    private DepartmentUpdateMenu(Department department) throws DataAccessException, IllegalAccessException {
        this.department = department;
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        departmentController = new DepartmentController();

        mainPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        submitButton = new JButton("Update Department");
        titleLabel = new JLabel("Update Department Menu");
        departmentFields = new ArrayList<>();
        menuComponents = new ArrayList<>();
        runGetInfo = new ArrayList<>();

        getCreateUpdateGUIComponentsFromObject(menuComponents, runGetInfo, departmentFields, department);

        /**
         * When the submit button is clicked, the runGetInfo Functions will pull and map the data to the Department provided to getCreateUpdateGUIComponentsFromObject
         * The Department will then get verified for input errors and be pushed to the database if there are no errors within the input
         */
        submitButton.addActionListener(actionEvent -> {
            runGetInfo.forEach(Runnable::run);
            try {
                submitUpdateDepartment();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });

        cancelButton.addActionListener(actionEvent -> {
            this.dispose();
            DepartmentMenu.start();
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
        setTitle("Update Department");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    private void submitUpdateDepartment() throws DataAccessException {
        try {
            MenuUtil.checkName(department.getName());
        } catch (InvalidTextException e) {
            JOptionPane.showMessageDialog(rootPane, e, "The validation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = departmentController.updateDepartment(department,
                department.getName());
        if (updated) {
            JOptionPane.showMessageDialog(rootPane, "Department has been updated!", "Department Updated", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            DepartmentMenu.start();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Department could not be updated!", "The validation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void start(Department department) {
        EventQueue.invokeLater(() -> {
            try {
                new DepartmentUpdateMenu(department).setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
