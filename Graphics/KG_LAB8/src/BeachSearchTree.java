import java.awt.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Анастасия on 12.03.2017.
 */
public class BeachSearchTree {

    private class Node {
        Node left = null;
        Node right = null;
        Arch arch;

        public Node(Point site) {
            arch = new Arch(site);
        }
    }

    private Node root = null;

    /**
     * Find arch in the tree which is intersected by the new arch
     */
    private Node searchPlaceForNewNode(Node newNode) {
        Node current = root;
        if ( current != newNode) {
            if(current.arch.getFocus().x < newNode.arch.getFocus().x) {

            } else {
                if (current.left != null) {

                }
            }
        }

    }

    public void insert(Point site) {
        if (root == null) {
            root = new Node(site);
        } else {
            Node node = searchPlaceForNewNode(site);
        }
    }

    public void handlePointEvent(Event currentEvent, Deque<Event> eventsQueue, LinkedList<Edge> edges) {
    }

    public void handleCircleEvent(Event currentEvent, Deque<Event> eventsQueue, LinkedList<Edge> edges ) {}


    private Point intersectionWithArch(ArrayList<Arch> arches, int x) {
        if(arches.isEmpty())
            return null;
        Point minPoint = new Point(x, arches.get(0).f(x));
        for(int i = 0; i < arches.size(); i++) {
            int y = arches.get(i).f(x);
            if(minPoint.y < y)
                minPoint.y = y;
        }
        return minPoint;
    }

    private void updateBreakingPoints(ArrayList<Arch> arches, int y) {
        /*for(int i = 0; i < arches.size(); i++) {
            arches.get(i).p = y - points.get(arches.get(i).siteId).y;
        }

        for (int i = 0; i < arches.size(); i++) {
            if( i != 0){

                if(arches.get(i - 1).p != arches.get(i).p) {
                    arches.get(i).rightBreakingPoint.y = (int)(Math.abs(points.get(arches.get(i - 1).siteId).x - points.get(arches.get(i).siteId).x) /
                            Math.abs(arches.get(i - 1).p - arches.get(i).p));
                } else {
                    arches.get(i).rightBreakingPoint.y = (int)((points.get(arches.get(i - 1).siteId).x + points.get(arches.get(i).siteId).x) / 2);
                }
                arches.get(i).rightBreakingPoint.x = arches.get(i).argF(arches.get(i).rightBreakingPoint.y);
            }
        }*/
    }
}
