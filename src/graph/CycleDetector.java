package graph;

import model.Course;
import java.util.Set;
import java.util.HashSet;

public class CycleDetector {
    private boolean detectCycle(CourseGraph graph, Course current, Set<Course> visited, Set<Course> currentlyVisiting) {
        if (currentlyVisiting.contains(current)) {
            return true;
        }
        
        if (visited.contains(current)) {
            return false;
        }

        visited.add(current);
        currentlyVisiting.add(current);

        for (Course neighbor : graph.getNeighbors(current)) {
            if (detectCycle(graph, neighbor, visited, currentlyVisiting)) {
                return true;
            }
        }
        currentlyVisiting.remove(current);

        return false;
    }

    public boolean hasCycle(CourseGraph graph) {
        if (graph == null) {
            return false;
        }

        Set<Course> visited = new HashSet<>();
        Set<Course> currentlyVisiting = new HashSet<>();

        for (Course course : graph.getVertices()) {
            if (!visited.contains(course)) {
                if (detectCycle(graph, course, visited, currentlyVisiting)) {
                    return true;
                }
            }
        }

        return false;
    }
}
