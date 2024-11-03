import controller.Controller;
import view.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                View view = new View();
                Controller controller = new Controller(view);
            } catch (Exception e) {
                e.printStackTrace(); // Maneja la excepción de manera adecuada
                JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación: " + e.getMessage());
            }
        });
    }
}
