package ai;

public class Bomb {
    private int x, y;
    private int countdown;
    private boolean exploded;
    private int explosionDuration;
    private boolean explosionProcessed;
    private String owner; // "Player" hoặc "AIPlayer"
    private int explosionRange;

    public Bomb(int x, int y, int countdown, String owner, int explosionRange) {
        this.x = x;
        this.y = y;
        this.countdown = countdown;
        this.exploded = false;
        this.explosionDuration = 1;
        this.explosionProcessed = false;
        this.owner = owner;
        this.explosionRange = explosionRange;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getCountdown() { return countdown; }
    public String getOwner() { return owner; }
    public int getExplosionRange() { return explosionRange; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setCountdown(int countdown) { this.countdown = countdown; }

    /**
     * Cập nhật trạng thái của bom
     */
    public void tick() {
        if (!exploded) {
            if (countdown > 0) {
                countdown--;
            }
            if (countdown == 0) {
                exploded = true;
            }
        } else {
            if (explosionDuration > 0) {
                explosionDuration--;
            }
        }
    }

    public boolean isExploded() { return exploded; }
    public boolean isExplosionFinished() { return exploded && explosionDuration == 0; }
    public boolean isExplosionProcessed() { return explosionProcessed; }
    public void setExplosionProcessed(boolean explosionProcessed) { this.explosionProcessed = explosionProcessed; }
}
