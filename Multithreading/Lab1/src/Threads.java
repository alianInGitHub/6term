import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by anastasia on 3/29/17.
 */
public class Threads extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JSlider slider;
    private JPanel rootPanel;
    private ReentrantLock lock;

    private Thread[] threads;

    /*class FirstTask implements Runnable {

        private void setValue() {
            lock.lock();
            slider.setValue(10);
            lock.unlock();
        }

        @Override
        public void run() {
            for (;;) {
                //System.out.println("set 10");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("stopepd");
                    return;
                }
                setValue();
                repaint();
            }
        }
    }

    class SecondTask implements Runnable {
        private void setValue() {
            lock.lock();
            slider.setValue(90);
            lock.unlock();
        }

        @Override
        public void run() {
            for (;;) {
                //System.out.println("set 90");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("stopped");
                    return;
                }
                setValue();
                repaint();
            }
        }
    }*/

    private class FirstTask extends SwingWorker<Void, Integer> {

        @Override
        protected Void doInBackground() throws Exception {
            return null;
        }


    }

    public Threads() {
        super("Threads");
        initialize();
        setUpView();
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

    private void setUpView() {
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        this.setBounds(500,200,500,350);
        setContentPane(rootPanel);
    }

    private void initialize() {
        initializeSlider();
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
        threads[0] = new Thread(new FirstTask());
        threads[1] = new Thread(new SecondTask());

        threads[0].setPriority((Integer) spinner1.getValue());
        threads[1].setPriority((Integer) spinner2.getValue());
    }

    private void initializeSlider() {
        slider.setMinimum(0);
        slider.setMaximum(100);
        slider.setValue(50);
    }

    private void initializeSpinners() {
        Integer[] list = new Integer[10];
        for (int i = Thread.MIN_PRIORITY; i <= Thread.MAX_PRIORITY; i++)
            list[i - 1] = i;
        spinner1.setModel(new SpinnerListModel(list));
        spinner2.setModel(new SpinnerListModel(list));
    }

    private void addListeners() {
        lock = new ReentrantLock();

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
        JFrame task = new Threads();
    }
}
