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
    static final int DELAY = 15;
    boolean running = false;
    boolean newGame = true;
    boolean gameOver = false;
    int score;
    int lives = 3;
    boolean missilesActive = false;
    int ammoCount;
    Timer timer;
    private List<Brick> bricks;
    private List<Missile> missiles;
    private List<Bonus> bonuses;
    private List<FloatingText> floatingTexts;
    Ball ball = new Ball(0,0);
    Paddle paddle = new Paddle(1, 1, 0);
    int missileSwitch = 0;
    long actionTime = 0L;

    BrickBreakerPanel() {
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        paddleReset();
        createBricks();
        createMissiles();
        createBonuses();
        createFloatingTexts();
    }
    public void startGame() {
        timer = new Timer(DELAY, this);
        timer.start();
        newGame = false;
        running = true;
    }
    public void newGame() {
        gameOver = false;
        running = true;
        ball.stick = true;
        lives = 3;
        score = 0;
        ball.dx = ball.DEFAULT_DX;
        ball.dy = ball.DEFAULT_DY;
        paddleReset();
        createBricks();
        createMissiles();
        createBonuses();
        createFloatingTexts();
    }
    public void paddleReset() {
        ball.interactable = true;
        paddle.x = (SCREEN_WIDTH / 2) - (paddle.width / 2);
        paddle.y = SCREEN_HEIGHT - 30;
        missilesActive = false;
        ballReset();
    }
    public void ballReset() {
        ball.x = paddle.x + paddle.width/2 - ball.radius;
        ball.y = paddle.y - ball.radius*2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollisions();
            updateBricks();
            updateMissiles();
            updateBonuses();
            updateFloatingText();
            checkDeath();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);



            drawObjects(g);





    }

    private void drawObjects(Graphics g) {
        if (newGame) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 36));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("BREAKING BRICKS", SCREEN_WIDTH/2 - metrics.stringWidth("BREAKING BRICKS")/2, 200);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.drawString("Controls:", 150, 250);
            g.drawString("Move the paddle: [ <- ] and [ -> ]", 150, 275);
            g.drawString("Activate the ball / special ability: [SPACEBAR]" , 150, 300);
            g.drawString("Press [ENTER] to start the game!", 150, 350);
        }
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.BOLD, 45));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("GAME OVER", SCREEN_WIDTH/2 - metrics.stringWidth("GAME OVER")/2, 300);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Press [ENTER] to restart", SCREEN_WIDTH/2 - metrics2.stringWidth("Press [ENTER] to restart")/2, 340);
        }
        if (missilesActive) {
            paddle.loadImage("src/resources/paddle_missiles_60.png");

            g.setColor(Color.WHITE);
            g.setFont(new Font("Consolas", Font.PLAIN, 20));
            g.drawString("AMMO: ", 200, 20);
            for (int i = 0; i < ammoCount; i++) {
                g.drawImage(new ImageIcon("src/resources/missile.png").getImage(), 265 + i*10, 5,this);
            }
        } else {
            paddle.loadImage("src/resources/paddle_unarmed_60.png");
            g.drawImage(new ImageIcon("src/resources/paddle_unarmed_60.png").getImage(), paddle.getX(), paddle.getY(),this);
        }
        g.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),this);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Consolas", Font.PLAIN, 20));
        g.drawString("LIVES: " + lives, 10, 20);
        g.drawString("SCORE: " + score, 650, 20);

        for (Brick brick : bricks) {
            if (brick.isVisible()) {
                brick.loadImage("src/resources/brick_hp"+brick.hp+".png");
                g.drawImage(brick.getImage(), brick.getX(), brick.getY(),this);
            }
        }
        for (Missile missile : missiles) {
            if (missile.isVisible()) {
                g.drawImage(missile.getImage(), missile.getX(), missile.getY(),this);
            }
        }
        for (Bonus bonus : bonuses) {
            if (bonus.isVisible()) {
                g.drawImage(bonus.getImage(), bonus.getX(), bonus.getY(),this);
            }
        }
        for (FloatingText ft : floatingTexts) {
            if (ft.isVisible()) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Consolas", Font.PLAIN, 16));
                g.drawString("+" + ft.scoreIncrease, ft.getX(), ft.getY());
            }
        }
        g.setColor(Color.GREEN);
        g.fillOval((int)ball.getX(), (int)ball.getY(), ball.radius*2, ball.radius*2);
    }

    public void move() {
        paddle.x += paddle.dx;
        if (ball.stick) {
            ballReset();
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
        collisionWithMissile();
        collisionBonusPaddle();
    }
    public void collisionWithBrick() {
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                brick.hp--;
                score += 10;
                floatingTexts.add(new FloatingText(brick.getX() + 10, (int)brick.getY() + 15, 10, System.currentTimeMillis()));
                if (brick.hp == 0) {
                    brick.setVisible(false);
                    int id = 0; // dodać random
                    bonuses.add(new Bonus(brick.x + brick.width/2 - 12, brick.y, id)); //?
                }
                Rectangle intersectBallBrick = ball.getBounds().intersection(brick.getBounds());
                if (intersectBallBrick.width > intersectBallBrick.height) {
                    ball.dy = -ball.dy;
                }
                if (intersectBallBrick.width <= intersectBallBrick.height) {
                    ball.dx = -ball.dx;
                }
            }
        }

    }
    public void collisionWithPaddle() {
        if  (ball.getBounds().intersects(paddle.getBounds()) && ball.interactable) {
            Rectangle intersectBallPaddle = ball.getBounds().intersection(paddle.getBounds());
            if (intersectBallPaddle.width >= intersectBallPaddle.height && intersectBallPaddle.getY() <= paddle.y) {
                double distance = (ball.x + ball.radius) - (paddle.x + (double) paddle.width / 2); //sprawdzam jak daleko od środka kładki uderza piłka
                double v = Math.sqrt(Math.pow(ball.dx,2) + Math.pow(ball.dy,2)); //predkosc pilki przed uderzeniem w kłądkę
                ball.dx = (ball.DEFAULT_DX) * (double) distance / ((double) paddle.width / 2);
                ball.dy = (Math.sqrt(Math.pow(v,2) - Math.pow(ball.dx,2)));
                System.out.println("dx: " + ball.dx + " dy: " + ball.dy + " v: " + v);

            }
            if (intersectBallPaddle.width < intersectBallPaddle.height) {
                ball.interactable = false; //piłka po dotknięciu boku kładki nie ma już wykrywania kolizji
                if ((ball.dx > 0 && intersectBallPaddle.x > paddle.x + paddle.width/2) || (ball.dx < 0 && intersectBallPaddle.x < paddle.x + paddle.width/2)) {
                    ball.dx = 1.2 * ball.dx;
                } else {
                    ball.dx = -ball.dx;
                }
                System.out.println("W " +intersectBallPaddle.width + " H " +intersectBallPaddle.height);
                System.out.println("bok!!!");

            }

        }

    }
    public void collisionWithMissile() {
        for (Brick brick : bricks) {
            for (Missile missile : missiles) {
                if (missile.getBounds().intersects(brick.getBounds())) {
                    brick.hp--;
                    score += 10;
                    floatingTexts.add(new FloatingText(brick.getX() + 10, (int)brick.getY() + 15, 10, System.currentTimeMillis()));
                    missile.setVisible(false);
                    if (brick.hp == 0) {
                        brick.setVisible(false);
                        int id = 0; ///dodać random
                        bonuses.add(new Bonus(brick.x + brick.width/2, brick.y, id));
                    }
                }
            }
        }
    }
    public void collisionBonusPaddle() {
        for (Bonus bonus : bonuses) {
                if (bonus.getBounds().intersects(paddle.getBounds())) {
                    missilesActive = true;
                    ammoCount = 10;
                    score += 30;
                    floatingTexts.add(new FloatingText(paddle.x + 10, paddle.y + 15, 30, System.currentTimeMillis()));
                    bonus.setVisible(false);
                }
                if (bonus.getY() > SCREEN_HEIGHT) {
                    bonus.setVisible(false);
                    System.out.println("bonus lost");
                }

        }
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ENTER:
                    if (newGame) {
                        startGame();
                    } else if (!running) {
                        newGame();
                    }

                    break;
                case KeyEvent.VK_LEFT:
                    if (running) {
                        paddle.dx = -4;
                        break;
                    }
                case KeyEvent.VK_RIGHT:
                    if (running) {
                        paddle.dx = 4;
                        break;
                    }
                case KeyEvent.VK_SPACE:
                    if (running) {
                        ball.stick = false;
                        long currentTime = System.currentTimeMillis();
                        if (missilesActive && currentTime - actionTime >= 100) {
                            actionTime = currentTime;
                            ammoCount--;
                            switch (missileSwitch) {
                                case 0:
                                    missiles.add(new Missile(paddle.x + 4, paddle.y));
                                    missileSwitch = 1;
                                    break;
                                case 1:
                                    missiles.add(new Missile(paddle.x + paddle.width - 10, paddle.y));
                                    missileSwitch = 0;
                                    break;
                            }
                            if (ammoCount == 0) {
                                missilesActive = false;
                            }
                    }


                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT && paddle.dx < 0) {
                paddle.dx = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddle.dx > 0) {
                paddle.dx = 0;
            }
        }
    }

    public void createBricks() {
        bricks = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            for (int j = 0; j <= 15; j++) {
                bricks.add(new Brick(j*50, 30 + i * 20, 4));
            }
        }
    }
    public void createMissiles() {
        missiles = new ArrayList<>();
    }
    public void createBonuses() {
        bonuses = new ArrayList<>();
    }
    public void createFloatingTexts() {
        floatingTexts = new ArrayList<>();
    }
    private void updateBricks() {
        for (int i = 0; i < bricks.size(); i++) {
            Brick b = bricks.get(i);
            if (!b.isVisible()) {
                bricks.remove(i);
            }
        }
    }
    private void updateMissiles() {
        for (int i = 0; i < missiles.size(); i++) {
            Missile m = missiles.get(i);
            if (!m.isVisible()) {
                missiles.remove(i);
            }
            else {
                m.move();
            }
        }
    }
    private void updateBonuses() {
        for (int i = 0; i < bonuses.size(); i++) {
            Bonus b = bonuses.get(i);
            if (!b.isVisible()) {
                bonuses.remove(i);
            }
            else {
                b.move();
            }
        }
    }
    private void updateFloatingText() {
        for (int i = 0; i < floatingTexts.size(); i++) {
            FloatingText ft = floatingTexts.get(i);
            ft.move();
            if (System.currentTimeMillis() - ft.spawnTime >= 750) {
                floatingTexts.remove(i);
            }
        }
    }
    public void checkDeath() {
        if (ball.y > SCREEN_HEIGHT) {
            lives--;
            if (lives == 0) {
                running = false;
                gameOver = true;
            } else {
                ball.stick = true;
                ball.dx = ball.DEFAULT_DX;
                ball.dy = ball.DEFAULT_DY;
                paddleReset();
            }
        }
    }
}






