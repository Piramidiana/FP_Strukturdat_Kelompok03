package tree;

import model.Course;
import java.util.ArrayList;
import java.util.List;

public class BPlusTree {
    private Node root;

    public BPlusTree() {
        this.root = new LeafNode();
    }

    // Fungsi Insert: Memasukkan Course ke dalam Tree
    public void insert(Course course) {
        LeafNode leaf = (LeafNode) root;
        leaf.keys.add(course.getKode());
        leaf.values.add(course);
        System.out.println("Berhasil insert: " + course.getKode());
    }

    // Fungsi Search: Mencari Course berdasarkan kode (Key)
    public Course search(String code) {
        LeafNode leaf = (LeafNode) root;
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i).equals(code)) {
                return leaf.values.get(i);
            }
        }
        return null; // Kalau tidak ketemu
    }
}

// Node dasar untuk B+ Tree
class Node {
    List<String> keys = new ArrayList<>();
}

// Leaf Node khusus untuk menyimpan data Course
class LeafNode extends Node {
    List<Course> values = new ArrayList<>();
}