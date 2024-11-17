import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/address_book";
    private static final String USER = "root";
    private static final String PASSWORD = "Haslo123!";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT id, first_name, last_name, phone, email FROM contact";
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            contacts = mapResultSetToContactList(resultSet);
        } catch (SQLException e) {
            showErrorMessage("An error occurred while getting the contacts. Please try again.");
        }
        return contacts;
    }

    public List<Contact> getFilteredContacts(String searchText) {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT id, first_name, last_name, phone, email FROM contact " +
                "WHERE first_name LIKE ? OR last_name LIKE ? OR phone LIKE ? OR email LIKE ?";
        String searchPattern = "%" + searchText + "%";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                contacts = mapResultSetToContactList(resultSet);
            }

        } catch (SQLException e) {
            showErrorMessage("An error occurred while getting the contacts. Please try again.");
        }

        return contacts;
    }

    public boolean addContact(Contact contact) {
        String query = "INSERT INTO contact(first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPhone());
            statement.setString(4, contact.getEmail());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            showErrorMessage("An error occurred while adding contact. Please try again.");
            return false;
        }
    }

    public boolean editContact(Contact contact) {
        String query = "UPDATE contact SET first_name = ?, last_name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, contact.getFirstName());
            statement.setString(2, contact.getLastName());
            statement.setString(3, contact.getPhone());
            statement.setString(4, contact.getEmail());
            statement.setString(5, String.valueOf(contact.getId()));

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {
            showErrorMessage("An error occurred while editing contact. Please try again.");
            return false;
        }
    }

    public boolean deleteContact(int id) {
        boolean isDeleted = false;
        String query = "DELETE FROM contact WHERE id = " + id  + ";";
        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            int result = statement.executeUpdate(query);
            isDeleted = result > 0;

        } catch (SQLException e) {
            showErrorMessage("An error occurred while deleting contact. Please try again.");
        }
        return isDeleted;
    }

    private List<Contact> mapResultSetToContactList(ResultSet resultSet) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String phone = resultSet.getString("phone");
            String email = resultSet.getString("email");

            contacts.add(new Contact(id, firstName, lastName, phone, email));
        }
        return contacts;
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
