import model.Course;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Kita panggil DataLoader untuk membaca file CSV
        // Pastikan path-nya benar (data/dataset.csv)
        List<Course> listMK = DataLoader.loadCourses("../data/dataset.csv");
        
        System.out.println("Data berhasil dimuat:");
        for (Course c : listMK) {
            System.out.println("Kode: " + c.kodeMK + " | Nama: " + c.namaMK);
        }
    }
}