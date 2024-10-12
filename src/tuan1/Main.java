package tuan1;

public class Main {
    public static void main(String[] args) {
        // Tạo các Node
        Node node40 = new Node(40);
        Node node10 = new Node(10);
        Node node20 = new Node(20);
        Node node30 = new Node(30);
        Node node60 = new Node(60);
        Node node50 = new Node(50);
        Node node70 = new Node(70);

        // Thiết lập các kết nối giữa các Node
        node40.addNeighbour(node10);
        node40.addNeighbour(node20);
        node10.addNeighbour(node30);
        node20.addNeighbour(node10);
        node20.addNeighbour(node30);
        node20.addNeighbour(node60);
        node20.addNeighbour(node50);
        node30.addNeighbour(node60);
        node60.addNeighbour(node70);
        node50.addNeighbour(node70);

        // Thực hiện BFS
        BFS bfsExample = new BFS();
        System.out.println("Thuật toán BFS duyệt đồ thị sử dụng hàng đợi:");
        bfsExample.bfsUsingQueue(node40, 70);

        // Reset thuộc tính visited và parent cho các Node trước khi chạy DFS
        resetNodes(node40, node10, node20, node30, node60, node50, node70);

        // Thực hiện DFS
        DFS dfsExample = new DFS();
        System.out.println("\nThuật toán DFS duyệt đồ thị sử dụng ngăn xếp:");
        dfsExample.dfsUsingDeque(node40, 70);
    }

    // Hàm reset thuộc tính visited và parent cho tất cả các Node
    public static void resetNodes(Node... nodes) {
        for (Node node : nodes) {
            node.setVisited(false);
            node.setParent(null);
        }
    }
}
