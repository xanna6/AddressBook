import javax.swing.*;
import java.awt.*;

public class FormWindow extends JDialog {
    private final DatabaseConnection dbConnection;
    private final MainWindow mainWindow;
    private Contact contact;

    public FormWindow(MainWindow parent, DatabaseConnection dbConnection) {
        this(parent, dbConnection, null);
    }

    public FormWindow(MainWindow parent, DatabaseConnection dbConnection, Contact contact) {
        super(parent, contact == null ? "Add New Contact" : "Edit Contact", true);
        this.dbConnection = dbConnection;
        this.mainWindow = parent;
        this.contact = contact;

        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField firstNameField = addField(formPanel, "First Name:");
        JTextField lastNameField = addField(formPanel, "Last Name:");
        JTextField phoneField = addField(formPanel, "Phone:");
        JTextField emailField = addField(formPanel, "Email:");

        add(formPanel, BorderLayout.CENTER);

        if (contact != null) {
            firstNameField.setText(contact.getFirstName());
            lastNameField.setText(contact.getLastName());
            phoneField.setText(contact.getPhone());
            emailField.setText(contact.getEmail());
        }

        JButton submitButton = new JButton(contact == null ? "Add" : "Save");
        submitButton.addActionListener(e -> saveContact(firstNameField.getText(), lastNameField.getText(), phoneField.getText(), emailField.getText()));

        add(submitButton, BorderLayout.SOUTH);
    }

    private static JTextField addField(JPanel formPanel, String label) {
        formPanel.add(new JLabel(label));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        return emailField;
    }

    private void saveContact(String firstName, String lastName, String phone, String email) {
        boolean isSaved;
        if (validateForm(firstName, phone, email)) {
            if (contact == null) {
                contact = new Contact(firstName, lastName, phone, email);
                isSaved = dbConnection.addContact(contact);
            } else {
                contact.setFirstName(firstName);
                contact.setLastName(lastName);
                contact.setPhone(phone);
                contact.setEmail(email);
                isSaved = dbConnection.editContact(contact);
            }
        } else {
            return;
        }

        dispose();
        if (isSaved) {
            JOptionPane.showMessageDialog(mainWindow, "Contact saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Failed to save contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        mainWindow.loadDataToContactTable();
    }

    private boolean validateForm(String firstName, String phone, String email) {
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (phone.isEmpty() && email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone or email is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!phone.isEmpty() && phone.length() != 9) {
            JOptionPane.showMessageDialog(this, "Phone number must be 9 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!email.isEmpty() && !email.toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Provided e-mail address is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
