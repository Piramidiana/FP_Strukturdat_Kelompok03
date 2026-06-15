import model.Course;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<Course> loadCourses(String filePath) {
        List<Course> courses = new ArrayList<>();
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Melewati baris pertama (header: kode,nama,sks,semester,prasyarat)
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Memasukkan data ke dalam objek Course
                Course course = new Course(values[0], values[1], Integer.parseInt(values[2]), values[4]);
                courses.add(course);
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file: " + e.getMessage());
        }
        return courses;
    }
}