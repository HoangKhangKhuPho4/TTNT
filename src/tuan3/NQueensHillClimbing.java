package tuan3;

import java.util.Scanner;

public class NQueensHillClimbing {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = 0;
        while (true) {
            System.out.print("Nhập số lượng quân hậu (N) [4-8]: ");
            try {
                N = Integer.parseInt(scanner.nextLine());
                if (N >= 4 && N <= 8) {
                    break;
                } else {
                    System.out.println("Vui lòng nhập một số từ 4 đến 8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Dữ liệu nhập không hợp lệ. Vui lòng nhập một số nguyên từ 4 đến 8.");
            }
        }

        int maxAttempts = 100; // Số lần thử tối đa
        while (true) {
            System.out.print("Bạn muốn sử dụng Khởi Động Ngẫu Nhiên Nhiều Lần? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y") || choice.equals("yes")) {
                System.out.print("Nhập số lần nỗ lực tối đa (vd: 100): ");
                try {
                    maxAttempts = Integer.parseInt(scanner.nextLine());
                    if (maxAttempts <= 0) {
                        System.out.println("Số lần nỗ lực phải lớn hơn 0. Sử dụng giá trị mặc định: 100");
                        maxAttempts = 100;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Dữ liệu nhập không hợp lệ. Sử dụng giá trị mặc định: 100");
                    maxAttempts = 100;
                }
                break;
            } else if (choice.equals("n") || choice.equals("no")) {
                break;
            } else {
                System.out.println("Lựa chọn không hợp lệ. Vui lòng nhập 'y' hoặc 'n'.");
            }
        }

        scanner.close();

        LocalSearch ls = new LocalSearch();
        if (maxAttempts > 0 && maxAttempts != 1) { // Nếu chọn sử dụng Random Restart
            ls.runWithRandomRestart(N, maxAttempts);
        } else { // Nếu không sử dụng Random Restart
            ls.run(N);
        }
    }
}
