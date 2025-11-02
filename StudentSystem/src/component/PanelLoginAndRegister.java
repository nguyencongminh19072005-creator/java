package component;

import swing.MyTextField;
import swing.MyPasswordField;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import studentsystem.SF;

public class PanelLoginAndRegister extends javax.swing.JLayeredPane {

    // TextField cho Register
    private MyTextField txtUser;
    private MyTextField txtEmailRegister;
    private MyTextField txtPasswordRegister;

    // TextField cho Login
    private MyTextField txtEmailLogin;
    private MyPasswordField txtPassLogin;

    public PanelLoginAndRegister() {
        initComponents();
        initRegister();
        initLogin();
        showRegister(true); // mặc định hiện register
    }

    // REGISTER 
    private void initRegister() {
        register.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Tạo tài khoản");
        label.setFont(new Font("sansserif", Font.BOLD, 30));
        label.setForeground(new Color(186, 85, 211));
        register.add(label);

        txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/user.png")));
        txtUser.setHint("Name");
        register.add(txtUser, "w 60%");

        txtEmailRegister = new MyTextField();
        txtEmailRegister.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/mail.png")));
        txtEmailRegister.setHint("Email");
        register.add(txtEmailRegister, "w 60%");

        txtPasswordRegister = new MyTextField();
        txtPasswordRegister.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/pass.png")));
        txtPasswordRegister.setHint("Password");
        register.add(txtPasswordRegister, "w 60%");

        JButton btnRegister = new JButton("Đăng Ký") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRegister.setBackground(new Color(237, 2, 157));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setOpaque(false);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegister.addActionListener(e -> registerUser());

        register.add(btnRegister, "w 150!, h 50!");
    }

    private void registerUser() {
        String username = txtUser.getText();
        String password = txtPasswordRegister.getText();
        String email = txtEmailRegister.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO users(username, password, email) VALUES(?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password); 
            ps.setString(3, email);
            int result = ps.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                // Clear field
                txtUser.setText("");
                txtEmailRegister.setText("");
                txtPasswordRegister.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Đăng ký thất bại!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void initLogin() {
        login.setLayout(new MigLayout("wrap", "push[center]push", "push[]25[]10[]10[]25[]push"));
        JLabel label = new JLabel("Đăng nhập");
        label.setFont(new Font("sansserif", Font.BOLD, 30));
        label.setForeground(new Color(186, 85, 211));
        login.add(label);

        txtEmailLogin = new MyTextField();
        txtEmailLogin.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/mail.png")));
        txtEmailLogin.setHint("Email");
        login.add(txtEmailLogin, "w 60%");

        txtPassLogin = new MyPasswordField();
        txtPassLogin.setPrefixIcon(new ImageIcon(getClass().getResource("/icon/pass.png")));
        txtPassLogin.setHint("Password");
        login.add(txtPassLogin, "w 60%");

        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("sansserif", Font.BOLD, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        login.add(cmdForget, "wrap");

        JButton cmdLogin = new JButton("Đăng nhập") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        cmdLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        cmdLogin.setBackground(new Color(237, 2, 157));
        cmdLogin.setForeground(Color.WHITE);
        cmdLogin.setFocusPainted(false);
        cmdLogin.setBorderPainted(false);
        cmdLogin.setOpaque(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cmdLogin.addActionListener(e -> loginUser());

        login.add(cmdLogin, "w 150!, h 50!");
    }

    private void loginUser() {
        String email = txtEmailLogin.getText();
        String password = txtPassLogin.getText();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

                // Mở SF.java (giả sử SF là JFrame)
                SF sf = new SF();
                sf.setVisible(true);

                // Đóng JFrame hiện tại chứa PanelLoginAndRegister
                SwingUtilities.getWindowAncestor(this).dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Email hoặc mật khẩu không đúng!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new javax.swing.JPanel();
        register = new javax.swing.JPanel();

        setLayout(new java.awt.CardLayout());

        login.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
            loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        register.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
            registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables

}
