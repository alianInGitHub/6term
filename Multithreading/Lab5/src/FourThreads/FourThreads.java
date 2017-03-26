package FourThreads;

import java.io.*;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by anastasia on 3/26/17.
 */
public class FourThreads {

    private StringProcessor[] threads;
    private CyclicBarrier barrier;
    private boolean toStop;

    private class Controller implements Runnable {

        @Override
        public void run() {
            //System.out.println();
            for (int i = 0; i < 4; i++) {
                int countAEqualLines = 0;
                int countBEqualLines = 0;
                for (int j = 0; j < 4; j++) {
                    if (i != j) {
                        if (threads[i].countALetter == threads[j].countALetter){
                            countAEqualLines++;
                        } else if (threads[i].countBLetter == threads[j].countBLetter) {
                            countBEqualLines++;
                        }
                    }
                }

                if(Math.max(countAEqualLines, countBEqualLines) >= 3) {
                    toStop = true;
                }
            }
        }
    }

    private class StringProcessor extends Thread {
        private char[] line;
        private Integer countALetter;
        private Integer countBLetter;

        public StringProcessor(String line) {
            this.line = line.toCharArray();
            countABLettersInLine();
        }

        private char changeLetter(char letter) throws Exception {
            switch (letter){
                case 'A' : countALetter--; return 'C';
                case 'C' : countALetter++; return 'A';
                case 'B' : countBLetter--; return 'D';
                case 'D' : countBLetter++; return 'B';
            }
            throw new Exception("Invalid input. The string must consist of A, B, C and D letters.");
        }

        private void countABLettersInLine() {
            countALetter = 0;
            countBLetter = 0;
            for (int i = 0; i < line.length; i++) {
                if (line[i] == 'A') {
                    countALetter++;
                } else if (line[i] == 'B') {
                    countBLetter++;
                }
            }
        }

        @Override
        public void run() {
            Random random = new Random();
            boolean toChange;
            while (true) {
                for (int i = 0; i < line.length; i++) {
                    try {
                        toChange = random.nextBoolean();
                        if(toChange) {
                            line[i] = changeLetter(line[i]);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    if (toStop) {
                        return;
                    }

                }
            }
        }

    }

    private String[] loadStringsFromFile(String filePath) {
        String[] strings = new String[4];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

            for(int i = 0; i < 4; i++) {
                strings[i] = reader.readLine();
            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return strings;
    }

    public void run() {
        threads = new StringProcessor[4];
        barrier = new CyclicBarrier(4, new Controller());
        toStop = false;
        String[] strings = loadStringsFromFile("data.txt");

        for(int i = 0; i < 4; i++) {
            threads[i] = new StringProcessor(strings[i]);
            threads[i].start();
        }

        for(int i = 0; i < 4; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < 4; i++) {
            System.out.println(Integer.toString(threads[i].countALetter) + "\t" +
                    Integer.toString(threads[i].countBLetter) + "\t" + String.valueOf(threads[i].line));
        }

    }

    private static void fillFileWithRandomLetters(String filepath) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
            Random random = new Random();
            int n = random.nextInt(50) + 50;
            for (int count = 0;count < 4; count++) {
                for (int i = 0; i < n; i++) {
                    int choice = random.nextInt(4);
                    switch (choice) {
                        case 0:
                            writer.write('A');
                            break;
                        case 1:
                            writer.write('B');
                            break;
                        case 2:
                            writer.write('C');
                            break;
                        case 3:
                            writer.write('D');
                            break;
                    }
                }
                writer.newLine();
            }
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //Uncomment to set new data
        //fillFileWithRandomLetters("data.txt");
        
        FourThreads task = new FourThreads();
        task.run();
    }
}
