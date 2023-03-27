import java.awt.*;

public class Brick {
    protected int x;
    protected int y;
    protected int hp;
    public int width=60;
    public int height=25;
    protected boolean visible;

    public Brick(int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
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
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getWidth() {
        return width;
    }
}
