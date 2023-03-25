import javax.swing.*;

public class BrickBreakerFrame extends JFrame {
    BrickBreakerFrame(){
        BrickBreakerPanel panel = new BrickBreakerPanel();
        this.add(panel);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();                   //okno dopasuje się rozmiarem do komponentów w środku
        this.setLocationRelativeTo(null);  //okno wyśrodkowane
        this.setTitle("Brick Breaker by JG");

    }
}
