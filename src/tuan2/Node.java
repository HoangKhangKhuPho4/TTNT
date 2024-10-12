package tuan2;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Node đại diện cho một trạng thái trong cây tìm kiếm N-Queens.
 */
public class Node {
    private int n; // Số quân hậu cần đặt
    private List<Integer> state; // Vị trí các quân hậu theo cột
    private List<Node> neighbours; // Danh sách các node con

    /**
     * Constructor cho node gốc (không có quân hậu nào được đặt).
     *
     * @param n Số quân hậu cần đặt
     */
    public Node(int n) {
        this.n = n;
        this.state = new ArrayList<>();
        this.neighbours = new ArrayList<>();
    }

    /**
     * Constructor cho node con với trạng thái cụ thể.
     *
     * @param n     Số quân hậu cần đặt
     * @param state Trạng thái hiện tại của bàn cờ
     */
    public Node(int n, List<Integer> state) {
        this.n = n;
        this.state = new ArrayList<>(state);
        this.neighbours = new ArrayList<>();
    }

    /**
     * Thêm một node con vào danh sách neighbours.
     *
     * @param neighbourNode Node con cần thêm
     */
    public void addNeighbour(Node neighbourNode) {
        this.neighbours.add(neighbourNode);
    }

    /**
     * Kiểm tra tính hợp lệ của trạng thái hiện tại (không có quân hậu nào tấn công nhau).
     *
     * @param state Trạng thái cần kiểm tra
     * @return true nếu hợp lệ, false nếu không
     */
    public boolean isValid(List<Integer> state) {
        int size = state.size();
        if (size == 1) {
            return true; // Chỉ có một quân hậu, luôn hợp lệ
        }
        int lastQueenRow = state.get(size - 1);
        for (int i = 0; i < size - 1; i++) {
            int queenRow = state.get(i);
            if (queenRow == lastQueenRow) {
                return false; // Xung đột hàng
            }
            if (Math.abs(queenRow - lastQueenRow) == size - 1 - i) {
                return false; // Xung đột đường chéo
            }
        }
        return true;
    }

    /**
     * Thử đặt quân hậu mới vào hàng x của cột mới.
     *
     * @param x Hàng muốn đặt quân hậu mới
     * @return Trạng thái mới nếu hợp lệ, ngược lại null
     */
    private List<Integer> place(int x) {
        List<Integer> newState = new ArrayList<>(state);
        newState.add(x);
        if (isValid(newState)) {
            return newState;
        }
        return null;
    }

    /**
     * Lấy danh sách các node con có thể từ trạng thái hiện tại.
     *
     * @return Danh sách các node con, hoặc null nếu đã đặt đủ N quân hậu
     */
    public List<Node> getNeighbours() {
        if (state.size() == n) {
            return null; // Đã đặt đủ N quân hậu
        }
        List<Node> neighboursList = new ArrayList<>();
        for (int x = 0; x < n; x++) {
            List<Integer> newState = place(x);
            if (newState != null) {
                Node newNode = new Node(n, newState);
                addNeighbour(newNode);
                neighboursList.add(newNode);
            }
        }
        return neighboursList;
    }

    // Getter methods
    public int getN() {
        return n;
    }

    public List<Integer> getState() {
        return state;
    }

    public List<Node> getNeighboursList() {
        return neighbours;
    }
}
