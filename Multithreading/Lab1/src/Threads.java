import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by anastasia on 3/29/17.
 */
class Threads extends BaseThreads {
    private JButton startButton;
    private JButton stopButton;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSlider slider;
    private JPanel rootPanel;

    private Thread[] threads;

    Threads() {
        super();
        initialize();
        setUpView(rootPanel);
        spinner1.addChangeListener(e -> {
            if (threads != null) {
                threads[0].setPriority((Integer) spinner1.getValue());
            }
        });
        spinner2.addChangeListener(e -> {
            if (threads != null) {
                threads[1].setPriority((Integer) spinner2.getValue());
            }
        });
    }

    private void initialize() {
        initializeSlider(slider);
        initializeSpinners();
        initializeButtons();
        addListeners();
    }

    private void initializeButtons() {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    private void initializeThreads() {
        threads = new Thread[2];
        threads[0] = new Thread(new Task(10));
        threads[1] = new Thread(new Task(90));

        threads[0].setPriority((Integer) spinner1.getValue());
        threads[1].setPriority((Integer) spinner2.getValue());
    }

    private void initializeSpinners() {
        Integer[] list = new Integer[10];
        for (int i = Thread.MIN_PRIORITY; i <= Thread.MAX_PRIORITY; i++)
            list[i - 1] = i;
        spinner1.setModel(new SpinnerListModel(list));
        spinner2.setModel(new SpinnerListModel(list));
    }

    private void addListeners() {
        ActionListener listener = e -> {
            if (e.getSource() == startButton) {
                initializeThreads();
                for (int i = 0; i < 2; i++) {
                    threads[i].start();
                }
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            } else if (e.getSource() == stopButton) {
                for (int i = 0; i < 2; i++) {
                    threads[i].interrupt();
                }
                slider.setValue(90);
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        };

        startButton.addActionListener(listener);
        stopButton.addActionListener(listener);
    }

    public static void main(String[] args) {
        new Threads();
    }
}
