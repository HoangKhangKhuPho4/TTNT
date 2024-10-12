package tuan3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
    int n; // Kích thước của bàn cờ (N x N)
    List<Integer> state; // state.get(i) đại diện cho vị trí hàng của quân hậu trong cột i

    // Constructor để tạo trạng thái ban đầu ngẫu nhiên
    public Node(int n) {
        this.n = n;
        this.state = new ArrayList<Integer>();
        Random r = new Random();
        for (int i = 0; i < n; i++) {
            this.state.add(r.nextInt(n));
        }
    }

    // Constructor để tạo một node với trạng thái cụ thể
    public Node(int n, List<Integer> state) {
        this.n = n;
        this.state = new ArrayList<Integer>(state);
    }
}
