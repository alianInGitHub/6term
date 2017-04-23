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
        return sortFromLeftToRight(result, edges, value);
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

    static ArrayList<Integer> sortFromLeftToRight(ArrayList<Integer> edgesIds, LinkedList<Edge> edges, int yValue) {
        int n = edgesIds.size();
        for (int i = 0; i < n; i++) {
            for(int j = i + 1; j < n; j++) {
                Edge first = edges.get(edgesIds.get(i));
                Edge second = edges.get(edgesIds.get(j));
                if((first.getFrom().getY() < yValue)&& (second.getFrom().getY() < yValue)) {
                    if(first.getFrom().getX() > second.getFrom().getX()) {
                        swap(edgesIds, i, j);
                    } else if((first.getFrom().getX() == second.getFrom().getX()) && (first.getTo().getX() > second.getTo().getX())){
                        swap(edgesIds, i, j);
                    }
                } else if((first.getFrom().getY() < yValue)&& (second.getTo().getY() < yValue)) {
                    if(first.getFrom().getX() > second.getTo().getX()) {
                        swap(edgesIds, i, j);
                    }else if((first.getFrom().getX() == second.getTo().getX()) && (first.getTo().getX() > second.getFrom().getX())){
                        swap(edgesIds, i, j);
                    }
                } else if((first.getTo().getY() < yValue)&& (second.getFrom().getY() < yValue)) {
                    if (first.getTo().getX() > second.getFrom().getX()) {
                        swap(edgesIds, i, j);
                    } else if((first.getTo().getX() == second.getFrom().getX()) && (first.getFrom().getX() > second.getTo().getX())){
                        swap(edgesIds, i, j);
                    }
                } else if((first.getTo().getY() < yValue)&& (second.getTo().getY() < yValue)) {
                    if (first.getTo().getX() > second.getTo().getX()) {
                        swap(edgesIds, i, j);
                    } else if((first.getTo().getX() == second.getTo().getX()) && (first.getFrom().getX() > second.getFrom().getX())){
                        swap(edgesIds, i, j);
                    }
                }
            }
        }
        return edgesIds;
    }
}
