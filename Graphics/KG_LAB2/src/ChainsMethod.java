import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Class implements creating chains sorted from left
 * to right in graph and locating a point where
 * Created by anastasia on 4/23/17.
 */
public class ChainsMethod {
    private ArrayList<LinkedList<Integer>> chains;
    private ArrayList<Point> sortedVertexes;
    private LinkedList<Edge> edges;

    public ChainsMethod(ArrayList<Point> sortedVertexes, LinkedList<Edge> edges) {
        this.sortedVertexes = sortedVertexes;
        this.edges = edges;
    }

    public void createChains() {
        ArrayList<LinkedList<Integer>> listOfEdgesOut = findEdgeWeights();

        int n = sumOutWeights(listOfEdgesOut.get(listOfEdgesOut.size() - 1));
        chains = new ArrayList<>(listOfEdgesOut.size());
        for (int j = 0; j < listOfEdgesOut.get(listOfEdgesOut.size() - 1).size(); j++) {
            createNewChain(0, j);
        }

        createChainsRecursive(listOfEdgesOut, sortedVertexes.size() - 1, 0);

        showChains();
    }

    private int createChainsRecursive(ArrayList<LinkedList<Integer>> listOfEdgesOut, int currentNode, int chainId) {
        ArrayList<Integer> currentList = Computations.sortEdgesFromLeftToRightForVertex(
                new ArrayList<Integer>(listOfEdgesOut.get(currentNode)),
                edges,
                currentNode);

        for (int i = 0; i < currentList.size(); i++) {
            if (i > 0) {
                chainId++;
            }
            chains.get(chainId).add(currentList.get(i));
            int nextNode;
            if (edges.get(currentList.get(i)).getFromId() == currentNode)
                nextNode = edges.get(currentList.get(i)).getToId();
            else
                nextNode = edges.get(currentList.get(i)).getFromId();

            if (listOfEdgesOut.get(nextNode).size() > 1) {
                for (int j = 1; j < listOfEdgesOut.get(nextNode).size(); j++) {
                    createNewChain(chainId, j);
                }
            }
            chainId = createChainsRecursive(listOfEdgesOut, nextNode, chainId);
        }
        return chainId;
    }

    private void showChains() {
        System.out.println("CHAINS");
        for (int i = 0; i < chains.size(); i++) {
            LinkedList<Integer> array = chains.get(i);
            System.out.print(i + ":\t");
            System.out.println(array);
        }
        System.out.println();
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
            sum += edges.get(i).getWeight();
        }
        return sum;
    }

    private ArrayList<LinkedList<Integer>> findEdgeWeights() {
        ArrayList<LinkedList<Integer>> vertexListOfEdgesIn = new ArrayList<>(sortedVertexes.size());
        ArrayList<LinkedList<Integer>> vertexListOfEdgesOut = new ArrayList<>(sortedVertexes.size());

        createInAndOutEdgesListsForEachVertex(vertexListOfEdgesIn, vertexListOfEdgesOut);

        doIterations(vertexListOfEdgesIn, vertexListOfEdgesOut);
        return vertexListOfEdgesOut;
    }

    private void createInAndOutEdgesListsForEachVertex( ArrayList<LinkedList<Integer>> vertexListOfEdgesIn,
                                           ArrayList<LinkedList<Integer>> vertexListOfEdgesOut) {
        for (int i = 0; i < sortedVertexes.size(); i++) {
            vertexListOfEdgesIn.add(new LinkedList<>());
            vertexListOfEdgesOut.add(new LinkedList<>());
        }
        for (int i = 0; i < edges.size(); i++) {
            Edge e = edges.get(i);
            if(e.getFrom().y < e.getTo().y) {
                vertexListOfEdgesIn.get(e.getFromId()).add(i);
                vertexListOfEdgesOut.get(e.getToId()).add(i);
                edges.get(i).swapDirection();
            } else {
                vertexListOfEdgesIn.get(e.getToId()).add(i);
                vertexListOfEdgesOut.get(e.getFromId()).add(i);
            }
            edges.get(i).setWeight(1);
        }
    }

    private void doIterations( ArrayList<LinkedList<Integer>> vertexListOfEdgesIn,
                               ArrayList<LinkedList<Integer>> vertexListOfEdgesOut) {
        upIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);
        downIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);

        upIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);
        downIteration(vertexListOfEdgesIn, vertexListOfEdgesOut);
    }

    private void upIteration( ArrayList<LinkedList<Integer>> vertexListOfEdgesIn,  ArrayList<LinkedList<Integer>> vertexListOfEdgesOut) {
        for (int i = 0; i < sortedVertexes.size(); i++) {
            int wSumIn = sumWeights(vertexListOfEdgesIn.get(i));
            int wSumOut = vertexListOfEdgesOut.get(i).size();

            int theLeftestEdgeFromCurrentVertex = getIdOfTheLeftestEdge(vertexListOfEdgesOut.get(i));
            if ( theLeftestEdgeFromCurrentVertex == -1)
                continue;

            if (wSumIn > wSumOut) {
                edges.get(theLeftestEdgeFromCurrentVertex).setWeight(wSumIn - wSumOut + 1);
            }
        }
    }

    private void downIteration(ArrayList<LinkedList<Integer>> vertexListOfEdgesIn, ArrayList<LinkedList<Integer>> vertexListOfEdgesOut) {
        for (int i = sortedVertexes.size() - 1; i >= 0; i--) {
            int wSumOut = sumWeights(vertexListOfEdgesOut.get(i));
            int wSumIn = sumWeights(vertexListOfEdgesIn.get(i));

            int theLeftestEdgeFromCurrentVertex = getIdOfTheRightestEdge(vertexListOfEdgesIn.get(i));

            if ( theLeftestEdgeFromCurrentVertex == -1)
                continue;

            if (wSumOut > wSumIn) {
                int weight = edges.get(theLeftestEdgeFromCurrentVertex).getWeight();
                edges.get(theLeftestEdgeFromCurrentVertex).setWeight(weight + wSumOut - wSumIn);
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

    private int getIdOfTheLeftestEdge(LinkedList<Integer> list) {
        int theLeftestEdgeFromCurrentVertex = -1;
        for (int k = 0; k < list.size(); k++) {
            if ((theLeftestEdgeFromCurrentVertex == -1) ||
                    (edges.get(list.get(k)).getTo().x < edges.get(theLeftestEdgeFromCurrentVertex).getTo().x)) {
                theLeftestEdgeFromCurrentVertex = list.get(k);
            }
        }
        return theLeftestEdgeFromCurrentVertex;
    }

    private int getIdOfTheRightestEdge(LinkedList<Integer> list) {
        int theRightestEdgeFromCurrentVertex = -1;
        for (Integer edgeIndex : list) {
            if ((theRightestEdgeFromCurrentVertex == -1) ||
                    (edges.get(edgeIndex).getFrom().x < edges.get(theRightestEdgeFromCurrentVertex).getFrom().x)) {
                theRightestEdgeFromCurrentVertex = edgeIndex;
            }
        }
        return theRightestEdgeFromCurrentVertex;
    }

    public String locate(Point currentPoint) {
        for (int i = 0; i < chains.size(); i++) {
            for (Integer e : chains.get(i)) {
                if (Computations.isBetween(edges.get(e).getFrom().y, edges.get(e).getTo().y, currentPoint.y)) {
                    Point baseVertex = edges.get(e).lowerVertex();
                    Point vectorFromBaseToCurrentPoint = new Point(currentPoint.x - baseVertex.x, currentPoint.y - baseVertex.y);
                    if(!Computations.isOnTheRightSide(vectorFromBaseToCurrentPoint, edges.get(e).toVector())) {
                        if (i != 0) {
                            return "Between chains " + (i - 1) + " and " + i;
                        }
                        return "ERROR:\tCannot locate";
                    }
                }
            }
        }
        return "Out of graph";
    }
}
