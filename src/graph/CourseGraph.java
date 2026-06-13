package graph;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;

public class CourseGraph {
    private Map<String, List<String>> adjacencyList;

    public CourseGraph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(String code) {
        if (code == null || code.isBlank()) {
            return;
        }

        adjacencyList.putIfAbsent(code, new ArrayList<>());
    }

    public void addEdge(String prerequisite, String course) {
        addVertex(prerequisite);
        addVertex(course);

        List<String> neighbors = adjacencyList.get(prerequisite);

        if (!neighbors.contains(course)) {
            neighbors.add(course);
        }
    }

    public List<String> getNeighbors(String code) {
        return adjacencyList.getOrDefault(code, new ArrayList<>());
    }
    
    public Set<String> getVertices() {
        return adjacencyList.keySet();
    }

    public void printGraph() {
        for (String vertex : adjacencyList.keySet()) {
            System.out.println(vertex + " " + adjacencyList.get(vertex));
        }
    }
}