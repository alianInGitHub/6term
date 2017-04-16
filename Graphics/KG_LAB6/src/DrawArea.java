import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by anastasia on 4/12/17.
 */
public class DrawArea extends JComponent{
    //.............................................VARIABLES..........................................................//

    private ArrayList<Point> points;
    private ArrayList<Point> convexHull;

    private Image image;
    private Graphics2D graphics2D;

    private final int PAINT_RADIUS = 10;

    private enum Mode {
            ADD_POINT, REMOVE_POINT
    }
    private Mode drawingMode;

    //.............................................PUBLIC..METHODS....................................................//

    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (drawingMode == Mode.ADD_POINT) {
                    points.add(e.getPoint());
                    drawPoint(points.size() - 1);
                } else {
                    removePoint(e.getPoint());
                }
            }
        });
    }

    public void setAddingPointsMode() {
        drawingMode = Mode.ADD_POINT;
    }

    public void setRemovingPointsMode() {
        drawingMode = Mode.REMOVE_POINT;
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
        drawingMode = Mode.ADD_POINT;
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

    private void removePoint(Point e) {
        for (int i = 0; i < points.size(); i++) {
            if ()
        }
    }
}
