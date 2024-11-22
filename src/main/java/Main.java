package main.java;

import javax.swing.*;
/**
 *Klasa uruchamiająca aplikację
 *@author Anna Piotrowska
 *@version 1.0
 */
public class Main {

    /**metoda główna klasy main.java.Main
     *metoda odpowiada za wyświetlenie głównego okna aplikacji
     *@param args tablica argumentów wejściowych
     */
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        new MainWindow();
    }
}