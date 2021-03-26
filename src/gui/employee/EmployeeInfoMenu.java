package gui.employee;

import db.DataAccessException;
import model.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static gui.MenuUtil.getInfoGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class EmployeeInfoMenu extends JFrame{
    private Employee employee;

    private JPanel mainPanel;
    private JButton goBackButton;
    private JLabel titleLabel;
    private ArrayList<Field> employeeFields;
    private ArrayList<Component> menuComponents;
    private Runnable openPreviousWindow;

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private EmployeeInfoMenu(Runnable openPreviousWindow, Employee employee) throws DataAccessException, IllegalAccessException {
        this.openPreviousWindow = openPreviousWindow;
        this.employee = employee;
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        mainPanel = new JPanel();
        goBackButton = new JButton("Go Back");
        titleLabel = new JLabel("Employee Info Menu");
        employeeFields = new ArrayList<>();
        menuComponents = new ArrayList<>();

        getInfoGUIComponentsFromObject(menuComponents, employeeFields, employee);

        goBackButton.addActionListener(actionEvent -> {
            this.dispose();
            openPreviousWindow.run();
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

        mainPanel.add(goBackButton, "grow, span, height 40");
    }

    private void initializeMainFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Employee Information");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    public static void start(Runnable openPreviousWindow, Employee employee) {
        EventQueue.invokeLater(() -> {
            try {
                new EmployeeInfoMenu(openPreviousWindow, employee).setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
