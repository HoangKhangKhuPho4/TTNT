package tuan2;

import java.util.List;

/**
 * Lớp Queens quản lý việc giải bài toán N-Queens bằng DFS và BFS.
 */
public class Queens {
    private int n; // Số quân hậu cần đặt

    /**
     * Constructor.
     *
     * @param n Số quân hậu cần đặt
     */
    public Queens(int n) {
        this.n = n;
    }

    /**
     * Giải bài toán N-Queens bằng Depth-First Search.
     *
     * @return Node goal nếu tìm thấy giải pháp, ngược lại null
     */
    public Node solveDFS() {
        SearchStrategy dfs = new DFS();
        Node root = new Node(n);
        return dfs.search(root);
    }

    /**
     * Giải bài toán N-Queens bằng Breadth-First Search.
     *
     * @return Node goal nếu tìm thấy giải pháp, ngược lại null
     */
    public Node solveBFS() {
        SearchStrategy bfs = new BFS();
        Node root = new Node(n);
        return bfs.search(root);
    }

    /**
     * In ra giải pháp dưới dạng bàn cờ và danh sách vị trí các quân hậu.
     *
     * @param goal Node goal chứa giải pháp
     */
    public void printSolution(Node goal) {
        if (goal == null) {
            System.out.println("No solution found.");
            return;
        }

        List<Integer> state = goal.getState();

        // In bàn cờ
        System.out.println("Solution as Board:");
        for (int i = 0; i < n; i++) { // Duyệt từng hàng
            for (int j = 0; j < n; j++) { // Duyệt từng cột
                if (state.get(j) == i) {
                    System.out.print("Q ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }

        // In danh sách vị trí
        System.out.print("Positions: ");
        for (int i = 0; i < state.size(); i++) {
            System.out.print(state.get(i));
            if (i < state.size() - 1) {
                System.out.print(",");
            }
        }
        System.out.println("\n");
    }
}
