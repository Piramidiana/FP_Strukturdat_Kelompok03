package model;

public class Course {
    public String kodeMK;
    public String namaMK;
    public int sks;
    public String prasyarat; // Ini untuk menyimpan kode prasyaratnya

    public Course(String kodeMK, String namaMK, int sks, String prasyarat) {
        this.kodeMK = kodeMK;
        this.namaMK = namaMK;
        this.sks = sks;
        this.prasyarat = prasyarat;
    }
}