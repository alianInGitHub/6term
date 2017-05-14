import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Basic computations
 * Created by anastasia on 4/23/17.
 */
public class Computations {
     static boolean isOnTheRightSide(Point vectorA, Point vectorB) {
        int res = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        if(res <= 0) {
            return true;
        } else
            return false;
    }

    static ArrayList<Integer> findIntersectedEdges(int value, LinkedList<Edge> edges) {
        ArrayList<Integer> result = new ArrayList<>();
        for(int i = 0; i < edges.size(); i++) {
            if(isBetween(edges.get(i).getFrom().y, edges.get(i).getTo().y, value)) {
                result.add(i);
            }
        }
        return sortEdgesFromLeftToRightInStrip(result, edges, value);
    }

    static void swap(ArrayList<Integer> list, int a, int b) {
        int c = list.get(a);
        list.set(a, list.get(b));
        list.set(b, c);
    }

    static boolean isBetween(int y1, int y2, int value) {
        if( ((y1 <= value) && (value <= y2)) || ((y2 <= value) && (value <= y1)))
            return true;
        return false;
    }

    static ArrayList<Integer> sortEdgesFromLeftToRightInStrip(ArrayList<Integer> edgesIds, LinkedList<Edge> edges, int yValue) {
        for (int i = 0; i < edgesIds.size(); i++) {
            for(int j = i + 1; j < edgesIds.size(); j++) {
                Edge first = edges.get(edgesIds.get(i));
                Edge second = edges.get(edgesIds.get(j));
                validateEdge(first);
                validateEdge(second);
                if(first.getFrom().getX() > second.getFrom().getX()) {
                    swap(edgesIds, i, j);
                } else if((first.getFromId() == second.getFromId()) &&
                        ((first.getTo().getX() > second.getTo().getX()) || (isOnTheRightSide(first.toVector(), second.toVector())))){
                    swap(edgesIds, i, j);
                }
            }
        }
        return edgesIds;
    }

    static ArrayList<Integer> sortEdgesFromLeftToRightForVertex(ArrayList<Integer> edgeIds, LinkedList<Edge> edges, int fromVertexId) {
         for (int i = 0; i < edgeIds.size(); i++) {
             for (int j = i + 1; j < edgeIds.size(); j++) {
                 Point firstVector = edgeToVectorWithValidation(edgeIds.get(i), edges, fromVertexId);
                 Point secondVector = edgeToVectorWithValidation(edgeIds.get(j), edges, fromVertexId);
                 if (!isOnTheRightSide(secondVector, firstVector)) {
                     swap(edgeIds, i, j);
                 }
             }
         }
         return edgeIds;
    }

    static private Point edgeToVectorWithValidation(int edgeId, LinkedList<Edge> edges, int fromVertexId) {
         Edge edge = edges.get(edgeId);
         if (edge.getFromId() != fromVertexId) {
             edge.swapDirection();
         }
         return edge.toVector();
    }

    static private void validateEdge(Edge edge) {
        if (edge.getFrom().getY() < edge.getTo().getY()) {
            edge.swapDirection();
        }
    }
}
