package ai;

import java.util.*;

public class Pathfinding {
    private GameMap map;
    private List<Bomb> bombs;

    public Pathfinding(GameMap map) {
        this.map = map;
        this.bombs = new ArrayList<>();
    }

    public Pathfinding(GameMap map, List<Bomb> bombs) {
        this.map = map;
        this.bombs = bombs;
    }

    /**
     * Tìm đường từ (startX, startY) đến (goalX, goalY) sử dụng thuật toán A*
     */
    public List<int[]> findPath(int startX, int startY, int goalX, int goalY) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Map<String, Node> allNodes = new HashMap<>();

        Node start = new Node(startX, startY, null, 0, heuristic(startX, startY, goalX, goalY));
        openList.add(start);
        allNodes.put(start.key(), start);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.x == goalX && current.y == goalY) {
                return constructPath(current);
            }

            for (int[] dir : getDirections()) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (!map.isWalkable(newX, newY)) continue;
                if (allNodes.containsKey(Node.key(newX, newY))) continue;

                int tentativeG = current.g + 1;

                Node neighbor = new Node(newX, newY, current);
                neighbor.g = tentativeG;
                neighbor.f = neighbor.g + heuristic(newX, newY, goalX, goalY);

                allNodes.put(neighbor.key(), neighbor);
                openList.add(neighbor);
            }
        }

        return Collections.emptyList(); // Không tìm thấy đường
    }

    /**
     * Tìm đường đến khu vực an toàn
     */
    public List<int[]> findSafePath(int startX, int startY) {
        // Tìm đường đến ô an toàn gần nhất
        Queue<Node> queue = new LinkedList<>();
        Map<String, Node> allNodes = new HashMap<>();

        Node start = new Node(startX, startY, null);
        queue.add(start);
        allNodes.put(start.key(), start);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (isSafe(current.x, current.y)) {
                // Tìm thấy ô an toàn
                return constructPath(current);
            }

            for (int[] dir : getDirections()) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];

                if (!map.isWalkable(newX, newY)) continue;
                if (allNodes.containsKey(Node.key(newX, newY))) continue;

                Node neighbor = new Node(newX, newY, current);
                allNodes.put(neighbor.key(), neighbor);
                queue.add(neighbor);
            }
        }

        return Collections.emptyList(); // Không tìm thấy đường an toàn
    }

    private boolean isSafe(int x, int y) {
        for (Bomb bomb : bombs) {
            List<int[]> explosionTiles = getExplosionTiles(bomb);
            for (int[] tile : explosionTiles) {
                if (tile[0] == x && tile[1] == y) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<int[]> getExplosionTiles(Bomb bomb) {
        List<int[]> explosionTiles = new ArrayList<>();
        explosionTiles.add(new int[]{bomb.getX(), bomb.getY()});

        int[][] directions = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0}
        };

        for (int[] dir : directions) {
            for (int i = 1; i <= bomb.getExplosionRange(); i++) {
                int tx = bomb.getX() + dir[0] * i;
                int ty = bomb.getY() + dir[1] * i;
                if (map.getTile(tx, ty) == '#') {
                    break;
                }
                explosionTiles.add(new int[]{tx, ty});
                if (map.getTile(tx, ty) == 'D') {
                    break;
                }
            }
        }

        return explosionTiles;
    }

    private List<int[]> constructPath(Node node) {
        List<int[]> path = new ArrayList<>();
        while (node.parent != null) {
            path.add(new int[]{node.x, node.y});
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }

    private int heuristic(int x, int y, int goalX, int goalY) {
        // Sử dụng khoảng cách Manhattan
        return Math.abs(x - goalX) + Math.abs(y - goalY);
    }

    private int[][] getDirections() {
        return new int[][]{
            {0, -1}, // Lên
            {0, 1},  // Xuống
            {-1, 0}, // Trái
            {1, 0}   // Phải
        };
    }

    private static class Node {
        int x, y;
        Node parent;
        int g; // Chi phí từ đầu đến node hiện tại
        int f; // Tổng chi phí (g + h)

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.g = Integer.MAX_VALUE;
            this.f = Integer.MAX_VALUE;
        }

        Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.g = 0;
            this.f = 0;
        }

        Node(int x, int y, Node parent, int g, int f) {
            this.x = x;
            this.y = y;
            this.parent = parent;
            this.g = g;
            this.f = f;
        }

        String key() {
            return key(this.x, this.y);
        }

        static String key(int x, int y) {
            return x + "," + y;
        }
    }
}
