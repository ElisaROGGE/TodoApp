package src;

import javax.swing.SwingUtilities;

public class TodoApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}