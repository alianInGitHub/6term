import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Анастасия on 11.03.2017.
 */
public class DrawArea extends JComponent {

    private ArrayList<Point> points;
    private LinkedList<Point> convexHull;

    private Image image;
    private Graphics2D graphics2D;

    private final int PAINT_RADIUS = 10;

    public DrawArea() {
        setDoubleBuffered(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(e.getPoint());
                graphics2D.fillOval(e.getX(), e.getY(), PAINT_RADIUS, PAINT_RADIUS);
                graphics2D.drawString(new Integer(points.size() - 1).toString(), e.getX(), e.getY());
                repaint();
            }
        });
    }

    public void clearData() {
        clear();
        initialize();
    }

    public void createConvexHull() {
        if(points.isEmpty())
            return;
        convexHull = new LinkedList<>();

        if(points.size() == 1){
            convexHull.add(points.get(0));
            return;
        }

        // find the leftest and the rightest points
        Point left = points.get(0);
        Point right = left;

        for(int i = 1; i < points.size(); i++) {
            if(points.get(i).getX() < left.getX())
                left = points.get(i);
            else if(points.get(i).getX() > right.getX())
                right = points.get(i);
        }

        // devide the set of points on two: which are on the left side (upper)
        // of the line LR and which are on the right (lower) side
        ArrayList<Point> upperPoints = new ArrayList<>(points.size());
        ArrayList<Point> lowerPoints  = new ArrayList<>(points.size());

        Point splittingVector = new Point(right.x - left.x, right.y - left.y);
        //graphics2D.drawLine(left.x, left.y, right.x, right.y);
        for(int i = 0; i < points.size(); i++) {
            if(isOnTheLeftSide(new Point(points.get(i).x - left.x, points.get(i).y - left.y), splittingVector)) {
                upperPoints.add(points.get(i));
            } else {
                lowerPoints.add(points.get(i));
            }
        }

        convexHullRecursive(upperPoints, left, right);

        convexHullRecursive(lowerPoints, right, left);

        graphics2D.setPaint(Color.green);
        for (int i = 0; i < convexHull.size(); i++){
            graphics2D.fillOval(convexHull.get(i).x, convexHull.get(i).y, PAINT_RADIUS, PAINT_RADIUS);
        }
        graphics2D.setPaint(Color.black);
        repaint();
    }

    private void convexHullRecursive(ArrayList<Point> points, Point left, Point right) {
        convexHull.add(left);
        convexHull.add(right);

        if(points.isEmpty())
            return;

        if(points.size() == 1) {
            convexHull.add(points.get(0));
            return;
        }

        Point splittingVector = new Point(right.x - left.x, right.y - left.y);

        // find the point PI which is the farthest from the line LR
        Point farthest = findTheFarthestPoint(points, left, splittingVector);
        convexHull.add(farthest);
            /*graphics2D.setPaint(Color.red);
            graphics2D.fillOval(farthest.x, farthest.y, PAINT_RADIUS, PAINT_RADIUS);
            graphics2D.setPaint(Color.black);*/

        ArrayList<Point> newUpperPointsLeft = new ArrayList<>(points.size());
        ArrayList<Point> newUpperPointsRight = new ArrayList<>(points.size());

        splittingVector = new Point(farthest.x - left.x, farthest.y - left.y);
        Point rightSplittingVector = new Point(right.x - farthest.x, right.y - farthest.y);

        for(int i = 0; i < points.size(); i++) {
            if(points.get(i).equals(farthest))
                continue;
            if(isOnTheLeftSide(new Point(points.get(i).x - left.x, points.get(i).y - left.y), splittingVector)) {
                newUpperPointsLeft.add(points.get(i));
                //graphics2D.fillOval(upperPoints.get(i).x, upperPoints.get(i).y, PAINT_RADIUS, PAINT_RADIUS);
            } else if(isOnTheLeftSide(new Point(points.get(i).x - farthest.x, points.get(i).y - farthest.y), rightSplittingVector)) {
                newUpperPointsRight.add(points.get(i));
                //graphics2D.fillOval(upperPoints.get(i).x, upperPoints.get(i).y, PAINT_RADIUS, PAINT_RADIUS);
            }
        }
        convexHullRecursive(newUpperPointsLeft, left, farthest);
        convexHullRecursive(newUpperPointsRight, farthest, right);

        repaint();
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

    private boolean isOnTheLeftSide(Point vectorA, Point vectorB) {
        int res = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        if(res <= 0) {
            return false;
        }
        return true;
    }

    private double distance(Point M, double A, double B, double C) {
        return Math.abs(A * M.getX() - B * M.getY() + C) / Math.sqrt(A * A + B * B);
    }

    private Point findTheFarthestPoint(ArrayList<Point> upperPoints, Point left, Point splittingVector) {
        double A =  splittingVector.y;
        double B = splittingVector.x;
        double C = left.getY() * splittingVector.getX() - left.getX() * splittingVector.getY();
        double maxDistance = distance(upperPoints.get(0), A, B, C);
        int maxId = 0;
        for(int i = 1; i < upperPoints.size(); i++){
            double d = distance(upperPoints.get(i), A, B, C);
            if (d > maxDistance) {
                maxDistance = d;
                maxId = i;
            } else if( d == maxDistance) {
                if (upperPoints.get(i).getX() < upperPoints.get(maxId).getX()) {
                    maxId = i;
                }
            }
        }
        return upperPoints.get(maxId);
    }

    private void clear() {
        graphics2D.setPaint(Color.white);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        repaint();
    }
}
