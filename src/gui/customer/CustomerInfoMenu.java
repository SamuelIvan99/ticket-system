package gui.customer;

import db.DataAccessException;
import model.Customer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static gui.MenuUtil.*;

public class CustomerInfoMenu extends JFrame{
    private Customer customer;

    private JPanel mainPanel;
    private JButton goBackButton;
    private JLabel titleLabel;
    private ArrayList<Field> customerFields;
    private ArrayList<Component> menuComponents;
    private Runnable openPreviousWindow;

    @Override
    public void setDefaultCloseOperation(int operation) {
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private CustomerInfoMenu(Runnable openPreviousWindow, Customer customer) throws DataAccessException, IllegalAccessException {
        this.openPreviousWindow = openPreviousWindow;
        this.customer = customer;
        initializeVariables();
        initializeMainPanel();
        initializeMainFrame();
    }

    private void initializeVariables() throws DataAccessException, IllegalAccessException {
        mainPanel = new JPanel();
        goBackButton = new JButton("Go Back");
        titleLabel = new JLabel("Customer Info Menu");
        customerFields = new ArrayList<>();
        menuComponents = new ArrayList<>();

        getInfoGUIComponentsFromObject(menuComponents, customerFields, customer);

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
        setTitle("Customer Information");

        setWindowSizeAndLocation(this);

        getContentPane().add(mainPanel);
        pack();
    }

    public static void start(Runnable openPreviousWindow, Customer customer) {
        EventQueue.invokeLater(() -> {
            try {
                new CustomerInfoMenu(openPreviousWindow, customer).setVisible(true);
            } catch (DataAccessException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
