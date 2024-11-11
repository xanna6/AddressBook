import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow  extends JFrame {
    private final JTable contactTable;
    private final DatabaseConnection dbConnection;

    public MainWindow() {
        setTitle("AddressBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Contact List", JLabel.CENTER);
        add(label, BorderLayout.NORTH);

        JPanel searchPanel = getSearchPanel();

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        searchContainer.add(searchPanel);

        contactTable = new JTable();
        dbConnection = new DatabaseConnection();
        loadDataToContactTable();
        JScrollPane tableScrollPane = new JScrollPane(contactTable);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(searchContainer);
        mainPanel.add(tableScrollPane);

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel getSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10,0));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setToolTipText("Search");
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            loadFilteredDataToContactTable(searchText);
        });
        JButton openFormButton = new JButton("Add contact");
        openFormButton.addActionListener(e -> openFormWindow());

        searchPanel.add(openFormButton, BorderLayout.EAST);
        searchPanel.add(searchField, BorderLayout.WEST);
        searchPanel.add(searchButton, BorderLayout.CENTER);
        return searchPanel;
    }

    private void openFormWindow() {
        FormWindow formWindow = new FormWindow(this, dbConnection);
        formWindow.setVisible(true);
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
    }
}