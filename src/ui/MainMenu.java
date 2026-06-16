package ui;

import graph.CourseGraph;
import graph.CycleDetector;
import graph.TopologicalSorter;
import model.Course;
import system.CSVHandler;
import system.CRUDManager;
import tree.AVLTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MainMenu {
    private static final String DATA_PATH = "data/dataset.csv";

    private final Scanner scanner = new Scanner(System.in);
    private final CRUDManager crudManager = new CRUDManager();

    // Karena Course tidak menyimpan field prasyarat,
    // relasi prasyarat disimpan terpisah di map ini.
    private Map<String, List<String>> prerequisiteMap = new LinkedHashMap<>();

    // Modul milik B dan C.
    private AVLTree tree = new AVLTree();
    private CourseGraph graph = new CourseGraph();

    public void start() {
        loadInitialData();

        while (true) {
            printMenu();
            int choice = readInt("Pilih menu: ", -1);

            switch (choice) {
                case 1:
                    addCourseMenu();
                    break;
                case 2:
                    deleteCourseMenu();
                    break;
                case 3:
                    updateCourseMenu();
                    break;
                case 4:
                    searchCourseMenu();
                    break;
                case 5:
                    showAllCoursesMenu();
                    break;
                case 6:
                    showPrerequisiteMenu();
                    break;
                case 7:
                    recommendOrderMenu();
                    break;
                case 8:
                    detectCycleMenu();
                    break;
                case 0:
                    saveAndExit();
                    return;
                default:
                    System.out.println("Pilihan tidak valid.");
            }

            pressEnter();
        }
    }

    private void loadInitialData() {
        List<Course> courses = CSVHandler.loadFromCSV(DATA_PATH);
        prerequisiteMap = CSVHandler.loadPrerequisiteMap(DATA_PATH);
        crudManager.loadFromList(courses);

        rebuildTreeAndGraph();

        System.out.println("Berhasil memuat " + crudManager.getTotalCourses()
                + " mata kuliah dari " + DATA_PATH);
    }

    private void rebuildTreeAndGraph() {
        tree = new AVLTree();
        graph = new CourseGraph();

        // Semua course dimasukkan ke B+ Tree dan Graph.
        for (Course course : crudManager.getAllCourses()) {
            tree.insert(course);
            graph.addVertex(course);
        }

        // Bentuk edge graph: prerequisite -> course.
        // Contoh IF102 prasyarat IF103, maka edge: IF102 -> IF103.
        for (Map.Entry<String, List<String>> entry : prerequisiteMap.entrySet()) {
            Course course = crudManager.getCourseByCode(entry.getKey());

            if (course == null) {
                continue;
            }

            for (String prereqCode : entry.getValue()) {
                Course prerequisite = crudManager.getCourseByCode(prereqCode);

                if (prerequisite != null) {
                    graph.addEdge(prerequisite, course);
                }
            }
        }
    }

    private void addCourseMenu() {
        System.out.println("\n=== TAMBAH MATA KULIAH ===");

        String kode = normalizeCode(readString("Kode: "));

        if (crudManager.courseExists(kode)) {
            System.out.println("Kode sudah ada. Tambah data dibatalkan.");
            return;
        }

        String nama = readString("Nama: ");
        int sks = readPositiveInt("SKS: ");
        int semester = readPositiveInt("Semester: ");
        String prasyaratInput = readString("Prasyarat (None / IF101 / IF101;IF102): ");

        Course course = new Course(kode, nama, sks, semester, "-", "-");

        if (crudManager.addCourse(course)) {
            prerequisiteMap.put(
                    kode,
                    filterExistingPrerequisites(CSVHandler.parsePrerequisiteCodes(prasyaratInput), kode)
            );

            rebuildTreeAndGraph();
        }
    }

    private void deleteCourseMenu() {
        System.out.println("\n=== HAPUS MATA KULIAH ===");

        String kode = normalizeCode(readString("Kode yang dihapus: "));

        if (crudManager.deleteCourse(kode)) {
            prerequisiteMap.remove(kode);

            // Kalau course dihapus, hapus juga dari daftar prasyarat course lain.
            for (List<String> prereqs : prerequisiteMap.values()) {
                prereqs.remove(kode);
            }

            rebuildTreeAndGraph();
        }
    }

    private void updateCourseMenu() {
        System.out.println("\n=== UPDATE MATA KULIAH ===");

        String kode = normalizeCode(readString("Kode yang diupdate: "));
        Course course = crudManager.getCourseByCode(kode);

        if (course == null) {
            System.out.println("Mata kuliah tidak ditemukan.");
            return;
        }

        System.out.println("Data lama: " + course.getKode() + " - " + course.getNama()
                + " | SKS " + course.getSks()
                + " | Semester " + course.getSemester()
                + " | Prasyarat " + CSVHandler.formatPrerequisiteCodes(prerequisiteMap.get(kode)));

        String namaBaru = readOptionalString("Nama baru (Enter jika tidak diubah): ");
        int sksBaru = readOptionalPositiveInt("SKS baru (Enter jika tidak diubah): ");
        int semesterBaru = readOptionalPositiveInt("Semester baru (Enter jika tidak diubah): ");
        String prasyaratBaru = readOptionalString("Prasyarat baru (Enter jika tidak diubah): ");

        boolean courseChanged = crudManager.updateCourse(kode, namaBaru, sksBaru, semesterBaru);
        boolean prereqChanged = false;

        if (!prasyaratBaru.trim().isEmpty()) {
            prerequisiteMap.put(
                    kode,
                    filterExistingPrerequisites(CSVHandler.parsePrerequisiteCodes(prasyaratBaru), kode)
            );
            prereqChanged = true;
        }

        if (courseChanged || prereqChanged) {
            rebuildTreeAndGraph();
        }
    }

    private void searchCourseMenu() {
        System.out.println("\n=== CARI MATA KULIAH ===");

        String keyword = normalizeCode(readString("Masukkan kode/nama: "));

        // Search exact code memakai BPlusTree milik B.
        Course exact = tree.search(keyword);

        if (exact != null) {
            printCourseDetail(exact);
            return;
        }

        // Jika tidak ketemu di BPlusTree, pakai search sederhana dari CRUDManager.
        List<Course> results = crudManager.searchByPrefix(keyword);

        if (results.isEmpty()) {
            System.out.println("Data tidak ditemukan.");
        } else {
            printCourseTable(results);
        }
    }

    private void showAllCoursesMenu() {
        System.out.println("\n=== DAFTAR SEMUA MATA KULIAH ===");
        printCourseTable(crudManager.getAllCourses());
    }

    private void showPrerequisiteMenu() {
        System.out.println("\n=== PRASYARAT MATA KULIAH ===");

        String kode = normalizeCode(readString("Kode mata kuliah: "));
        Course course = tree.search(kode);

        if (course == null) {
            System.out.println("Mata kuliah tidak ditemukan.");
            return;
        }

        List<String> directPrereqs = prerequisiteMap.get(kode);

        System.out.println("Mata kuliah: " + course.getKode() + " - " + course.getNama());
        System.out.println("Prasyarat langsung: " + CSVHandler.formatPrerequisiteCodes(directPrereqs));

        Set<String> allPrereqs = new HashSet<>();
        collectAllPrerequisites(kode, allPrereqs);

        if (allPrereqs.isEmpty()) {
            System.out.println("Semua prasyarat: None");
        } else {
            System.out.println("Semua prasyarat: " + allPrereqs);
        }

        // Karena graph arahnya prerequisite -> course,
        // neighbor berarti course lanjutan yang bergantung pada course ini.
        System.out.println("Mata kuliah lanjutan yang bergantung pada "
                + kode + ": " + graph.getNeighbors(course));
    }

    private void recommendOrderMenu() {
        System.out.println("\n=== REKOMENDASI URUTAN PENGAMBILAN MK ===");

        CycleDetector detector = new CycleDetector();

        if (detector.hasCycle(graph)) {
            System.out.println("Tidak bisa membuat urutan karena graph memiliki siklus prasyarat.");
            return;
        }

        TopologicalSorter sorter = new TopologicalSorter();
        List<Course> order = sorter.sort(graph);

        for (int i = 0; i < order.size(); i++) {
            Course course = order.get(i);
            System.out.println((i + 1) + ". " + course.getKode() + " - " + course.getNama());
        }
    }

    private void detectCycleMenu() {
        System.out.println("\n=== DETEKSI SIKLUS PRASYARAT ===");

        CycleDetector detector = new CycleDetector();

        if (detector.hasCycle(graph)) {
            System.out.println("Terdapat siklus pada graph prasyarat.");
        } else {
            System.out.println("Tidak ada siklus. Graph prasyarat valid.");
        }
    }

    private void saveAndExit() {
        System.out.println("\nMenyimpan data...");

        boolean saved = CSVHandler.saveToCSV(
                DATA_PATH,
                crudManager.getAllCourses(),
                prerequisiteMap
        );

        if (saved) {
            System.out.println("Program selesai.");
        } else {
            System.out.println("Data gagal disimpan.");
        }
    }

    private void collectAllPrerequisites(String kode, Set<String> result) {
        List<String> direct = prerequisiteMap.get(kode);

        if (direct == null) {
            return;
        }

        for (String prereq : direct) {
            if (result.add(prereq)) {
                collectAllPrerequisites(prereq, result);
            }
        }
    }

    private List<String> filterExistingPrerequisites(List<String> prereqCodes, String currentCode) {
        List<String> valid = new ArrayList<>();

        for (String prereq : prereqCodes) {
            String normalized = normalizeCode(prereq);

            if (normalized.equals(currentCode)) {
                System.out.println("Prasyarat " + normalized
                        + " diabaikan karena sama dengan kode mata kuliah.");
                continue;
            }

            if (!crudManager.courseExists(normalized)) {
                System.out.println("Prasyarat " + normalized
                        + " diabaikan karena belum ada di data.");
                continue;
            }

            if (!valid.contains(normalized)) {
                valid.add(normalized);
            }
        }

        return valid;
    }

    private void printMenu() {
        System.out.println("\n==============================");
        System.out.println(" COURSE PREREQUISITE PLANNER ");
        System.out.println("==============================");
        System.out.println("1. Tambah mata kuliah");
        System.out.println("2. Hapus mata kuliah");
        System.out.println("3. Update mata kuliah");
        System.out.println("4. Cari mata kuliah");
        System.out.println("5. Tampilkan semua mata kuliah");
        System.out.println("6. Tampilkan prasyarat mata kuliah");
        System.out.println("7. Rekomendasi urutan pengambilan MK");
        System.out.println("8. Deteksi siklus prasyarat");
        System.out.println("0. Simpan dan keluar");
    }

    private void printCourseTable(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            System.out.println("Belum ada data mata kuliah.");
            return;
        }

        System.out.printf("%-8s %-35s %-5s %-9s %-15s%n",
                "Kode", "Nama", "SKS", "Semester", "Prasyarat");
        System.out.println("-------------------------------------------------------------------------------");

        for (Course course : courses) {
            String prereqs = CSVHandler.formatPrerequisiteCodes(prerequisiteMap.get(course.getKode()));

            System.out.printf("%-8s %-35s %-5d %-9d %-15s%n",
                    course.getKode(),
                    limit(course.getNama(), 35),
                    course.getSks(),
                    course.getSemester(),
                    prereqs);
        }
    }

    private void printCourseDetail(Course course) {
        System.out.println("Kode      : " + course.getKode());
        System.out.println("Nama      : " + course.getNama());
        System.out.println("SKS       : " + course.getSks());
        System.out.println("Semester  : " + course.getSemester());
        System.out.println("Prasyarat : "
                + CSVHandler.formatPrerequisiteCodes(prerequisiteMap.get(course.getKode())));
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt, -1);

            if (value > 0) {
                return value;
            }

            System.out.println("Masukkan angka lebih dari 0.");
        }
    }

    private int readOptionalPositiveInt(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            return -1;
        }

        try {
            int value = Integer.parseInt(input);

            if (value > 0) {
                return value;
            }
        } catch (NumberFormatException ignored) {
        }

        System.out.println("Input angka tidak valid, nilai tidak diubah.");
        return -1;
    }

    private int readInt(String prompt, int defaultValue) {
        System.out.print(prompt);

        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String normalizeCode(String kode) {
        if (kode == null) {
            return "";
        }

        return kode.trim().toUpperCase();
    }

    private String limit(String text, int max) {
        if (text == null) {
            return "";
        }

        if (text.length() <= max) {
            return text;
        }

        return text.substring(0, max - 3) + "...";
    }

    private void pressEnter() {
        System.out.print("\nTekan Enter untuk lanjut...");
        scanner.nextLine();
    }
}