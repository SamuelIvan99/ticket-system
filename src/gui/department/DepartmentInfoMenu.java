package gui.department;

import db.DataAccessException;
import model.Department;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static gui.MenuUtil.getInfoGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class DepartmentInfoMenu extends JFrame {
    private Department department;

    private JPanel mainPanel;
    private JButton goBackButton;
    private JLabel titleLabel;
    private ArrayList<Component> menuComponents;
    private Runnable openPreviousWindow;

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private DepartmentInfoMenu(Runnable openPreviousWindow, Department department) throws DataAccessException, IllegalAccessException {
        this.openPreviousWindow = openPreviousWindow;
        this.department = department;
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        mainPanel = new JPanel();
        goBackButton = new JButton("Go Back");
        titleLabel = new JLabel("Department Info Menu");
        menuComponents = new ArrayList<>();

        getInfoGUIComponentsFromObject(menuComponents, new ArrayList<>(), department);

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
        setTitle("Department Information");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    public static void start(Runnable openPreviousWindow, Department department) {
        EventQueue.invokeLater(() -> {
            try {
                new DepartmentInfoMenu(openPreviousWindow, department).setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
