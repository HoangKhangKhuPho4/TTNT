package ai;

import java.util.*;

public class Game {
    private Player player;
    private AIPlayer aiPlayer;
    private GameMap gameMap;
    private List<Bomb> bombs;
    private List<Balloon> balloons;
    private boolean gameOver;
    private boolean gameWon; // Thêm biến này
    private Random rand;
    private int level;

    public Game() {
        gameMap = new GameMap(20, 20);
        player = new Player(10, 10);
        aiPlayer = new AIPlayer(5, 5, gameMap, this);
        bombs = new ArrayList<>();
        balloons = new ArrayList<>();
        gameOver = false;
        gameWon = false; // Khởi tạo biến này
        rand = new Random();
        level = 1;

        initializeBalloons(3);

        System.out.println("Người chơi được khởi tạo tại (" + player.getX() + ", " + player.getY() + ")");
    }

    /**
     * Khởi tạo Balloon
     */
    private void initializeBalloons(int count) {
        int placed = 0;
        boolean chaserPlaced = false;
        while (placed < count) {
            int x = rand.nextInt(gameMap.getWidth());
            int y = rand.nextInt(gameMap.getHeight());
            if (gameMap.getTile(x, y) == ' ' && !(x == player.getX() && y == player.getY())) {
                Balloon.MovementType movementType;
                if (!chaserPlaced) {
                    movementType = Balloon.MovementType.CHASER;
                    chaserPlaced = true;
                } else {
                    movementType = rand.nextBoolean() ? Balloon.MovementType.RANDOM_WALK : Balloon.MovementType.PATROL;
                }
                Balloon balloon = new Balloon(x, y, gameMap, player, movementType);
                balloons.add(balloon);
                placed++;
            }
        }
        System.out.println(count + " Balloon đã được khởi tạo.");
    }

    /**
     * Đặt bom tại vị trí người chơi
     */
    public void placeBomb() {
        // Kiểm tra xem người chơi đã đặt bom tại vị trí này chưa
        for (Bomb bomb : bombs) {
            if (bomb.getX() == player.getX() && bomb.getY() == player.getY()) {
                return; // Đã có bom tại vị trí này
            }
        }
        if (player.placeBomb()) {
            int countdown = 30; // Đặt countdown là 30 để bom nổ sau 3 giây
            bombs.add(new Bomb(player.getX(), player.getY(), countdown, "Player", player.getExplosionRange()));
        }
    }

    /**
     * Cập nhật trạng thái của trò chơi
     */
    public void update() {
        if (gameOver || gameWon) return; // Thêm kiểm tra gameWon

        // Cập nhật bom
        for (int i = bombs.size() - 1; i >= 0; i--) {
            Bomb bomb = bombs.get(i);
            bomb.tick();
            if (bomb.isExploded() && !bomb.isExplosionProcessed()) {
                explodeBomb(bomb.getX(), bomb.getY(), bomb.getExplosionRange());
                bomb.setExplosionProcessed(true);
                if (bomb.getOwner().equals("Player")) {
                    player.increaseBombCount();
                } else if (bomb.getOwner().equals("AIPlayer")) {
                    aiPlayer.increaseBombCount();
                }
            }
            if (bomb.isExplosionFinished()) {
                bombs.remove(i);
            }
        }

        // Cập nhật di chuyển của Balloon
        for (Balloon balloon : balloons) {
            if (balloon.isAlive()) {
                balloon.update();
                if (balloon.checkCollision(player)) {
                    gameOver = true;
                    System.out.println("Người chơi bị Balloon tấn công! Game Over.");
                    break;
                }
            }
        }

        // Cập nhật AIPlayer
        if (aiPlayer.isAlive()) {
            aiPlayer.update();
        }

        // Kiểm tra va chạm giữa AIPlayer và bom
        if (aiPlayer.isAlive() && isAIInExplosion()) {
            aiPlayer.setAlive(false);
            System.out.println("AI bị bom nổ!");
        }

        // Kiểm tra va chạm giữa người chơi và bom
        if (player.isAlive() && isPlayerInExplosion()) {
            gameOver = true;
            System.out.println("Người chơi bị bom nổ! Game Over.");
        }

        // Xử lý các Balloon bị nổ do bom
        handleBombExplosionsOnBalloons();

        // Kiểm tra điều kiện chiến thắng
        if (!aiPlayer.isAlive() && player.isAlive()) {
            gameWon = true;
            System.out.println("Bạn đã chiến thắng!");
        }

        // Kiểm tra điều kiện tăng cấp độ (nếu muốn)
        // if (balloons.isEmpty()) {
        //     levelUp();
        // }
    }

    /**
     * Kiểm tra trạng thái trò chơi
     */
    public boolean isGameOver() {
        return gameOver || gameWon;
    }

    /**
     * Kiểm tra người chơi có thắng không
     */
    public boolean isGameWon() {
        return gameWon;
    }

    /**
     * Xử lý các Balloon bị nổ do bom
     */
    private void handleBombExplosionsOnBalloons() {
        List<Balloon> balloonsToRemove = new ArrayList<>();
        for (Bomb bomb : bombs) {
            if (bomb.isExploded()) {
                List<int[]> explosionTiles = getExplosionTiles(bomb);
                for (Balloon balloon : balloons) {
                    if (balloon.isAlive()) {
                        for (int[] tile : explosionTiles) {
                            if (balloon.getX() == tile[0] && balloon.getY() == tile[1]) {
                                balloon.setAlive(false);
                                balloonsToRemove.add(balloon);
                                System.out.println("Balloon tại (" + balloon.getX() + ", " + balloon.getY() + ") bị nổ.");
                                break;
                            }
                        }
                    }
                }
            }
        }
        balloons.removeAll(balloonsToRemove);
    }

    /**
     * Xử lý nổ bom
     */
    private void explodeBomb(int x, int y, int range) {
        GameMap map = getGameMap();

        List<int[]> explosionTiles = new ArrayList<>();
        explosionTiles.add(new int[]{x, y});

        int[][] directions = {
            {0, -1}, // Lên
            {0, 1},  // Xuống
            {-1, 0}, // Trái
            {1, 0}   // Phải
        };

        for (int[] dir : directions) {
            for (int i = 1; i <= range; i++) {
                int tx = x + dir[0] * i;
                int ty = y + dir[1] * i;
                if (map.getTile(tx, ty) == '#') {
                    break;
                }
                explosionTiles.add(new int[]{tx, ty});
                if (map.getTile(tx, ty) == 'D') {
                    break;
                }
            }
        }

        for (int[] tile : explosionTiles) {
            int tx = tile[0];
            int ty = tile[1];
            handleExplosion(tx, ty);
        }

        // Kích hoạt phản ứng chuỗi
        for (Bomb bomb : bombs) {
            if (!bomb.isExploded()) {
                for (int[] tile : explosionTiles) {
                    if (bomb.getX() == tile[0] && bomb.getY() == tile[1]) {
                        bomb.setCountdown(0); // Kích nổ ngay lập tức
                        break;
                    }
                }
            }
        }
    }

    /**
     * Lấy danh sách các ô bị nổ của một bom
     */
    public List<int[]> getExplosionTiles(Bomb bomb) {
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
                if (gameMap.getTile(tx, ty) == '#') {
                    break;
                }
                explosionTiles.add(new int[]{tx, ty});
                if (gameMap.getTile(tx, ty) == 'D') {
                    break;
                }
            }
        }

        return explosionTiles;
    }

    /**
     * Xử lý từng ô bị nổ
     */
    private void handleExplosion(int x, int y) {
        GameMap map = getGameMap();
        char tile = map.getTile(x, y);
        if (tile == 'D') {
            map.setTile(x, y, ' ');
            System.out.println("Tường phá hủy bị phá hủy tại: (" + x + ", " + y + ")");
        } else if (tile == ' ') {
            System.out.println("Bom nổ tại: (" + x + ", " + y + ")");
        }
    }

    /**
     * Kiểm tra xem AIPlayer có nằm trong phạm vi nổ không
     */
    private boolean isAIInExplosion() {
        for (Bomb bomb : bombs) {
            if (bomb.isExploded()) {
                List<int[]> explosionTiles = getExplosionTiles(bomb);
                for (int[] tile : explosionTiles) {
                    if (aiPlayer.getX() == tile[0] && aiPlayer.getY() == tile[1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem người chơi có nằm trong phạm vi nổ không
     */
    private boolean isPlayerInExplosion() {
        for (Bomb bomb : bombs) {
            if (bomb.isExploded()) {
                List<int[]> explosionTiles = getExplosionTiles(bomb);
                for (int[] tile : explosionTiles) {
                    if (player.getX() == tile[0] && player.getY() == tile[1]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Lấy bản đồ trò chơi
     */
    public GameMap getGameMap() {
        return gameMap;
    }

    /**
     * Lấy danh sách các bom hiện có
     */
    public List<Bomb> getBombs() {
        return bombs;
    }

    /**
     * Lấy danh sách Balloon hiện có
     */
    public List<Balloon> getBalloons() {
        return balloons;
    }

    /**
     * Lấy người chơi
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Lấy AIPlayer
     */
    public AIPlayer getAIPlayer() {
        return aiPlayer;
    }

    /**
     * Di chuyển người chơi
     */
    public void movePlayer(int dx, int dy) {
        if (gameOver || gameWon) return;

        int newX = player.getX() + dx;
        int newY = player.getY() + dy;

        if (newX >= 0 && newX < gameMap.getWidth() && newY >= 0 && newY < gameMap.getHeight()) {
            char tile = gameMap.getTile(newX, newY);
            boolean blocked = false;

            // Kiểm tra va chạm với Balloon
            for (Balloon balloon : balloons) {
                if (balloon.isAlive() && balloon.getX() == newX && balloon.getY() == newY) {
                    blocked = true;
                    break;
                }
            }
            // Kiểm tra va chạm với AIPlayer (không gây chết người chơi)
            if (aiPlayer.isAlive() && aiPlayer.getX() == newX && aiPlayer.getY() == newY) {
                blocked = true;
            }
            if (tile == ' ' && !blocked) {
                player.setX(newX);
                player.setY(newY);

                // Kiểm tra vật phẩm
                Item item = getItemAt(newX, newY);
                if (item != null) {
                    applyItemEffect(item, player);
                    gameMap.removeItem(item);
                    System.out.println("Người chơi đã nhặt vật phẩm: " + item.getType());
                }
            }
        }
    }

    /**
     * Thêm bom vào danh sách bom
     */
    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    /**
     * Lấy vật phẩm tại vị trí (x, y)
     */
    private Item getItemAt(int x, int y) {
        for (Item item : gameMap.getItems()) {
            if (item.getX() == x && item.getY() == y) {
                return item;
            }
        }
        return null;
    }

    /**
     * Áp dụng hiệu ứng của vật phẩm
     */
    private void applyItemEffect(Item item, Player player) {
        if (item.getType() == Item.ItemType.SPEED) {
            player.increaseSpeed();
        } else if (item.getType() == Item.ItemType.EXPLOSION_RANGE) {
            player.increaseExplosionRange();
        }
    }
}
