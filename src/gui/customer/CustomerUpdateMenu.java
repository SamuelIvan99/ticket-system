package gui.customer;

import controller.CustomerController;
import db.DataAccessException;
import gui.InvalidTextException;
import gui.MenuUtil;
import model.Customer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static gui.MenuUtil.getCreateUpdateGUIComponentsFromObject;
import static gui.MenuUtil.setWindowSizeAndLocation;

public class CustomerUpdateMenu extends JFrame {

    private CustomerController customerController;
    private Customer customer;
    private ArrayList<Runnable> runGetInfo;

    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton submitButton;
    private JLabel titleLabel;
    private ArrayList<Field> customerFields;
    private ArrayList<Component> menuComponents;

    private CustomerUpdateMenu(Customer customer) throws DataAccessException, IllegalAccessException {
        this.customer = customer;
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        customerController = new CustomerController();

        mainPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        submitButton = new JButton("Update Customer");
        titleLabel = new JLabel("Update Customer Menu");
        customerFields = new ArrayList<>();
        menuComponents = new ArrayList<>();
        runGetInfo = new ArrayList<>();

        getCreateUpdateGUIComponentsFromObject(menuComponents, runGetInfo, customerFields, customer);

        /**
         * When the submit button is clicked, the runGetInfo Functions will pull and map the data to the Customer provided to getCreateUpdateGUIComponentsFromObject
         * The Customer will then get verified for input errors and be pushed to the database if there are no errors within the input
         */
        submitButton.addActionListener(actionEvent -> {
            runGetInfo.forEach(Runnable::run);
            try {
                submitUpdateCustomer();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        });

        cancelButton.addActionListener(actionEvent -> {
            this.dispose();
            CustomerMenu.start();
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
        setTitle("Update Customer");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    private void submitUpdateCustomer() throws DataAccessException {
        try {
            MenuUtil.checkName(customer.getFirstName());
            MenuUtil.checkName(customer.getLastName());
            MenuUtil.checkPhone(customer.getPhoneNumber());
            MenuUtil.checkEmail(customer.getEmail());
            MenuUtil.checkText(customer.getCompanyName());

            MenuUtil.checkText(customer.getAddress().getCountry());
            MenuUtil.checkNumber(customer.getAddress().getZipCode());
            MenuUtil.checkText(customer.getAddress().getStreetName());
            MenuUtil.checkNumber(String.valueOf(customer.getAddress().getStreetNumber()));
        } catch (InvalidTextException e) {
            JOptionPane.showMessageDialog(rootPane, e, "The validation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean updated = customerController.updateCustomer(customer,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhoneNumber(),
                customer.getEmail(),
                customer.getCustomerType().getLabel(),
                customer.getCompanyName(),
                customer.getAddress());
        if (updated) {
            JOptionPane.showMessageDialog(rootPane, "Customer has been updated!", "Customer Updated", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            CustomerMenu.start();
        } else {
            JOptionPane.showMessageDialog(rootPane, "Customer with that email already exists!", "The validation failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void start(Customer customer) {
        EventQueue.invokeLater(() -> {
            try {
                new CustomerUpdateMenu(customer).setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
