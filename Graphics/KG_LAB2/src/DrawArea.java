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

    private final int PAINT_RADIUS = 10;

    //.............................................STRUCTURES.........................................................//

    public enum DrawingMode {
        GRAPH_VERTEXES, GRAPH_EDGES, POINTS, NONE
    }

    public enum METHOD {
        STRIPS, CHAINS, NONE
    }

    //.............................................VARIABLES..........................................................//

    private DrawingMode currentMode;
    private METHOD currentLocationMethod;
    private Image image;
    private Graphics2D graphics2D;
    private JTextArea info;

    private ArrayList<Point> sortedVertexes;        // vertexes are sorted by increasing y-value coordinate
    private LinkedList<Edge> edges;
    private int VERTEX_AMOUNT = 0;
    private Point fromVertex = null;
    private int fromVertexId;
    private Point currentPoint = null;
    private ArrayList<LinkedList<Integer>> chains;

    //.............................................PUBLIC..METHODS....................................................//

    public DrawArea() {
        initialize();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
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
                    case NONE:
                        break;
                }
            }
        });
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
                switch (currentMode) {
                    case GRAPH_EDGES:
                        if(fromVertex != null) {
                            graphics2D.setPaint(Color.black);
                            drawPoint((int)fromVertex.getX(), (int)fromVertex.getY(), PAINT_RADIUS);
                            fromVertex = null;
                        }
                        if(edges.size() != 0) {
                            createStrips();
                            createChains();
                        }
                        break;
                    case POINTS:
                        eraseCurrentPoint();
                        break;
                    default:
                        break;
                }
        }
        this.currentMode = newMode;
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

    public void setTextArea(JTextArea info) {
        this.info = info;
    }


    //.............................................PRIVATE..METHODS...................................................//


    private void initialize() {
        edges = new LinkedList<>();
        sortedVertexes = new ArrayList<>();

        currentMode = DrawingMode.NONE;
        currentLocationMethod = METHOD.NONE;

        setDoubleBuffered(false);
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


    private void createAndLocatePointOnCanvas(int x, int y) {
        eraseCurrentPoint();
        if(currentLocationMethod != METHOD.NONE) {
            currentPoint = new Point(x, y);
            drawPoint(x, y, PAINT_RADIUS);
            showInfo();
        }
    }

    private void addPoint(int x, int y) {
        if(VERTEX_AMOUNT == 0) {
            sortedVertexes.add(new Point(x, y));
        } else {
            addToSortedVertexesList(new Point(x, y), VERTEX_AMOUNT / 2, VERTEX_AMOUNT / 2);
        }
        VERTEX_AMOUNT++;
    }

    private void addToSortedVertexesList(Point p, int position, int range) {
        if((position == 0) || (range == 0)) {
            if(position == VERTEX_AMOUNT) {
                sortedVertexes.add(p);
                return;
            }
            if(sortedVertexes.get(position).getY() < p.getY()) {
                sortedVertexes.add(position + 1, p);
            } else {
                sortedVertexes.add(position, p);
            }
            return;
        }

        int newRange = range / 2;
        if(newRange != 0) {
            range = newRange;
        }
        if(sortedVertexes.get(position).getY() < p.getY()) {
            addToSortedVertexesList(p, position + range, newRange);
        } else {
            addToSortedVertexesList(p, position - range, newRange);
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

    private void eraseCurrentPoint() {
        if(currentPoint != null) {
            graphics2D.setPaint(Color.white);
            drawPoint((int)currentPoint.getX(), (int)currentPoint.getY(), PAINT_RADIUS + 1);
            graphics2D.setPaint(Color.green);
            info.setText("");
        }
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
        info.setText("");
        repaint();
    }

    private void showInfo() {
        if(currentLocationMethod == METHOD.STRIPS)
            locateInStrips();
        else
            locateInChains();
    }


    private void redraw() {
        clear();

        graphics2D.setPaint(Color.black);
        for(int i = 0; i < edges.size(); i++) {
            drawLine(edges.get(i).from, edges.get(i).to);
        }

        for(int i= 0; i < VERTEX_AMOUNT; i++) {
            drawPoint(sortedVertexes.get(i).x, sortedVertexes.get(i).y, PAINT_RADIUS);
        }

        currentPoint = null;
    }

    private void createChains() {
        ArrayList<LinkedList<Integer>> listOfEdgesOut = findEdgeWeights();

        int n = sumOutWeights(listOfEdgesOut.get(listOfEdgesOut.size() - 1));
        chains = new ArrayList<>(listOfEdgesOut.size());
        for (int j = 0; j < listOfEdgesOut.get(listOfEdgesOut.size() - 1).size(); j++) {
            createNewChain(0, j);
        }

        createChainsRecursive(listOfEdgesOut, sortedVertexes.size() - 1, 0);

        for (LinkedList<Integer> array : chains) {
            System.out.println(array);
        }
    }

    private int createChainsRecursive(ArrayList<LinkedList<Integer>> listOfEdgesOut, int currentNode, int chainId) {
        // 1. sort edges from right to left
        // 2. for each vertex run createChainsRecursive
        // 3. return current chainId

        ArrayList<Integer> currentList = sortFromLeftToRight(new ArrayList<Integer>(listOfEdgesOut.get(currentNode)), sortedVertexes.get(currentNode).y - 1);
        for (int i = 0; i < currentList.size(); i++) {
            if (i > 0) {
                chainId++;
            }
            chains.get(chainId).add(currentList.get(i));
            int nextNode;
            if (edges.get(currentList.get(i)).fromId == currentNode)
                nextNode = edges.get(currentList.get(i)).toId;
            else
                nextNode = edges.get(currentList.get(i)).fromId;

            if (listOfEdgesOut.get(nextNode).size() > 1) {
                for (int j = 1; j < listOfEdgesOut.get(nextNode).size(); j++) {
                    createNewChain(chainId, j);
                }
            }
            chainId = createChainsRecursive(listOfEdgesOut, nextNode, chainId);
        }
        return chainId;
    }

    private void createNewChain(int chainId, int shift) {
        chains.add(chainId + shift, new LinkedList<>());
        for (Integer e : chains.get(chainId))
            if (!chains.get(chainId + shift).contains(e))
                chains.get(chainId + shift).add(e);
    }

    private int sumOutWeights(LinkedList<Integer> edgeList) {
        int sum = 0;
        for (Integer i:edgeList) {
            sum += edges.get(i).w;
        }
        return sum;
    }

    private ArrayList<LinkedList<Integer>> findEdgeWeights() {
        ArrayList<LinkedList<Integer>> vertexListOfEdgesIn = new ArrayList<>(sortedVertexes.size());
        ArrayList<LinkedList<Integer>> vertexListOfEdgesOut = new ArrayList<>(sortedVertexes.size());

        for (int i = 0; i < sortedVertexes.size(); i++) {
            vertexListOfEdgesIn.add(new LinkedList<>());
            vertexListOfEdgesOut.add(new LinkedList<>());
        }
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if(e.from.y < e.to.y) {
                vertexListOfEdgesIn.get(e.fromId).add(i);
                vertexListOfEdgesOut.get(e.toId).add(i);
                edges.get(i).swapDirection();
            } else {
                vertexListOfEdgesIn.get(e.toId).add(i);
                vertexListOfEdgesOut.get(e.fromId).add(i);
            }
            edges.get(i).w = 1;
        }

        for (int i = 0; i < vertexListOfEdgesIn.size(); i++) {
            graphics2D.drawString(new Integer(i).toString(), sortedVertexes.get(i).x, sortedVertexes.get(i).y);
            for (int j = 0; j < vertexListOfEdgesIn.get(i).size(); j++) {
            }
        }
        repaint();

        upIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);
        downIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);

        upIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);
        downIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);

        return vertexListOfEdgesOut;
    }

    private void upIteration( ArrayList<LinkedList<Integer>> vertexListOfEdgesIn,  ArrayList<LinkedList<Integer>> vertexListOfEdgesOut) {
        //System.out.println("UP");
        for (int i = 0; i < sortedVertexes.size(); i++) {
            int wSumIn = sumWeights(vertexListOfEdgesIn.get(i));
            int wSumOut = vertexListOfEdgesOut.get(i).size();

            int theLeftestEdgeFromCurrentVertex = idOfTheLeftestEdge(vertexListOfEdgesOut.get(i), true);
            if ( theLeftestEdgeFromCurrentVertex == -1)
                continue;

            if (wSumIn > wSumOut) {
                edges.get(theLeftestEdgeFromCurrentVertex).w = wSumIn - wSumOut + 1;
            }
        }
    }

    private void downIteration(ArrayList<LinkedList<Integer>> vertexListOfEdgesIn, ArrayList<LinkedList<Integer>> vertexListOfEdgesOut) {
        //System.out.println("DOWN");
        for (int i = sortedVertexes.size() - 1; i >= 0; i--) {
            int wSumOut = sumWeights(vertexListOfEdgesOut.get(i));
            int wSumIn = sumWeights(vertexListOfEdgesIn.get(i));

            int theLeftestEdgeFromCurrentVertex = idOfTheLeftestEdge(vertexListOfEdgesIn.get(i), false);

            if ( theLeftestEdgeFromCurrentVertex == -1)
                continue;

            if (wSumOut > wSumIn) {
                edges.get(theLeftestEdgeFromCurrentVertex).w += wSumOut - wSumIn;
            }
        }
    }

    private int sumWeights(LinkedList<Integer> w) {
        int sum = 0;
        for (int i = 0; i < w.size(); i++) {
            sum += edges.get(w.get(i)).getWeight();
        }
        return sum;
    }

    private int idOfTheLeftestEdge(LinkedList<Integer> list, boolean isIn) {
        int theLeftestEdgeFromCurrentVertex = -1;
        if (isIn) {
            for (int k = 0; k < list.size(); k++) {
                if ((theLeftestEdgeFromCurrentVertex == -1) ||
                        (edges.get(list.get(k)).to.x < edges.get(theLeftestEdgeFromCurrentVertex).to.x)) {
                    theLeftestEdgeFromCurrentVertex = list.get(k);
                }
            }
        } else {
            for (int k = 0; k < list.size(); k++) {
                if ((theLeftestEdgeFromCurrentVertex == -1) ||
                        (edges.get(list.get(k)).from.x < edges.get(theLeftestEdgeFromCurrentVertex).from.x)) {
                    theLeftestEdgeFromCurrentVertex = list.get(k);
                }
            }
        }
        return theLeftestEdgeFromCurrentVertex;
    }

    private void locateInChains() {
        for (int i = 0; i < chains.size(); i++) {
            for (Integer e : chains.get(i)) {
                if (isBetween(edges.get(e).from.y, edges.get(e).to.y, currentPoint.y)) {
                    Point baseVertex = edges.get(e).lowerVertex();
                    if(!isOnTheRightSide(new Point(currentPoint.x - baseVertex.x, currentPoint.y - baseVertex.y), edges.get(e).toVector())) {
                        if (i == 0) {
                            System.out.println("Out of graph");
                        } else {
                            System.out.println("Between chains " + (i - 1) + " and " + i);
                        }
                        return;
                    }
                }
            }
        }
        System.out.println("Out of graph");
    }
}
