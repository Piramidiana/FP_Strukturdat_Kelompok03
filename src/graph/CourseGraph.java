package graph;

import model.Course;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

public class CourseGraph {
    private Map<Course, List<Course>> adjacencyList;

    public CourseGraph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(Course course) {
        if (course == null) {
            return;
        }

        adjacencyList.putIfAbsent(course, new ArrayList<>());
    }

    public void addEdge(Course prerequisite, Course course) {
        if (prerequisite.equals(course)) {
            return;
        }

        addVertex(prerequisite);
        addVertex(course);

        List<Course> neighbors = adjacencyList.get(prerequisite);
        
        if (!neighbors.contains(course)) {
            neighbors.add(course);
        }
    }

    public List<Course> getNeighbors(Course course) {
        return adjacencyList.getOrDefault(course, new ArrayList<>());
    }
    
    public Set<Course> getVertices() {
        return adjacencyList.keySet();
    }

    public void printGraph() {
        for (Course vertex : adjacencyList.keySet()) {
            System.out.println(vertex + " " + adjacencyList.get(vertex));
        }
    }
}