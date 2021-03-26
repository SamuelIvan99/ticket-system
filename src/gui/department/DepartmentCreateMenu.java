package gui.department;

import controller.DepartmentController;
import db.DataAccessException;
import gui.InvalidTextException;
import gui.MenuUtil;
import model.Department;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static gui.MenuUtil.getCreateUpdateGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class DepartmentCreateMenu extends JFrame {
    private DepartmentController departmentController;
    private Department department;
    private ArrayList<Runnable> runGetInfo;

    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel titleLabel;
    private ArrayList<Component> menuComponents;

    private DepartmentCreateMenu() throws DataAccessException, IllegalAccessException {
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        departmentController = new DepartmentController();
        department = new Department();

        mainPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        submitButton = new JButton("Create Department");
        titleLabel = new JLabel("Create Department Menu");
        menuComponents = new ArrayList<>();
        runGetInfo = new ArrayList<>();

        getCreateUpdateGUIComponentsFromObject(menuComponents, runGetInfo, new ArrayList<>(), department);

        /**
         * When the submit button is clicked, the runGetInfo Functions will pull and map the data to the Department provided to getCreateUpdateGUIComponentsFromObject
         * The Department will then get verified for input errors and be pushed to the database if there are no errors within the input
         */
        submitButton.addActionListener(actionEvent -> {
            runGetInfo.forEach(Runnable::run);
            try {
                submitCreateDepartment();
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
        setTitle("Create Department");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    private void submitCreateDepartment() throws DataAccessException {
        try {
            MenuUtil.checkName(department.getName());
        } catch (InvalidTextException e) {
            JOptionPane.showMessageDialog(rootPane, e, "The validation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean created = departmentController.createDepartment(department);
        if (created) {
            JOptionPane.showMessageDialog(rootPane, "Department has been added to the database!", "Department Added", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            DepartmentMenu.start();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Department could not be created!", "The validation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void start() {
        EventQueue.invokeLater(() -> {
            try {
                new DepartmentCreateMenu().setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
