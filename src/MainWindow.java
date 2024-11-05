import javax.swing.*;
import java.awt.*;

public class MainWindow  extends JFrame {

    public MainWindow() {
        setTitle("AddressBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ContactTableModel tableModel = new ContactTableModel();
        JTable table = new JTable(tableModel);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Contact List", JLabel.CENTER);
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel);

        pack();
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}