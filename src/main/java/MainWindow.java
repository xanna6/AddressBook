package main.java;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**Klasa budująca główne okno aplikacji*/
public class MainWindow  extends JFrame {
    /**tabela z kontaktami*/
    private final JTable contactTable;
    /**połączenie z bazą danych*/
    private final DatabaseConnection dbConnection;
    /**JTextField do wpisywania tekstu do wyszukiwania kontaktów*/
    private JTextField searchField;

    /**konstruktor klasy MainWindow
     * buduje i wyświetla główne okno aplikacji*/
    public MainWindow() {
        setTitle("AddressBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(600, 400);
        setLocationRelativeTo(null);

        JLabel label = new JLabel("Contact List", JLabel.CENTER);
        add(label, BorderLayout.NORTH);

        JPanel searchPanel = getSearchPanel();

        contactTable = new JTable();
        dbConnection = new DatabaseConnection();
        JScrollPane tableScrollPane = new JScrollPane(contactTable);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(searchPanel);
        mainPanel.add(tableScrollPane);

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = getButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        contactTable.getSelectionModel().addListSelectionListener(e -> buttonPanel.setVisible(contactTable.getSelectedRow() != -1));

        setVisible(true);
        loadDataToContactTable();
    }

    /**buduje panel zawierający przyciski do edytowania i usuwania kontaktów z tabeli
     *@return zwraca JPanel z dwoma przyciskami */
    private JPanel getButtonPanel() {
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            int selectedRow = contactTable.getSelectedRow();
            Contact selectedContact = ((ContactTableModel) contactTable.getModel()).getContactAt(selectedRow);
            openFormWindow(selectedContact);
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedRow = contactTable.getSelectedRow();
            Contact selectedContact = ((ContactTableModel) contactTable.getModel()).getContactAt(selectedRow);
            deleteContact(selectedContact.getId());
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.setVisible(false);
        return buttonPanel;
    }

    /**buduje panel zawierający input i przycisk do wyszukiwania
     * oraz przyciski do wyświetlania listy kontaktów i dodawania nowego kontaktu
     *@return zwraca JPanel */
    private JPanel getSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,15));
        this.searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setToolTipText("Search");
        searchButton.addActionListener(e -> {
            String searchText = this.searchField.getText();
            loadFilteredDataToContactTable(searchText);
        });
        JButton showAllButton = new JButton("Show all");
        showAllButton.addActionListener(e -> loadDataToContactTable());
        JButton openFormButton = new JButton("Add contact");
        openFormButton.addActionListener(e -> openFormWindow());

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        searchPanel.add(openFormButton);
        return searchPanel;
    }

    /**otwiera okno z formularzem dodawania kontaktu */
    private void openFormWindow() {
        FormWindow formWindow = new FormWindow(this, dbConnection);
        formWindow.setVisible(true);
    }

    /**otwiera okno z formularzem edycji kontaktu
     *@param selectedContact kontakt do edycji */
    private void openFormWindow(Contact selectedContact) {
        FormWindow formWindow = new FormWindow(this, dbConnection, selectedContact);
        formWindow.setVisible(true);
    }

    /**usuwa zaznaczony kontakt z bazy danych i wyświetla okno z odpowiednim komunikatem w zależności od rezultatu operacji
     * @param id id usuwanego kontaktu */
    private void deleteContact(int id) {
        boolean isDeleted = dbConnection.deleteContact(id);
        if (isDeleted) {
            JOptionPane.showMessageDialog(this, "Contact deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        loadDataToContactTable();
    }

    /**pobiera listę kontaktów przefiltrowanych na podstawie zawartości textFieldu i przekazuje ją do modelu tabeli
     * @param searchText zawartość JTextFieldu do wyszukiwania kontaktów*/
    private void loadFilteredDataToContactTable(String searchText) {
        List<Contact> filteredContacts = dbConnection.getFilteredContacts(searchText);
        ContactTableModel model = new ContactTableModel(filteredContacts);

        contactTable.setModel(model);

    }

    /**pobiera listę wszystkich kontaktów i przekazuje ją do modelu tabeli*/
    void loadDataToContactTable() {
        List<Contact> contacts = dbConnection.getContacts();
        ContactTableModel model = new ContactTableModel(contacts);

        contactTable.setModel(model);
        searchField.setText("");
    }
}