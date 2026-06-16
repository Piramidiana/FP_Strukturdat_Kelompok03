package system;

import graph.CourseGraph;
import graph.CycleDetector;
import graph.DFS;
import graph.TopologicalSorter;
import model.Course;
import tree.AVLTree;

import java.util.ArrayList;
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

        if (prerequisite == null || course == null) {
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

    public List<Course> getPrerequisiteChain(String code) {
        Course course = tree.search(code);

        if (course == null) {
            return new ArrayList<>();
        }

        DFS dfs = new DFS();
        return dfs.traverseBackwards(graph, course);
    }

    public List<Course> getDependentCourses(String code) {
        Course course = tree.search(code);

        if (course == null) {
            return new ArrayList<>();
        }

        DFS dfs = new DFS();
        return dfs.traverseForward(graph, course);
    }

    public List<Course> getStudyPlan() {
        TopologicalSorter sorter = new TopologicalSorter();
        return sorter.sort(graph);
    }
}