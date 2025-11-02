package studentsystem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Map;
import javax.swing.JPanel;

public class ChartPanel extends JPanel {

    // Dữ liệu thống kê (ví dụ: "Giỏi", 5)
    private Map<String, Integer> data;
    // Thứ tự các cột
    private final String[] RANKS = {"Yếu", "Trung bình", "Khá", "Giỏi", "Xuất sắc"};
    
    private int panelWidth = 750;
    private int panelHeight = 400;

    public ChartPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Tìm giá trị lớn nhất để chia tỷ lệ
        int maxCount = 0;
        for (String rank : RANKS) {
            int count = data.getOrDefault(rank, 0);
            if (count > maxCount) {
                maxCount = count;
            }
        }
        
        if (maxCount == 0) {
             g.setFont(new Font("Arial", Font.BOLD, 20));
             g.setColor(Color.BLACK);
             g.drawString("Không có dữ liệu để hiển thị biểu đồ.", 50, 150);
             return;
        }

        // 2. Định nghĩa các thông số
        int padding = 50;
        int labelPadding = 30;
        int chartWidth = panelWidth - 2 * padding;
        int chartHeight = panelHeight - 2 * padding - labelPadding;
        
        int barWidth = chartWidth / (RANKS.length * 2); // Khoảng cách giữa các cột
        int barGap = barWidth;

        // 3. Vẽ các cột
        for (int i = 0; i < RANKS.length; i++) {
            String rank = RANKS[i];
            int count = data.getOrDefault(rank, 0);
            
            // Tính toán chiều cao cột (tỷ lệ)
            int barHeight = (int) ((double) count / maxCount * chartHeight);
            
            // Tọa độ X, Y
            int x = padding + i * (barWidth + barGap) + (barGap / 2);
            int y = panelHeight - padding - labelPadding - barHeight;

            // Đặt màu cho cột
            if (rank.equals("Yếu")) g.setColor(Color.RED);
            else if (rank.equals("Trung bình")) g.setColor(Color.ORANGE);
            else if (rank.equals("Khá")) g.setColor(Color.BLUE);
            else if (rank.equals("Giỏi")) g.setColor(new Color(0, 153, 51)); // Green
            else g.setColor(Color.MAGENTA); // Xuất sắc

            // Vẽ cột
            g.fillRect(x, y, barWidth, barHeight);

            // 4. Vẽ nhãn (Label)
            g.setColor(Color.BLACK);
            FontMetrics fm = g.getFontMetrics();
            int labelWidth = fm.stringWidth(rank);
            
            // Nhãn X (Tên loại)
            g.drawString(rank, x + barWidth / 2 - labelWidth / 2, panelHeight - padding - labelPadding / 2 + 10);
            
            // Nhãn Y (Số lượng)
            String countStr = String.valueOf(count);
            int countLabelWidth = fm.stringWidth(countStr);
            g.drawString(countStr, x + barWidth / 2 - countLabelWidth / 2, y - 5);
        }
        
        // 5. Vẽ tiêu đề biểu đồ
        g.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics titleFm = g.getFontMetrics();
        String title = "Biểu đồ phân bố học lực sinh viên";
        int titleWidth = titleFm.stringWidth(title);
        g.drawString(title, panelWidth / 2 - titleWidth / 2, padding / 2 + 10);
    }
}

