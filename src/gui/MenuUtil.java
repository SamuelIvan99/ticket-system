package gui;

import controller.*;
import db.DataAccessException;
import model.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuUtil {

    public static String checkName(String name) throws InvalidTextException {
        if (!name.matches("\\b([A-z]{2,})\\b"))
            throw new InvalidTextException("The name is invalid.", null);
        return name;
    }

    public static String checkEmail(String email) throws InvalidTextException {
        if (!email.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$"))
            throw new InvalidTextException("The email is invalid.", null);
        return email;
    }

    public static String checkPhone(String phone) throws InvalidTextException {
        if (!phone.matches("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-\\s\\./0-9]*$"))
            throw new InvalidTextException("The phone is invalid.", null);
        return phone;
    }

    public static String checkNumber(String number) throws InvalidTextException {
        if (!number.matches("([1-9]\\d{0,})"))
            throw new InvalidTextException("The number is invalid.", null);
        return number;
    }

    public static String checkAddress(String address) throws InvalidTextException {
        if (!address.matches("(\\b.{0,75}\\b)"))
            throw new InvalidTextException("The address is invalid.", null);
        return address;
    }

    public static String checkText(String text) throws InvalidTextException {
        if (text.isEmpty())
            throw new InvalidTextException("Empty fields detected.", null);
        return text;
    }

    /**
     * Gets all fields names from a class
     * @param fields
     * @param type
     */
    static final int FIELDS_FOR_CREATE = 0;
    static final int FIELDS_FOR_UPDATE = 1;
    static final int FIELDS_FOR_INFO = 2;

    public static void getAllClassFields(ArrayList<Field> fields, Class<?> type, int fieldsForOperation) {

        if (type.getSuperclass() != null) {
            getAllClassFields(fields, type.getSuperclass(), fieldsForOperation);
        }

        addFieldsToArrayForOperation(fields, type.getDeclaredFields(), fieldsForOperation);
    }

    /**
     * Filter class fields to get only the fields that are necessary to complete
     * @param fields
     */
    private static void addFieldsToArrayForOperation(ArrayList<Field> fields, Field[] fieldsToAdd, int fieldsForOperation) {
        fields.addAll(Arrays.asList(fieldsToAdd));
        fields.removeIf(field -> field.getType().isAssignableFrom(ArrayList.class));

        switch (fieldsForOperation) {
            case FIELDS_FOR_CREATE :
                fields.removeIf(field ->
                    field.getName().equals("fullName") ||
                    field.getName().contains("ID"));
                break;

            case FIELDS_FOR_UPDATE :
                fields.removeIf(field ->
                    field.getName().equals("fullName") ||
                    field.getName().contains("ID"));
                break;

            case FIELDS_FOR_INFO :
                break;
        }

    }

    /**
     * Returns a list of field names as String from an array of type Field
     * @param declaredFields
     */
    public static List<String> getFieldsNames(Field[] declaredFields) {
        return Stream
            .of(declaredFields)
            .map(Field::getName)
            .collect(Collectors.toList());
    }

    /**
     * Gets GUI Layout components based on the Class fields provided through menuComponents
     * Gets an ArrayList of Lambdas through getInfoAfterInput which are meant to be run in the menu for getting the input from the text fields and combo boxes later on
     *      getInfoAfterInput Runnables get information from all text fields and combo boxes and directly assign them to the fields in the objectToCreate passed Object
     * fields represents the class fields of objectToCreate that are to be set
     * objectToCreate is the object to which the data will be mapped on
     * @param menuComponents
     * @param getInfoAfterInput
     * @param fields
     * @param objectToCreate
     * @throws DataAccessException
     */
    public static void getCreateUpdateGUIComponentsFromObject(ArrayList<Component> menuComponents, ArrayList<Runnable> getInfoAfterInput, ArrayList<Field> fields, Object objectToCreate) throws DataAccessException, IllegalAccessException {
        getAllClassFields(fields, objectToCreate.getClass(), FIELDS_FOR_CREATE);
        for (Field field : fields) {
            //TODO delete sout later
            System.out.println(objectToCreate.getClass() + " " + field.getType() + " " + field.getName());

            JPanel fieldPanel = new JPanel();
            fieldPanel.setLayout(new MigLayout(
                    "",
                    "[150][grow]",
                    ""
            ));
            String labelString = getLabelStringFromField(field);
            Class<?> fieldType = field.getType();

            /**
             * In case of primitives we only have integers
             * Therefore, it is safe as well as convenient to just use int and Integer in casting and instantiating
             */
            if (fieldType.isPrimitive()) {
                field.setAccessible(true);
                String textFieldHelp = (int)field.get(objectToCreate) != 0 ? String.valueOf(field.get(objectToCreate)) : "";
                field.setAccessible(false);

                JTextField fieldTextField = new JTextField(textFieldHelp);
                fieldPanel.add(new JLabel(labelString));
                fieldPanel.add(fieldTextField, "left, width 100:300");
                getInfoAfterInput.add(() -> {
                    try {
                        field.setAccessible(true);
                        field.set(objectToCreate, Integer.parseInt(fieldTextField.getText()));
                        field.setAccessible(false);
                    } catch (Exception e) {
//                        JOptionPane.showMessageDialog(null, "Invalid Number", "The validation failed", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else if (String.class.isAssignableFrom(fieldType)) {
                field.setAccessible(true);
                String textFieldHelp = field.get(objectToCreate) != null ? field.get(objectToCreate).toString() : "";
                field.setAccessible(false);

                JTextField fieldTextField = new JTextField(textFieldHelp);
                fieldPanel.add(new JLabel(labelString));
                fieldPanel.add(fieldTextField, "left, width 100:300");
                getInfoAfterInput.add(() -> {
                    field.setAccessible(true);
                    try {
                        field.set(objectToCreate, fieldTextField.getText());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                });
            } else if (fieldType.isEnum()) {
                fieldPanel.add(new JLabel(labelString));
                Field[] enumFields = fieldType.getFields();
                //Stream.of(enumFields).filter(field::equals).findFirst();
                List<String> enumFieldsNames = getFieldsNames(enumFields);
                JComboBox<Object> fieldComboBox = new JComboBox<>(enumFieldsNames.toArray());
                fieldPanel.add(fieldComboBox, "left, width 100:300");
                getInfoAfterInput.add(() -> {
                    field.setAccessible(true);
                    try {
                        field.set(objectToCreate, Enum.valueOf((Class<Enum>) fieldType, (String) fieldComboBox.getSelectedItem()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                });
            } else if (Address.class.isAssignableFrom(fieldType)) {
                ArrayList<Field> addressFields = new ArrayList<>();
                field.setAccessible(true);
                Address address = field.get(objectToCreate) != null ? (Address) field.get(objectToCreate) : new Address();
                field.setAccessible(false);
                getInfoAfterInput.add(() -> {
                    field.setAccessible(true);
                    try {
                        field.set(objectToCreate, address);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                });
                getCreateUpdateGUIComponentsFromObject(menuComponents, getInfoAfterInput, addressFields, address);
            } else if (LocalDateTime.class.isAssignableFrom(fieldType)) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                JTextField fieldTextField = new JTextField("Date Format : DD/MM/YYYY HH:MM");
                fieldPanel.add(new JLabel(labelString));
                fieldPanel.add(fieldTextField, "left, width 100:300");
                getInfoAfterInput.add(() -> {
                    field.setAccessible(true);
                    try {
                        field.set(objectToCreate, LocalDateTime.parse(fieldTextField.getText(), formatter));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                });
            } else {
                ArrayList<Object> classEntries = getClassEntries(fieldType);
                ArrayList<String> allComboBoxChoices = getComboBoxChoices(classEntries);

                DefaultComboBoxModel model = new DefaultComboBoxModel();
                fieldPanel.add(new JLabel(labelString));
                JTextField fieldTextField = new JTextField();
                JComboBox<Object> fieldComboBox = new JComboBox<>();
                fieldComboBox.setModel(model);
                fieldPanel.add(fieldTextField, "left, width 100:200");
                fieldPanel.add(fieldComboBox, "left, width 100:400");

                filterDisplaySet(model, fieldTextField, allComboBoxChoices);
                getInfoAfterInput.add(() -> {
                    field.setAccessible(true);
                    try {
                        int indexOfObjectToAdd = allComboBoxChoices.indexOf(fieldComboBox.getSelectedItem());
                        Object objectToAdd = classEntries.get(indexOfObjectToAdd);
                        field.set(objectToCreate, objectToAdd);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(false);
                });
                fieldTextField.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent documentEvent) {
                        filterDisplaySet(model, fieldTextField, allComboBoxChoices);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent documentEvent) {
                        filterDisplaySet(model, fieldTextField, allComboBoxChoices);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent documentEvent) { }
                });
            }
            menuComponents.add(fieldPanel);
        }
    }

    public static void getInfoGUIComponentsFromObject(ArrayList<Component> menuComponents, ArrayList<Field> fields, Object objectToDisplay) throws IllegalAccessException {
        getAllClassFields(fields, objectToDisplay.getClass(), FIELDS_FOR_INFO);
        for (Field field : fields) {
            JPanel fieldPanel = new JPanel();
            fieldPanel.setLayout(new MigLayout(
                    "",
                    "[150][grow]",
                    ""
            ));
            String labelString = getLabelStringFromField(field);
            Class<?> fieldType = field.getType();

            fieldPanel.add(new JLabel(labelString));
            JLabel fieldInfoText = new JLabel();

            field.setAccessible(true);
            if (fieldType.isPrimitive() ||
                String.class.isAssignableFrom(fieldType) ||
                fieldType.isEnum()
            ) {
                fieldInfoText.setText(field.get(objectToDisplay).toString());
            } else {
                String infoToShowFromReference = mapObjectToComboBoxString(field.get(objectToDisplay));
                fieldInfoText.setText(infoToShowFromReference);
            }
            field.setAccessible(false);
            fieldPanel.add(fieldInfoText);

            menuComponents.add(fieldPanel);
        }
    }

    private static String getLabelStringFromField(Field field) {
        String fieldName = field.getName();
        String[] words = fieldName.split("(?=\\p{Upper})");
        words[0] = words[0].substring(0,1).toUpperCase() + words[0].substring(1).toLowerCase();
        return Stream
            .of(words)
            .reduce("", (word, nextWord) -> word + " " + nextWord);
    }

    private static ArrayList<String> getComboBoxChoices(ArrayList<Object> classEntries) {
        return classEntries
            .stream()
            .map(MenuUtil::mapObjectToComboBoxString)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private static void filterDisplaySet(DefaultComboBoxModel model, JTextField fieldTextField, ArrayList<String> comboBoxChoices) {
        String filterText = fieldTextField.getText();
        model.removeAllElements();
        comboBoxChoices
            .stream()
            .filter(choice -> choice.contains(filterText))
            .forEach(model::addElement);
    }

    private static ArrayList<Object> getClassEntries(Class<?> fieldType) throws DataAccessException {
        ArrayList<Object> allChoicesForClass = new ArrayList<>();
        if (Customer.class.isAssignableFrom(fieldType)) {
            CustomerController customerController = new CustomerController();
            allChoicesForClass.addAll(customerController.getAllCustomers(false));
        } else if (Employee.class.isAssignableFrom(fieldType)) {
            EmployeeController employeeController = new EmployeeController();
            allChoicesForClass.addAll(employeeController.getAllEmployees(false));
        } else if (Department.class.isAssignableFrom(fieldType)) {
            DepartmentController departmentController = new DepartmentController();
            allChoicesForClass.addAll(departmentController.getAllDepartments(false));
        } else if (Inquiry.class.isAssignableFrom(fieldType)) {
            InquiryController inquiryController = new InquiryController();
            allChoicesForClass.addAll(inquiryController.getAllInquiries(false));
        } else if (Response.class.isAssignableFrom(fieldType)) {
            ResponseController responseController = new ResponseController();
            allChoicesForClass.addAll(responseController.getAllResponses(false));
        } else if (Ticket.class.isAssignableFrom(fieldType)) {
            TicketController ticketController = new TicketController();
            allChoicesForClass.addAll(ticketController.getAllTickets(false));
        }
        return allChoicesForClass;
    }

    private static String mapObjectToComboBoxString(Object object) {
        String comboBoxString = "";
        if (object instanceof Customer) {
            comboBoxString = ((Customer) object).getFullName() + " / " + ((Customer) object).getEmail();
        } else if (object instanceof Employee) {
            comboBoxString = ((Employee) object).getFullName() + " / " + ((Employee) object).getEmail();
        } else if (object instanceof Department) {
            comboBoxString = ((Department) object).getName() + " / Department ID: " + ((Department) object).getDepartmentID();
        } else if (object instanceof Inquiry) {
            comboBoxString = ((Inquiry) object).getTitle() + " / Inquiry ID: " + ((Inquiry) object).getInquiryID();
        } else if (object instanceof Response) {
            comboBoxString = ((Response) object).getTitle() + " / Response ID: " + ((Response) object).getResponseID();
        } else if (object instanceof Ticket) {
            comboBoxString = String.valueOf(((Ticket) object).getTicketID());
        } else if (object instanceof Address) {
            comboBoxString = ((Address) object).getStreetName() + " " + ((Address) object).getStreetNumber() + ", " + ((Address) object).getZipCode() + " " + ((Address) object).getCountry();
        }
        return comboBoxString;
    }

    public static <T extends JFrame> void setWindowSizeAndLocation(T menu) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() / 1.5;
        double height = screenSize.getHeight() / 1.5;
        screenSize.setSize(width, height);

        menu.setPreferredSize(screenSize);
        menu.setLocation((int) width / 4, (int) height / 4);
    }
}