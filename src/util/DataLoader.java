package util; // Sesuaikan dengan lokasi foldermu

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
            br.readLine(); // Skip header
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Pastikan urutan CSV sesuai dengan parameter ini:
                // values[0]=kode, values[1]=nama, values[2]=sks, values[3]=semester, values[4]=major, values[5]=deskripsi
                
                Course course = new Course(
                    values[0].trim(), 
                    values[1].trim(), 
                    Integer.parseInt(values[2].trim()), 
                    Integer.parseInt(values[3].trim()), 
                    values[4].trim(), 
                    values[5].trim()
                );
                courses.add(course);
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca file: " + e.getMessage());
        }
        return courses;
    }
}