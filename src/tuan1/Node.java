package tuan1;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int state;
    private boolean visited;
    private List<Node> neighbours;
    private Node parent;

    // Constructor
    public Node(int state) {
        this.state = state;
        this.neighbours = new ArrayList<>();
        this.parent = null;
        this.visited = false;
    }

    // Thêm neighbour vào danh sách kề
    public void addNeighbour(Node neighbourNode) {
        this.neighbours.add(neighbourNode);
    }

    // Lấy danh sách các neighbour
    public List<Node> getNeighbours() {
        return this.neighbours;
    }

    // Getter và Setter cho các thuộc tính
    public int getState() {
        return state;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }
}
