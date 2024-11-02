package ai;

public class Player {
    private int x, y;
    private int bombCount;
    private int speed;
    private int explosionRange;
    private boolean alive;

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.bombCount = 1;
        this.speed = 1;
        this.explosionRange = 1;
        this.alive = true;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    /**
     * Đặt bom
     */
    public boolean placeBomb() {
        if (bombCount > 0) {
            bombCount--;
            System.out.println("Người chơi đã đặt một quả bom tại: (" + x + ", " + y + ")");
            return true;
        }
        return false;
    }

    /**
     * Tăng số lượng bom
     */
    public void increaseBombCount() {
        bombCount++;
    }

    /**
     * Lấy số lượng bom hiện tại
     */
    public int getBombCount() {
        return bombCount;
    }

    /**
     * Tăng tốc độ di chuyển
     */
    public void increaseSpeed() {
        if (speed < 5) {
            speed++;
        }
    }

    /**
     * Lấy tốc độ di chuyển
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Tăng phạm vi nổ
     */
    public void increaseExplosionRange() {
        explosionRange++;
    }

    /**
     * Lấy phạm vi nổ hiện tại
     */
    public int getExplosionRange() {
        return explosionRange;
    }
}
