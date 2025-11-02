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


public class SF extends javax.swing.JFrame {

    private javax.swing.JComboBox<String> cbSort;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblStudent;
    private javax.swing.JTextField txtAge;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtSearchName;

    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtDiemCC;
    private javax.swing.JTextField txtDiemGK;
    private javax.swing.JTextField txtDiemCK;

    private javax.swing.JButton btnStats;

    List<Student> list = new ArrayList<Student>();
    static int pos = 0;
    Student x;
    static int check = 0;
    JPanel panel;

    private final Collator vietnameseCollator;


    public SF() {
        vietnameseCollator = Collator.getInstance(new Locale("vi", "VN"));
        vietnameseCollator.setStrength(Collator.PRIMARY);

        initComponents();

        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        
        // Đơn giản hóa màu nền
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
    }

    public void loadList() {
        list = new ArrayList<>();
        list.add(new Student("1111", "Nguyễn Văn Sơn", 23, 10, 8, 7.5));
        list.add(new Student("2222", "Trần Thị Mai", 20, 9, 9, 9));
        list.add(new Student("3333", "Lê Thảo Mai", 21, 8.5, 7, 8));
        list.add(new Student("4444", "Nguyễn Thị Ánh", 22, 10, 10, 10));
    }

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

    public void ViewTable(String name) {
        DefaultTableModel model = (DefaultTableModel) this.tblStudent.getModel();
        model.setNumRows(0);
        int n = 1;
        for (Student x : list) {
            if (x.getName().toLowerCase().contains(name.toLowerCase())) {
                String tongDiemStr = x.getTongDiem();
                model.addRow(new Object[]{
                    n++,
                    x.getId(),
                    x.getName(),
                    x.getAge(),
                    x.getDiemCC(),
                    x.getDiemGK(),
                    x.getDiemCK(),
                    tongDiemStr
                });
            }
        }
        this.tblStudent.setRowHeight(25);

        this.tblStudent.getColumnModel().getColumn(0).setPreferredWidth(40);
        this.tblStudent.getColumnModel().getColumn(1).setPreferredWidth(80);
        this.tblStudent.getColumnModel().getColumn(2).setPreferredWidth(200);
        this.tblStudent.getColumnModel().getColumn(3).setPreferredWidth(50);
        this.tblStudent.getColumnModel().getColumn(4).setPreferredWidth(70);
        this.tblStudent.getColumnModel().getColumn(5).setPreferredWidth(70);
        this.tblStudent.getColumnModel().getColumn(6).setPreferredWidth(70);
        this.tblStudent.getColumnModel().getColumn(7).setPreferredWidth(70);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblStudent.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        tblStudent.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        tblStudent.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        tblStudent.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        tblStudent.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);
        tblStudent.getColumnModel().getColumn(6).setCellRenderer(cellRenderer);
        tblStudent.getColumnModel().getColumn(7).setCellRenderer(cellRenderer);
    }

    public Student Search(String s) {
        for (Student x : list) {
            if (x.getId().equals(s)) {
                return x;
            }
        }
        return null;
    }

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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblStudent = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtSearchName = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cbSort = new javax.swing.JComboBox<>();
        btnStats = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản lý sinh viên");

        // HEADER - đơn giản hơn
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

        // PANEL TRÁI - font nhỏ hơn
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
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Arial", 0, 14));
        btnEdit.setText("Sửa");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Arial", 0, 14));
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Arial", 0, 14));
        btnSave.setText("Lưu");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnCancel.setFont(new java.awt.Font("Arial", 0, 14));
        btnCancel.setText("Hủy");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(10, 10, 10)
                                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(10, 10, 10)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDiemCC, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDiemGK, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDiemCK, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtAge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel7)
                                        .addComponent(txtDiemCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(txtDiemGK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel9)
                                        .addComponent(txtDiemCK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAdd)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete))
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSave)
                                        .addComponent(btnCancel))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // PANEL PHẢI - đơn giản hơn
        tblStudent.setFont(new java.awt.Font("Arial", 0, 13));
        tblStudent.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "STT", "Mã SV", "Họ tên", "Tuổi", "CC", "GK", "CK", "Tổng"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false
            };

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
        btnSearch.setText("Tìm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Arial", 0, 14));
        jLabel6.setText("Sắp xếp:");
        cbSort.setFont(new java.awt.Font("Arial", 0, 14));
        cbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
            "Theo Tên", "Theo Tuổi", "Theo ID", "Điểm: Cao -> Thấp", "Điểm: Thấp -> Cao"
        }));
        cbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSortActionPerformed(evt);
            }
        });

        btnStats.setFont(new java.awt.Font("Arial", 1, 14));
        btnStats.setText("Thống kê");
        btnStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatsActionPerformed(evt);
            }
        });

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
                                                .addComponent(txtSearchName, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(5, 5, 5)
                                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(15, 15, 15)
                                                .addComponent(jLabel6)
                                                .addGap(5, 5, 5)
                                                .addComponent(cbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnStats, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 740, Short.MAX_VALUE))
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
                                        .addComponent(btnStats))
                                .addGap(10, 10, 10)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
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

    private void btnStatsActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có sinh viên nào trong danh sách để thống kê.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StatsFrame statsWindow = new StatsFrame(list);
        statsWindow.setVisible(true);
    }

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

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên nào để sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        check = -1;
        OnOff(false, true);
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có sinh viên nào để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Arial", Font.BOLD, 16)));
        int n = JOptionPane.showConfirmDialog(panel, "Bạn có chắc chắn muốn xóa không?", "Thông báo", JOptionPane.YES_NO_OPTION);
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
        }
    }

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
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int age = Integer.parseInt(ageText);
            if (age < 18 || age > 100) {
                JOptionPane.showMessageDialog(this, "Tuổi phải trong khoảng 18-100.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double diemCC = Double.parseDouble(diemCCText);
            double diemGK = Double.parseDouble(diemGKText);
            double diemCK = Double.parseDouble(diemCKText);

            if (diemCC < 0 || diemCC > 10 || diemGK < 0 || diemGK > 10 || diemCK < 0 || diemCK > 10) {
                JOptionPane.showMessageDialog(this, "Điểm phải nằm trong khoảng từ 0 đến 10.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student newStudent = new Student(ID, name, age, diemCC, diemGK, diemCK);

            if (check == 1) {
                if (Search(ID) != null) {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                this.list.add(newStudent);
                pos = list.size() - 1;
            } else {
                Student existingStudentWithNewId = Search(ID);
                if (existingStudentWithNewId != null && !existingStudentWithNewId.equals(list.get(pos))) {
                    JOptionPane.showMessageDialog(this, "Mã sinh viên mới đã tồn tại cho sinh viên khác.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                this.list.set(pos, newStudent);
            }

            View();
            ViewTable(this.txtSearchName.getText());
            OnOff(true, false);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tuổi và Điểm phải là các số hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        View();
        OnOff(true, false);
    }

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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        ViewTable(this.txtSearchName.getText());
    }

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
            list.sort((a, b) -> {
                if (a.getAge() > b.getAge()) {
                    return 1;
                } else if (a.getAge() < b.getAge()) {
                    return -1;
                } else {
                    return 0;
                }
            });
        } else if (loai.equals("Theo ID")) {
            list.sort((a, b) -> a.getId().compareTo(b.getId()));
        } else if (loai.equals("Điểm: Cao -> Thấp")) {
            list.sort((a, b) -> Double.compare(b.getTongDiemAsDouble(), a.getTongDiemAsDouble()));
        } else if (loai.equals("Điểm: Thấp -> Cao")) {
            list.sort((a, b) -> Double.compare(a.getTongDiemAsDouble(), b.getTongDiemAsDouble()));
        }

        ViewTable(txtSearchName.getText());
        pos = list.isEmpty() ? -1 : 0;
        View();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(SF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SF().setVisible(true);
            }
        });
    }
}