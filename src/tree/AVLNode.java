package tree;

import model.Course;

class AVLNode {

    Course course;

    AVLNode left;
    AVLNode right;

    int height;

    AVLNode(Course course) {
        this.course = course;
        this.height = 1;
    }
}