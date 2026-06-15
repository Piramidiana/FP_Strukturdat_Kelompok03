package system;

import model.Course;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVHandler {
    private static final String HEADER = "kode\tnama\tsks\tsemester\tprasyarat";
    private static final String NONE = "None";

    // Membaca data mata kuliah dari data/dataset.csv.
    // Format dataset: kode<TAB>nama<TAB>sks<TAB>semester<TAB>prasyarat.
    public static List<Course> loadFromCSV(String filePath) {
        List<Course> courses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1) {
                    continue; // Lewati header.
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = splitLine(line);

                if (values.length < 4) {
                    System.err.println("[CSVHandler] Baris " + lineNumber + " tidak valid, dilewati.");
                    continue;
                }

                try {
                    String kode = normalizeCode(values[0]);
                    String nama = values[1].trim();
                    int sks = Integer.parseInt(values[2].trim());
                    int semester = parseSemester(values[3]);

                    // Dataset terbaru tidak punya kolom major dan description.
                    // Karena constructor Course tetap butuh dua field ini, diisi default "-".
                    String major = "-";
                    String description = "-";

                    if (!kode.isEmpty()) {
                        courses.add(new Course(kode, nama, sks, semester, major, description));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[CSVHandler] Angka tidak valid pada baris "
                            + lineNumber + ", dilewati. Detail: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("[CSVHandler] File tidak ditemukan: " + filePath);
        } catch (IOException e) {
            System.err.println("[CSVHandler] Gagal membaca file: " + e.getMessage());
        }

        return courses;
    }

    // Membaca kolom prasyarat dari dataset.
    // Hasil map: kode mata kuliah -> daftar kode prasyarat.
    public static Map<String, List<String>> loadPrerequisiteMap(String filePath) {
        Map<String, List<String>> prerequisiteMap = new LinkedHashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (lineNumber == 1) {
                    continue; // Lewati header.
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] values = splitLine(line);

                if (values.length < 1) {
                    continue;
                }

                String kode = normalizeCode(values[0]);
                List<String> prereqs = new ArrayList<>();

                if (values.length >= 5) {
                    prereqs = parsePrerequisiteCodes(values[4]);
                }

                prerequisiteMap.put(kode, prereqs);
            }
        } catch (FileNotFoundException e) {
            System.err.println("[CSVHandler] File tidak ditemukan saat membaca prasyarat: " + filePath);
        } catch (IOException e) {
            System.err.println("[CSVHandler] Gagal membaca prasyarat: " + e.getMessage());
        }

        return prerequisiteMap;
    }

    public static boolean saveToCSV(String filePath, Collection<Course> courses) {
        return saveToCSV(filePath, courses, new LinkedHashMap<String, List<String>>());
    }

    // Menyimpan data kembali ke dataset, termasuk kolom prasyarat.
    public static boolean saveToCSV(String filePath, Collection<Course> courses,
                                    Map<String, List<String>> prerequisiteMap) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(HEADER);
            bw.newLine();

            for (Course course : courses) {
                String kode = normalizeCode(course.getKode());
                List<String> prereqs = prerequisiteMap.get(kode);
                String prereqText = formatPrerequisiteCodes(prereqs);

                bw.write(course.getKode());
                bw.write('\t');
                bw.write(course.getNama());
                bw.write('\t');
                bw.write(String.valueOf(course.getSks()));
                bw.write('\t');
                bw.write(String.valueOf(course.getSemester()));
                bw.write('\t');
                bw.write(prereqText);
                bw.newLine();
            }

            System.out.println("[CSVHandler] Data berhasil disimpan ke " + filePath);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("[CSVHandler] Path file tidak valid: " + filePath);
        } catch (IOException e) {
            System.err.println("[CSVHandler] Gagal menyimpan file: " + e.getMessage());
        }

        return false;
    }

    public static List<String> parsePrerequisiteCodes(String raw) {
        List<String> result = new ArrayList<>();

        if (raw == null) {
            return result;
        }

        String trimmed = raw.trim();

        if (trimmed.isEmpty() || trimmed.equalsIgnoreCase(NONE) || trimmed.equals("-")) {
            return result;
        }

        // Mendukung input lebih dari satu prasyarat:
        // IF101;IF102 atau IF101,IF102 atau IF101|IF102.
        String[] parts = trimmed.split("[;,|]");

        for (String part : parts) {
            String code = normalizeCode(part);

            if (!code.isEmpty() && !result.contains(code)) {
                result.add(code);
            }
        }

        return result;
    }

    public static String formatPrerequisiteCodes(List<String> prereqs) {
        if (prereqs == null || prereqs.isEmpty()) {
            return NONE;
        }

        StringBuilder sb = new StringBuilder();

        for (String code : prereqs) {
            String normalized = normalizeCode(code);

            if (normalized.isEmpty()) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(';');
            }

            sb.append(normalized);
        }

        return sb.length() == 0 ? NONE : sb.toString();
    }

    private static String[] splitLine(String line) {
        // Dataset kamu memakai TAB.
        // Fallback koma tetap disediakan supaya aman kalau nanti diubah menjadi CSV biasa.
        if (line.contains("\t")) {
            return line.split("\t", -1);
        }

        return line.split(",", -1);
    }

    private static int parseSemester(String raw) {
        // Bisa membaca "1" maupun "Semester 1".
        String cleaned = raw.trim().replaceAll("[^0-9]", "");

        if (cleaned.isEmpty()) {
            throw new NumberFormatException("semester kosong/tidak mengandung angka");
        }

        return Integer.parseInt(cleaned);
    }

    private static String normalizeCode(String code) {
        if (code == null) {
            return "";
        }

        return code.trim().toUpperCase();
    }
}