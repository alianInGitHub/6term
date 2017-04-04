import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by anastasia on 3/28/17.
 */
public class GameOfLife extends JComponent {

    private class DrawProcessor implements Runnable {

        private void unlockAliveCells() {
            if (stopProcess())
                return;
            countAliveCellsInGame = 0;
            for (int i = 0; i < WIDTH / CELL_SIZE; i ++) {
                for (int j = 0; j < HEIGHT / CELL_SIZE; j++) {
                    if (gridLocks[i][j]) {
                        gridLocks[i][j] = false;
                    }
                    if (grid[i][j] != backgroundColor) {
                        if (stopProcess())
                            return;
                        countAliveCellsInGame++;
                        drawCell(i, j);
                    }
                }
            }
        }

        private boolean stopProcess() {
            return countAliveCellsInGame == -1;
        }

        @Override
        public void run() {
            sleep(60);
            unlockAliveCells();
            repaint();
        }
    }

    private class GridProcessor implements Runnable {
        private ArrayList<Point> aliveCells;
        private Color color;

        public GridProcessor(Color color) {
            this.color = color;
        }

        private void generateAliveCells() {
            Random random = new Random();
            int n = WIDTH / CELL_SIZE;
            int m = HEIGHT / CELL_SIZE;
            int amount = random.nextInt(n * m / 5) + m;
            aliveCells = new ArrayList<>(amount);
            for (int count = 0; count < amount; count++) {
                int i = random.nextInt(n / 2) + n / 4;
                int j = random.nextInt(m / 2) + m / 4;
                Point p = new Point(i, j);
                if (!aliveCells.contains(p)) {
                    aliveCells.add(p);
                }
            }
            tryLockGridCells();
        }

        private void update() {
            ArrayList<Point> buffer = (ArrayList<Point>) aliveCells.clone();
            for (int i = 0; i < WIDTH / CELL_SIZE; i ++) {
                for (int j = 0; j < HEIGHT / CELL_SIZE; j++) {
                    useRules(new Point(i, j), buffer);
                }
            }

            setDeadCells(buffer);
            aliveCells = buffer;
            tryLockGridCells();
        }

        private void setDeadCells(ArrayList<Point> buffer) {
            for (Point p : aliveCells) {
                if (!gridLocks[p.x][p.y] && !buffer.contains(p)) {
                    grid[p.x][p.y] = backgroundColor;
                }
            }
        }

        private void tryLockGridCells() {
            for (Point p : aliveCells) {
                if (!gridLocks[p.x][p.y]) {
                    gridLocks[p.x][p.y] = true;
                    grid[p.x][p.y] = color;
                }
            }
        }

        private void useRules(Point gridPosition, ArrayList<Point> buffer) {
            Boolean cellValue = false;
            if (aliveCells.contains(gridPosition))
                cellValue = true;

            int countAliveCells = countAliveCellsInNeighborhood(gridPosition.x, gridPosition.y);

            if (cellValue) {
                if (countAliveCells < 2 || countAliveCells > 3)
                    buffer.remove(gridPosition);
            } else {
                if (countAliveCells == 3) {
                    buffer.add(gridPosition);
                }
            }
        }

        private int countAliveCellsInNeighborhood(int x, int y) {
            int countAliveCells = 0;
            for (int l = -1; l <= 1; l++) {
                for (int k = -1; k <= 1; k++) {
                    if (!((l == k) && (k == 0)) && (aliveCells.contains(new Point(x + l, y + k))))
                        countAliveCells++;
                }
            }
            return countAliveCells;
        }

        @Override
        public void run() {
            generateAliveCells();
            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
                return;
            }
            while (gameIsContinuing()) {
                update();
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    //..............................................CONSTANTS.........................................................//

    private static final int CELL_SIZE = 10;
    static final int HEIGHT = 900;
    static final int WIDTH = 1200;
    static final int MAX_THREADS_AMOUNT = 10;


    //............................................PRIVATE FIELDS......................................................//

    private Color[][] grid;                                           // total layout of cells
    private volatile int countAliveCellsInGame;                       // total current amount of alive cells in game
    private volatile boolean[][] gridLocks;
    private Thread[] threads;
    private CyclicBarrier barrier;

    private Graphics2D graphics2D;
    private Image image;
    private static final Color backgroundColor = Color.DARK_GRAY;
    private Color[] colors;


    //.............................................PUBLIC METHODS.....................................................//

    public GameOfLife() {
        setSize(WIDTH, HEIGHT);
        initializeColors();
        initialize();
    }

    @Override
    public void paintComponent(Graphics g) {
        if(image == null) {
            initializeGraphics();
            drawGrid();
        }
        g.drawImage(image, 0, 0, null);
        drawGrid();
    }

    public void run() {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public void stop() {
        countAliveCellsInGame = -1;
        initialize();
    }

    public void setAmountOfThreads(int amountOfThreads) {
        threads = new Thread[amountOfThreads];
        for (int i = 0; i < amountOfThreads; i++) {
            threads[i] = new Thread(new GridProcessor(colors[i]));
        }
        barrier = new CyclicBarrier(amountOfThreads, new DrawProcessor());
        System.out.println(amountOfThreads);
    }

    //.............................................PRIVATE METHODS....................................................//

    private void initialize() {
        setAmountOfThreads(1);

        grid = new Color[WIDTH / CELL_SIZE][HEIGHT / CELL_SIZE];
        gridLocks = new boolean[WIDTH / CELL_SIZE][HEIGHT / CELL_SIZE];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = backgroundColor;
                gridLocks[i][j] = false;
            }
        }
        countAliveCellsInGame = 0;
    }

    private void initializeColors() {
        colors = new Color[MAX_THREADS_AMOUNT];
        colors[0] = Color.GREEN;
        colors[1] = Color.RED;
        colors[2] = Color.CYAN;
        colors[3] = Color.ORANGE;
        colors[4] = Color.PINK;
        colors[5] = Color.BLUE;
    }

    private void initializeGraphics() {
        image = createImage(getSize().width, getSize().height);
        graphics2D = (Graphics2D) image.getGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clear();
    }

    private void drawGrid(){
        clear();
        graphics2D.setColor(Color.GRAY);

        for (int i = 0; i <= WIDTH; i += CELL_SIZE)
            graphics2D.drawLine(i, 0, i, HEIGHT);
        for (int i = 0; i <= HEIGHT; i += CELL_SIZE)
            graphics2D.drawLine(0, i, WIDTH, i);
    }

    private void drawCell(int i, int j) {
        graphics2D.setPaint(grid[i][j]);
        graphics2D.fillRect(i * CELL_SIZE + 1, j * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
    }

    private boolean gameIsContinuing() {
        return countAliveCellsInGame > 0;
    }


    private void clear() {
        graphics2D.setPaint(backgroundColor);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
