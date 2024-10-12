package tuan2;

/**
 * Interface SearchStrategy định nghĩa các phương thức cho thuật toán tìm kiếm.
 */
public interface SearchStrategy {
    /**
     * Thực hiện tìm kiếm để giải bài toán N-Queens.
     *
     * @param root Node gốc của cây tìm kiếm
     * @return Node goal nếu tìm thấy giải pháp, ngược lại null
     */
    Node search(Node root);
}
