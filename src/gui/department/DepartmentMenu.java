package gui.department;

import controller.DepartmentController;
import db.DBConnection;
import db.DataAccessException;
import gui.MenuBar;
import model.Department;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import static gui.MenuUtil.setWindowSizeAndLocation;

public class DepartmentMenu extends JFrame {
    private DepartmentController departmentController;
    private ArrayList<Department> departments;
    private DepartmentMenu thisDepartmentMenu;

    private DefaultTableModel departmentTableModel;
    private JTable departmentsTable;
    private JScrollPane departmentScrollPane;
    private JPanel mainPanel;
    private JButton createDepartmentButton;
    private JButton departmentInfoButton;
    private JButton updateDepartmentButton;
    private JButton deleteDepartmentButton;
    private JLabel loadingLabel;

    private DepartmentMenu() throws DataAccessException {
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
        readDataFromDatabase();
    }

    private void readDataFromDatabase() {
        SwingWorker loadDepartmentData = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                departments = new ArrayList<>(departmentController.getAllDepartments(true));
                return null;
            }

            @Override
            protected void done() {
                mainPanel.remove(loadingLabel);
                mainPanel.add(departmentScrollPane, "grow, spany");

                initDepartmentTable();
                initializeMainFrame();
            }
        };

        loadDepartmentData.execute();
    }

    private void initializeVariables() {
        DBConnection.connect();

        departmentController = new DepartmentController();
        thisDepartmentMenu = this;

        mainPanel = new JPanel();
        loadingLabel = new JLabel("Loading Data...");
        loadingLabel.setFont(new Font(loadingLabel.getFont().getName(), Font.ITALIC, 50));

        /**
         * Create department button logic
         */
        createDepartmentButton = new JButton("Create Department");
        createDepartmentButton.addActionListener(actionEvent -> {
            this.dispose();
            DepartmentCreateMenu.start();
        });

        /**
         * Department info button logic
         * @NOTE When starting the info menu, the department data does not load again from the database
         * This is done by passing the setVisible method to the other menu
         */
        departmentInfoButton = new JButton("Department Information");
        departmentInfoButton.addActionListener(actionEvent -> {
            Department selectedDepartment = null;
            if (!departmentsTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = departmentsTable.convertRowIndexToModel(departmentsTable.getSelectedRow());
                selectedDepartment = departments.get(selectedRow);
                this.setVisible(false);
                DepartmentInfoMenu.start(() -> this.setVisible(true), selectedDepartment);
            }
        });

        /**
         * Update Department button logic
         */
        updateDepartmentButton = new JButton("Update Department");
        updateDepartmentButton.addActionListener(actionEvent -> {
            Department selectedDepartment = null;
            if (!departmentsTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = departmentsTable.convertRowIndexToModel(departmentsTable.getSelectedRow());
                selectedDepartment = departments.get(selectedRow);
                this.dispose();
                DepartmentUpdateMenu.start(selectedDepartment);
            }
        });

        /**
         * Delete Department button logic
         */
        deleteDepartmentButton = new JButton("Delete Department");
        deleteDepartmentButton.addActionListener(actionEvent -> {
            Department selectedDepartment = null;
            if (!departmentsTable.getSelectionModel().isSelectionEmpty()) {
                int selectedRow = departmentsTable.convertRowIndexToModel(departmentsTable.getSelectedRow());
                selectedDepartment = departments.get(selectedRow);

                int reply = JOptionPane.showConfirmDialog(
                        rootPane,
                        "Are you sure you want to delete the department " + selectedDepartment.getName(),
                        "Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (reply == JOptionPane.YES_OPTION) {
                    try {
                        departmentController.deleteDepartment(selectedDepartment.getDepartmentID());
                        this.dispose();
                        DepartmentMenu.start();
                    } catch (DataAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        departmentTableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "DepartmentID", "Department Name"}) {

            private static final long serialVersionUID = 1L;
            final boolean[] canEdit = { false, false };

            @Override
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        };

        departmentsTable = new JTable(departmentTableModel);
        departmentScrollPane = new JScrollPane(departmentsTable);
    }

    private void initializeMainPanel() {
        mainPanel.setLayout(new MigLayout(
                "flowy",
                "[25%][grow]",
                "[50][grow]"
        ));

        mainPanel.add(new MenuBar().getMainPanel(), "span, grow");
        mainPanel.add(createDepartmentButton, "grow");
        mainPanel.add(departmentInfoButton, "grow");
        mainPanel.add(updateDepartmentButton, "grow");
        mainPanel.add(deleteDepartmentButton, "grow, wrap");
        mainPanel.add(loadingLabel, "center, spany");
    }

    public void initializeMainFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create Department");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);

        pack();
    }

    private void initDepartmentTable() {
        departmentTableModel.setRowCount(0);
        departments.forEach((department) -> departmentTableModel.addRow(new String[] {
                Integer.toString(department.getDepartmentID()),
                department.getName(),
        }));
    }

    public static void start() {
        DBConnection.connect();
        EventQueue.invokeLater(() -> {
            try {
                new DepartmentMenu().setVisible(true);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });
    }

    //TODO get removed later
    public static void main(String[] args) throws DataAccessException {
        DepartmentMenu departmentMenu = new DepartmentMenu();
        departmentMenu.start();
    }
}
