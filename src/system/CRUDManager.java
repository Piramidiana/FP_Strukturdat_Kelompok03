package system;

import model.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CRUDManager {
    // Menyimpan Course dengan kode sebagai key.
    // LinkedHashMap dipakai agar urutan data tetap rapi sesuai urutan input.
    private final LinkedHashMap<String, Course> courses = new LinkedHashMap<>();

    public boolean addCourse(Course course) {
        if (course == null) {
            System.out.println("Data mata kuliah tidak boleh null.");
            return false;
        }

        String kode = normalizeCode(course.getKode());

        if (kode.isEmpty()) {
            System.out.println("Kode mata kuliah tidak boleh kosong.");
            return false;
        }

        if (courses.containsKey(kode)) {
            System.out.println("Kode " + kode + " sudah ada.");
            return false;
        }

        if (course.getSks() <= 0 || course.getSemester() <= 0) {
            System.out.println("SKS dan semester harus lebih dari 0.");
            return false;
        }

        courses.put(kode, course);
        System.out.println("Mata kuliah berhasil ditambahkan: " + course.getKode() + " - " + course.getNama());
        return true;
    }

    public boolean deleteCourse(String kodeInput) {
        String kode = normalizeCode(kodeInput);

        if (kode.isEmpty()) {
            System.out.println("Kode tidak boleh kosong.");
            return false;
        }

        Course removed = courses.remove(kode);

        if (removed == null) {
            System.out.println("Mata kuliah dengan kode " + kode + " tidak ditemukan.");
            return false;
        }

        System.out.println("Mata kuliah berhasil dihapus: " + removed.getKode() + " - " + removed.getNama());
        return true;
    }

    public boolean updateCourse(String kodeInput, String namaBaru, int sksBaru, int semesterBaru) {
        String kode = normalizeCode(kodeInput);
        Course course = courses.get(kode);

        if (course == null) {
            System.out.println("Mata kuliah dengan kode " + kode + " tidak ditemukan.");
            return false;
        }

        boolean changed = false;

        // Nama hanya diubah kalau input tidak kosong.
        if (namaBaru != null && !namaBaru.trim().isEmpty()) {
            course.setNama(namaBaru.trim());
            changed = true;
        }

        // SKS hanya diubah kalau input valid.
        if (sksBaru > 0) {
            course.setSks(sksBaru);
            changed = true;
        }

        // Semester hanya diubah kalau input valid.
        if (semesterBaru > 0) {
            course.setSemester(semesterBaru);
            changed = true;
        }

        if (changed) {
            System.out.println("Data mata kuliah berhasil diupdate.");
        } else {
            System.out.println("Tidak ada data yang berubah.");
        }

        return changed;
    }

    public Course getCourseByCode(String kodeInput) {
        return courses.get(normalizeCode(kodeInput));
    }

    public List<Course> searchByPrefix(String keyword) {
        List<Course> result = new ArrayList<>();
        String key = keyword == null ? "" : keyword.trim().toLowerCase();

        // Fallback search jika BPlusTree tidak menemukan exact code.
        // Bisa mencari dari prefix kode atau sebagian nama.
        for (Course course : courses.values()) {
            String kode = course.getKode() == null ? "" : course.getKode().toLowerCase();
            String nama = course.getNama() == null ? "" : course.getNama().toLowerCase();

            if (kode.startsWith(key) || nama.contains(key)) {
                result.add(course);
            }
        }

        return result;
    }

    public List<Course> getAllCourses() {
        return Collections.unmodifiableList(new ArrayList<>(courses.values()));
    }

    public int getTotalCourses() {
        return courses.size();
    }

    public boolean courseExists(String kodeInput) {
        return courses.containsKey(normalizeCode(kodeInput));
    }

    public void loadFromList(List<Course> data) {
        courses.clear();

        if (data == null) {
            return;
        }

        for (Course course : data) {
            if (course != null && course.getKode() != null) {
                courses.put(normalizeCode(course.getKode()), course);
            }
        }
    }

    public Map<String, Course> asMap() {
        return Collections.unmodifiableMap(courses);
    }

    private String normalizeCode(String kode) {
        if (kode == null) {
            return "";
        }

        return kode.trim().toUpperCase();
    }
}