import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by anastasia on 4/8/17.
 */
public class Computations {
    public static boolean isOnTheLeftSide(Point vectorA, Point vectorB) {
        int res = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        if(res <= 0) {
            return false;
        }
        return true;
    }

    public static ArrayList<Point> splitArrayInTwoParts(ArrayList<Point> array)  {
        ArrayList<Point> secondPart;
        int n = array.size();
        secondPart = new ArrayList<>(n / 2);
        for (int i = 0; i < n / 2; i += 1) {
            secondPart.add(array.get(i));
            array.remove(i);
        }
        return secondPart;
    }

    public static boolean isBetween(int y, int y1, int y2) {
        if(((y1 <= y) && (y < y2)) || ((y2 <= y) && (y < y1)))
            return true;
        return false;
    }

    public static ArrayList<Point> concatenate(ArrayList<Point> firstArray, ArrayList<Point> secondArray) {
        ArrayList<Point> result = (ArrayList<Point>) firstArray.clone();
        for (int i = 0; i < secondArray.size(); i++) {
            result.add(secondArray.get(i));
        }
        return result;
    }

    public static boolean polygonContains(Point p, ArrayList<Point> polygon) {
        int count = 0;
        for(int i = 0; i < polygon.size(); i++) {
            Point from = polygon.get(i);
            Point to = polygon.get((i + 1) % polygon.size());
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

    public static Point computeCentroidOfTriangle (ArrayList<Point> triangle) {
        Point point = new Point();
        for (int i = 0; i < 3; i++) {
            point.x += triangle.get(i).x;
            point.y += triangle.get(i).y;
        }

        point.x /= 3;
        point.y /= 3;

        return point;
    }

    public static Point findTheLowestPoint(ArrayList<Point> points) {
        int lowest = 0;
        for (int i = 1; i < points.size(); i++) {
            if (points.get(lowest).y < points.get(i).y) {
                lowest = i;
            } else if ((points.get(lowest).y == points.get(i).y) && (points.get(lowest).x > points.get(i).x)) {
                lowest = i;
            }
        }
        return points.get(lowest);
    }

    public static ArrayList<Integer> getIndexesInAnotherPointsSet(ArrayList<Point> subset, ArrayList<Point> set) {
        ArrayList<Integer> result = new ArrayList<>(subset.size());
        for (int i = 0; i < subset.size(); i++) {
            result.add(set.indexOf(subset.get(i)));
        }
        return result;
    }

    public static ArrayList<Point> Grehem(ArrayList<Point> points) {
        ArrayList<Point> hull = new ArrayList<>(points.size());
        Point lowest = findTheLowestPoint(points);
        hull.add(lowest);
        for (;;) {
            int j = 0;
            Point next = points.get(j);
            j++;
            if (next == lowest) {
                if (points.size() == 1)
                    break;
                else {
                    next = points.get(j);
                    j++;
                }
            }
            for (int i = j; i < points.size(); i++) {
                Point current = points.get(i);
                if (isOnTheLeftSide(new Point(next.x - lowest.x, next.y - lowest.y),
                        new Point(current.x - lowest.x, current.y - lowest.y))) {
                    next = current;
                }
            }
            // if next is the start point of convex hull
            if (next == hull.get(0)) {
                break;
            }
            hull.add(next);
            points.remove(next);
            lowest = next;
        }
        return hull;
    }

    public static Point findNextRightToPointInSet (Point pointOutside, ArrayList<Point> points) {
        Point nextRight = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point current = points.get(i);
            if (isOnTheLeftSide(new Point(nextRight.x - pointOutside.x,  nextRight.x - pointOutside.y),
                    new Point(current.x - nextRight.x, current.y - nextRight.y))) {
                nextRight = current;
            }
        }
        return nextRight;
    }

    public static Point findNextLeftToPointInSet (Point pointOutside, ArrayList<Point> points) {
        Point nextLeft = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point current = points.get(i);
            if (isOnTheLeftSide(new Point(nextLeft.x - pointOutside.x,  nextLeft.x - pointOutside.y),
                    new Point(current.x - pointOutside.x, current.y - pointOutside.y))) {
                nextLeft = current;
            }
        }
        return nextLeft;
    }

    public static ArrayList<Point> removeEdgesBetweenVertexes(Point v1, Point v2, ArrayList<Point> convexHull) {
        if (v1.y < v2.y) {
            Point temp = v1;
            v1 = v2;
            v2 = temp;
        }
        Point splittingVector = new Point(v1.x - v2.x, v1.y - v2.y);
        for (int i = 0; i < convexHull.size(); i++) {
            Point current = convexHull.get(i);
            if (Computations.isOnTheLeftSide(splittingVector, new Point(current.x - v2.x, current.y - v2.y))) {
                convexHull.remove(i);
            }
        }
        convexHull.remove(v1);
        convexHull.remove(v2);
        return convexHull;
    }

    public static ArrayList<Point> generateRandomTriangle(ArrayList<Point> points) {
        Random random =  new Random();
        ArrayList<Point> triangle = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            int id = random.nextInt(points.size());
            while (triangle.contains(points.get(id))) {
                id = random.nextInt(points.size());
            }
            triangle.add(points.get(id));
        }
        return triangle;
    }

}
