package studentsystem;

import javax.swing.UIManager;

public class MainStart {

    public static void main(String[] args) {
        // Thiết lập Look and Feel của hệ thống
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Main.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true); // Main chứa PanelLoginAndRegister
        });
    }
}
