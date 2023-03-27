import java.awt.*;

public class Paddle {
    protected int x;
    protected int dx;
    protected int y;
    protected int width;
    protected int height;
    protected int life;

    public Paddle(int x, int y, int dx, int width) {
        this.x = x;
        this.dx = dx;
        this.y = y;
        this.width = width;
        height = 8;
        life = 3;
    }

    public int  getX() {
        return x;
    }

    public int  getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
