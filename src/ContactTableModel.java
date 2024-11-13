import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class ContactTableModel extends AbstractTableModel {
    private final List<Contact> contacts;
    private final String[] columnNames = {"First Name", "Last Name", "Phone", "Email"};

    public ContactTableModel(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
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
            case 0 -> contact.getFirstName();
            case 1 -> contact.getLastName();
            case 2 -> contact.getPhone();
            case 3 -> contact.getEmail();
            default -> "";
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Contact getContactAt(int row) {
        return contacts.get(row);
    }
}
