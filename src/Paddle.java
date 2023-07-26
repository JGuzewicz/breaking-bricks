import java.awt.*;
import javax.swing.ImageIcon;
public class Paddle {
    protected int x;
    protected int dx;
    protected int y;
    protected int width;
    protected int height;
    protected Image image;
    public final int MAX_WIDTH = 80;
    public final int DEFAULT_WIDTH = 60;

    public Paddle(int x, int y, int dx) {
        this.x = x;
        this.dx = dx;
        this.y = y;
        loadImage("src/resources/paddle_unarmed_60.png");
        getImageDimensions();
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
