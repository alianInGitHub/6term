import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Created by Анастасия on 12.03.2017.
 */
public class DrawArea extends JComponent {

    private ArrayList<Point> points;
    private LinkedList<Edge> voronnoyDiagram;

    private Image image;
    private Graphics2D graphics2D;

    private final int PAINT_RADIUS = 10;

    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                addPoint(e.getPoint());
                graphics2D.fillOval(e.getX(), e.getY(), PAINT_RADIUS, PAINT_RADIUS);
                repaint();
            }
        });
    }

    public void clearData() {
        clear();
        initialize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clearData();
        }
        g.drawImage(image, 0, 0, null);
    }

    private void  initialize() {
        points = new ArrayList<>();
        graphics2D.setPaint(Color.black);
    }

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }

    private void addPoint(Point p) {
        if (points.isEmpty())
            points.add(p);

        for (int i = 0; i < points.size(); i++) {
            if(isInsideTheArea(p, points.get(i), PAINT_RADIUS, PAINT_RADIUS))
                return;
            if (points.get(i).y > p.y) {
                points.add(i, p);
                return;
            }
        }
        points.add(p);
    }

    private boolean isInsideTheArea(Point p, Point center, int width, int height) {
        float w = width / 2;
        float h = height / 2;
        if(isInRange(p.getX(), center.getX() - w, center.getX() + w) &&
                isInRange(p.getY(), center.getY() - h, center.getY() + h))
            return true;
        return false;
    }

    private boolean isInRange(double value, double from, double to) {
        if((from <= value) && (value <= to))
            return true;
        return false;
    }

    public void buildVoronnoyDiagram() {
        voronnoyDiagram = new LinkedList<>();
        Deque<Event> eventsQueue = new ArrayDeque<>();
        for(int i = 0; i < points.size(); i++) {
            eventsQueue.add(new Event(points.get(i)));
        }
        BeachSearchTree beachSearchTree  = new BeachSearchTree();

        while (!eventsQueue.isEmpty()) {
            Event currentEvent = eventsQueue.poll();
            switch (currentEvent.getEventType()) {
                case POINT_EVENT:
                    beachSearchTree.handlePointEvent(currentEvent, eventsQueue, voronnoyDiagram);
                    break;
                case CIRCLE_EVENT:
                    beachSearchTree.handleCircleEvent(currentEvent, eventsQueue, voronnoyDiagram);
                    break;
            }
        }
        drawDiagram();
    }

    private void drawDiagram() {
        for (int i = 0; i < voronnoyDiagram.size(); i++) {
            Edge e = voronnoyDiagram.get(i);
            graphics2D.drawLine(e.from.x, e.from.y, e.to.x, e.to.y);
        }
        repaint();
    }

    private void showSortedSetOfPoints() {
        if(points == null)
            return;
        for(int i = 0; i < points.size(); i++) {
            System.out.print(points.get(i).toString() + ", ");
        }
        System.out.println();
    }
}
