package graph;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import model.Course;

public class DFS {
    private void dfs(CourseGraph graph, Course current, Set<Course> visited, List<Course> traversal) {
        if (visited.contains(current)) {
            return;
        }

        visited.add(current);
        traversal.add(current);

        for (Course neighbor : graph.getNeighbors(current)) {
            dfs(graph, neighbor, visited, traversal);
        }
    }

    public List<Course> traverse(CourseGraph graph, Course startCourse) {
        List<Course> traversal = new ArrayList<>();

        if (graph == null || startCourse == null) {
            return traversal;
        }

        Set<Course> visited = new HashSet<>();
        
        dfs(graph, startCourse, visited, traversal);

        return traversal;
    }
    
}
