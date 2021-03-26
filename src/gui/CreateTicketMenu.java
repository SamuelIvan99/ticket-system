package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import controller.CustomerController;
import controller.EmployeeController;
import controller.InquiryController;
import controller.TicketController;
import db.DBConnection;
import db.DataAccessException;
import model.Address;
import model.Customer;
import model.Inquiry;
import model.Ticket;
import net.miginfocom.swing.MigLayout;

public class CreateTicketMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JLabel emailLabel;
    private JLabel countryLabel;
    private JLabel companyLabel;
    private JLabel cityLabel;
    private JLabel zipLabel;
    private JLabel streetNameLabel;
    private JLabel streetNumberLabel;
    private JLabel phoneNumberLabel;

    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JTextField emailTextField;
    private JTextField countryTextField;
    private JTextField companyTextField;
    private JTextField cityTextField;
    private JTextField zipTextField;
    private JTextField streetNameTextField;
    private JTextField streetNumberTextField;
    private JTextField phoneNumberTextField;

    private JLabel chooseCatLabel;
    private JLabel titleLabel;
    private JLabel descriptLabel;

    private JButton submitTicket;
    private JButton goBack;
    private JComboBox<String> catComboBox;

    private JTextField inquiryTitleTextField;
    private JTextArea inquiryDescriptTextField;

    private JPanel inquiryPanel;
    private JPanel custPanel;
    private JPanel mainPanel;

    private CustomerController customerController;
    private TicketController ticketController;
    private InquiryController inquiryController;

    private void submitTicketActionPerformed() {
        isSubmitting = true;
        // Customer fields
        String firstName, lastName;
        String phone, email;
        String company;
        // Address fields
        String country, city, zipCode;
        String streetName, streetNumber;
        // Inquiry fields
        String title;
        String descritp;
        try {
            firstName = MenuUtil.checkName(firstNameTextField.getText());
            lastName = MenuUtil.checkName(lastNameTextField.getText());
            phone = MenuUtil.checkPhone(phoneNumberTextField.getText());
            email = MenuUtil.checkEmail(emailTextField.getText());
            company = MenuUtil.checkText(companyTextField.getText());

            country = MenuUtil.checkText(countryTextField.getText());
            city = MenuUtil.checkText(cityTextField.getText());
            zipCode = MenuUtil.checkNumber(zipTextField.getText());
            streetName = MenuUtil.checkText(streetNameTextField.getText());
            streetNumber = MenuUtil.checkNumber(streetNumberTextField.getText());

            title = MenuUtil.checkText(inquiryTitleTextField.getText());
            descritp = MenuUtil.checkText(inquiryDescriptTextField.getText());
        } catch (InvalidTextException e) {
            JOptionPane.showMessageDialog(rootPane, e, "The validation failed", JOptionPane.ERROR_MESSAGE);
            isSubmitting = false;
            return;
        }

        Customer customer = null;
        Ticket ticket = new Ticket("in progress", "low").setStartDate(LocalDateTime.now());
        Inquiry inquiry = new Inquiry(title, descritp, LocalDateTime.now())
                .setCategory(catComboBox.getSelectedItem().toString());

        try {
            submitTicket.setText("Submitting...");
            if ((customer = customerController.findCustomerByEmail(email, true)) == null) {
                Address address = new Address(country, zipCode, streetName, Integer.parseInt(streetNumber));
                customer = new Customer(firstName, lastName, phone, email, "none", company).setAddress(address);
                customerController.createCustomer(customer);
            }
            ticket.setCustomer(customer);
            ticket.addInquiry(inquiry);
            ticketController.createTicket(ticket);
            inquiryController.createInquiry(inquiry, ticket.getTicketID());
            JOptionPane.showMessageDialog(rootPane, "Ticket submitted!", "Success", JOptionPane.PLAIN_MESSAGE);
        } catch (NumberFormatException | DataAccessException e) {
            JOptionPane.showMessageDialog(rootPane, "Reason: " + e.toString(), "Could not submit the ticket",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            submitTicket.setText("Submit Ticket");
            isSubmitting = false;
        }
    }

    private CreateTicketMenu() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Create a new ticket");
        setWindowSizeAndLocation();
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        customerController = new CustomerController();
        ticketController = new TicketController();
        inquiryController = new InquiryController();
        initMainMenu();
        try {
            LoginMenu.setLoggedEmployee(new EmployeeController().findEmployeeByEmail("Corse.Sofia98@gmail.com", false));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 2.5;
        double height = screenSize.getHeight() / 1.5;
        screenSize.setSize(width, height);

        setPreferredSize(screenSize);
        setLocation((int) width / 4, (int) height / 4);
    }

    private void clean() {
        for (Component component : getComponents()) {
            mainPanel.setVisible(false);
            mainPanel.remove(component);
        }
        getContentPane().remove(mainPanel);
    }

    private void initNewTicket() {
        initComponentsNewTicket();

        custPanel.add(goBack, "wrap");
        custPanel.add(firstNameLabel);
        custPanel.add(firstNameTextField, "wrap");
        custPanel.add(lastNameLabel);
        custPanel.add(lastNameTextField, "wrap");
        custPanel.add(emailLabel);
        custPanel.add(emailTextField, "wrap");
        custPanel.add(companyLabel);
        custPanel.add(companyTextField, "wrap");
        custPanel.add(countryLabel);
        custPanel.add(countryTextField, "wrap");
        custPanel.add(cityLabel);
        custPanel.add(cityTextField, "wrap");
        custPanel.add(zipLabel);
        custPanel.add(zipTextField, "wrap");
        custPanel.add(streetNameLabel);
        custPanel.add(streetNameTextField, "growx");
        custPanel.add(streetNumberLabel);
        custPanel.add(streetNumberTextField, "wrap");
        custPanel.add(phoneNumberLabel);
        custPanel.add(phoneNumberTextField, "wrap");

        inquiryPanel.add(chooseCatLabel, "split 2");
        inquiryPanel.add(catComboBox, "wrap, growx, pushx");
        inquiryPanel.add(titleLabel, "split 2");
        inquiryPanel.add(inquiryTitleTextField, "wrap, growx");
        inquiryPanel.add(descriptLabel, "wrap");
        inquiryPanel.add(inquiryDescriptTextField, "wrap, growx");
        inquiryPanel.add(submitTicket, "span, growx");

        mainPanel.add(custPanel, "top");
        mainPanel.add(inquiryPanel, "gapleft 150, pushx, growx");

        getContentPane().add(mainPanel);
        pack();
    }

    boolean isSubmitting = false;

    private void initComponentsNewTicket() {
        firstNameTextField = new JTextField(30);
        lastNameTextField = new JTextField(30);
        emailTextField = new JTextField(30);
        countryTextField = new JTextField(30);
        companyTextField = new JTextField(30);
        cityTextField = new JTextField(30);
        zipTextField = new JTextField(30);
        streetNameTextField = new JTextField(30);
        streetNumberTextField = new JTextField(3);
        phoneNumberTextField = new JTextField(30);

        chooseCatLabel = new JLabel("Choose category");
        firstNameLabel = new JLabel("First name");
        lastNameLabel = new JLabel("Last name");
        emailLabel = new JLabel("Email address");
        countryLabel = new JLabel("Country");
        companyLabel = new JLabel("Company");
        cityLabel = new JLabel("City");
        zipLabel = new JLabel("Zip code");
        streetNameLabel = new JLabel("Street name");
        streetNumberLabel = new JLabel("No.");

        phoneNumberLabel = new JLabel("Phone number");
        titleLabel = new JLabel("Title");
        descriptLabel = new JLabel("Description");

        inquiryTitleTextField = new JTextField();
        inquiryDescriptTextField = new JTextArea();
        catComboBox = new JComboBox<>(new String[] { "Complaint", "Refund", "Return", "Question", "Mixed", "Other" });
        submitTicket = new JButton("Submit Ticket");
        goBack = new JButton("Go back");
        goBack.addActionListener(e -> {
            clean();
            initMainMenu();
        });

        inquiryDescriptTextField.setPreferredSize(new Dimension(250, 150));
        submitTicket.addActionListener(e -> {
            if (!isSubmitting) {
                Thread t = new Thread(() -> submitTicketActionPerformed());
                t.start();
            } else {
                System.out.println("Still Submitting.");
            }
        });

        custPanel = new JPanel(new MigLayout());
        inquiryPanel = new JPanel(new MigLayout());
        mainPanel = new JPanel(new MigLayout());
    }

    private JTextField email;
    private JLabel nameField;
    private JButton searchButton;
    private JScrollPane ticketsScrollPane;
    private JTable ticketsTable;
    private DefaultTableModel model = new DefaultTableModel(new String[0][6],
            new String[] { "TicketID", "Title", "StartDate", "ComplaintSatus", "Priority", "Employee Name" });
    boolean isSearching = false;

    private void searchButtonMouseClicked(Customer customer) {
        isSearching = true;
        List<Ticket> tickets;
        try {
            tickets = ticketController.findTicketsByCustomer(customer, true);
            for (Ticket ticket : tickets) {
                model.addRow(new Object[] { ticket.getTicketID(),
                        ticket.getInquiries().isEmpty() ? "" : ticket.getInquiries().get(0).getTitle(),
                        ticket.getStartDate().toString(), ticket.getComplaintStatus(), ticket.getPriority(),
                        ticket.getEmployee() == null ? "" : ticket.getEmployee().getFullName() });
            }
        } catch (DataAccessException e1) {
            e1.printStackTrace();
        } finally {
            isSearching = false;
        }
    }

    Thread searchingThread;

    private void initComponentsOldTicket() {
        ticketsTable = new JTable(model);
        ticketsScrollPane = new JScrollPane(ticketsTable);
        email = new JTextField("Agard.Peter65@gmail.com", 30);
        nameField = new JLabel("");
        nameField.setVisible(false);
        searchButton = new JButton("Search for account");
        goBack = new JButton("Go back");
        goBack.addActionListener(e -> {
            model.setRowCount(0);
            clean();
            initMainMenu();
        });
        ticketsTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg) {
                try {
                    Ticket ticket = ticketController.findTicketByID(
                            Integer.valueOf(model.getValueAt(ticketsTable.getSelectedRow(), 0).toString()), true);
                    TicketMenu.start(ticket, false);
                } catch (NumberFormatException | DataAccessException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void mouseEntered(MouseEvent arg) {
            }

            @Override
            public void mouseExited(MouseEvent arg) {
            }

            @Override
            public void mousePressed(MouseEvent arg) {
            }

            @Override
            public void mouseReleased(MouseEvent arg) {
            }
        });
        searchButton.addActionListener(e -> {
            if (!isSearching) {
                model.setRowCount(0);
                searchButton.setText("Searching...");
                searchingThread = new Thread(() -> {
                    try {
                        nameField.setVisible(true);
                        MenuUtil.checkEmail(email.getText());
                        Customer customer = customerController.findCustomerByEmail(email.getText(), true);
                        if (customer != null) {
                            searchButtonMouseClicked(customer);
                            nameField.setText("Account found!");
                            nameField.setForeground(Color.green);
                        } else {
                            throw new DataAccessException("", null);
                        }
                    } catch (InvalidTextException | DataAccessException e1) {
                        nameField.setText("Could not find email");
                        nameField.setForeground(Color.red);
                        model.setRowCount(0);
                    } finally {
                        searchButton.setText("Search for account");
                    }
                });
                searchingThread.start();
            } else {
                System.out.println("Still searching.");
            }
        });

        mainPanel = new JPanel(new MigLayout("center"));
        mainPanel.setForeground(Color.WHITE);
    }

    private void initOldTicket() {
        initComponentsOldTicket();

        mainPanel.add(goBack, "wrap");
        mainPanel.add(email, "gapy 50, split 3, growx");
        mainPanel.add(nameField, "growx");
        mainPanel.add(searchButton, "width 30, wrap, growx");
        mainPanel.add(ticketsScrollPane, "span, growx");

        getContentPane().add(mainPanel);
        pack();
    }

    private JLabel welcomeLogo;
    private JLabel newTicketButton;
    private JLabel oldTicketButton;

    private void initComponentsMainMenu() {
        char c = File.separatorChar;
        ImageIcon welcomeImage = new ImageIcon(String.format(".%cassets%cimages%cwelcome.png", c, c, c));
        welcomeLogo = new JLabel("", welcomeImage, JLabel.CENTER);

        ImageIcon newTicketImage = new ImageIcon(String.format(".%cassets%cimages%cnewticket.png", c, c, c));
        ImageIcon hoverNewTicketImage = new ImageIcon(String.format(".%cassets%cimages%chovernewticket.png", c, c, c));
        ImageIcon pressedNewTicketImage = new ImageIcon(
                String.format(".%cassets%cimages%cpressednewticket.png", c, c, c));
        newTicketButton = new JLabel("", newTicketImage, JLabel.CENTER);
        newTicketButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg) {
                clean();
                initNewTicket();
            }

            @Override
            public void mouseEntered(MouseEvent arg) {
                newTicketButton.setIcon(hoverNewTicketImage);
            }

            @Override
            public void mouseExited(MouseEvent arg) {
                newTicketButton.setIcon(newTicketImage);
            }

            @Override
            public void mousePressed(MouseEvent arg) {
                newTicketButton.setIcon(pressedNewTicketImage);
            }

            @Override
            public void mouseReleased(MouseEvent arg) {
                newTicketButton.setIcon(hoverNewTicketImage);
            }
        });

        ImageIcon oldTicketImage = new ImageIcon(String.format(".%cassets%cimages%coldticket.png", c, c, c));
        ImageIcon hoverOldTicketImage = new ImageIcon(String.format(".%cassets%cimages%choveroldticket.png", c, c, c));
        ImageIcon pressedOldTicketImage = new ImageIcon(
                String.format(".%cassets%cimages%cpressedoldticket.png", c, c, c));
        oldTicketButton = new JLabel("", oldTicketImage, JLabel.CENTER);
        oldTicketButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent arg) {
                clean();
                initOldTicket();
            }

            @Override
            public void mouseEntered(MouseEvent arg) {
                oldTicketButton.setIcon(hoverOldTicketImage);
            }

            @Override
            public void mouseExited(MouseEvent arg) {
                oldTicketButton.setIcon(oldTicketImage);
            }

            @Override
            public void mousePressed(MouseEvent arg) {
                oldTicketButton.setIcon(pressedOldTicketImage);
            }

            @Override
            public void mouseReleased(MouseEvent arg) {
                oldTicketButton.setIcon(hoverOldTicketImage);
            }
        });
    }

    private void initMainMenu() {
        initComponentsMainMenu();

        mainPanel = new JPanel(new MigLayout("center"));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(welcomeLogo, "center, wrap, gapy 150");
        mainPanel.add(newTicketButton, "center, wrap, gapy 75");
        mainPanel.add(oldTicketButton, "center, gapy 25");
        getContentPane().add(mainPanel);
        pack();
    }

    public void start() {
        DBConnection.connect();
        EventQueue.invokeLater(() -> new CreateTicketMenu().setVisible(true));
    }

    public static void main(String[] args) {
        CreateTicketMenu menu = new CreateTicketMenu();
        menu.start();
    }
}