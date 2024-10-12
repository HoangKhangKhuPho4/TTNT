package tuan2;

import java.util.Scanner;

/**
 * Lớp Main là điểm khởi đầu của chương trình N-Queens.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = 0;

        // Nhập dữ liệu từ người dùng
        while (true) {
            System.out.print("Enter N (4 <= N <= 8) for N-Queens: ");
            if (scanner.hasNextInt()) {
                N = scanner.nextInt();
                if (N >= 4 && N <= 8) {
                    break;
                } else {
                    System.out.println("N must be between 4 and 8. Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter an integer between 4 and 8.");
                scanner.next(); // Loại bỏ input không hợp lệ
            }
        }
        scanner.close();

        // Tạo đối tượng Queens và giải bài toán
        Queens queens = new Queens(N);

        // Giải bằng Depth-First Search
        System.out.println("\nSolving using Depth-First Search (DFS)...");
        Node dfsSolution = queens.solveDFS();
        System.out.println("DFS Solution:");
        queens.printSolution(dfsSolution);

        // Giải bằng Breadth-First Search
        System.out.println("Solving using Breadth-First Search (BFS)...");
        Node bfsSolution = queens.solveBFS();
        System.out.println("BFS Solution:");
        queens.printSolution(bfsSolution);
    }
}
