
import javax.swing.*;
import java.awt.*;

public class Ball {
    protected double x;
    protected double dx;
    protected double DEFAULT_DX=4;
    protected double y;
    protected double DEFAULT_DY=4;
    protected double dy;
    protected int radius = 5;
    protected boolean visible;
    protected boolean stick;
    protected boolean interactable;

    public Ball(int x, int y) {
        this.x = x;
        this.y = y;
        this.dx = DEFAULT_DX;
        this.dy = DEFAULT_DY;
        visible = true;
        stick = true;
        interactable = false;
    }

    public double  getX() {
        return x;
    }

    public double  getY() {
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
        return new Rectangle((int)x, (int)y, radius*2, radius*2);
    }

}