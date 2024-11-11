import javax.swing.*;
import java.awt.*;

public class FormWindow extends JDialog {
    DatabaseConnection dbConnection;
    MainWindow mainWindow;

    public FormWindow(MainWindow parent, DatabaseConnection dbConnection) {
        super(parent, "Contact Form", true);
        this.dbConnection = dbConnection;
        this.mainWindow = parent;

        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("First Name:"));
        JTextField firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name:"));
        JTextField lastNameField = new JTextField();
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);

        add(formPanel, BorderLayout.CENTER);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            addContactToDatabase(firstNameField.getText(), lastNameField.getText(), phoneField.getText(), emailField.getText());
        });

        add(submitButton, BorderLayout.SOUTH);
    }

    private void addContactToDatabase(String firstName, String lastName, String phone, String email) {
        if (firstName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (phone.isEmpty() && email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone or email is required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!phone.isEmpty() && phone.length() != 9) {
            JOptionPane.showMessageDialog(this, "Phone number must be 9 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.isEmpty() && !email.toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) {
            JOptionPane.showMessageDialog(this, "Provided e-mail address is not valid!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isAdded = dbConnection.addContact(firstName, lastName, phone, email);
        dispose();
        if (isAdded) {
            JOptionPane.showMessageDialog(mainWindow, "Contact added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Failed to add contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        mainWindow.loadDataToContactTable();
    }
}
