package tuan1;

import java.util.LinkedList;
import java.util.Queue;

public class BFS {
    public void bfsUsingQueue(Node initial, int goal) {
        Queue<Node> queue = new LinkedList<>();
        initial.setVisited(true);
        queue.add(initial);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
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

            // Duyệt qua các neighbour của current
            for (Node neighbour : current.getNeighbours()) {
                if (!neighbour.isVisited()) {
                    neighbour.setVisited(true);
                    neighbour.setParent(current);
                    queue.add(neighbour);
                }
            }
        }
        System.out.println("Không tìm thấy đường đi đến goal.");
    }
}
