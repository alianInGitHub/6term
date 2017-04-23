import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class for drawing on canvas
 * Created by Анастасия on 03.03.2017.
 */
public class DrawArea extends JComponent {

    private final int PAINT_RADIUS = 10;

    public enum DrawingMode {
        GRAPH_VERTEXES, GRAPH_EDGES, POINTS, NONE
    }

    public enum METHOD {
        STRIPS, CHAINS, NONE
    }

    private DrawingMode currentMode;
    private METHOD currentLocationMethod;
    private Image image;
    private Graphics2D graphics2D;

    private ArrayList<Point> sortedVertexes;        // vertexes are sorted by increasing y-value coordinate
    private LinkedList<Edge> edges;
    private int VERTEX_AMOUNT = 0;
    private Point fromVertex = null;
    private int fromVertexId;
    private Point currentPoint = null;

    private StripsMethod strips;
    private ChainsMethod chains;


    public DrawArea() {
        initialize();
        addMouseListener();
    }

    private void initialize() {
        edges = new LinkedList<>();
        sortedVertexes = new ArrayList<>();
        currentMode = DrawingMode.NONE;
        currentLocationMethod = METHOD.NONE;
        setDoubleBuffered(false);
    }

    private void addMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                chooseAction(e);
            }
        });
    }

    private void chooseAction(MouseEvent e) {
        switch (currentMode) {
            case GRAPH_VERTEXES:
                createGraphVertex(e.getX(), e.getY());
                break;
            case GRAPH_EDGES:
                createGraphEdge(e.getX(), e.getY());
                break;
            case POINTS:
                createAndLocatePointOnCanvas(e.getX(), e.getY());
                break;
            default: break;
        }
    }

    private void createAndLocatePointOnCanvas(int x, int y) {
        eraseCurrentPoint();
        if(currentLocationMethod != METHOD.NONE) {
            currentPoint = new Point(x, y);
            drawPoint(x, y, PAINT_RADIUS);
            showInfo();
        }
    }

    private void showInfo() {
        String info = locatePointAndGetInfo();
        System.out.println(info);
    }

    private String locatePointAndGetInfo() {
        if(currentLocationMethod == METHOD.STRIPS)
            return strips.locate(currentPoint);
        else
            return chains.locate(currentPoint);
    }

    public void setCurrentLocationMethod(METHOD currentLocationMethod) {
        this.currentLocationMethod = currentLocationMethod;
    }

    public void setCurrentMode(DrawingMode newMode) {
        switch (newMode) {
            case GRAPH_VERTEXES:
                graphics2D.setPaint(Color.black);
                break;
            case GRAPH_EDGES:
                graphics2D.setPaint(Color.red);
                break;
            case POINTS:
                graphics2D.setPaint(Color.green);
                break;
            case NONE:
                switchFromNoneMode();
        }
        this.currentMode = newMode;
    }

    private void switchFromNoneMode() {
        if (currentMode == DrawingMode.GRAPH_EDGES) {
            setColorToBlack();
            initMethods();
        } else if (currentMode == DrawingMode.POINTS) {
            eraseCurrentPoint();
        }
    }

    private void setColorToBlack() {
        if(fromVertex != null) {
            graphics2D.setPaint(Color.black);
            drawPoint((int)fromVertex.getX(), (int)fromVertex.getY(), PAINT_RADIUS);
            fromVertex = null;
        }
    }

    private void initMethods() {
        if(edges.size() != 0) {
            initStrips();
            initChains();
        }
    }

    private void eraseCurrentPoint() {
        if(currentPoint != null) {
            graphics2D.setPaint(Color.white);
            drawPoint((int)currentPoint.getX(), (int)currentPoint.getY(), PAINT_RADIUS + 1);
            graphics2D.setPaint(Color.green);
        }
    }

    private void initStrips() {
        strips = new StripsMethod(sortedVertexes, edges);
        strips.createStrips();
    }

    private void initChains() {
        chains = new ChainsMethod(sortedVertexes, edges);
        chains.createChains();
    }

    public void clearData() {
        clear();
        sortedVertexes = new ArrayList<>();
        edges = new LinkedList<>();
        VERTEX_AMOUNT = 0;
        fromVertex = null;
        strips = null;
        currentMode = DrawingMode.NONE;
    }

    private void createGraphVertex(int x, int y) {
        addPoint(x, y);
        drawPoint(x, y, PAINT_RADIUS);
    }

    private void createGraphEdge(int x, int y) {
        for (int i = 0; i < VERTEX_AMOUNT; i++) {
            Point p = new Point((int) sortedVertexes.get(i).getX(), (int) sortedVertexes.get(i).getY());
            if(isSelected( p, x, y)) {
                createEdge(p, i);
            }
        }
    }

    private void addPoint(int x, int y) {
        if(VERTEX_AMOUNT == 0) {
            sortedVertexes.add(new Point(x, y));
        } else {
            addToSortedVertexesList(new Point(x, y));
        }
        VERTEX_AMOUNT++;
    }

    private void addToSortedVertexesList(Point p) {
        addToSortedVertexesListRecursive(p, VERTEX_AMOUNT / 2, VERTEX_AMOUNT / 2);
    }

    private void addToSortedVertexesListRecursive(Point p, int position, int range) {
        if((position == 0) || (range == 0)) {
            insertPointIntoList(p, position);
            return;
        }

        int newRange = range / 2;
        if(newRange != 0) {
            range = newRange;
        }
        if(sortedVertexes.get(position).getY() < p.getY()) {
            addToSortedVertexesListRecursive(p, position + range, newRange);
        } else {
            addToSortedVertexesListRecursive(p, position - range, newRange);
        }
    }

    private void insertPointIntoList(Point p, int position) {
        if(position == VERTEX_AMOUNT) {
            sortedVertexes.add(p);
            return;
        }
        if(sortedVertexes.get(position).getY() < p.getY()) {
            sortedVertexes.add(position + 1, p);
        } else {
            sortedVertexes.add(position, p);
        }
    }

    private void createEdge(Point vertex, int vertexId) {
        if(fromVertex != null) {
            graphics2D.setPaint(Color.black);
            createAndDrawEdgeTo(vertex, vertexId);

            fromVertex = null;
            graphics2D.setPaint(Color.red);
        } else {
            fromVertex = vertex;
            fromVertexId = vertexId;
            drawPoint((int)fromVertex.getX(), (int)fromVertex.getY(), PAINT_RADIUS);
        }
    }

    private void createAndDrawEdgeTo(Point vertex, int vertexId) {
        edges.add(new Edge(fromVertex, fromVertexId, vertex, vertexId));

        drawLine(vertex, fromVertex);
        drawPoint((int)fromVertex.getX(), (int)fromVertex.getY(), PAINT_RADIUS);
        graphics2D.drawString(new Integer(edges.size() - 1).toString(), (fromVertex.x + vertex.x) / 2, (fromVertex.y + vertex.y) / 2);
    }

    private void drawPoint(int x, int y, int radius) {
        graphics2D.fillOval(x, y, radius, radius);
        repaint();
    }

    private void drawLine(Point from, Point to) {
        graphics2D.drawLine(from.x, from.y, to.x, to.y);
        repaint();
    }

    private boolean isSelected(Point vertex, int x, int y ) {
        if((vertex.getX() < x) && (vertex.getY() < y) &&
                (vertex.getX() + PAINT_RADIUS > x) && (vertex.getY() + PAINT_RADIUS > y))
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
        }
        g.drawImage(image, 0, 0, null);
    }

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }
}
