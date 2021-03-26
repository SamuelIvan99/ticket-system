package gui;

import controller.FieldController;
import db.DataAccessException;
import model.FormField;
import model.Inquiry;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class FieldsMenu extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel;
    private JPanel fieldsPanel;
    private JScrollPane scrollPane;

    private JButton createFieldButton;
    private JLabel lblFieldName;
    private JLabel lblFieldContent;
    private JTextField txtFieldName;
    private JTextArea txtFieldContent;

    private FieldController fieldController;
    private Inquiry inquiry;

    /**
     * Launch the application.
     */
    public static void start(Inquiry inquiry) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FieldsMenu frame = new FieldsMenu(inquiry);
                    frame.setTitle("Field Menu");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public FieldsMenu(Inquiry inquiry) {
        this.inquiry = inquiry;
        fieldController = new FieldController();
        init();
        createGUI();
        loadInquiryFields();
    }

    private void init() {
        mainPanel = new JPanel();
        fieldsPanel = new JPanel();
        scrollPane = new JScrollPane(fieldsPanel);

        createFieldButton = new JButton("Create");
        createFieldButton.addActionListener(e -> {
            createField(inquiry.getInquiryID());
            loadInquiryFields();
        });
        lblFieldName = new JLabel("Name: ");
        lblFieldContent = new JLabel("Content: ");
        txtFieldName = new JTextField(20);
        txtFieldContent = new JTextArea(10, 20);
    }

    private void createGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setContentPane(mainPanel);
        MenuUtil.setWindowSizeAndLocation(this);

        mainPanel.setLayout(new MigLayout(
                "flowy",
                "[25%][grow]",
                ""
        ));

        mainPanel.add(createFieldButton, "grow");
        mainPanel.add(lblFieldName);
        mainPanel.add(txtFieldName);
        mainPanel.add(lblFieldContent);
        mainPanel.add(txtFieldContent, "wrap");
        mainPanel.add(scrollPane, "grow, push, spany");

        pack();
    }

    private void createField(int inquiryID) {
        FormField field = new FormField(txtFieldName.getText(), txtFieldContent.getText());
        try {
            fieldController.createField(field, inquiryID);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadInquiryFields() {
        scrollPane.remove(fieldsPanel);
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new MigLayout());
        ArrayList<FormField> fields = new ArrayList<>();
        try {
            fields = new ArrayList<>(fieldController.findFieldsByInquiryID(inquiry.getInquiryID(), true));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        fields.forEach(e -> createFieldPanel(e));
        scrollPane.setViewportView(fieldsPanel);
    }

    private void createFieldPanel(FormField field) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new MigLayout());

        JLabel lblField = new JLabel("Field");
        JLabel lblFieldName = new JLabel("Name: " + field.getName());
        JLabel lblFieldContent = new JLabel("Content: " + field.getContent());
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int reply = JOptionPane.showConfirmDialog(
                    rootPane,
                    "Are you sure you want to delete " + field.getName(),
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (reply == JOptionPane.YES_OPTION) {
                try {
                    fieldController.deleteField(field.getFieldID());
                    loadInquiryFields();
                } catch (DataAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });

        fieldPanel.add(lblField, "wrap");
        fieldPanel.add(lblFieldName, "wrap");
        fieldPanel.add(lblFieldContent, "wrap");
        fieldPanel.add(deleteButton, "wrap");

        fieldsPanel.add(fieldPanel, "wrap");
    }
}
