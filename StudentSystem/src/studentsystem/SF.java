package studentsystem;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.text.Collator;
import java.util.Locale;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SF extends javax.swing.JFrame {

    // ========================================
    // KHAI BÁO BIẾN VÀ COMPONENTS
    // ========================================
    private javax.swing.JComboBox<String> cbSort;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6;
    private javax.swing.JLabel jLabel7, jLabel8, jLabel9, jLabel10, jLabel11;
    private javax.swing.JPanel jPanel1, jPanel2, jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblStudent;
    private javax.swing.JTextField txtAge, txtID, txtName, txtSearchName;
    private javax.swing.JTextField txtDiemCC, txtDiemGK, txtDiemCK;
    private javax.swing.JButton btnStats, btnClear;
    private javax.swing.JButton btnBulkAdd;
    private javax.swing.JComboBox<String> cbFilter;
    private javax.swing.JLabel lblTotal, lblAverage, lblStatus;
    private javax.swing.JCheckBox chkAutoSave;
    private javax.swing.JTextField txtQuickFilter;

    List<Student> list = new ArrayList<Student>();
    static int pos = 0;
    Student x;
    static int check = 0;
    JPanel panel;
    private final Collator vietnameseCollator;
    private Timer autoSaveTimer;
    private boolean hasUnsavedChanges = false;
    private Student comparisonStudent = null;

    // ========================================
    // CONSTRUCTOR
    // ========================================
    public SF() {
        vietnameseCollator = Collator.getInstance(new Locale("vi", "VN"));
        vietnameseCollator.setStrength(Collator.PRIMARY);

        initComponents();
        this.setSize(1400, 800);
        this.setLocationRelativeTo(null);
        this.setResizable(true);

        this.jPanel1.setBackground(new Color(240, 240, 240));
        this.jPanel2.setBackground(Color.WHITE);
        this.jPanel3.setBackground(Color.WHITE);

        loadList();

        if (!list.isEmpty()) {
            list.sort(new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    String[] key1 = getSortKey(s1.getName());
                    String[] key2 = getSortKey(s2.getName());
                    int result = vietnameseCollator.compare(key1[0], key2[0]);
                    if (result != 0) {
                        return result;
                    }
                    result = vietnameseCollator.compare(key1[1], key2[1]);
                    if (result != 0) {
                        return result;
                    }
                    return vietnameseCollator.compare(key1[2], key2[2]);
                }
            });
        }

        View();
        ViewTable(this.txtSearchName.getText());
        updateStatistics();

        autoSaveTimer = new Timer(30000, e -> {
            if (chkAutoSave.isSelected() && hasUnsavedChanges) {
                autoSave();
            }
        });
        autoSaveTimer.start();

        setupQuickFilter();
    }
    private void SaveFile() {
    try {
        FileWriter fw = new FileWriter("students.txt");
        for (Student s : list) {
            fw.write(s.getId() + "," + s.getName() + "," + s.getAge() + ","
                    + s.getDiemCC() + "," + s.getDiemGK() + "," + s.getDiemCK() + "\n");
        }
        fw.close();
        System.out.println("Đã lưu vào file students.txt");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // ========================================
    // [TÍNH NĂNG 1] LOAD DỮ LIỆU MẪU
    public void loadList() {
    list = new ArrayList<>();
    try {
        File file = new File("students.txt");
        if (!file.exists()) {
            // Nếu chưa có file, tạo 1 vài dữ liệu mẫu như ban đầu
            list.add(new Student("1111", "Nguyễn Văn Sơn", 23, 10, 8, 7.5));
            list.add(new Student("2222", "Trần Thị Mai", 20, 9, 9, 9));
            list.add(new Student("3333", "Lê Thảo Mai", 21, 8.5, 7, 8));
            list.add(new Student("4444", "Nguyễn Thị Ánh", 22, 10, 10, 10));
            list.add(new Student("5555", "Phạm Minh Tuấn", 24, 7, 6, 7));
            list.add(new Student("6666", "Hoàng Thị Lan", 19, 8, 8.5, 9));
            SaveFile(); // Lưu lại để tạo file lần đầu
            return;
        }

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length == 6) {
                String id = parts[0].trim();
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                double cc = Double.parseDouble(parts[3].trim());
                double gk = Double.parseDouble(parts[4].trim());
                double ck = Double.parseDouble(parts[5].trim());
                list.add(new Student(id, name, age, cc, gk, ck));
            }
        }
        br.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // ========================================
    // [TÍNH NĂNG 2] TỰ ĐỘNG LƯU
    // ========================================
    private void autoSave() {
        SaveFile();
        updateStatus("Đã tự động lưu...");
        hasUnsavedChanges = false;
        Timer statusTimer = new Timer(2000, e -> updateStatus(""));
        statusTimer.setRepeats(false);
        statusTimer.start();
    }

    private void markAsChanged() {
        hasUnsavedChanges = true;
    }

    // ========================================
    // [TÍNH NĂNG 4] THÊM HÀNG LOẠT
    // ========================================
    private void bulkAddStudents() {
        JTextArea textArea = new JTextArea(10, 40); // 10 dòng, 40 cột
        JScrollPane scrollPane = new JScrollPane(textArea);

        int result = JOptionPane.showConfirmDialog(
                this,
                scrollPane,
                "Nhập thông tin sinh viên (mỗi dòng 1 sinh viên):\n"
                + "Định dạng: Mã SV, Họ tên, Tuổi, Điểm CC, Điểm GK, Điểm CK\n"
                + "Ví dụ: 7777, Nguyễn Văn A, 20, 8, 8.5, 9",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String input = textArea.getText().trim();
        if (input.isEmpty()) {
            return;
        }

        String[] lines = input.split("\n");
        int successCount = 0;
        int errorCount = 0;
        StringBuilder errors = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) {
                continue;
            }

            try {
                String[] parts = line.split(",");
                if (parts.length != 6) {
                    errors.append("Dòng ").append(i + 1).append(": Thiếu thông tin\n");
                    errorCount++;
                    continue;
                }

                String id = parts[0].trim();
                String name = parts[1].trim();
                int age = Integer.parseInt(parts[2].trim());
                double diemCC = Double.parseDouble(parts[3].trim());
                double diemGK = Double.parseDouble(parts[4].trim());
                double diemCK = Double.parseDouble(parts[5].trim());

                if (Search(id) != null) {
                    errors.append("Dòng ").append(i + 1).append(": Mã SV đã tồn tại\n");
                    errorCount++;
                    continue;
                }

                if (age < 18 || age > 100) {
                    errors.append("Dòng ").append(i + 1).append(": Tuổi không hợp lệ\n");
                    errorCount++;
                    continue;
                }

                if (diemCC < 0 || diemCC > 10 || diemGK < 0 || diemGK > 10 || diemCK < 0 || diemCK > 10) {
                    errors.append("Dòng ").append(i + 1).append(": Điểm không hợp lệ\n");
                    errorCount++;
                    continue;
                }

                list.add(new Student(id, name, age, diemCC, diemGK, diemCK));
                successCount++;

            } catch (Exception e) {
                errors.append("Dòng ").append(i + 1).append(": Lỗi định dạng\n");
                errorCount++;
            }
        }

        ViewTable(txtSearchName.getText());
        markAsChanged();
        SaveFile();
        String message = String.format("Đã thêm: %d sinh viên\nLỗi: %d dòng", successCount, errorCount);
        if (errorCount > 0) {
            message += "\n\nChi tiết lỗi:\n" + errors.toString();
        }

        JOptionPane.showMessageDialog(this, message, "Kết quả",
                errorCount == 0 ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    // ========================================
    // [TÍNH NĂNG 5] CHỌN NGẪU NHIÊN
    // ========================================
    private void pickRandomStudent() {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên nào!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int randomIndex = (int) (Math.random() * list.size());
        pos = randomIndex;
        View();

        Student s = list.get(randomIndex);
        JOptionPane.showMessageDialog(this,
                String.format("🎲 Sinh viên được chọn:\n\n"
                        + "Mã SV: %s\n"
                        + "Họ tên: %s\n"
                        + "Điểm: %.2f (%s)",
                        s.getId(), s.getName(), s.getTongDiemAsDouble(), s.getXepLoai()),
                "Chọn ngẫu nhiên", JOptionPane.INFORMATION_MESSAGE);

        for (int i = 0; i < tblStudent.getRowCount(); i++) {
            if (tblStudent.getValueAt(i, 1).equals(s.getId())) {
                tblStudent.setRowSelectionInterval(i, i);
                tblStudent.scrollRectToVisible(tblStudent.getCellRect(i, 0, true));
                break;
            }
        }
    }

    // ========================================
    // [TÍNH NĂNG 6] SAO CHÉP SINH VIÊN
    // ========================================
    private void duplicateStudent() {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên để sao chép!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Student current = list.get(pos);
        String newId = JOptionPane.showInputDialog(this,
                "Nhập mã SV mới cho bản sao:", current.getId() + "_copy");

        if (newId == null || newId.trim().isEmpty()) {
            return;
        }

        if (Search(newId) != null) {
            JOptionPane.showMessageDialog(this, "Mã SV đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        list.add(new Student(newId, current.getName(), current.getAge(),
                current.getDiemCC(), current.getDiemGK(), current.getDiemCK()));
        ViewTable(txtSearchName.getText());
        markAsChanged();
        SaveFile();
        updateStatus("Đã sao chép sinh viên");
    }

    // ========================================
    // [TÍNH NĂNG 7] LỌC NHANH
    // ========================================
    private void setupQuickFilter() {
    txtQuickFilter.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String text = txtQuickFilter.getText().trim();
            if(text.isEmpty()) {
                ViewTable(""); // Hiển thị tất cả nếu ô trống
                return;
            }

            try {
                double diemCanLoc = Double.parseDouble(text);
                ViewTableByDiem(diemCanLoc);
            } catch (NumberFormatException ex) {
                ViewTable(""); // Nếu không phải số → hiển thị tất cả
            }
        }
    });
}

private void ViewTableByDiem(double diem) {
    DefaultTableModel model = (DefaultTableModel) tblStudent.getModel();
    model.setRowCount(0); // Xóa bảng cũ

    for(Student s : list) { // list sinh viên
        if(s.getTongDiemAsDouble() == diem) { // lọc theo tổng điểm
            model.addRow(new Object[] {
                s.getId(),
                s.getName(),
                s.getAge(),
                s.getDiemCC(),
                s.getDiemGK(),
                s.getDiemCK(),
                s.getTongDiem(),
                s.getXepLoai()
            });
        }
    }
}



    // ========================================
    // [TÍNH NĂNG 8] CẬP NHẬT TRẠNG THÁI
    // ========================================
    private void updateStatus(String message) {
        lblStatus.setText(message);
        if (!message.isEmpty()) {
            Timer timer = new Timer(3000, e -> lblStatus.setText(""));
            timer.setRepeats(false);
            timer.start();
        }
    }

    // ========================================
    // [TÍNH NĂNG 9] SẮP XẾP TIẾNG VIỆT
    // ========================================
    private String[] getSortKey(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return new String[]{"", "", ""};
        }
        String[] parts = fullName.trim().split("\\s+");
        int n = parts.length;
        String lastName = "", middleName = "", firstName = "";
        if (n > 0) {
            firstName = parts[n - 1];
        }
        if (n > 1) {
            lastName = parts[0];
        }
        if (n > 2) {
            StringBuilder middle = new StringBuilder();
            for (int i = 1; i < n - 1; i++) {
                middle.append(parts[i]).append(" ");
            }
            middleName = middle.toString().trim();
        }
        return new String[]{firstName, lastName, middleName};
    }

    // ========================================
    // [TÍNH NĂNG 10] HIỂN THỊ CHI TIẾT
    // ========================================
    public void View() {
        if (list.isEmpty()) {
            this.txtID.setText("");
            this.txtName.setText("");
            this.txtAge.setText("");
            this.txtDiemCC.setText("");
            this.txtDiemGK.setText("");
            this.txtDiemCK.setText("");
            OnOff(true, false);
            return;
        }

        if (pos < 0 || pos >= list.size()) {
            if (!list.isEmpty()) {
                pos = 0;
            } else {
                return;
            }
        }

        x = list.get(pos);
        this.txtID.setText(x.getId());
        this.txtName.setText(x.getName());
        this.txtAge.setText("" + x.getAge());
        this.txtDiemCC.setText("" + x.getDiemCC());
        this.txtDiemGK.setText("" + x.getDiemGK());
        this.txtDiemCK.setText("" + x.getDiemCK());
        OnOff(true, false);

        this.txtID.setEditable(false);
        this.txtName.setEditable(false);
        this.txtAge.setEditable(false);
        this.txtDiemCC.setEditable(false);
        this.txtDiemGK.setEditable(false);
        this.txtDiemCK.setEditable(false);
    }

    public void OnOff(boolean a, boolean b) {
        this.btnSave.setVisible(b);
        this.btnCancel.setVisible(b);
        this.btnAdd.setVisible(a);
        this.btnEdit.setVisible(a);
        this.btnDelete.setVisible(a);
        this.txtID.setEditable(b);
        this.txtName.setEditable(b);
        this.txtAge.setEditable(b);
        this.txtDiemCC.setEditable(b);
        this.txtDiemGK.setEditable(b);
        this.txtDiemCK.setEditable(b);
    }

    // ========================================
    // [TÍNH NĂNG 11] THỐNG KÊ TỰ ĐỘNG
    // ========================================
    private void updateStatistics() {
        if (list.isEmpty()) {
            lblTotal.setText("Tổng: 0 sinh viên");
            lblAverage.setText("Điểm TB: N/A");
            return;
        }

        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (Student s : list) {
            double diem = s.getTongDiemAsDouble();
            sum += diem;
            max = Math.max(max, diem);
            min = Math.min(min, diem);
        }
        double avg = sum / list.size();

        lblTotal.setText(String.format("Tổng: %d SV | Cao: %.2f | Thấp: %.2f", list.size(), max, min));
        lblAverage.setText(String.format("Điểm TB: %.2f", avg));
    }

    // ========================================
    // TÌm kiếm và hiển thị bảng và lọc
    // ========================================
    public void ViewTable(String name) {
        DefaultTableModel model = (DefaultTableModel) this.tblStudent.getModel();
        model.setNumRows(0);
        int n = 1;

        String filterType = (String) cbFilter.getSelectedItem();

        for (Student x : list) {
            if (x.getName().toLowerCase().contains(name.toLowerCase())
                    || x.getId().toLowerCase().contains(name.toLowerCase())) {
                boolean shouldShow = true;

                if (filterType != null && !filterType.equals("Tất cả")) {
                    String xepLoai = x.getXepLoai();
                    switch (filterType) {
                        case "Xuất sắc (≥9)":
                            shouldShow = xepLoai.equals("Xuất sắc");
                            break;
                        case "Giỏi (8-8.9)":
                            shouldShow = xepLoai.equals("Giỏi");
                            break;
                        case "Khá (7-7.9)":
                            shouldShow = xepLoai.equals("Khá");
                            break;
                        case "Trung bình (5-6.9)":
                            shouldShow = xepLoai.equals("Trung bình");
                            break;
                        case "Yếu (<5)":
                            shouldShow = xepLoai.equals("Yếu");
                            break;
                    }
                }

                if (shouldShow) {
                    String tongDiemStr = x.getTongDiem();
                    String xepLoai = x.getXepLoai();
                    model.addRow(new Object[]{n++, x.getId(), x.getName(), x.getAge(),
                        x.getDiemCC(), x.getDiemGK(), x.getDiemCK(), tongDiemStr, xepLoai});
                }
            }
        }
        // set độ rộng cột
        this.tblStudent.setRowHeight(25);
        this.tblStudent.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tblStudent.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblStudent.getColumnModel().getColumn(2).setPreferredWidth(180);
        this.tblStudent.getColumnModel().getColumn(3).setPreferredWidth(50);
        this.tblStudent.getColumnModel().getColumn(4).setPreferredWidth(60);
        this.tblStudent.getColumnModel().getColumn(5).setPreferredWidth(60);
        this.tblStudent.getColumnModel().getColumn(6).setPreferredWidth(60);
        this.tblStudent.getColumnModel().getColumn(7).setPreferredWidth(70);
        this.tblStudent.getColumnModel().getColumn(8).setPreferredWidth(100);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < 9; i++) {
            if (i != 2) {
                tblStudent.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            }
        }

        DefaultTableCellRenderer xepLoaiRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (!isSelected) {
                    String xepLoai = (String) value;
                    switch (xepLoai) {
                        case "Xuất sắc":
                            c.setBackground(new Color(76, 175, 80));
                            c.setForeground(Color.WHITE);
                            break;
                        case "Giỏi":
                            c.setBackground(new Color(139, 195, 74));
                            c.setForeground(Color.WHITE);
                            break;
                        case "Khá":
                            c.setBackground(new Color(255, 235, 59));
                            c.setForeground(Color.BLACK);
                            break;
                        case "Trung bình":
                            c.setBackground(new Color(255, 152, 0));
                            c.setForeground(Color.WHITE);
                            break;
                        case "Yếu":
                            c.setBackground(new Color(244, 67, 54));
                            c.setForeground(Color.WHITE);
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                }
                return c;
            }
        };
        tblStudent.getColumnModel().getColumn(8).setCellRenderer(xepLoaiRenderer);

        updateStatistics();
    }

    // ========================================
    // [TÍNH NĂNG 13] TÌM KIẾM THEO ID
    // ========================================
    public Student Search(String s) {
        for (Student x : list) {
            if (x.getId().equals(s)) {
                return x;
            }
        }
        return null;
    }

    // ========================================
    // [TÍNH NĂNG 14] XÓA TẤT CẢ
    // ========================================
    private void clearAllData() {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách đã trống.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa TẤT CẢ sinh viên?\nHành động này không thể hoàn tác!",
                "Xác nhận xóa tất cả", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            list.clear();
            pos = -1;
            View();
            ViewTable(txtSearchName.getText());
            markAsChanged();
            updateStatus("Đã xóa tất cả dữ liệu");
        }
    }

    // ========================================
    // [TÍNH NĂNG 15] TOP 5 SINH VIÊN
    // ========================================
    private void showTopStudents() {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sinh viên nào.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<Student> sortedList = new ArrayList<>(list);
        sortedList.sort((a, b) -> Double.compare(b.getTongDiemAsDouble(), a.getTongDiemAsDouble()));

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════\n");
        sb.append("         TOP 5 SINH VIÊN XUẤT SẮC\n");
        sb.append("═══════════════════════════════════════\n\n");

        int count = Math.min(5, sortedList.size());
        for (int i = 0; i < count; i++) {
            Student s = sortedList.get(i);
            sb.append(String.format("🏆 TOP %d\n", i + 1));
            sb.append(String.format("   Mã SV: %s\n", s.getId()));
            sb.append(String.format("   Tên: %s\n", s.getName()));
            sb.append(String.format("   Điểm: %.2f (%s)\n", s.getTongDiemAsDouble(), s.getXepLoai()));
            sb.append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Top sinh viên", JOptionPane.INFORMATION_MESSAGE);
    }

    // ========================================
    // KHỞI TẠO GIAO DIỆN
    // ========================================
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtID = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAge = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtDiemCC = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtDiemGK = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtDiemCK = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudent = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtSearchName = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbSort = new javax.swing.JComboBox<>();
        btnStats = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cbFilter = new javax.swing.JComboBox<>();
        lblTotal = new javax.swing.JLabel();
        lblAverage = new javax.swing.JLabel();
        btnBulkAdd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtQuickFilter = new javax.swing.JTextField();
        chkAutoSave = new javax.swing.JCheckBox();
        lblStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản lý sinh viên");

        jLabel1.setFont(new java.awt.Font("Arial", 1, 28));
        jLabel1.setText("QUẢN LÝ SINH VIÊN");
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addGap(20, 20, 20))
        );

        txtID.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel2.setText("Mã SV:");
        jLabel3.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel3.setText("Tuổi:");
        txtAge.setFont(new java.awt.Font("Arial", 0, 14));
        txtName.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel4.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel4.setText("Họ tên:");
        jLabel7.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel7.setText("Điểm CC:");
        txtDiemCC.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel8.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel8.setText("Điểm GK:");
        txtDiemGK.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel9.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel9.setText("Điểm CK:");
        txtDiemCK.setFont(new java.awt.Font("Arial", 0, 14));

        btnAdd.setFont(new java.awt.Font("Arial", 0, 14));
        btnAdd.setText("Thêm");
        btnAdd.addActionListener(evt -> btnAddActionPerformed(evt));

        btnEdit.setFont(new java.awt.Font("Arial", 0, 14));
        btnEdit.setText("Sửa");
        btnEdit.addActionListener(evt -> btnEditActionPerformed(evt));

        btnDelete.setFont(new java.awt.Font("Arial", 0, 14));
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(evt -> btnDeleteActionPerformed(evt));

        btnSave.setFont(new java.awt.Font("Arial", 0, 14));
        btnSave.setText("Lưu");
        btnSave.addActionListener(evt -> btnSaveActionPerformed(evt));

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14));
        btnCancel.setText("Hủy");
        btnCancel.addActionListener(evt -> btnCancelActionPerformed(evt));

        btnClear.setFont(new java.awt.Font("Arial", 1, 12));
        btnClear.setText("Xóa tất cả");
        btnClear.setForeground(new Color(200, 0, 0));
        btnClear.addActionListener(evt -> clearAllData());


        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(5, 5, 5)
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(5, 5, 5)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(5, 5, 5)
                                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDiemCC, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDiemGK, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDiemCK, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        
                                       
                                        .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtDiemCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(txtDiemGK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(txtDiemCK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAdd)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete))
                                .addGap(8, 8, 8)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSave)
                                        .addComponent(btnCancel))
                                .addGap(20, 20, 20)
                                
                                
                                
                                .addComponent(btnClear)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblStudent.setFont(new java.awt.Font("Arial", 0, 13));
        tblStudent.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"STT", "Mã SV", "Họ tên", "Tuổi", "CC", "GK", "CK", "Tổng", "Xếp loại"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tblStudent.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblStudent.getTableHeader().setReorderingAllowed(false);
        tblStudent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblStudentMouseClicked(evt);
            }
        });
        tblStudent.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tblStudentKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tblStudent);

        jLabel5.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel5.setText("Tìm kiếm:");
        txtSearchName.setFont(new java.awt.Font("Arial", 0, 14));
        btnSearch.setFont(new java.awt.Font("Arial", 0, 14));
        btnSearch.setText("🔍");
        btnSearch.addActionListener(evt -> btnSearchActionPerformed(evt));

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel6.setText("Sắp xếp:");
        cbSort.setFont(new java.awt.Font("Arial", 0, 13));
        cbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Theo Tên", "Theo Tuổi", "Theo ID", "Điểm: Cao → Thấp", "Điểm: Thấp → Cao"
        }));
        cbSort.addActionListener(evt -> cbSortActionPerformed(evt));

        btnStats.setFont(new java.awt.Font("Arial", 1, 13));
        btnStats.setText("Thống kê");
        btnStats.addActionListener(evt -> btnStatsActionPerformed(evt));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel10.setText("Lọc:");
        cbFilter.setFont(new java.awt.Font("Arial", 0, 13));
        cbFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Tất cả", "Xuất sắc (≥9)", "Giỏi (8-8.9)", "Khá (7-7.9)", "Trung bình (5-6.9)", "Yếu (<5)"
        }));
        cbFilter.addActionListener(evt -> cbFilterActionPerformed(evt));

        lblTotal.setFont(new java.awt.Font("Arial", 1, 14));
        lblTotal.setText("Tổng: 0 sinh viên");
        lblTotal.setForeground(new Color(33, 150, 243));

        lblAverage.setFont(new java.awt.Font("Arial", 1, 14));
        lblAverage.setText("Điểm TB: N/A");
        lblAverage.setForeground(new Color(76, 175, 80));

        btnBulkAdd.setFont(new java.awt.Font("Arial", 1, 13));
        btnBulkAdd.setText("Thêm hàng loạt");
        btnBulkAdd.addActionListener(evt -> bulkAddStudents());

        jLabel11.setFont(new java.awt.Font("Arial", 0, 13));
        jLabel11.setText("Lọc nhanh:");
        txtQuickFilter.setFont(new java.awt.Font("Arial", 0, 13));
        txtQuickFilter.setToolTipText("Gõ để lọc theo tên hoặc mã SV");

        chkAutoSave.setFont(new java.awt.Font("Arial", 0, 12));
        chkAutoSave.setText("Tự động lưu");
        chkAutoSave.setSelected(true);

        lblStatus.setFont(new java.awt.Font("Arial", 2, 12));
        lblStatus.setText("");
        lblStatus.setForeground(new Color(100, 100, 100));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(5, 5, 5)
                                                .addComponent(txtSearchName, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(3, 3, 3)
                                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel6)
                                                .addGap(5, 5, 5)
                                                .addComponent(cbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel10)
                                                .addGap(5, 5, 5)
                                                .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnStats, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(lblAverage, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(chkAutoSave)
                                                .addGap(10, 10, 10)
                                                .addComponent(lblStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addGap(5, 5, 5)
                                                .addComponent(txtQuickFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(20, 20, 20)
                                                .addComponent(btnBulkAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                ))
                                .addGap(10, 10, 10))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(txtSearchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(jLabel6)
                                        .addComponent(cbSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel10)
                                        .addComponent(cbFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnStats))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel11)
                                        .addComponent(txtQuickFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnBulkAdd)
                                        )
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                                .addGap(10, 10, 10)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblTotal)
                                        .addComponent(lblAverage)
                                        .addComponent(chkAutoSave)
                                        .addComponent(lblStatus))
                                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }

    // ========================================
    // [TÍNH NĂNG 16] THỐNG KÊ CHI TIẾT
    // ========================================
    private void btnStatsActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sinh viên nào trong danh sách để thống kê.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StatsFrame statsWindow = new StatsFrame(list);
        statsWindow.setVisible(true);
    }

    // ========================================
    // [TÍNH NĂNG 17] CRUD - THÊM MỚI
    // ========================================
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        this.txtID.setText("");
        this.txtName.setText("");
        this.txtAge.setText("");
        this.txtDiemCC.setText("");
        this.txtDiemGK.setText("");
        this.txtDiemCK.setText("");
        OnOff(false, true);
        check = 1;
    }

    // ========================================
    // [TÍNH NĂNG 18] CRUD - CHỈNH SỬA
    // ========================================
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên nào để sửa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        check = -1;
        OnOff(false, true);
    }

    // ========================================
    // [TÍNH NĂNG 19] CRUD - XÓA
    // ========================================
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên nào để xóa.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 16)));
        int n = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn xóa không?",
                "Thông báo", JOptionPane.YES_NO_OPTION);
        if (n == JOptionPane.YES_OPTION) {
            list.remove(pos);
            if (!list.isEmpty()) {
                if (pos >= list.size()) {
                    pos = list.size() - 1;
                }
                if (pos < 0 && !list.isEmpty()) {
                    pos = 0;
                }
            } else {
                pos = -1;
            }
            View();
            ViewTable(this.txtSearchName.getText());
            markAsChanged();
            updateStatus("Đã xóa sinh viên");
        }
    }

    // ========================================
    // [TÍNH NĂNG 20] CRUD - LƯU
    // ========================================
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String ID = this.txtID.getText().trim();
            String name = this.txtName.getText().trim();
            String ageText = this.txtAge.getText().trim();
            String diemCCText = this.txtDiemCC.getText().trim();
            String diemGKText = this.txtDiemGK.getText().trim();
            String diemCKText = this.txtDiemCK.getText().trim();

            if (ID.isEmpty() || name.isEmpty() || ageText.isEmpty()
                    || diemCCText.isEmpty() || diemGKText.isEmpty() || diemCKText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);
            if (age < 18 || age > 100) {
                JOptionPane.showMessageDialog(this, "Tuổi phải trong khoảng 18-100.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double diemCC = Double.parseDouble(diemCCText);
            double diemGK = Double.parseDouble(diemGKText);
            double diemCK = Double.parseDouble(diemCKText);

            if (diemCC < 0 || diemCC > 10 || diemGK < 0 || diemGK > 10 || diemCK < 0 || diemCK > 10) {
                JOptionPane.showMessageDialog(this, "Điểm phải nằm trong khoảng từ 0 đến 10.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student newStudent = new Student(ID, name, age, diemCC, diemGK, diemCK);

            if (check == 1) {
                if (Search(ID) != null) {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                this.list.add(newStudent);
                pos = list.size() - 1;
                updateStatus("Đã thêm sinh viên mới");
            } else {
                Student existingStudentWithNewId = Search(ID);
                if (existingStudentWithNewId != null && !existingStudentWithNewId.equals(list.get(pos))) {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên mới đã tồn tại cho sinh viên khác.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                this.list.set(pos, newStudent);
                updateStatus("Đã cập nhật thông tin sinh viên");
            }

            View();
            ViewTable(this.txtSearchName.getText());
            OnOff(true, false);
            markAsChanged();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tuổi và Điểm phải là các số hợp lệ.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ========================================
    // [TÍNH NĂNG 21] CRUD - HỦY
    // ========================================
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        View();
        OnOff(true, false);
        updateStatus("Đã hủy thao tác");
    }

    // ========================================
    // [TÍNH NĂNG 22] CHỌN TỪ BẢNG - CHUỘT => An
    // ========================================
    private void tblStudentMouseClicked(java.awt.event.MouseEvent evt) {
        int row = this.tblStudent.getSelectedRow();
        if (row != -1) {
            String studentIdFromTable = this.tblStudent.getValueAt(row, 1).toString();
            Student selectedStudent = Search(studentIdFromTable);
            if (selectedStudent != null) {
                pos = list.indexOf(selectedStudent);
                View();
            }
        }
    }

    // ========================================
    // [TÍNH NĂNG 23] CHỌN TỪ BẢNG - PHÍM => An
    // ========================================
    private void tblStudentKeyReleased(java.awt.event.KeyEvent evt) {
        int row = this.tblStudent.getSelectedRow();
        if (row != -1) {
            String studentIdFromTable = this.tblStudent.getValueAt(row, 1).toString();
            Student selectedStudent = Search(studentIdFromTable);
            if (selectedStudent != null) {
                pos = list.indexOf(selectedStudent);
                View();
            }
        }
    }

    // ========================================
    // [TÍNH NĂNG 24] TÌM KIẾM CƠ BẢN
    // ========================================
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        ViewTable(this.txtSearchName.getText());
    }

    // ========================================
    // [TÍNH NĂNG 25] LỌC THEO XẾP LOẠI
    // ========================================
    private void cbFilterActionPerformed(java.awt.event.ActionEvent evt) {
        ViewTable(this.txtSearchName.getText());
    }

    // ========================================
    // [TÍNH NĂNG 26] SẮP XẾP ĐA TIÊU CHÍ
    // ========================================
    private void cbSortActionPerformed(java.awt.event.ActionEvent evt) {
        String loai = (String) cbSort.getSelectedItem();
        if (loai == null) {
            return;
        }

        if (loai.equals("Theo Tên")) {
            list.sort((a, b) -> {
                String[] n1 = getSortKey(a.getName());
                String[] n2 = getSortKey(b.getName());
                int kq = vietnameseCollator.compare(n1[0], n2[0]);
                if (kq != 0) {
                    return kq;
                }
                kq = vietnameseCollator.compare(n1[1], n2[1]);
                if (kq != 0) {
                    return kq;
                }
                return vietnameseCollator.compare(n1[2], n2[2]);
            });
        } else if (loai.equals("Theo Tuổi")) {
            list.sort((a, b) -> Integer.compare(a.getAge(), b.getAge()));
        } else if (loai.equals("Theo ID")) {
            list.sort((a, b) -> a.getId().compareTo(b.getId()));
        } else if (loai.contains("Cao")) {
            list.sort((a, b) -> Double.compare(b.getTongDiemAsDouble(), a.getTongDiemAsDouble()));
        } else if (loai.contains("Thấp")) {
            list.sort((a, b) -> Double.compare(a.getTongDiemAsDouble(), b.getTongDiemAsDouble()));
        }

        ViewTable(txtSearchName.getText());
        pos = list.isEmpty() ? -1 : 0;
        View();
        updateStatus("Đã sắp xếp danh sách");
    }

    // ========================================
    // MAIN - KHỞI CHẠY ỨNG DỤNG
    // ========================================
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SF.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            SF frame = new SF();
            frame.setVisible(true);
        });
    }
}
