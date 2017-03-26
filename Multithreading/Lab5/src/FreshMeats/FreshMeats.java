package FreshMeats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by anastasia on 3/22/17.
 */
public class FreshMeats {
    enum Position {
        STRAIGHT, LEFT, RIGHT
    }

    private Position[] freshMeats;
    private Thread[] threads;
    private int amount;
    private CyclicBarrier barrier;

    private Boolean[] modificationControllers;
    private Boolean arrayWasModified;

    private class Visualizer implements Runnable {
        @Override
        public void run() {
            synchronized (freshMeats) {
                for (int i = 0; i < amount; i++) {
                    System.out.print(freshMeats[i].ordinal());
                }
                System.out.println('\n');
            }

            int count = 0;
            for (; count < modificationControllers.length; count++) {
                if(modificationControllers[count]) {
                    arrayWasModified = true;
                    break;
                }
            }
            int countChangePosition = 0;
            if(count == modificationControllers.length) {
                for (int i = 0; i < amount - 1; i++) {
                    if(freshMeats[i] != freshMeats[i + 1])
                        countChangePosition++;
                }
                if(countChangePosition == 1)
                    arrayWasModified = false;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private class Teacher extends Thread{
        private int fromPosition, toPosition;
        private int personalNumber;

        public Teacher(int from, int to, int personalNumber) {
            fromPosition = from;
            toPosition = to;
            this.personalNumber = personalNumber;
        }

        @Override
        public void run() {
            while (arrayWasModified) {
                Position[] newFreshMeats = copyFreshMeats();
                for (int i = fromPosition; i < toPosition; i++) {
                    switch (freshMeats[i]) {
                        case LEFT:
                            if (i > fromPosition) {
                                newFreshMeats[i - fromPosition] = getNewDirectionLeft(i);
                            }
                            break;
                        case RIGHT:
                            if (i < toPosition - 1) {
                                newFreshMeats[i - fromPosition] = getNewDirectionRight(i);
                            }
                            break;
                        default:
                            break;
                    }
                }

                if (freshMeats[fromPosition] == Position.RIGHT) {
                    newFreshMeats[0] = getNewDirectionRight(fromPosition);
                } else {
                    synchronized (freshMeats) {
                        if (fromPosition > 0) {
                            newFreshMeats[0] = getNewDirectionLeft(fromPosition);
                        }
                    }
                }
                if (freshMeats[toPosition - 1] == Position.LEFT) {
                    newFreshMeats[toPosition - fromPosition - 1] = getNewDirectionLeft(toPosition - 1);
                } else {
                    synchronized (freshMeats) {
                        if(toPosition < amount) {
                            newFreshMeats[toPosition - fromPosition - 1] = getNewDirectionRight(toPosition - 1);
                        }
                    }
                }

                boolean partIsStable = false;
                for(int i = fromPosition; i < toPosition; i++) {
                    if(freshMeats[i] != newFreshMeats[i - fromPosition]) {
                        freshMeats[i] = newFreshMeats[i - fromPosition];
                        partIsStable = true;
                    }
                }

                if (partIsStable) {
                    modificationControllers[personalNumber] = false;
                }

                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }

            }

            System.out.println("Ok");

        }

        private Position[] copyFreshMeats() {
            Position[] array = new Position[toPosition - fromPosition];
            for (int i = 0; i < array.length; i++) {
                array[i] = freshMeats[i + fromPosition];
            }
            return array;
        }

        private synchronized Position getNewDirectionLeft(int i) {
            if(freshMeats[i - 1] != freshMeats[i]) {
                 return Position.RIGHT;
            }
            return Position.LEFT;
        }
        private synchronized Position getNewDirectionRight(int i) {
            if(freshMeats[i] != freshMeats[i + 1]) {
                return Position.LEFT;
            }
            return Position.RIGHT;
        }

        @Override
        public String toString() {
            return Integer.toString(toPosition - fromPosition);
        }
    }


    public FreshMeats(int amount) {
        this.amount = amount;
        initializeFreshMeats();
        initializeThreads();
    }

    private void initializeFreshMeats() {
        freshMeats = new Position[amount];
        for (int i = 0; i < amount; i++) {
            freshMeats[i] = Position.STRAIGHT;
        }
    }

    private void initializeThreads() {
        int threadsNumber = amount / 50;
        this.barrier = new CyclicBarrier(threadsNumber, new Visualizer());
        threads = new Thread[threadsNumber];
        for (int i = 0; i < threadsNumber; i++) {
            int to = (i + 1) * 50;
            if ((i == threadsNumber - 1) && (to - i * 50 < amount - i * 50))
                to = amount;

            threads[i] = new Teacher(i * 50, to, i);
        }

    }

    public void runThreads() throws InterruptedException {

        Random random = new Random();
        for( int i = 0; i< amount; i++) {
            boolean leftRotate = random.nextBoolean();
            if(leftRotate)
                freshMeats[i] = Position.LEFT;
            else
                freshMeats[i] = Position.RIGHT;
        }

        arrayWasModified = true;
        modificationControllers = new Boolean[threads.length];

        for (int i = 0; i < threads.length; i++) {
            modificationControllers[i] = true;
            threads[i].start();
        }

        while (true) {
            for (int i = 0; i < threads.length; i++) {
                if(!threads[i].isAlive()) {
                    break;
                }
            }
            break;
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.print("Enter amount of solders ( > 100)  >>\t");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int number = Integer.parseInt(br.readLine());
        FreshMeats freshMeats = new FreshMeats(number);

        freshMeats.runThreads();
    }
}

