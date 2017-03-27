import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by anastasia on 3/28/17.
 */
public class Form {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game of life");
        Container content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        GameOfLife game = new GameOfLife();
        content.add(game, BorderLayout.CENTER);
        content.add(createControlPanel(game), BorderLayout.NORTH);

        frame.setSize(GameOfLife.WIDTH + 5, GameOfLife.HEIGHT + 70);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static JPanel createControlPanel(GameOfLife gameOfLife) {
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));

        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton stopButton = new JButton("Stop");

        ActionListener listener = e -> {
            if (e.getSource() == startButton) {
                if(startButton.getName() == "Start") {
                    gameOfLife.run();
                } else if(startButton.getName() == "Continue") {
                    gameOfLife.notify();
                }
            }
            if (e.getSource() == pauseButton) {
                gameOfLife.pause();
                startButton.setName("Continue");
            }

            if(e.getSource() == stopButton) {
                gameOfLife.notify();
                gameOfLife.stop();
            }
        };

        startButton.addActionListener(listener);
        pauseButton.addActionListener(listener);
        stopButton.addActionListener(listener);

        startButton.setBackground(Color.GREEN);
        pauseButton.setBackground(Color.ORANGE);
        stopButton.setBackground(Color.RED);

        controls.add(startButton);
        controls.add(pauseButton);
        controls.add(stopButton);

        return controls;
    }
}
