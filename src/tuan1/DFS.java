package tuan1;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class DFS {
    public void dfsUsingDeque(Node initial, int goal) {
        Deque<Node> stack = new ArrayDeque<>();
        initial.setVisited(true);
        stack.push(initial);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
            System.out.print(current.getState() + " "); // In trạng thái hiện tại

            if (current.getState() == goal) {
                // Nếu tìm thấy goal, thực hiện backtrack để tìm đường đi
                StringBuilder path = new StringBuilder();
                Node node = current;
                while (node != null) {
                    path.insert(0, node.getState() + " ");
                    node = node.getParent();
                }
                System.out.println("\nĐường đi từ " + initial.getState() + " đến " + goal + ": " + path.toString().trim());
                return;
            }

            // Duyệt qua các neighbour của current (đảo ngược để duyệt theo thứ tự mong muốn)
            List<Node> neighbours = current.getNeighbours();
            for (int i = neighbours.size() - 1; i >= 0; i--) {
                Node neighbour = neighbours.get(i);
                if (!neighbour.isVisited()) {
                    neighbour.setVisited(true);
                    neighbour.setParent(current);
                    stack.push(neighbour);
                }
            }
        }
        System.out.println("Không tìm thấy đường đi đến goal.");
    }
}
