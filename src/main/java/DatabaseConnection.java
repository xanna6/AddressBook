package main.java;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *Klasa odpowiedzialna za połączenie z bazą danych i wykonywanie operacji w bazie danych
 */
public class DatabaseConnection {
    /**adres url bazy danych*/
    private static final String URL = "jdbc:mysql://localhost:3306/address_book";
    /**login użytkownika łączącego się z bazą danych*/
    private static final String USER = "root";
    /**hasło użytkownika łączącego się z bazą danych*/
    private static final String PASSWORD = "Haslo123!";

    /**tworzy i zwraca połączenie z bazą danych
     * @return połączenie z bazą danych w postaci obiektu Connection
     * @throws SQLException jeśli wystąpi błąd podczas tworzenia połączenia z bazą danych*/
    public Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**wysyła do bazy danych zapytanie SELECT o wszystkie kontakty
     *@return metoda zwraca listę wszystkich kontaktów
     */
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

    /**wysyła do bazy danych zapytanie SELECT o wszystkie kontakty
     * @param searchText tekst do filtrowania listy kontaktów
     *@return metoda zwraca listę przefiltrowanych kontaktów
     */
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

    /**wysyła do bazy danych INSERT z nowym kontaktem
     * @param contact obiekt klasy Contact
     *@return metoda zwraca boolean informujący o powodzeniu zapisu obiektu do bazy
     */
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

    /**wysyła do bazy danych UPDATE z nowymi wartościami pól kontaktu
     * @param contact obiekt klasy Contact
     *@return metoda zwraca boolean informujący o powodzeniu edycji obiektu w bazie
     */
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

    /**wysyła do bazy danych DELETE kontaktu
     * @param id id kontaktu
     *@return metoda zwraca boolean informujący o powodzeniu operacji usunięcia rekordu z bazy
     */
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

    /**
     * Mapuje wynik zapytania SQL reprezentowany przez {@link ResultSet} na listę obiektów {@link Contact}.
     * Metoda iteruje przez wszystkie wiersze wyniku i dla każdego wiersza tworzy
     * nowy obiekt {@link Contact}, wypełniając jego pola wartościami z odpowiednich kolumn.
     *
     * @param resultSet zestaw wyników zapytania SQL, który ma być przekształcony na listę kontaktów
     * @return lista obiektów {@link Contact} odwzorowanych z wierszy {@link ResultSet}
     * @throws SQLException jeśli wystąpi błąd podczas przetwarzania danych w {@link ResultSet},
     *                      np. brak wymaganych kolumn, niezgodność typów lub problem z połączeniem do bazy danych
     */
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

    /**wyświetla okno z wiadomością o błędzie
     * @param message komunikat o błędzie
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
