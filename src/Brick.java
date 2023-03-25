
public class Brick {
    protected int x;
    protected int y;
    public int width=60;
    public int height=10;
    protected boolean visible;

    public Brick(int x, int y) {
        this.x = x;
        this.y = y;
        visible = true;
    }

    public int  getX() {
        return x;
    }

    public int  getY() {
        return y;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }


}
