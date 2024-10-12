package tuan2;

import java.util.List;
import java.util.Stack;

/**
 * Lớp DFS triển khai thuật toán Depth-First Search cho bài toán N-Queens.
 */
public class DFS implements SearchStrategy {

    @Override
    public Node search(Node root) {
        Stack<Node> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node current = stack.pop();

            if (current.getState().size() == current.getN()) {
                return current; // Tìm thấy giải pháp
            }

            List<Node> neighbours = current.getNeighbours();
            if (neighbours != null) {
                // Đẩy các neighbours vào stack theo thứ tự ngược để ưu tiên đi sâu trước
                for (int i = neighbours.size() - 1; i >= 0; i--) {
                    stack.push(neighbours.get(i));
                }
            }
        }

        return null; // Không tìm thấy giải pháp
    }
}
