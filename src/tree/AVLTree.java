package tree;

import java.util.ArrayList;
import java.util.List;

import model.Course;

public class AVLTree {
    private AVLNode root;

    public AVLTree() {
        root = null;
    }

    private int getHeight(AVLNode node) {
        if (node == null) {
            return 0;
        }

        return node.height;
    }

    private void updateHeight(AVLNode node) {
        if (node != null) {
            node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }
    }
    
    private int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }
        
        return getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);
        
        return y;
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode insert(AVLNode node, Course course) {

        if (node == null) {
            return new AVLNode(course);
        }

        int compare = course.getCode().compareTo(node.course.getCode());

        if (compare < 0) {
            node.left = insert(node.left, course);
        }
        else if (compare > 0) {
            node.right = insert(node.right, course);
        }
        else {
            return node;
        }

        updateHeight(node);

        int balance = getBalance(node);

        // Left Left
        if (balance > 1 &&
            course.getCode().compareTo(node.left.course.getCode()) < 0) {

            return rotateRight(node);
        }

        // Right Right
        if (balance < -1 &&
            course.getCode().compareTo(node.right.course.getCode()) > 0) {

            return rotateLeft(node);
        }

        // Left Right
        if (balance > 1 &&
            course.getCode().compareTo(node.left.course.getCode()) > 0) {

            node.left = rotateLeft(node.left);

            return rotateRight(node);
        }

        // Right Left
        if (balance < -1 &&
            course.getCode().compareTo(node.right.course.getCode()) < 0) {

            node.right = rotateRight(node.right);

            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode search(AVLNode node, String code) {

        if (node == null) {
            return null;
        }

        int compare = code.compareTo(node.course.getCode());

        if (compare == 0) {
            return node;
        }

        if (compare < 0) {
            return search(node.left, code);
        }

        return search(node.right, code);
    }

    private void inorder(AVLNode node, List<Course> courses) {

        if (node == null) {
            return;
        }

        inorder(node.left, courses);

        courses.add(node.course);

        inorder(node.right, courses);
    }

    private AVLNode getMinNode(AVLNode node) {

        while (node.left != null) {
            node = node.left;
        }

        return node;
    }

    private AVLNode delete(AVLNode node, String code) {

        if (node == null) {
            return null;
        }

        int compare = code.compareTo(node.course.getCode());

        if (compare < 0) {

            node.left = delete(node.left, code);

        }
        else if (compare > 0) {

            node.right = delete(node.right, code);

        }
        else {

            // One or no child
            if (node.left == null) {
                return node.right;
            }

            if (node.right == null) {
                return node.left;
            }

            // Two Children

            AVLNode successor = getMinNode(node.right);
            node.course = successor.course;
            node.right = delete(node.right, successor.course.getCode());
            
        }

        updateHeight(node);
        int balance = getBalance(node);

        // Left Left
        if (balance > 1 &&
            getBalance(node.left) >= 0) {

            return rotateRight(node);
        }

        // Left Right
        if (balance > 1 &&
            getBalance(node.left) < 0) {

            node.left = rotateLeft(node.left);

            return rotateRight(node);
        }

        // Right Right
        if (balance < -1 &&
            getBalance(node.right) <= 0) {

            return rotateLeft(node);
        }

        // Right Left
        if (balance < -1 &&
            getBalance(node.right) > 0) {

            node.right = rotateRight(node.right);

            return rotateLeft(node);
        }

        return node;
    }


    public void insert(Course course) {
        if (course == null) {
            return;
        }
        root = insert(root, course);
    }

    public Course search(String code) {
        AVLNode node = search(root, code);

        if (node == null) {
            return null;
        }

        return node.course;
    }

    public boolean contains(String code) {
        return search(code) != null;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();

        inorder(root, courses);
        
        return courses;
    }

    public boolean delete(String code) {

        if (!contains(code)) {
            return false;
        }

        root = delete(root, code);

        return true;
    }
}