package gui;

import gui.customer.CustomerMenu;
import gui.department.DepartmentMenu;
import gui.employee.EmployeeMenu;
import model.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MenuBar {
    private JPanel mainPanel;

    private JButton allTicketsButton;
    private JButton myTicketsButton;
    private JButton employeesButton;
    private JButton customersButton;
    private JButton departmentsButton;
    private JButton logoutButton;

    private ImageIcon homeIcon;
    private ImageIcon myTicketsIcon;
    private ImageIcon employeesIcon;
    private ImageIcon customersIcon;
    private ImageIcon departmentsIcon;
    private ImageIcon logoutIcon;

    public MenuBar() {
        initComponents();
        createMainPanel();
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private void initImages() {
        char c = File.separatorChar;

        homeIcon = getScaledIcon(String.format(".%cassets%cimages%cMenuBar%chome.png", c, c, c, c));
        myTicketsIcon = getScaledIcon(String.format(".%cassets%cimages%cMenuBar%cmyTickets.png", c, c, c, c));
        employeesIcon = getScaledIcon(String.format(".%cassets%cimages%cMenuBar%cemployee.png", c, c, c, c));
        customersIcon = getScaledIcon(String.format(".%cassets%cimages%cMenuBar%ccustomer.png", c, c, c, c));
        departmentsIcon = getScaledIcon(String.format(".%cassets%cimages%cMenuBar%cdepartment.png", c, c, c, c));
        logoutIcon = getScaledIcon(String.format(".%cassets%cimages%cMenuBar%clogout.png", c, c, c, c));
    }

    private ImageIcon getScaledIcon(String location) {
        ImageIcon initialIcon = new ImageIcon(location);
        Image scaledImage = initialIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        return scaledIcon;
    }

    private void initComponents() {
        initImages();
        mainPanel = new JPanel();

        allTicketsButton = new JButton("All Tickets", homeIcon);
        allTicketsButton.addActionListener(e -> {
            closeActiveFrame();
            MainMenu.start(false);
        });
        allTicketsButton.setBackground(Color.BLACK);
        allTicketsButton.setForeground(Color.WHITE);
        allTicketsButton.setOpaque(true);
        allTicketsButton.setBorderPainted(false);

        myTicketsButton = new JButton("My Tickets", myTicketsIcon);
        myTicketsButton.addActionListener(e -> {
            closeActiveFrame();
            MainMenu.start(true);
        });
        myTicketsButton.setBackground(Color.BLACK);
        myTicketsButton.setForeground(Color.WHITE);
        myTicketsButton.setOpaque(true);
        myTicketsButton.setBorderPainted(false);

        employeesButton = new JButton("Employees", employeesIcon);
        employeesButton.addActionListener(e -> {
            closeActiveFrame();
            EmployeeMenu.start();
        });
        employeesButton.setBackground(Color.BLACK);
        employeesButton.setForeground(Color.WHITE);
        employeesButton.setOpaque(true);
        employeesButton.setBorderPainted(false);

        customersButton = new JButton("Customers", customersIcon);
        customersButton.addActionListener(e -> {
            closeActiveFrame();
            CustomerMenu.start();
        });
        customersButton.setBackground(Color.BLACK);
        customersButton.setForeground(Color.WHITE);
        customersButton.setOpaque(true);
        customersButton.setBorderPainted(false);

        departmentsButton = new JButton("Departments", departmentsIcon);
        departmentsButton.addActionListener(e -> {
            closeActiveFrame();
            DepartmentMenu.start();
        });
        departmentsButton.setBackground(Color.BLACK);
        departmentsButton.setForeground(Color.WHITE);
        departmentsButton.setOpaque(true);
        departmentsButton.setBorderPainted(false);

        logoutButton = new JButton("Logout", logoutIcon);
        logoutButton.addActionListener(e ->
        {
            closeActiveFrame();
            LoginMenu.main(null);
        });
        logoutButton.setBackground(Color.BLACK);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setOpaque(true);
        logoutButton.setBorderPainted(false);

        if (LoginMenu.getLoggedEmployee() != null && LoginMenu.getLoggedEmployee().getEmployeeType() == Employee.EmployeeType.SALES_ASSISTANT) {
            allTicketsButton.setEnabled(false);
            employeesButton.setEnabled(false);
            customersButton.setEnabled(false);
            departmentsButton.setEnabled(false);
        }
    }

    private void createMainPanel() {
        mainPanel.setLayout(new MigLayout(
            "",
            "[grow]",
            ""
        ));
        mainPanel.add(allTicketsButton, "grow");
        mainPanel.add(myTicketsButton, "grow");
        mainPanel.add(employeesButton, "grow");
        mainPanel.add(customersButton, "grow");
        mainPanel.add(departmentsButton, "grow");
        mainPanel.add(logoutButton, "grow");
    }

    private void closeActiveFrame() {
        for (Frame f : Frame.getFrames()) {
            if (f.isActive())
                f.dispose();
        }
    }
}
