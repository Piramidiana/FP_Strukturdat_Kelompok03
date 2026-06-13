package graph;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

import model.Course;

public class TopologicalSorter {
    private void topoDFS(CourseGraph graph, Course current, Set<Course> visited, List<Course> order) {
        if (visited.contains(current)) {
            return;
        }

        visited.add(current);

        for (Course neighbor : graph.getNeighbors(current)) {
            topoDFS(graph, neighbor, visited, order);
        }

        order.add(current);
    }

    public List<Course> sort(CourseGraph graph) {
        List<Course> order = new ArrayList<>();
        if (graph == null) {
            return order;
        }
        Set<Course> visited = new HashSet<>();

        for (Course course : graph.getVertices()) {
            if (!visited.contains(course)) {
                topoDFS(graph, course, visited, order);
            }
        }

        Collections.reverse(order);
        return order;
    }
    
}
