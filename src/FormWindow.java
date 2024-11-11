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
        dbConnection.addContact(firstName, lastName, phone, email);
        mainWindow.loadDataToContactTable();
        dispose();
    }
}
