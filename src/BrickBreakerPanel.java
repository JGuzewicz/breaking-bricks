import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class BrickBreakerPanel extends JPanel implements ActionListener {

    int SCREEN_WIDTH = 600;
    int SCREEN_HEIGHT = 600;
    static final int DELAY = 15;
    boolean running = false;
    Timer timer;
    private List<Brick> bricks;
    Ball ball = new Ball(0,0);
    Paddle paddle = new Paddle(1, 1, 0, 1);

//    private final int[][] pos = null;
//            {
//            {10, 5}, {89, 5}, {168, 5}, {247, 5}, {326, 5}, {405, 5}, {484, 5}, {563, 5}, {642, 5}, {721, 5}
//    };

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
        createBricks();
        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paddleReset() {
        ball.interactable = true;
        paddle.width = 60;
        paddle.x = (SCREEN_WIDTH / 2) - (paddle.width / 2);
        paddle.y = SCREEN_HEIGHT - 30;
    }

    public void ballFire() {
        ball.stick = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
            updateBricks();
            checkDeath();
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
        g.fillRect((int)paddle.getX(), (int)paddle.getY(), (int)paddle.width, (int)paddle.height);


        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                Color color = null;
                switch (brick.hp) {
                    case 1:
                        color = Color.green;
                        break;
                    case 2:
                        color = Color.orange;
                        break;
                    case 3:
                        color = Color.red;
                        break;
                }
                g.setColor(color);
                g.fillRect(brick.getX()+1, brick.getY()+1, brick.width-2, brick.height-2);

            }
        }

        g.setColor(Color.GREEN);
        g.fillOval((int)ball.getX(), (int)ball.getY(), ball.radius*2, ball.radius*2);
    }

    public void move() {
        paddle.x += paddle.dx;
        if (ball.stick) {
            ball.x = paddle.x + paddle.width/2 - ball.radius;
            ball.y = paddle.y - ball.radius*2;
        } else {
            ball.x += ball.dx;
            ball.y -= ball.dy;
        }

        if (paddle.x < 1) {
            paddle.x = 0;
        }
        if (paddle.x + paddle.width > SCREEN_WIDTH) {
            paddle.x = SCREEN_WIDTH - paddle.width;
        }
    }

    public void checkCollisions() {
        if (ball.x + 2*ball.radius > SCREEN_WIDTH || ball.x < 0) {  //kolizja piłki ze ścianami bocznymi
            ball.dx = -ball.dx;
        }

        if (ball.y < 0) {  // kolizja piłki z sufitem
            ball.dy = -ball.dy;
        }

        collisionWithPaddle();
        collisionWithBrick();

    }
    public void collisionWithBrick() {
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                brick.hp--;
                if (brick.hp == 0) {
                    brick.setVisible(false);
                }
                Rectangle intersectBallBrick = ball.getBounds().intersection(brick.getBounds());
                if (intersectBallBrick.width > intersectBallBrick.height) {
                    ball.dy = -ball.dy;
                }
                if (intersectBallBrick.width < intersectBallBrick.height) {
                    ball.dx = -ball.dx;
                }
                if (intersectBallBrick.width == intersectBallBrick.height) {
//                    ball.dx = -ball.dx;
//                    ball.dy = -ball.dy;
                    System.out.println("kant!");

                }

            }
        }

    }
    public void collisionWithPaddle() {

        if  (ball.getBounds().intersects(paddle.getBounds()) && ball.interactable) {
            Rectangle intersectBallPaddle = ball.getBounds().intersection(paddle.getBounds());
            if (intersectBallPaddle.width >= intersectBallPaddle.height && intersectBallPaddle.getY() <= paddle.y) {
                double distance = (ball.x + ball.radius) - (paddle.x + (double) paddle.width / 2);
//            System.out.println("dystans:" + distance);
                double distancePercent = (double) distance / ((double) paddle.width / 2) * 100;
//            System.out.println("dystans w %:" + distancePercent);

                if (distancePercent >= -100 && distancePercent < -80) {
                    ball.dx = -ball.DEFAULT_DX;
                }
                if (distancePercent >= -80 && distancePercent < -60) {
                    ball.dx = -ball.DEFAULT_DX * 0.8;
                }
                if (distancePercent >= -60 && distancePercent < -40) {
                    ball.dx = -ball.DEFAULT_DX * 0.6;
                }
                if (distancePercent >= -40 && distancePercent < -20) {
                    ball.dx = -ball.DEFAULT_DX * 0.4;
                }
                if (distancePercent >= -20 && distancePercent < 0) {
                    ball.dx = -ball.DEFAULT_DX * 0.2;
                }
                if (distancePercent >= 0 && distancePercent < 20) {
                    ball.dx = ball.DEFAULT_DX * 0.2;
                }
                if (distancePercent >= 20 && distancePercent < 40) {
                    ball.dx = ball.DEFAULT_DX * 0.4;
                }
                if (distancePercent >= 40 && distancePercent < 60) {
                    ball.dx = ball.DEFAULT_DX * 0.6;
                }
                if (distancePercent >= 60 && distancePercent < 80) {
                    ball.dx = ball.DEFAULT_DX * 0.8;
                }
                if (distancePercent >= 80 && distancePercent <= 100) {
                    ball.dx = ball.DEFAULT_DX;
                }
                ball.dy = -ball.dy;
//            System.out.println("dx: " + ball.dx + " dy: " + ball.dy);

            }
            if (intersectBallPaddle.width < intersectBallPaddle.height) {
                ball.interactable = false; //piłka po dotknięciu boku kładki nie ma już wykrywania kolizji
                ball.dx = -ball.dx;
                System.out.println("W " +intersectBallPaddle.width + " H " +intersectBallPaddle.height);
                System.out.println("bok!!!");

            }

        }

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    paddle.dx = -4;
                    break;
                case KeyEvent.VK_RIGHT:
                    paddle.dx = 4;
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
                    paddle.dx = 0;
                    break;
                case KeyEvent.VK_RIGHT:
                    paddle.dx = 0;
                    break;
            }
        }
    }

    public void createBricks() {
        bricks = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 9; j++) {

                bricks.add(new Brick(j*60, i * 25, i+1));
            }
        }
    }

    private void updateBricks() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            if (!b.isVisible()) {
                bricks.remove(i);
            }
        }
    }

    public void checkDeath() {
        if (ball.y > SCREEN_HEIGHT) {
            ball.stick = true;
            ball.dx = ball.DEFAULT_DX;
            ball.dy = ball.DEFAULT_DY;
            paddleReset();
        }
    }



}






