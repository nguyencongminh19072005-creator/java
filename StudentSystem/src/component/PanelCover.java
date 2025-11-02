package component;

import swing.ButtonOutLine;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import swing.ButtonOutLine;

public class PanelCover extends javax.swing.JPanel {

    private final DecimalFormat df = new DecimalFormat("##0.###", DecimalFormatSymbols.getInstance(Locale.US));
    private ActionListener event;
    private MigLayout layout;
    private JLabel title;
    private JLabel description;
    private JLabel description1;
    private ButtonOutLine button;
    private boolean isLogin;

    public PanelCover() {
        initComponents();
        setOpaque(false);
        layout = new MigLayout("wrap, fill", "[center]", "push[]25[]10[]25[]push");
        setLayout(layout);
        init();
    }

    private void init() {
    // Tiêu đề
    title = new JLabel("<html><center>Welcome Back, Teacher!</center></html>");
    title.setFont(new Font("sansserif", Font.BOLD, 28)); // giảm chút để vừa panel
    title.setForeground(Color.WHITE);
    title.setHorizontalAlignment(JLabel.CENTER);
    add(title, "growx"); // cho title mở rộng theo chiều ngang

    // Mô tả 1
    description = new JLabel("<html><center>Access your class schedules and student records</center></html>");
    description.setFont(new Font("sansserif", Font.PLAIN, 16));
    description.setForeground(Color.WHITE);
    description.setHorizontalAlignment(JLabel.CENTER);
    add(description, "growx");

    // Mô tả 2
    description1 = new JLabel("<html><center>Enter your account information to continue</center></html>");
    description1.setFont(new Font("sansserif", Font.PLAIN, 16));
    description1.setForeground(Color.WHITE);
    description1.setHorizontalAlignment(JLabel.CENTER);
    add(description1, "growx");

    // Nút SIGN IN
    button = new ButtonOutLine();
    button.setText("SIGN IN");
    button.setFont(new Font("sansserif", Font.BOLD, 16));
    button.setBackground(new Color(0, 0, 0, 0)); // trong suốt để viền nổi bật
    button.setForeground(Color.WHITE); // chữ trắng
    button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // viền trắng
    button.setFocusPainted(false);
    button.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            event.actionPerformed(ae); // gọi sự kiện
        }
    });
    add(button, "w 60%, h 40, align center"); // căn giữa
}


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
        GradientPaint gra = new GradientPaint(0, 0, new Color(131, 58, 180), 0, getHeight(), new Color(253, 29, 29));
        g2.setPaint(gra);
        g2.fillRect(0, 0, getWidth(), getHeight());

    }

    public void addEvent(ActionListener event) {
        this.event = event;

    }

    public void registerLeft(double v) {
        v = Double.valueOf(df.format(v));
        login(false);
        layout.setComponentConstraints(title, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description1, "pad 0 -" + v + "% 0 0");
    }

    public void registerRight(double v) {
        v = Double.valueOf(df.format(v));
        login(false);
        layout.setComponentConstraints(title, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description1, "pad 0 -" + v + "% 0 0");
    }

    public void loginLeft(double v) {
        v = Double.valueOf(df.format(v));
        login(true);
        layout.setComponentConstraints(title, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description1, "pad 0 " + v + "% 0 " + v + "%");
    }

    public void loginRight(double v) {
        v = Double.valueOf(df.format(v));
        login(true);
        layout.setComponentConstraints(title, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description1, "pad 0 " + v + "% 0 " + v + "%");
    }

   public void login(boolean login) {
    if (this.isLogin != login) {
        if (login) { // trạng thái đăng nhập
            title.setText("<html><center>Welcome Back, Teacher!</center></html>");
            description.setText("<html><center>Access your class schedules and student records</center></html>");
            description1.setText("<html><center>Enter your account information to continue</center></html>");
            button.setText("SIGN IN");
        } else { // trạng thái đăng ký
            title.setText("<html><center>Join Our Teacher System!</center></html>");
            description.setText("<html><center>Create your teacher account to manage</center></html>");
            description1.setText("<html><center>classes, courses, and student progress</center></html>");
            button.setText("SIGN UP");
        }
        this.isLogin = login;
    }
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
