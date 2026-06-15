package tree;

import model.Course;
import tree.BPlusTree;

public class TestBPlusTree {
    public static void main(String[] args) {
        BPlusTree tree = new BPlusTree();
        
        // 1. Buat data Course (sesuai konstruktor Course.java)
        Course matkul = new Course("IF101", "Logika Informatika", 3, 1, "Informatika", "Dasar pemrograman");
        
        // 2. Insert ke Tree
        tree.insert(matkul);
        
        // 3. Search dari Tree
        Course hasil = tree.search("IF101");
        
        if (hasil != null) {
            System.out.println("Data ditemukan: " + hasil.getNama());
        } else {
            System.out.println("Data tidak ditemukan.");
        }
    }
}