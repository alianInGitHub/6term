package FreshMeats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * Created by anastasia on 3/22/17.
 */
public class FreshMeats {
    enum Position {
        STRAIGHT, LEFT, RIGHT
    }

    //TODO: cyclic barrier of n threads(freshmen) and 1 thread, which show info

    private class Visualizer extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (freshMeats) {
                    for (int i = 0; i < amount; i++) {
                        System.out.print(freshMeats[i].ordinal());
                    }
                    System.out.println('\n');
                }

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class Teacher extends Thread{
        private int fromPosition, toPosition;

        public Teacher(int from, int to) {
            fromPosition = from;
            toPosition = to;
        }

        @Override
        public void run() {
            boolean areRegular = false;
            while (!areRegular) {
                Position[] newFreshMeats = copyFreshMeats();
                for (int i = fromPosition; i < toPosition; i++) {
                    //TODO : regular algorithm
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


                boolean soldiersAreReguled = true;
                for(int i = fromPosition; i < toPosition; i++) {
                    if(freshMeats[i] != newFreshMeats[i - fromPosition]) {
                        freshMeats[i] = newFreshMeats[i - fromPosition];
                        soldiersAreReguled = false;
                    }
                }

                if (soldiersAreReguled) {
                    areRegular = true;
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
            return new Integer(toPosition - fromPosition).toString();
        }
    }

    private Position[] freshMeats;
    private Thread[] threads;
    private int amount;

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
        threads = new Thread[threadsNumber];
        for (int i = 0; i < threadsNumber; i++) {
            int to = (i + 1) * 50;
            if ((i == threadsNumber - 1) && (to - i * 50 < amount - i * 50))
                to = amount;

            threads[i] = new Teacher(i * 50, to);
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

        Thread show = new Visualizer();
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        show.run();

        for (int i = 0; i < threads.length; i++) {
            threads[i].join();
        }
        show.interrupt();

        for (int i = 0; i < amount; i++) {
            System.out.print(freshMeats[i].toString() + ", ");
        }

        System.out.println();
    }


    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.print("Enter amount of solders ( > 100)  >>\t");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int number = Integer.parseInt(br.readLine());
        FreshMeats freshMeats = new FreshMeats(number);

        freshMeats.runThreads();
    }
}
