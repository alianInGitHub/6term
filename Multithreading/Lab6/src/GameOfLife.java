import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by anastasia on 3/28/17.
 */
public class GameOfLife extends JComponent implements Runnable{
    private boolean[][] grid;
    private int countAliveCellsInGame;
    private ArrayList<ArrayList<Point>> aliveCellsLists;
    private Graphics2D graphics2D;
    private Image image;

    private static final Color backgroundColor = Color.DARK_GRAY;
    private static final int CELL_SIZE = 12;
    static final int HEIGHT = 720;
    static final int WIDTH = 960;
    static final int MAX_THREADS_AMOUNT = 10;


    public GameOfLife() {
        setSize(WIDTH, HEIGHT);
        initialize();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectCell(e.getPoint());
            }
        });
    }

    private boolean isBetween(int y1, int y2, int value) {
        if( ((y1 <= value) && (value <= y2)) || ((y2 <= value) && (value <= y1)))
            return true;
        return false;
    }

    public void selectCell(Point locationOnCanvas) {
        for (int i = 0; i < WIDTH / CELL_SIZE; i ++) {
            for (int j = 0; j < HEIGHT / CELL_SIZE; j ++) {
                if ( isBetween(i * CELL_SIZE, (i + 1) * CELL_SIZE, locationOnCanvas.x) &&
                        isBetween(j * CELL_SIZE, (j + 1) * CELL_SIZE, locationOnCanvas.y)) {
                    grid[i][j] = true;
                    int threadID = 0;
                    aliveCellsLists.get(threadID).add(new Point(i, j));
                    countAliveCellsInGame++;
                    break;
                }
            }
        }
        repaint();
    }

    private void drawGrid(){

        clear();
        graphics2D.setColor(Color.GRAY);

        for (int i = 0; i <= WIDTH; i += CELL_SIZE)
            graphics2D.drawLine(i, 0, i, HEIGHT);
        for (int i = 0; i <= HEIGHT; i += CELL_SIZE)
            graphics2D.drawLine(0, i, WIDTH, i);

        graphics2D.setColor(Color.GREEN);
        for (int i = 0; i < WIDTH / CELL_SIZE; i ++) {
            for (int j = 0; j < HEIGHT / CELL_SIZE; j++) {
                if (aliveCellsLists.get(0).contains(new Point(i, j)))
                    graphics2D.fillRect(i * CELL_SIZE + 1, j * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        if(image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }

        drawGrid();
        g.drawImage(image, 0, 0, null);

    }

    private void initialize() {
        grid = new boolean[WIDTH / CELL_SIZE][HEIGHT / CELL_SIZE];
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = false;
            }
        }

        aliveCellsLists = new ArrayList<>(MAX_THREADS_AMOUNT);
        aliveCellsLists.add(new ArrayList<>(grid.length * grid[0].length));
        countAliveCellsInGame = 0;
    }

    private void useRule(Point gridPosition, ArrayList<Point> aliveCellsList) {
        Boolean cellValue = false;
        if (aliveCellsList.contains(gridPosition))
            cellValue = true;

        int countAliveCells = 0;
        for (int l = -1; l <= 1; l++) {
            for (int k = -1; k <= 1; k++) {
                if (!((l == k) && (k == 0)) && (aliveCellsList.contains(new Point(gridPosition.x + l, gridPosition.y + k))))
                    countAliveCells++;
            }
        }

        if (cellValue) {
            if (countAliveCells < 2 || countAliveCells > 3)
                aliveCellsList.remove(gridPosition);
        } else {
            if (countAliveCells == 3) {
                aliveCellsList.add(gridPosition);
            }
        }
    }

    private boolean gameIsContinuing() {
        if (countAliveCellsInGame == 0)
            return false;
        return true;
    }

    @Override
    public void run() {
        int n = WIDTH / CELL_SIZE;
        int m = HEIGHT / CELL_SIZE;
        while (gameIsContinuing()) {
            for (int i = 1; i < n - 1; i++) {
                for (int j = 1; j < m - 1; j++) {
                    useRule(new Point(i, j), aliveCellsLists.get(0));
                }
            }
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clear() {
        graphics2D.setPaint(backgroundColor);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    public void pause() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        // interrupt threads
    }

    public void setAmountOfThreads(int amountOfThreads) {
        aliveCellsLists = new ArrayList<>(MAX_THREADS_AMOUNT);
        for (int i = 0; i < amountOfThreads; i++) {
            aliveCellsLists.add(new ArrayList<>());
        }
        System.out.println(amountOfThreads);
    }
}
