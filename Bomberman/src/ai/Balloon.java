package ai;

import java.util.*;

public class Balloon {
    public enum MovementType {
        CHASER,     // Balloon đuổi theo người chơi
        RANDOM_WALK, // Balloon di chuyển ngẫu nhiên
        PATROL       // Balloon tuần tra theo một đường dẫn cố định
    }

    private int x, y;
    private GameMap map;
    private Player player;
    private boolean alive;
    private int ticksUntilMove;
    private int moveDelay;
    private MovementType movementType;
    private Random rand;
    private List<int[]> patrolPath;
    private int patrolIndex;
    private int changePatternCounter;

    public Balloon(int startX, int startY, GameMap map, Player player, MovementType movementType) {
        this.x = startX;
        this.y = startY;
        this.map = map;
        this.player = player;
        this.alive = true;
        this.moveDelay = 4;
        this.ticksUntilMove = moveDelay;
        this.movementType = movementType;
        this.rand = new Random();

        if (movementType == MovementType.PATROL) {
            initializePatrolPath();
        }

        this.changePatternCounter = rand.nextInt(50) + 50;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    /**
     * Cập nhật trạng thái của Balloon
     */
    public void update() {
        if (!alive) return;

        ticksUntilMove--;
        if (ticksUntilMove <= 0) {
            move();
            ticksUntilMove = moveDelay;
        }

        if (movementType != MovementType.CHASER) {
            changePatternCounter--;
            if (changePatternCounter <= 0) {
                switchMovementPattern();
                changePatternCounter = rand.nextInt(50) + 50;
            }
        }
    }

    /**
     * Di chuyển Balloon dựa trên kiểu di chuyển
     */
    private void move() {
        switch (movementType) {
            case CHASER:
                moveTowardsPlayer();
                break;
            case RANDOM_WALK:
                moveRandomly();
                break;
            case PATROL:
                moveAlongPatrolPath();
                break;
        }
    }

    /**
     * Di chuyển theo đường ngắn nhất đến người chơi
     */
    private void moveTowardsPlayer() {
        Pathfinding pathfinding = new Pathfinding(map);
        List<int[]> path = pathfinding.findPath(x, y, player.getX(), player.getY());

        if (path.size() > 0) {
            int[] nextStep = path.get(0);
            x = nextStep[0];
            y = nextStep[1];
        } else {
            moveRandomly();
        }
    }

    /**
     * Di chuyển ngẫu nhiên
     */
    private void moveRandomly() {
        int[][] directions = {
            {0, -1},
            {0, 1},
            {-1, 0},
            {1, 0}
        };

        List<int[]> dirList = Arrays.asList(directions);
        Collections.shuffle(dirList, rand);

        for (int[] dir : dirList) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (map.isWalkable(newX, newY)) {
                x = newX;
                y = newY;
                break;
            }
        }
    }

    /**
     * Di chuyển theo đường tuần tra
     */
    private void moveAlongPatrolPath() {
        if (patrolPath == null || patrolPath.isEmpty()) {
            moveRandomly();
            return;
        }

        patrolIndex = (patrolIndex + 1) % patrolPath.size();
        int[] nextPosition = patrolPath.get(patrolIndex);

        if (map.isWalkable(nextPosition[0], nextPosition[1])) {
            x = nextPosition[0];
            y = nextPosition[1];
        } else {
            moveRandomly();
        }
    }

    /**
     * Khởi tạo đường tuần tra cho Balloon
     */
    private void initializePatrolPath() {
        patrolPath = new ArrayList<>();
        // Ví dụ: Tuần tra một hình vuông nhỏ xung quanh vị trí ban đầu
        int[][] pathPoints = {
            {x, y},
            {x + 1, y},
            {x + 1, y + 1},
            {x, y + 1}
        };
        for (int[] point : pathPoints) {
            if (map.isWalkable(point[0], point[1])) {
                patrolPath.add(point);
            }
        }
        patrolIndex = 0;
    }

    /**
     * Chuyển đổi mẫu di chuyển
     */
    private void switchMovementPattern() {
        if (movementType == MovementType.RANDOM_WALK) {
            movementType = MovementType.PATROL;
            initializePatrolPath();
        } else if (movementType == MovementType.PATROL) {
            movementType = MovementType.RANDOM_WALK;
        }
    }

    /**
     * Kiểm tra va chạm với người chơi
     */
    public boolean checkCollision(Player player) {
        return this.x == player.getX() && this.y == player.getY();
    }
}
