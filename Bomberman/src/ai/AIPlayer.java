package ai;

import java.util.*;

public class AIPlayer {
    private int x, y;
    private GameMap map;
    private Game game;
    private boolean alive;
    private int ticksUntilMove;
    private int moveDelay;
    private Random rand;
    private int bombCount;
    private int explosionRange;
    private int speed;

    public AIPlayer(int startX, int startY, GameMap map, Game game) {
        this.x = startX;
        this.y = startY;
        this.map = map;
        this.game = game;
        this.alive = true;
        this.moveDelay = 3;
        this.ticksUntilMove = moveDelay;
        this.rand = new Random();
        this.bombCount = 1;
        this.explosionRange = 1;
        this.speed = 1;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    public int getBombCount() { return bombCount; }
    public void increaseBombCount() { bombCount++; }

    public int getExplosionRange() { return explosionRange; }
    public void increaseExplosionRange() { explosionRange++; }

    public int getSpeed() { return speed; }
    public void increaseSpeed() { if (speed < 5) speed++; }

    /**
     * Cập nhật trạng thái của AIPlayer
     */
    public void update() {
        if (!alive) return;

        ticksUntilMove--;
        if (ticksUntilMove <= 0) {
            makeDecision();
            ticksUntilMove = moveDelay;
        }
    }

    /**
     * Đưa ra quyết định hành động cho AI
     */
    private void makeDecision() {
        // Ưu tiên 1: Tránh bom nếu có bom nguy hiểm gần đó
        if (isBombDangerNearby()) {
            moveToSafeZone();
            return;
        }

        // Ưu tiên 2: Thu thập vật phẩm nếu có vật phẩm giá trị gần đó
        Item valuableItem = findValuableItem();
        if (valuableItem != null) {
            moveToItem(valuableItem);
            return;
        }

        // Ưu tiên 3: Theo dõi và tấn công người chơi
        if (shouldChasePlayer()) {
            moveToPlayer();
            return;
        }

        // Ưu tiên 4: Cố gắng phá hủy tường phá hủy gần đó
        if (attemptToDestroyWall()) {
            placeBomb();
            moveToSafeZone();
            return;
        }

        // Di chuyển ngẫu nhiên
        moveRandomly();
    }

    /**
     * Kiểm tra xem có bom nguy hiểm gần đó không
     */
    private boolean isBombDangerNearby() {
        for (Bomb bomb : game.getBombs()) {
            List<int[]> explosionTiles = game.getExplosionTiles(bomb);
            for (int[] tile : explosionTiles) {
                if (tile[0] == x && tile[1] == y) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Di chuyển đến khu vực an toàn
     */
    private void moveToSafeZone() {
        Pathfinding pathfinding = new Pathfinding(map, game.getBombs());
        List<int[]> safePath = pathfinding.findSafePath(x, y);
        if (safePath.size() > 0) {
            int[] nextStep = safePath.get(0);
            x = nextStep[0];
            y = nextStep[1];
        } else {
            // Nếu không tìm thấy đường an toàn, di chuyển ngẫu nhiên
            moveRandomly();
        }
    }

    /**
     * Tìm vật phẩm có giá trị gần đó
     */
    private Item findValuableItem() {
        List<Item> items = map.getItems();
        Item bestItem = null;
        int minDistance = Integer.MAX_VALUE;

        for (Item item : items) {
            int distance = Math.abs(x - item.getX()) + Math.abs(y - item.getY());
            // Tính giá trị của vật phẩm
            int itemValue = evaluateItem(item);

            // Ưu tiên vật phẩm có giá trị cao và khoảng cách gần
            if (itemValue > 0 && distance < minDistance) {
                minDistance = distance;
                bestItem = item;
            }
        }
        // Chỉ quan tâm nếu vật phẩm ở trong phạm vi 5 ô
        if (minDistance <= 5) {
            return bestItem;
        }
        return null;
    }

    /**
     * Đánh giá giá trị của vật phẩm
     */
    private int evaluateItem(Item item) {
        if (item.getType() == Item.ItemType.SPEED && speed < 5) {
            return 10; // Giá trị cao nếu tốc độ chưa đạt tối đa
        } else if (item.getType() == Item.ItemType.EXPLOSION_RANGE) {
            return 8;
        }
        return 0;
    }

    /**
     * Di chuyển đến vị trí vật phẩm
     */
    private void moveToItem(Item item) {
        Pathfinding pathfinding = new Pathfinding(map);
        List<int[]> path = pathfinding.findPath(x, y, item.getX(), item.getY());
        if (path.size() > 0) {
            int[] nextStep = path.get(0);
            x = nextStep[0];
            y = nextStep[1];

            // Kiểm tra nếu đã đến vị trí vật phẩm
            if (x == item.getX() && y == item.getY()) {
                applyItemEffect(item);
                map.removeItem(item);
                System.out.println("AI đã nhặt vật phẩm: " + item.getType());
            }
        } else {
            moveRandomly();
        }
    }

    /**
     * Áp dụng hiệu ứng của vật phẩm
     */
    private void applyItemEffect(Item item) {
        if (item.getType() == Item.ItemType.SPEED) {
            increaseSpeed();
            // Giảm moveDelay để di chuyển nhanh hơn
            if (moveDelay > 1) moveDelay--;
        } else if (item.getType() == Item.ItemType.EXPLOSION_RANGE) {
            increaseExplosionRange();
        }
    }

    /**
     * Kiểm tra xem có nên theo đuổi người chơi hay không
     */
    private boolean shouldChasePlayer() {
        Player player = game.getPlayer();
        int distance = Math.abs(x - player.getX()) + Math.abs(y - player.getY());
        return distance <= 10; // Chỉ theo đuổi nếu người chơi ở trong phạm vi 10 ô
    }

    /**
     * Di chuyển đến vị trí người chơi
     */
    private void moveToPlayer() {
        Player player = game.getPlayer();
        Pathfinding pathfinding = new Pathfinding(map);
        List<int[]> path = pathfinding.findPath(x, y, player.getX(), player.getY());

        if (path.size() > 0 && path.size() <= 10) {
            int[] nextStep = path.get(0);
            x = nextStep[0];
            y = nextStep[1];

            // Nếu ở gần người chơi, đặt bom
            if (path.size() <= 2) {
                placeBomb();
                moveAwayFromPlayer();
            }
        } else {
            moveRandomly();
        }
    }

    /**
     * Đặt bom
     */
    private void placeBomb() {
        if (bombCount <= 0) return;

        // Kiểm tra xem đã có bom tại vị trí này chưa
        for (Bomb bomb : game.getBombs()) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return; // Đã có bom tại vị trí này
            }
        }

        bombCount--;
        game.addBomb(new Bomb(x, y, 30, "AIPlayer", explosionRange)); // Sử dụng explosionRange của AI
        System.out.println("AI đã đặt bom tại: (" + x + ", " + y + ")");
    }

    /**
     * Di chuyển tránh xa người chơi
     */
    private void moveAwayFromPlayer() {
        Player player = game.getPlayer();
        int dx = x - player.getX();
        int dy = y - player.getY();

        int newX = x + (dx != 0 ? dx / Math.abs(dx) : 0);
        int newY = y + (dy != 0 ? dy / Math.abs(dy) : 0);

        if (map.isWalkable(newX, newY)) {
            x = newX;
            y = newY;
        } else {
            moveRandomly();
        }
    }

    /**
     * Cố gắng phá hủy tường phá hủy gần đó
     */
    private boolean attemptToDestroyWall() {
        int[][] directions = {
            {0, -1}, // Lên
            {0, 1},  // Xuống
            {-1, 0}, // Trái
            {1, 0}   // Phải
        };
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (map.getTile(newX, newY) == 'D') {
                return true;
            }
        }
        return false;
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

        // Trộn ngẫu nhiên hướng đi
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
     * Kiểm tra va chạm với người chơi
     */
    public boolean checkCollision(Player player) {
        return this.x == player.getX() && this.y == player.getY();
    }
}
