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

        String query = "SELECT first_name, last_name, phone, email FROM contact";
        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");

                contacts.add(new Contact(firstName, lastName, phone, email));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}
