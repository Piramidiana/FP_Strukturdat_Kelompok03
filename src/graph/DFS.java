package graph;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import model.Course;

public class DFS {
    private void dfs(CourseGraph graph, Course current, Set<Course> visited, List<Course> traversal, boolean reverse) {
        if (visited.contains(current)) {
            return;
        }

        visited.add(current);
        traversal.add(current);

        List<Course> neighbors = reverse ? graph.getPrerequisites(current) : graph.getNeighbors(current);

        for (Course neighbor : neighbors) {
            dfs(graph, neighbor, visited, traversal, reverse);
        }
    }

    public List<Course> traverseForward(CourseGraph graph, Course startCourse) {
        List<Course> traversal = new ArrayList<>();

        if (graph == null || startCourse == null) {
            return traversal;
        }

        Set<Course> visited = new HashSet<>();
        
        dfs(graph, startCourse, visited, traversal, false);
        traversal.remove(startCourse);

        return traversal;
    }

    public List<Course> traverseBackwards(CourseGraph graph, Course startCourse) {
        List<Course> traversal = new ArrayList<>();

        if (graph == null || startCourse == null) {
            return traversal;
        }

        Set<Course> visited = new HashSet<>();

        dfs(graph, startCourse, visited, traversal, true);

        traversal.remove(startCourse);

        return traversal;
    }
    
}
