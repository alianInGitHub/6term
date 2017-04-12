import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by anastasia on 4/6/17.
 * <p>
 * Class for painting input and output information (vertexes
 * and graph edges) on canvas
 */
public class DrawArea extends JComponent {

    //.............................................VARIABLES..........................................................//

    private ArrayList<Point> points;
    private ArrayList<Point> convexHull;

    private Image image;
    private Graphics2D graphics2D;

    private final int PAINT_RADIUS = 10;

    //.............................................PUBLIC..METHODS....................................................//

    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(e.getPoint());
                drawPoint(points.size() - 1);
            }
        });
    }

    public void clearData() {
        clear();
        initialize();
    }

    public void createConvexHull() {
        if (points.isEmpty()) {
            System.out.println("There are no points");
            return;
        }

        if (points.size() < 4) {
            convexHull = points;
            drawConvexHull();
            return;
        }

        ArrayList<Point> firstArray = new ArrayList<>(points.size() / 2 + 1);
        ArrayList<Point> secondArray = new ArrayList<>(points.size() / 2 + 1);

        for (int i = 0; i < points.size(); i += 2) {
            firstArray.add(points.get(i));
            if (i + 1 < points.size()) {
                secondArray.add(points.get(i + 1));
            }
        }

        ForkJoinPool pool = new ForkJoinPool();
        ConvexHullProcessor processor = new ConvexHullProcessor(firstArray, secondArray);
        pool.invoke(processor);
        convexHull = processor.getConvexHull();

        drawConvexHull();

    }

    //.............................................PRIVATE..METHODS...................................................//

    @Override
    protected void paintComponent(Graphics g) {
        if (image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clearData();
        }
        g.drawImage(image, 0, 0, null);
    }

    private void initialize() {
        points = new ArrayList<>();
        graphics2D.setPaint(Color.black);
    }


    private void drawPoint(Point e) {
        graphics2D.fillOval(e.x, e.y, PAINT_RADIUS, PAINT_RADIUS);
        repaint();
    }

    private void drawPoint(Integer i) {
        graphics2D.fillOval(points.get(i).x, points.get(i).y, PAINT_RADIUS, PAINT_RADIUS);
        graphics2D.drawString(Integer.toString(i), points.get(i).x, points.get(i).y);
        repaint();
    }

    private void drawLine(Point from, Point to) {
        graphics2D.drawLine(from.x, from.y, to.x, to.y);
        repaint();
    }

    private void drawPoints() {
        for (int i = 0; i < points.size(); i++) {
            drawPoint(i);
        }
    }

    private void drawConvexHull() {
        clear();
        drawPoints();
        graphics2D.setPaint(Color.GREEN);
        for (int i = 0; i < convexHull.size(); i++) {
            drawPoint(convexHull.get(i));
            drawLine(convexHull.get(i), convexHull.get((i + 1) % convexHull.size()));
        }
        graphics2D.setPaint(Color.BLACK);
        repaint();
    }

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        graphics2D.setPaint(Color.BLACK);
        repaint();
    }

}
