package tuan2;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Lớp BFS triển khai thuật toán Breadth-First Search cho bài toán N-Queens.
 */
public class BFS implements SearchStrategy {

    @Override
    public Node search(Node root) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.getState().size() == current.getN()) {
                return current; // Tìm thấy giải pháp
            }

            List<Node> neighbours = current.getNeighbours();
            if (neighbours != null) {
                queue.addAll(neighbours);
            }
        }

        return null; // Không tìm thấy giải pháp
    }
}
