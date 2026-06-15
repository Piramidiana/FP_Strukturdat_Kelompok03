package tree;

import model.Course;
import java.util.ArrayList;
import java.util.List;

// Kelas utama B+ Tree
public class BPlusTree {
    private BPlusNode root;

    public BPlusTree() {
        this.root = new LeafNode(); // Awalnya tree hanya berisi satu LeafNode
    }
}

// 1. Kelas Dasar Node
class BPlusNode {
    boolean isLeaf;
    List<String> keys; // Menyimpan kodeMK (String)

    public BPlusNode(boolean isLeaf) {
        this.isLeaf = isLeaf;
        this.keys = new ArrayList<>();
    }
}

// 2. Kelas untuk Leaf Node (Tempat menyimpan data Course)
class LeafNode extends BPlusNode {
    LeafNode next;
    List<Course> values; // Menyimpan objek Course di sini

    public LeafNode() {
        super(true);
        next = null;
        values = new ArrayList<>();
    }
}