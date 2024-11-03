import javax.swing.*;

public class MainWindow  extends JFrame {

    public MainWindow() {
        setTitle("AddressBook");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("AddressBook", JLabel.CENTER);
        add(label);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}