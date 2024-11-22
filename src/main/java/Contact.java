package main.java;

/**
 *Klasa odzwierciedlająca tabelę contact z bazy danych
*/
public class Contact {
    /**id kontaktu */
    private int id;
    /**imię kontaktu */
    private String firstName;
    /**nazwisko kontaktu */
    private String lastName;
    /**numer telefonu kontaktu */
    private String phone;
    /**adres e-mail kontaktu */
    private String email;

    /**tworzy obiekt main.java.Contact na podstawie imienia, nazwiska, numeru telefonu i adresu e-mail
     * @param firstName imię kontaktu
     * @param lastName nazwisko kontaktu
     * @param phone numer telefonu kontaktu
     * @param email adres e-mail kontaktu
     * */
    public Contact(String firstName, String lastName, String phone, String email) {
        this(-1, firstName, lastName, phone, email);
    }

    /**tworzy obiekt main.java.Contact na podstawie id, imienia, nazwiska, numeru telefonu i adresu e-mail
     * @param id id kontaktu
     * @param firstName imię kontaktu
     * @param lastName nazwisko kontaktu
     * @param phone numer telefonu kontaktu
     * @param email adres e-mail kontaktu*/
    public Contact(int id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    /**metoda zwracająca id kontaktu
     * @return identyfikator obiektu jako liczba całkowita*/
    public int getId() {
        return id;
    }

    /**modyfikacja id kontaktu
     * @param id identyfikator kontaktu */
    public void setId(int id) {
        this.id = id;
    }

    /**metoda zwracająca imię kontaktu
     * @return imię kontaktu w postaci ciągu znaków */
    public String getFirstName() {
        return firstName;
    }

    /**modyfikacja imienia kontaktu
     * @param firstName imię kontaktu*/
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**metoda zwracająca nazwisko kontaktu
     * @return nazwisko kontaktu w postaci ciągu znaków*/
    public String getLastName() {
        return lastName;
    }

    /**modyfikacja nazwiska kontaktu
     * @param lastName nazwisko kontaktu*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**metoda zwracająca numer telefonu kontaktu
     * @return numer telefonu kontaktu w postaci ciągu znaków*/
    public String getPhone() {
        return phone;
    }

    /**modyfikacja numeru telefonu kontaktu
     * @param phone numer telefonu kontaktu*/
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**metoda zwracająca adres e-mail kontaktu
     * @return adres e-mail kontaktu w postaci ciągu znaków */
    public String getEmail() {
        return email;
    }

    /**modyfikacja adresu e-mail kontaktu
     * @param email adres e-mail kontaktu*/
    public void setEmail(String email) {
        this.email = email;
    }
}
