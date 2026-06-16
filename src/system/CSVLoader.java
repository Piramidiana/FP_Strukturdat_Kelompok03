package system;

import model.Course;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVLoader {

    public static void loadCourses(String filename, CRUDManager crud) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            reader.readLine(); // Skip header

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length < 6) {
                    continue;
                }

                Course course = new Course(
                        data[0].trim(),
                        data[1].trim(),
                        Integer.parseInt(data[2].trim()),
                        Integer.parseInt(data[3].trim()),
                        data[4].trim(),
                        data[5].trim()
                );

                crud.addCourse(course);
            }

        } catch (IOException e) {
            System.out.println("Failed to load courses: " + e.getMessage());
        }
    }

    public static void loadPrerequisites(String filename, CRUDManager crud) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            reader.readLine(); // Skip header

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length < 2) {
                    continue;
                }

                String courseCode = data[0].trim();
                String prerequisiteCode = data[1].trim();

                crud.addPrerequisite(prerequisiteCode, courseCode);
            }

        } catch (IOException e) {
            System.out.println("Failed to load prerequisites: " + e.getMessage());
        }
    }
}