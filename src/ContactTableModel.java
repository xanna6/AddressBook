import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class ContactTableModel extends AbstractTableModel {
    private final List<Contact> contacts;
    private final String[] columnNames = {"Name", "Phone", "Email"};

    public ContactTableModel() {
        contacts = new ArrayList<>();
        contacts.add(new Contact("John Doe", "123456789", "john@example.com"));
        contacts.add(new Contact("Jane Doe", "987654321", "jane@example.com"));
        contacts.add(new Contact("Jack Doe", "987654321", "jack@example.com"));
        contacts.add(new Contact("Jacob Doe", "987654321", "jacob@example.com"));
    }

    @Override
    public int getRowCount() {
        return contacts.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contact contact = contacts.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> contact.getName();
            case 1 -> contact.getPhone();
            case 2 -> contact.getEmail();
            default -> "";
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
