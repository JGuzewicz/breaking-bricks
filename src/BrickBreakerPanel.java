import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class BrickBreakerPanel extends JPanel implements ActionListener {

    int SCREEN_WIDTH = 800;
    int SCREEN_HEIGHT = 600;
    static final int DELAY = 20;
    int paddleX;
    int PADDLE_Y = SCREEN_HEIGHT - 30;
    int PADDLE_HEIGHT = 10;
    int paddleWidth;
    int paddleSpeed = 10;
    int paddleMove;
    boolean running = false;
    Timer timer;
    private List<Brick> bricks;
    Ball ball = new Ball(0,0, 5, 5);

    private final int[][] pos = {
            {5, 5}, {70, 5}, {135, 5},
    };

    BrickBreakerPanel() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        startGame();
        this.addKeyListener(new MyKeyAdapter());

    }

    public void startGame() {
        running = true;
        paddleReset();
        ballReset();
        createBricks();
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paddleReset() {
        paddleWidth = 120;
        paddleX = (SCREEN_WIDTH / 2) - (paddleWidth / 2);
    }

    public void ballReset() {

    }

    public void ballFire() {
        ball.stick = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (running) {

            drawObjects(g);

        } else {

            //drawGameOver(g);
        }
    }

    private void drawObjects(Graphics g) {
        g.setColor(Color.RED);
        Rectangle paddle = new Rectangle(paddleX, PADDLE_Y, paddleWidth, PADDLE_HEIGHT);
        g.fillRect((int)paddle.getX(), (int)paddle.getY(), (int)paddle.getWidth(), (int)paddle.getHeight());


        g.setColor(Color.WHITE);
        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                g.fillRect(brick.getX(), brick.getY(), brick.width, brick.height);
            }
        }

        g.setColor(Color.GREEN);
        g.fillOval(ball.getX(), ball.getY(), ball.radius*2, ball.radius*2);
    }

    public void move() {
        paddleX += paddleMove;
        if (ball.stick) {
            ball.x = paddleX + paddleWidth/2 - ball.radius;
            ball.y = PADDLE_Y - ball.radius*2;
        } else {
            ball.x += ball.dx;
            ball.y -= ball.dy;
        }

        if (paddleX < 1) {
            paddleX = 0;
        }
        if (paddleX+paddleWidth > SCREEN_WIDTH) {
            paddleX = SCREEN_WIDTH-paddleWidth;
        }
    }

    public void checkCollisions() {
        if (ball.x + 2*ball.radius > SCREEN_WIDTH || ball.x < 0) {
            ball.dx = -ball.dx;
        }

        if (ball.y < 0) {
            ball.dy = -ball.dy;
        }

       /* Rectangle r1 = new Rectangle(paddleX, PADDLE_Y, paddleWidth, PADDLE_HEIGHT).getBounds();
        Rectangle r2 = ball.getBounds();
        if  (r1.intersects(r2)) {
            ball.dy = -ball.dy;
        }*/
        if (ball.y + ball.radius*2 > PADDLE_Y) {
            ball.dy = -ball.dy;
        }

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    paddleMove = -9;
                    break;
                case KeyEvent.VK_RIGHT:
                    paddleMove = 9;
                    break;
                case KeyEvent.VK_SPACE:
                    ballFire();
                    break;
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    paddleMove = 0;
                    break;
                case KeyEvent.VK_RIGHT:
                    paddleMove = 0;
                    break;
            }
        }
    }

    public void createBricks(){
        bricks = new ArrayList<>();

        for (int[] p : pos) {
            bricks.add(new Brick(p[0], p[1]));
        }
    }

}






