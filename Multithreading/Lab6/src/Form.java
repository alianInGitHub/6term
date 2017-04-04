import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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

        ArrayList<Integer> threadsRange = new ArrayList<>(GameOfLife.MAX_THREADS_AMOUNT);
        for (int i = 0; i < GameOfLife.MAX_THREADS_AMOUNT; i++) {
            threadsRange.add(i + 1);
        }

        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JSpinner spinner = new JSpinner(new SpinnerListModel(threadsRange.toArray()));
        spinner.addChangeListener(e -> gameOfLife.setAmountOfThreads((Integer) spinner.getValue()));


        ActionListener buttonListener = e -> {
            if (e.getSource() == startButton) {
                if(startButton.getText().equals("Start")) {
                    gameOfLife.run();
                } else if(startButton.getText().equals("Continue")) {
                    //gameOfLife.notify();
                }
            }

            if(e.getSource() == stopButton) {
                gameOfLife.stop();
            }
        };

        startButton.addActionListener(buttonListener);
        stopButton.addActionListener(buttonListener);

        startButton.setBackground(Color.GREEN);
        stopButton.setBackground(Color.RED);

        controls.add(startButton);
        controls.add(stopButton);
        controls.add(spinner);
        controls.add(new JPanel());

        return controls;
    }
}
