import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainWindow  extends JFrame {
    private final JTable contactTable;
    private final DatabaseConnection dbConnection;

    public MainWindow() {
        setTitle("AddressBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contactTable = new JTable();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel label = new JLabel("Contact List", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);

        dbConnection = new DatabaseConnection();
        loadDataToContactTable();
        panel.add(new JScrollPane(contactTable), BorderLayout.CENTER);
        add(panel);

        pack();
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadDataToContactTable() {
        List<Contact> contacts = dbConnection.getContacts();
        ContactTableModel model = new ContactTableModel(contacts);

        contactTable.setModel(model);
    }
}