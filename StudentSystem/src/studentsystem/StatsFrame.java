package studentsystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class StatsFrame extends JFrame {

    private List<Student> studentList;
    private JPanel chartPanel;
    private JPanel infoPanel;

    public StatsFrame(List<Student> studentList) {
        this.studentList = studentList;
        
        setTitle("Cửa sổ Thống kê Sinh viên");
        setSize(800, 600);
        setLocationRelativeTo(null); // Mở cửa sổ ở giữa màn hình
        
        // Đặt layout chính
        setLayout(new BorderLayout());
        
        // Xử lý dữ liệu
        Map<String, Integer> rankCounts = processData();
        
        // 1. Panel Biểu đồ
        chartPanel = new ChartPanel(rankCounts);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(Color.GRAY)
        ));
        
        // 2. Panel Thông tin
        infoPanel = new JPanel();
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String infoText = createInfoText(rankCounts);
        JLabel infoLabel = new JLabel(infoText);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        infoPanel.add(infoLabel);

        // Thêm vào Frame
        add(chartPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Quan trọng: Chỉ đóng cửa sổ này, không đóng toàn bộ ứng dụng
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    }
    
    // Đếm số lượng sinh viên theo từng loại
    private Map<String, Integer> processData() {
        Map<String, Integer> counts = new HashMap<>();
        // Khởi tạo map
        counts.put("Yếu", 0);
        counts.put("Trung bình", 0);
        counts.put("Khá", 0);
        counts.put("Giỏi", 0);
        counts.put("Xuất sắc", 0);
        
        for (Student s : studentList) {
            // *** ĐÃ SỬA: Dùng getXepLoai() ***
            String rank = s.getXepLoai(); 
            counts.put(rank, counts.get(rank) + 1);
        }
        return counts;
    }
    
    private String createInfoText(Map<String, Integer> counts) {
        int total = studentList.size();
        double classAverage = 0;
        
        if (total > 0) {
            double totalScore = 0;
            for(Student s : studentList) {
                totalScore += s.getTongDiemAsDouble(); 
            }
            classAverage = totalScore / total;
        }

        return String.format("<html>Tổng số sinh viên: %d | Điểm trung bình toàn lớp: %.2f<br>"
                + "Xuất sắc: %d | Giỏi: %d | Khá: %d | Trung bình: %d | Yếu: %d</html>",
                total, classAverage,
                counts.get("Xuất sắc"), counts.get("Giỏi"), counts.get("Khá"),
                counts.get("Trung bình"), counts.get("Yếu"));
    }
}

