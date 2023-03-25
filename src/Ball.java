
import javax.swing.*;
import java.awt.*;

public class Ball {
    protected int x;
    protected int dx;
    protected int y;
    protected int dy;

    public int radius = 8;
    protected boolean visible;
    protected boolean stick;

    public Ball(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        visible = true;
        stick = true;
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

    public boolean isStick() {
        return stick;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, radius*2, radius*2);
    }

}