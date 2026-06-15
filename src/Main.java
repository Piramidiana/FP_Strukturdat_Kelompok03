import model.Course;
import tree.BPlusTree;
import graph.CourseGraph;
import util.DataLoader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Inisialisasi Modul
        BPlusTree tree = new BPlusTree();
        CourseGraph graph = new CourseGraph();
        String path = "../data/dataset.csv"; 

        // 2. Load Data dari CSV
        System.out.println("Memuat data...");
        List<Course> listData = DataLoader.loadCourses(path);

        // 3. Masukkan data ke B+ Tree dan Graph
        for (Course c : listData) {
            tree.insert(c);       
            graph.addVertex(c);   
        }
        System.out.println("Berhasil memuat " + listData.size() + " mata kuliah!");

        // --- HUBUNGKAN PRASYARAT ---
        Course if102 = tree.search("IF102");
        Course if103 = tree.search("IF103");
        
        // Cek dulu apakah datanya ada, baru dihubungkan
        if (if102 != null && if103 != null) {
            graph.addEdge(if102, if103); // <--- INI YANG KURANG TADI
            System.out.println("Menghubungkan: IF102 (Algoritma) -> IF103 (Struktur Data)");
        }

        // 4. Contoh Integrasi: Cari Matkul "IF102" dan cek prasyaratnya
        Course target = tree.search("IF102");
        if (target != null) {
            System.out.println("\nMatkul ditemukan: " + target.getNama());
            System.out.println("Prasyarat (Neighbors): " + graph.getNeighbors(target));
        } else {
            System.out.println("\nMatkul tidak ditemukan.");
        }
    }
}