import javax.swing.*;
import java.awt.*;

/**
 * Created by anastasia on 4/4/17.
 */
public class Form {
    public static void main (String[] args) {
        JFrame frame = new JFrame("Hunter game");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        HunterGame game = new HunterGame();
        content.add(game, BorderLayout.CENTER);

        frame.setSize(HunterGame.WIDTH + 5, HunterGame.HEIGHT + 70);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //game.start();
    }
}
