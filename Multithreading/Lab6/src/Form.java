import javax.swing.*;
import java.awt.*;

/**
 * Created by anastasia on 3/28/17.
 */
public class Form {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game of life");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        GameOfLife game = new GameOfLife();
        frame.add(game, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addMouseListener(game);
        frame.addKeyListener(game);
    }
}
