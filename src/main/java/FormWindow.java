package main.java;

import javax.swing.*;
import java.awt.*;

/**klasa budująca okno z formularzem dodawania/edycji kontaktu*/
public class FormWindow extends JDialog {
    /**połączenie z bazą danych*/
    private final DatabaseConnection dbConnection;
    /**referencja do głównego okienka aplikacji*/
    private final MainWindow mainWindow;
    /**edytowany kontakt*/
    private Contact contact;

    /**
     * Tworzy nowe okno formularza z podanym rodzicem i połączeniem z bazą danych.
     * Otwiera okno dodawania nowego kontaktu, z tego powodu ustawia wartość obiektu contact na {@code null}
     * @param parent okno główne, które jest właścicielem tego okna
     * @param dbConnection połączenie z bazą danych używane przez okno formularza
     */
    public FormWindow(MainWindow parent, DatabaseConnection dbConnection) {
        this(parent, dbConnection, null);
    }

    /**
     * Tworzy nowe okno formularza z podanym rodzicem, połączeniem z bazą danych i kontaktem.
     * Otwiera okno edycji kontaktu, przypisuje kontakt przekazany z tabeli do zmiennej contact
     * @param parent okno główne, które jest właścicielem tego okna
     * @param dbConnection połączenie z bazą danych używane przez okno formularza
     * @param contact edytowany kontakt
     */
    public FormWindow(MainWindow parent, DatabaseConnection dbConnection, Contact contact) {
        super(parent, contact == null ? "Add New main.java.Contact" : "Edit main.java.Contact", true);
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

    /**dodaje do panelu w formularzu TextField wraz z labele
     * @param formPanel panel z polami formularza
     * @param label tekst labela
     * @return metoda zwraca dodany textField*/
    private static JTextField addField(JPanel formPanel, String label) {
        formPanel.add(new JLabel(label));
        JTextField emailField = new JTextField();
        formPanel.add(emailField);
        return emailField;
    }

    /**sprawdza poprawność i wysyła zawartość formularza do bazy danych,
     * wyświetla komunikat o rezultacie zapisu danych w bazie danych i odświeża listę kontaktów w tabeli
     * @param firstName zawartość pola imię z formularza
     * @param lastName zawartość pola nazwisko z formularza
     * @param phone zawartość pola nmer telefonu z formularza
     * @param email zawartość pola adres e-mail z formularza
     * */
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
            JOptionPane.showMessageDialog(mainWindow, "main.java.Contact saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainWindow, "Failed to save contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        mainWindow.loadDataToContactTable();
    }

    /**waliduje formularz dodawania/edycji kontaktu
     * sprawdza, czy wypełnione są pole na imię oraz przynajmniej jedno z pól na numer telefonu lub adres e-mail
     * sprawdza, czy wprawodzone numer telefonu i adres e-mail są poprawne
     * metoda wyświetla okno z odpowiednim komunikatem, jeśli której z pól nie przejdzie walidacji
     * @param firstName zawartość pola z imieniem
     * @param phone zawartość pola z numerem telefonu
     * @param email zawartość pola z adresem e-mail
     * @return zwraca boolean w zależności, czy formularz przeszedł walidację czy nie*/
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
