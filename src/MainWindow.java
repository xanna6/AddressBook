import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow  extends JFrame {
    private final JTable contactTable;
    private final DatabaseConnection dbConnection;
    private JTextField searchField;

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

    private void openFormWindow() {
        FormWindow formWindow = new FormWindow(this, dbConnection);
        formWindow.setVisible(true);
    }

    private void openFormWindow(Contact selectedContact) {
        FormWindow formWindow = new FormWindow(this, dbConnection, selectedContact);
        formWindow.setVisible(true);
    }

    private void deleteContact(int id) {
        boolean isDeleted = dbConnection.deleteContact(id);
        if (isDeleted) {
            JOptionPane.showMessageDialog(this, "Contact deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to delete contact.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        loadDataToContactTable();
    }

    private void loadFilteredDataToContactTable(String searchText) {
        List<Contact> filteredContacts = dbConnection.getFilteredContacts(searchText);
        ContactTableModel model = new ContactTableModel(filteredContacts);

        contactTable.setModel(model);

    }

    void loadDataToContactTable() {
        List<Contact> contacts = dbConnection.getContacts();
        ContactTableModel model = new ContactTableModel(contacts);

        contactTable.setModel(model);
        searchField.setText("");
    }
}