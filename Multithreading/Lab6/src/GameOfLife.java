import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by anastasia on 3/28/17.
 */
public class GameOfLife extends JComponent implements Runnable {
    private ArrayList<ArrayList<Boolean>> grid;
    private Graphics2D graphics2D;
    private Image image;

    private static final Color backgroundColor = Color.DARK_GRAY;
    private static final int CELL_SIZE = 12;
    public static final int HEIGHT = 720;
    public static final int WIDTH = 960;

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

    public void selectCell(Point locationOnCanvas) {

    }

    private void drawGrid(){

        clear();
        graphics2D.setColor(Color.GRAY);

        for (int i = CELL_SIZE; i < WIDTH; i += CELL_SIZE)
            graphics2D.drawLine(i, 0, i, HEIGHT);
        for (int i = CELL_SIZE; i < HEIGHT; i += CELL_SIZE)
            graphics2D.drawLine(0, i, WIDTH, i);

        graphics2D.setColor(Color.GREEN);
        for (int i = 0; i < WIDTH / CELL_SIZE; i ++) {
            for (int j = 0; j < HEIGHT / CELL_SIZE; j++) {
                if (grid.get(i).get(j))
                    graphics2D.fillRect(i * CELL_SIZE + 1, j * CELL_SIZE + 1, 9, 9);
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
        grid = new ArrayList<>();
        for (int i = 0; i < 80; i++){
            grid.add(new ArrayList<>());
            for (int j = 0; j < 60; j++) {
                grid.get(i).add(false);
            }
        }
    }

    @Override
    public void run() {

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
}
