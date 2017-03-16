import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Анастасия on 03.03.2017.
 */

public class DrawArea extends JComponent {

    enum DrawingMode {
        DRAWING_POLYGON, LOCATING_POINT
    }

    private class Edge {
        int fromId;
        int toId;

        public Edge (int from , int to) {
            this.fromId = from;
            this.toId = to;
        }
    }

    private ArrayList<Edge> edges;
    private ArrayList<Point> polygonVertexes;
    private DrawingMode mode;


    final int RADIUS_PAINT = 8;
    private int VERTEX_AMOUNT;

    private Image image;
    private Graphics2D graphics2D;

    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                switch (mode) {
                    case DRAWING_POLYGON:
                        graphics2D.setPaint(Color.black);
                        polygonVertexes.add(e.getPoint());
                        drawPoint(e.getX(), e.getY());
                        break;
                    case LOCATING_POINT:
                        if (polygonContains(e.getPoint())) {
                            graphics2D.setPaint(Color.green);
                        } else {
                            graphics2D.setPaint(Color.red);
                        }
                        drawPoint(e.getX(), e.getY());
                        break;
                }
            }
        });
    }


    public void setMode(DrawingMode mode) {
        if(this.mode != mode) {
            if(this.mode == DrawingMode.LOCATING_POINT) {
                clear();
                initialize();
            } else {
                this.mode = mode;
                initializeGraph();
                paintPolygon();
            }
        }
    }

    private void initialize() {
        polygonVertexes = new ArrayList<>();
        mode = DrawingMode.DRAWING_POLYGON;
        VERTEX_AMOUNT = 0;
    }

    private void initializeGraph() {
        VERTEX_AMOUNT = polygonVertexes.size();
        edges = new ArrayList<>(VERTEX_AMOUNT - 1);
        for(int i = 0; i < VERTEX_AMOUNT; i++) {
            edges.add(i, new Edge(i, (i + 1) % VERTEX_AMOUNT));
        }
    }


    private void drawPoint( int x, int y) {
        graphics2D.fillOval(x, y, RADIUS_PAINT, RADIUS_PAINT);
        repaint();
    }

    private void paintPolygon() {
        graphics2D.setPaint(Color.black);
        for(int i = 0; i < VERTEX_AMOUNT; i++ ) {
            Edge e = edges.get(i);
            graphics2D.drawLine(polygonVertexes.get(e.fromId).x, polygonVertexes.get(e.fromId).y,
                    polygonVertexes.get(e.toId).x, polygonVertexes.get(e.toId).y);
        }
        repaint();
    }

    private boolean polygonContains(Point p) {
        int count = 0;
        for(int i = 0; i < VERTEX_AMOUNT; i++) {
            Point from = polygonVertexes.get(edges.get(i).fromId);
            Point to = polygonVertexes.get(edges.get(i).toId);
            if(from.y > to.y) {
                Point c = from;
                from = to;
                to = c;
            }

            if(isOnTheLeftSide(new Point(p.x - from.x, p.y  - from.y), new Point(to.x - from.x, to.y - from.y))
                    && isBetween(p.y, from.y, to.y))  {
                count++;
            }
        }
        if(count % 2 != 0)
            return true;
        return false;
    }

    private boolean isBetween(int y, int y1, int y2) {
        if(((y1 <= y) && (y < y2)) || ((y2 <= y) && (y < y1)))
            return true;
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(image == null) {
            image = createImage(getSize().width, getSize().height);
            graphics2D = (Graphics2D) image.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
            initialize();
        }
        g.drawImage(image, 0, 0, null);
    }

    private boolean isOnTheLeftSide(Point vectorA, Point vectorB) {
        int res = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        if(res <= 0) {
            return false;
        }
        return true;
    }

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }
}
