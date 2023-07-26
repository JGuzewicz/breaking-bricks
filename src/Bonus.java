import javax.swing.*;
import java.awt.*;

public class Bonus {
    protected int x;
    protected int y;
    protected int dy = 3;
    protected int id; // 0 - missiles, 1 - ....
    public int width;
    public int height;
    protected boolean visible;
    protected Image image;

    public Bonus(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
        visible = true;
        loadImage("src/resources/bonus_" + id + ".png");
        getImageDimensions();
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
    public void getImageDimensions() {

        width = image.getWidth(null);
        height = image.getHeight(null);
    }

    protected void loadImage(String imageName) {
        ImageIcon ii = new ImageIcon(imageName);
        image = ii.getImage();
    }

    public Image getImage() {
        return image;
    }
    public void move() {
        y += dy;
    }
}