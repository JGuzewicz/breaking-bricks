import javax.swing.*;
import java.awt.*;

public class Brick {
    protected int x;
    protected int y;
    protected int hp;
    public int width;
    public int height;
    protected boolean visible;
    protected Image image;

    public Brick(int x, int y, int hp) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        visible = true;
        loadImage("src/resources/brick_hp"+hp+".png");
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
    protected void getImageDimensions() {

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
}