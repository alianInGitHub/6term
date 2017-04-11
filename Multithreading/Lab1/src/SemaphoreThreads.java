import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by anastasia on 4/11/17.
 */
public class SemaphoreThreads extends BaseThreads{
    private JButton start1Button;
    private JButton start2Button;
    private JButton stop1Button;
    private JButton stop2Button;
    private JSlider slider;
    private JPanel rootPanel;

    private Thread[] threads;
    private volatile int semaphore;

    private SemaphoreThreads() {
        super();
        initialize();
        setUpView(rootPanel);
    }

    private void initialize() {
        initializeSlider(slider);
        initializeButtons();
        initializeThreads();
        addListeners();
    }

    private void initializeButtons() {
        start1Button.setEnabled(true);
        start2Button.setEnabled(true);
        stop1Button.setEnabled(false);
        stop2Button.setEnabled(false);
    }

    private void addListeners() {
        ActionListener listener = e -> {
            if (e.getSource() == start1Button) {
                if (semaphore != 0) {
                    System.out.println("Busy");
                    return;
                }
                semaphore = 1;
                createFirstThread();
                start1Button.setEnabled(false);
                stop1Button.setEnabled(true);
            } else if (e.getSource() == stop1Button) {
                threads[0].interrupt();
                start1Button.setEnabled(true);
                stop1Button.setEnabled(false);
                semaphore = 0;
            } else if (e.getSource() == start2Button) {
                if (semaphore != 0) {
                    System.out.println("Busy");
                    return;
                }
                semaphore = 1;
                createSecondThread();
                start2Button.setEnabled(false);
                stop2Button.setEnabled(true);
            } else if (e.getSource() == stop2Button) {
                threads[1].interrupt();
                start2Button.setEnabled(true);
                stop2Button.setEnabled(false);
                semaphore = 0;
            }
        };

        start1Button.addActionListener(listener);
        stop1Button.addActionListener(listener);

        start2Button.addActionListener(listener);
        stop2Button.addActionListener(listener);
    }

    private void initializeThreads() {
        threads = new Thread[2];
    }

    private void createFirstThread() {
        threads[0] = new Thread(new Task(10));
        threads[0].setPriority(Thread.MIN_PRIORITY);
        threads[0].start();
    }

    private void createSecondThread() {
        threads[1] = new Thread(new Task(90));
        threads[1].setPriority(Thread.MAX_PRIORITY);
        threads[1].start();
    }

    public static void main(String[] args) {
        new SemaphoreThreads();
    }
}
