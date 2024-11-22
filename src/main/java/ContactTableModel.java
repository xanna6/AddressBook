package main.java;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**klasa modelu tabeli do przechowywania kontaktów
 */
class ContactTableModel extends AbstractTableModel {
    /**lista kontaktów*/
    private final List<Contact> contacts;
    /**tablica nazw kolumn tabeli*/
    private final String[] columnNames = {"First Name", "Last Name", "Phone", "Email"};

    /**tworzy obiekt ContactTableModel
     * @param  contacts lista kontaktów*/
    public ContactTableModel(List<Contact> contacts) {
        this.contacts = new ArrayList<>(contacts);
    }

    /**zwraca ilość wierszy w tabeli
     *@return ilość wierszy w tabeli*/
    @Override
    public int getRowCount() {
        return contacts.size();
    }

    /**zwraca ilość kolumn w tabeli
     *@return ilość kolumn w tabeli*/
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**zwraca zawartość danej komórki tabeli
     * @param rowIndex numer wiersza
     * @param columnIndex numer kolumny
     *@return obiekt klasy Object będący zawartością komórki tabeli*/
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contact contact = contacts.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return contact.getFirstName();
            case 1:
                return contact.getLastName();
            case 2:
                return contact.getPhone();
            case 3:
                return contact.getEmail();
            default:
                return "";
        }
    }

    /**zwraca nazwę danej kolumny tabeli
     * @param column numer kolumny
     *@return tekst będący nazwą kolumny tabeli*/
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**zwraca kontakt z danego wiersza tabeli
     * @param row numer wiersza
     *@return obiekt klasy Contact*/
    public Contact getContactAt(int row) {
        return contacts.get(row);
    }
}
