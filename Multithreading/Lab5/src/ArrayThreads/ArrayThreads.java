package ArrayThreads;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by anastasia on 4/2/17.
 */
public class ArrayThreads {
    private class ArrayTask extends Thread {
        private int arrayLength;
        private long sum;

        ArrayTask(int arrayLength) {
            this.arrayLength = arrayLength;
        }


        private void generateArray() {
            sum = 0;
            Random random = new Random();
            for (int i = 0; i < arrayLength; i++) {
                sum += random.nextInt(5);
            }
        }

        private void changeRandomElement() {
            Random random = new Random();
            if (random.nextBoolean()) {
                sum++;
            } else if(sum > 0) {
                sum--;
            } else {
                sum++;
            }
        }

        @Override
        public void run() {
            generateArray();
            for (;;) {
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                if (toStop)
                    return;
                changeRandomElement();
            }
        }
    }

    private class Controller implements Runnable {

        @Override
        public void run() {
            if ((tasks[0].sum == tasks[1].sum) && (tasks[0].sum == tasks[2].sum)) {
                toStop = true;
            }
            iterations++;
        }
    }

    private CyclicBarrier barrier;
    private ArrayTask[] tasks;
    private boolean toStop;
    private long iterations;

    public ArrayThreads() {
        tasks = new ArrayTask[3];
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            tasks[i] = new ArrayTask(random.nextInt(5) + 5);
        }
        toStop = false;
        barrier = new CyclicBarrier(3, new Controller());
    }

    public void start() throws InterruptedException {
        for (int i = 0; i< 3; i++) {
            tasks[i].start();
        }
        for (int i = 0; i< 3; i++) {
            tasks[i].join();
        }
        for (int i = 0; i< 3; i++) {
            System.out.print(tasks[i].sum + "\t");
        }
        System.out.println("\nItterations\t" + iterations);
    }

    public static void main (String[] args) throws InterruptedException {
        ArrayThreads threads = new ArrayThreads();
        threads.start();
    }
}
