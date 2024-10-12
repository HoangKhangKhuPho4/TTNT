package tuan3;

import java.util.*;

public class LocalSearch {

    // Tính tổng số xung đột ngang (cùng hàng)
    public int checkHorizontal(Node node) {
        int conflicts = 0;
        List<Integer> state = node.state;
        int n = node.n;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (state.get(i) == state.get(j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    // Tính tổng số xung đột chéo
    public int checkDiagonal(Node node) {
        int conflicts = 0;
        List<Integer> state = node.state;
        int n = node.n;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(state.get(i) - state.get(j)) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    // Hàm heuristic: tổng số xung đột ngang và chéo
    public int heuristic(Node node) {
        return checkHorizontal(node) + checkDiagonal(node);
    }

    // Cố gắng di chuyển một quân hậu trong cột 'y' tới hàng 'x' và trả về heuristic của trạng thái mới
    public int tryMovingOneQueen(Node node, int x, int y) {
        List<Integer> newState = new ArrayList<Integer>(node.state);
        newState.set(y, x);
        Node newNode = new Node(node.n, newState);
        return heuristic(newNode);
    }

    // Tạo tất cả các trạng thái lá bằng cách di chuyển mỗi quân hậu tới mọi hàng khác trong cùng cột
    public SortedMap<Integer, Node> generateNeighbours(Node node) {
        SortedMap<Integer, Node> neighbours = new TreeMap<>();
        int n = node.n;
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                if (node.state.get(col) != row) { // Chỉ di chuyển nếu hàng mới khác hàng hiện tại
                    int currentHeuristic = tryMovingOneQueen(node, row, col);
                    // Nếu heuristic chưa tồn tại trong map, thêm node mới
                    if (!neighbours.containsKey(currentHeuristic)) {
                        List<Integer> newState = new ArrayList<Integer>(node.state);
                        newState.set(col, row);
                        Node neighbourNode = new Node(n, newState);
                        neighbours.put(currentHeuristic, neighbourNode);
                    }
                }
            }
        }
        return neighbours;
    }

    // Thêm phương thức Random Restart
    public void runWithRandomRestart(int n, int maxAttempts) {
        int attempt = 0;
        while (attempt < maxAttempts) {
            Node initial = new Node(n); // Tạo trạng thái ban đầu ngẫu nhiên
            System.out.println("Nỗ lực " + (attempt + 1) + ": Trạng thái ban đầu: " + initial.state + " | Heuristic: " + heuristic(initial));

            if (heuristic(initial) == 0) { // Nếu trạng thái ban đầu là giải pháp
                System.out.println("Đã tìm thấy trạng thái goal ngay lập tức!");
                printState(initial);
                return;
            }

            Node current = initial;
            SortedMap<Integer, Node> neighbours = generateNeighbours(current);
            Integer bestHeuristic = neighbours.firstKey();

            while (bestHeuristic < heuristic(current)) { // Tiếp tục nếu tìm được trạng thái tốt hơn
                current = neighbours.get(bestHeuristic);
                System.out.println("Di chuyển đến trạng thái: " + current.state + " | Heuristic: " + bestHeuristic);
                neighbours = generateNeighbours(current);
                bestHeuristic = neighbours.firstKey();
            }

            // Sau khi leo đồi
            if (heuristic(current) == 0) { // Nếu tìm được giải pháp
                System.out.println("Đã tìm thấy trạng thái goal: " + current.state);
                printState(current);
                return;
            } else {
                System.out.println("Không thể tìm thấy trạng thái goal trong nỗ lực này. Heuristic hiện tại: " + heuristic(current));
            }

            attempt++;
            System.out.println("======================================");
        }

        System.out.println("Đã đạt đến giới hạn số lần nỗ lực (" + maxAttempts + ") mà không tìm thấy giải pháp.");
    }

    // Thực hiện thuật toán Leo Đồi (không sử dụng Random Restart)
    public void run(int n) {
        Node initial = new Node(n); // Tạo trạng thái ban đầu ngẫu nhiên
        if (heuristic(initial) == 0) { // Kiểm tra xem trạng thái ban đầu đã là goal chưa
            System.out.println("Đã tìm thấy trạng thái goal ngay lập tức!");
            printState(initial);
            return;
        }
        System.out.println("Trạng thái ban đầu: " + initial.state + " | Heuristic: " + heuristic(initial));

        Node current = initial;
        SortedMap<Integer, Node> neighbours = generateNeighbours(current);
        Integer bestHeuristic = neighbours.firstKey();

        while (bestHeuristic < heuristic(current)) { // Tiếp tục nếu tìm được trạng thái tốt hơn
            current = neighbours.get(bestHeuristic);
            System.out.println("Di chuyển đến trạng thái: " + current.state + " | Heuristic: " + bestHeuristic);
            neighbours = generateNeighbours(current);
            bestHeuristic = neighbours.firstKey();
        }

        // Sau khi leo đồi
        if (heuristic(current) == 0) {
            System.out.println("Đã tìm thấy trạng thái goal: " + current.state);
            printState(current);
        } else {
            System.out.println("Không thể tìm thấy trạng thái goal! Trạng thái tốt nhất tìm được: " + current.state + " | Heuristic: " + heuristic(current));
        }
    }

    // Phương thức in trạng thái bàn cờ một cách trực quan
    public void printState(Node node) {
        int n = node.n;
        List<Integer> state = node.state;
        System.out.println("Trạng thái bàn cờ:");
        for (int row = 0; row < n; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < n; col++) {
                if (state.get(col) == row) {
                    sb.append("Q ");
                } else {
                    sb.append(". ");
                }
            }
            System.out.println(sb.toString());
        }
    }
}
