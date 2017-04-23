import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class which implements creating strips
 * on graph and locating a point where
 * Created by anastasia on 4/23/17.
 */
public class StripsMethod {
    private Strip[] strips;
    private ArrayList<Point> sortedVertexes;
    private LinkedList<Edge> edges;
    private int VERTEX_AMOUNT;

    public StripsMethod(ArrayList<Point> sortedVertexes, LinkedList<Edge> edges) {
        this.sortedVertexes = sortedVertexes;
        this.edges = edges;
        this.VERTEX_AMOUNT = sortedVertexes.size();
    }

    public void createStrips() {
        int amountOfStrips = countAmountOfStrips();
        strips = new Strip[amountOfStrips];
        strips[0] = new Strip(new ArrayList<>());
        int splittingLine = 0;
        int i = VERTEX_AMOUNT - 1;

        for(int count = 1; count < amountOfStrips - 1; count++, i--) {
            while ((splittingLine == sortedVertexes.get(i).y) && (i >= 0))
                i--;
            if(i >= 0) {
                splittingLine = sortedVertexes.get(i).y - 1;
                ArrayList<Integer> edgeIDS = Computations.findIntersectedEdges(splittingLine, edges);
                strips[count] = new Strip(edgeIDS);
            } else
                strips[count] = new Strip(new ArrayList<>());
        }
        strips[amountOfStrips - 1] = new Strip(new ArrayList<>());
        showStrips();
    }

    private int countAmountOfStrips() {
        int n = VERTEX_AMOUNT + 1;
        for(int i = 0; i < VERTEX_AMOUNT - 1; i++) {
            if(sortedVertexes.get(i).getY() == sortedVertexes.get(i + 1).getY())
                n--;
        }
        return n;
    }

    private void showStrips() {
        System.out.println("STRIPS");
        if(strips == null)
            return;
        for(int i = 0; i < strips.length; i++) {
            System.out.print(i + " : ");
            if(strips[i].edges == null) {
                System.out.println();
                continue;
            }
            for (int j = 0; j < strips[i].edges.length; j++) {
                System.out.print(strips[i].edges[j] + ", ");
            }
            System.out.print("\n");
        }
        System.out.println();
    }

    public String locate(Point currentPoint) {
        if(currentPoint.getY() > sortedVertexes.get(VERTEX_AMOUNT - 1).getY()) {
            return "strip 0";
        } else if(currentPoint.getY() < sortedVertexes.get(0).getY()) {
            return "strip " + strips.length;
        } else {
            return locateInPolygonStrips(currentPoint);
        }
    }

    private String locateInPolygonStrips(Point currentPoint) {
        int count = strips.length - 2;
        for (int i = 0; i < VERTEX_AMOUNT - 1; i++, count--) {
            if (Computations.isBetween(sortedVertexes.get(i).y, sortedVertexes.get(i + 1).y, currentPoint.y)) {
                int leftEdgeNum = -1, rightEdgeNum = -1;
                for(int k = 0; k < strips[count].edges.length; k++) {
                    Edge edge = edges.get(strips[count].edges[k]);
                    Point baseVertex = edge.lowerVertex();
                    // sorry ^_^ the point has to be on the right side, but somewhere in creating
                    // chains (in sorting) I swapped from and to vertexes in the edges, so now edge
                    // vector is pointing down, so the point we want to locate is on the left side!
                    Point vectorFromBaseToCurrentVertex = new Point(currentPoint.x - baseVertex.x, currentPoint.y - baseVertex.y);
                    if(!Computations.isOnTheRightSide(edge.toVector(), vectorFromBaseToCurrentVertex)) {
                        leftEdgeNum = strips[count].edges[k];
                    } else {
                        rightEdgeNum = strips[count].edges[k];
                        break;
                    }
                }
                String info = "strip " + count + ", between edges " + leftEdgeNum + " and " + rightEdgeNum;
                System.out.println(info);
                return info;
            }
        }
        return "";
    }

    private class Strip {
        int[] edges;
        public Strip(ArrayList<Integer> edgesIDs) {
            if(edgesIDs.isEmpty()) {
                edges = null;
                return;
            }

            edges = new int[edgesIDs.size()];
            for (int i = 0; i < edges.length; i++)
                edges[i] = edgesIDs.get(i);
        }
    }
}
