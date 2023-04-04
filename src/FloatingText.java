public class FloatingText {
        protected int x;
        protected int y;
        protected int dy = 1;
        protected int scoreIncrease;
        protected boolean visible;
        long spawnTime = 0L;
    public FloatingText(int x, int y, int scoreIncrease, long spawnTime) {
        this.x = x;
        this.y = y;
        this.scoreIncrease = scoreIncrease;
        this.spawnTime = spawnTime;
        visible = true;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean isVisible() {
        return visible;
    }
    public void move() {
        y -= dy;
    }
}
