package studentsystem;

public class Student {
    private String id;
    private String name;
    private int age;
    private double diemcc;
    private double diemgk;
    private double diemck;

    public Student(String id, String name, int age, double diemcc, double diemgk, double diemck) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.diemcc = diemcc;
        this.diemgk = diemgk;
        this.diemck = diemck;
    }

    public double getTongDiemAsDouble() {
        return diemcc * 0.1 + diemgk * 0.2 + diemck * 0.7;
    }

    public String getTongDiem() {
        return String.format("%.1f", getTongDiemAsDouble());
    }

    public String getXepLoai() {
        double tong = getTongDiemAsDouble();
        if (tong >= 9) return "Xuất sắc";
        else if (tong >= 8) return "Giỏi";
        else if (tong >= 6.5) return "Khá";
        else if (tong >= 5) return "Trung bình";
        else return "Yếu";
    }

    // Getter – Setter
    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getDiemCC() { return diemcc; }
    public double getDiemGK() { return diemgk; }
    public double getDiemCK() { return diemck; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setDiemCC(double diemcc) { this.diemcc = diemcc; }
    public void setDiemGK(double diemgk) { this.diemgk = diemgk; }
    public void setDiemCK(double diemck) { this.diemck = diemck; }
}
