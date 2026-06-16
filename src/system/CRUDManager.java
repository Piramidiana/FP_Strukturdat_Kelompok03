package system;

import graph.CourseGraph;
import graph.CycleDetector;
import model.Course;
import tree.AVLTree;

import java.util.Currency;
import java.util.List;

public class CRUDManager {
    private final AVLTree tree;
    private final CourseGraph graph;

    public CRUDManager() {
        tree = new AVLTree();
        graph = new CourseGraph();
    }

    public boolean courseExists(String code) {
        return tree.contains(code);
    }

    public Course getCourseByCode(String code) {
        return tree.search(code);
    }

    public boolean addCourse(Course course) {

        if (course == null) {
            return false;
        }

        if (courseExists(course.getCode())) {
            return false;
        }

        tree.insert(course);
        graph.addVertex(course);

        return true;
    }

    public boolean deleteCourse(String code) {
        Course course = tree.search(code);

        if (course == null) {
            return false;
        }

        graph.removeVertex(course);
        tree.delete(code);

        return true;
    }

    public List<Course> getAllCourses() {
        return tree.getAllCourses();
    }

    public boolean addPrerequisite(String prerequisiteCode, String courseCode) {
        Course prerequisite = tree.search(prerequisiteCode);
        Course course = tree.search(courseCode);

        if (prerequisite == null || courseCode == null) {
            return false;
        }

        graph.addEdge(prerequisite, course);
        CycleDetector detector = new CycleDetector();

        if (detector.hasCycle(graph)) {
            graph.removeEdge(prerequisite, course);
            return false;
        }

        return true;
    }
}