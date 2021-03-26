package gui;

import controller.EmployeeController;
import db.DBConnection;
import db.DataAccessException;
import gui.employee.EmployeeCreateMenu;
import model.Employee;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    private static Employee loggedEmployee;
    private static Updatable currentMenu;
    private static int delayTime;

    private JPanel mainPanel;
    private JLabel logo;
    private JLabel message;
    private JLabel loginExample1;
    private JLabel loginExample2;
    private JTextField emailTextField;
    private JPasswordField passwordTextField;
    private JLabel loginButton;
    private JLabel createAccount;

    private ImageIcon logoImage;
    private ImageIcon loginImage;
    private ImageIcon hoverLoginImage;
    private ImageIcon pressedLoginImage;

    private EmployeeController employeeController;

    private void setWindowSizeAndLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 2.5;
        double height = screenSize.getHeight() / 2.25;
        screenSize.setSize(width, height);

        setPreferredSize(screenSize);
        setLocation((int) (width / 1.25), (int) (height / 1.75));
        setResizable(true);
    }

    private void initImages() {
        char c = File.separatorChar;

        logoImage = new ImageIcon(String.format(".%cassets%cimages%cgrast&mtb.png", c, c, c));
        loginImage = new ImageIcon(String.format(".%cassets%cimages%clogin.png", c, c, c));
        hoverLoginImage = new ImageIcon(String.format(".%cassets%cimages%choverlogin.png", c, c, c));
        pressedLoginImage = new ImageIcon(String.format(".%cassets%cimages%cpressedlogin.png", c, c, c));
    }

    private void loginButtonClicked() {
        try {
            String email = MenuUtil.checkEmail(emailTextField.getText());
            String password = MenuUtil.checkText(passwordTextField.getText());

            SwingWorker<Employee, String> loginWorker = new SwingWorker<>() {
                @Override
                protected Employee doInBackground() throws DataAccessException {
                    publish("Searching for employee");
                    return employeeController.findEmployeeByEmail(email, true);
                }

                @Override
                protected void done() {
                    super.done();
                    try {
                        Employee employee = get();

                        if (employee != null) {
                            if (employee.getPassword().equals(password)) {
                                System.out.println("Logging in - " + Thread.currentThread().getName());
                                loggedEmployee = employee;
                                dispose();
                                MainMenu.start(false);
                            } else {
                                message.setVisible(true);
                                message.setText("Incorrect password!");
                                message.setForeground(Color.RED);
                            }
                        } else {
                            message.setVisible(true);
                            message.setText("No account with this email!");
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void process(List<String> chunks) {
                    super.process(chunks);
                    message.setVisible(true);
                    message.setText(chunks.get(chunks.size() - 1));
                }
            };
            loginWorker.execute();
        } catch (InvalidTextException e) {
            message.setVisible(true);
            message.setText(e.toString());
        }
    }

    private void initComponents() {
        mainPanel = new JPanel(new MigLayout("center"));

        loginExample1 = new JLabel("Manager: Carlsen.Andrew36@gmail.com - Civinomics");
        loginExample2 = new JLabel("Support Assistant: Carlsen.Rasmus58@gmail.com - Ecodesk");

        message = new JLabel();
        message.setFont(new Font("Ubuntu", Font.PLAIN, 16));
        message.setForeground(Color.red);
        logo = new JLabel("", logoImage, JLabel.CENTER);

        emailTextField = new JTextField("Carlsen.Andrew36@gmail.com", 30);
        emailTextField.setFont(new Font("Ubuntu", Font.BOLD, 18));
        emailTextField.setForeground(Color.GRAY);

        passwordTextField = new JPasswordField("Civinomics", 30);
        passwordTextField.setFont(new Font("Ubuntu", Font.BOLD, 18));
        passwordTextField.setForeground(Color.GRAY);

        loginButton = new JLabel("", loginImage, JLabel.CENTER);
        loginButton.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg) {
                loginButtonClicked();
            }

            @Override
            public void mouseEntered(MouseEvent arg) {
                loginButton.setIcon(hoverLoginImage);
            }

            @Override
            public void mouseExited(MouseEvent arg) {
                loginButton.setIcon(loginImage);
            }

            @Override
            public void mousePressed(MouseEvent arg) {
                loginButton.setIcon(pressedLoginImage);

            }

            @Override
            public void mouseReleased(MouseEvent arg) {
                loginButton.setIcon(hoverLoginImage);
            }
        });

        createAccount = new JLabel("No account? Create one here");
        createAccount.setFont(new Font("Ubuntu", Font.ITALIC, 12));
        createAccount.setForeground(Color.BLUE);
        createAccount.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                EmployeeCreateMenu.start();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });
    }

    private void init() {
        setTitle("Log In");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setWindowSizeAndLocation();
        setMinimumSize(new Dimension(350, 450));
        setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        initImages();
        initComponents();

        employeeController = new EmployeeController();

        mainPanel.add(logo, "wrap, center, gapy 30");
        mainPanel.add(loginExample1, "wrap, center");
        mainPanel.add(loginExample2, "wrap, center");
        mainPanel.add(message, "wrap, center, gapy 35");
        mainPanel.add(emailTextField, "wrap, center, height 30, grow");
        mainPanel.add(passwordTextField, "wrap, center, gapy 10, height 30, grow");
        mainPanel.add(loginButton, "center, gapy 15, width 30, wrap");
        mainPanel.add(createAccount, "center, gapy 5");
        mainPanel.setBackground(Color.WHITE);

        getContentPane().add(mainPanel);
        pack();
    }

    private LoginMenu() {
        DBConnection.connect();
        init();
    }

    public static void start() {
        System.out.println("Starting UI - " + Thread.currentThread().getName());
        EventQueue.invokeLater(() -> new LoginMenu().setVisible(true));
    }

    public static void main(String[] args) {
        Runnable updateChecker = () -> {
            Thread.currentThread().setName("UpdateCheckerThread");

            System.out.println("Starting update checker - " + Thread.currentThread().getName());
            int sleepTime = 2 * 1000; // 1 minute
            delayTime = 0;

            while (true) {
                try {
                    if (currentMenu != null) {
                        if (currentMenu.checkForUpdates()) {
                            System.out.println("In the checker - called update");
                            currentMenu.updateMenu();
                        }
                    }

                    Thread.sleep(sleepTime + delayTime);
                    delayTime = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread1 = new Thread(updateChecker);
        thread1.start();

        start();
    }

    public static void setCurrentMenu(Updatable currentMenu) {
        // addDelayTime(1 * 60 * 1000);
        LoginMenu.currentMenu = currentMenu;
    }

    public static void addDelayTime(int delayTime) {
        LoginMenu.delayTime = delayTime;
    }

    public static Employee getLoggedEmployee() {
        return loggedEmployee;
    }

    public static Employee setLoggedEmployee(Employee employee) {
        loggedEmployee = employee;
        return employee;
    }
}