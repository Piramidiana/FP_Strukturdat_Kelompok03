package model;

public class Course {
    // Kita buat 'kode' final agar tidak bisa diubah setelah di-set (seperti primary key)
    private final String kode; 
    private String nama;
    private int sks;
    private int semester;
    private String major;
    private String description;

    // Constructor sesuai urutan yang diminta
    public Course(String kode, String nama, int sks, int semester, String major, String description) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.major = major;
        this.description = description;
    }

    // Getter untuk semua atribut
    public String getKode() { return kode; }
    public String getNama() { return nama; }
    public int getSks() { return sks; }
    public int getSemester() { return semester; }
    public String getMajor() { return major; }
    public String getDescription() { return description; }

    // Setter untuk atribut yang boleh diubah
    public void setNama(String nama) { this.nama = nama; }
    public void setSks(int sks) { this.sks = sks; }
    public void setSemester(int semester) { this.semester = semester; }
    public void setMajor(String major) { this.major = major; }
    public void setDescription(String description) { this.description = description; }

    // Override toString agar saat diprint muncul kodenya saja
    @Override
    public String toString() {
        return kode;
    }
}