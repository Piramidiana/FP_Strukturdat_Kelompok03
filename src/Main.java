import java.util.List;
import java.util.Scanner;

import model.Course;
import system.CRUDManager;

public class Main {

    private static void printMenu() {
        System.out.println("\n==========================================");
        System.out.println("      COURSE PREREQUISITE PLANNER");
        System.out.println("==========================================");
        System.out.println("1. Add Course");
        System.out.println("2. Delete Course");
        System.out.println("3. Add Prerequisite");
        System.out.println("4. Search Course");
        System.out.println("5. Show Prerequisite Chain");
        System.out.println("6. Show Dependent Courses");
        System.out.println("7. Show Study Plan");
        System.out.println("8. Show All Courses");
        System.out.println("9. Load Data From Dataset");
        System.out.println("0. Exit");
        System.out.println("==========================================");
        System.out.print("Choice: ");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        CRUDManager crud = new CRUDManager();

        boolean running = true;

        while (running) {

            printMenu();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1: {
                    System.out.print("Course Code: ");
                    String code = scanner.nextLine();

                    System.out.print("Course Name: ");
                    String name = scanner.nextLine();

                    System.out.print("SKS: ");
                    int sks = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Semester: ");
                    int semester = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Major: ");
                    String major = scanner.nextLine();

                    System.out.print("Description: ");
                    String description = scanner.nextLine();

                    Course course = new Course(
                        code,
                        name,
                        sks,
                        semester,
                        major,
                        description
                    );

                    if (crud.addCourse(course)) {
                        System.out.println("Course added successfully.");
                    } else {
                        System.out.println("Failed to add course.");
                    }
                    break;
                }

                case 2: {
                    System.out.print("Course Code: ");
                    String code = scanner.nextLine();

                    if (crud.deleteCourse(code)) {
                        System.out.println("Course deleted successfully.");
                    } else {
                        System.out.println("Course not found.");
                    }

                    break;
                }

                case 3: {
                    System.out.print("Prerequisite Course Code: ");
                    String prerequisiteCode = scanner.nextLine();

                    System.out.print("Target Course Code: ");
                    String courseCode = scanner.nextLine();

                    if (crud.addPrerequisite(prerequisiteCode, courseCode)) {
                        System.out.println("Prerequisite added successfully.");
                    } else {
                        System.out.println("Failed to add prerequisite.");
                        System.out.println("Possible reasons:");
                        System.out.println("- One or both courses do not exist.");
                        System.out.println("- The prerequisite would create a cycle.");
                    }

                    break;
                }

                case 4: {
                    System.out.print("Course Code: ");
                    String code = scanner.nextLine();

                    Course course = crud.getCourseByCode(code);

                    if (course == null) {
                        System.out.println("Course not found.");
                    } else {
                        System.out.println("\n========== COURSE ==========");
                        System.out.println("Code       : " + course.getCode());
                        System.out.println("Name       : " + course.getName());
                        System.out.println("SKS        : " + course.getSks());
                        System.out.println("Semester   : " + course.getSemester());
                        System.out.println("Major      : " + course.getMajor());
                        System.out.println("Description: " + course.getDesc());
                    }

                    break;
                }

                case 5: {
                    System.out.print("Course Code: ");
                    String code = scanner.nextLine();

                    List<Course> prerequisites = crud.getPrerequisiteChain(code);

                    if (prerequisites.isEmpty()) {
                        System.out.println("No prerequisites found or course does not exist.");
                    } else {
                        System.out.println("\nPrerequisite Chain:");

                        int i = 1;
                        for (Course course : prerequisites) {
                            System.out.println(i++ + ". " +
                                course.getCode() + " - " + course.getName());
                        }
                    }

                    break;
                }

                case 6: {
                    System.out.print("Course Code: ");
                    String code = scanner.nextLine();

                    List<Course> dependents = crud.getDependentCourses(code);

                    if (dependents.isEmpty()) {
                        System.out.println("No dependent courses found or course does not exist.");
                    } else {
                        System.out.println("\nDependent Courses:");

                        int i = 1;
                        for (Course course : dependents) {
                            System.out.println(i++ + ". " +
                                course.getCode() + " - " + course.getName());
                        }
                    }

                    break;
                }

                case 7: {
                    List<Course> studyPlan = crud.getStudyPlan();

                    if (studyPlan.isEmpty()) {
                        System.out.println("No study plan available.");
                    } else {
                        System.out.println("\nRecommended Study Plan:");

                        int i = 1;
                        for (Course course : studyPlan) {
                            System.out.println(i++ + ". " +
                                course.getCode() + " - " + course.getName());
                        }
                    }

                    break;
                }

                case 8: {
                    List<Course> courses = crud.getAllCourses();

                    if (courses.isEmpty()) {
                        System.out.println("No courses available.");
                    } else {
                        System.out.println("\n========== COURSE LIST ==========");

                        int i = 1;
                        for (Course course : courses) {
                            System.out.println(i++ + ". " +
                                course.getCode() + " - " +
                                course.getName() +
                                " (" + course.getSks() + " SKS)");
                        }
                    }

                    break;
                }

                case 0:
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid menu option.");
            }
        }

        scanner.close();
    }
}