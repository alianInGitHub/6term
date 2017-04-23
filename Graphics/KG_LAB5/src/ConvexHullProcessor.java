import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * Created by anastasia on 4/8/17.
 * <p>
 * Class of multithreading recursive convex hull processing
 * of input set of points
 * Algorithm is mainly described in createConvexHull() function
 * BTW, small tip!! I'm just lazy to fix names, the convex hull
 * algorithm which is used here is not Grehem, but Jarvice, 
 * please don't argue me, just fix name localy :)
 */
class ConvexHullProcessor extends RecursiveTask<ArrayList<Point>> {
    private ArrayList<Point> firstArray;
    private ArrayList<Point> secondArray;
    private ArrayList<Point> convexHull;
    private static final int EPS = 6;

    public ConvexHullProcessor(ArrayList<Point> firstConvexHull, ArrayList<Point> secondConvexHull) {
        this.firstArray = firstConvexHull;
        this.secondArray = secondConvexHull;
    }

    public ArrayList<Point> getConvexHull() {
        return convexHull;
    }

    private ArrayList<Point> recursiveFirstConvexHull() {
        secondArray = Computations.splitArrayInTwoParts(firstArray);
        return compute();
    }

    private ConvexHullProcessor recursiveSecondConvexHullInit() {
        ArrayList<Point> halfSecondConvexHull = Computations.splitArrayInTwoParts(secondArray);
        ConvexHullProcessor processor = new ConvexHullProcessor(secondArray, halfSecondConvexHull);
        processor.fork();
        return processor;
    }

    private ArrayList<Point> recursiveSecondConvexHull() {
        ConvexHullProcessor processor = recursiveSecondConvexHullInit();
        return processor.join();
    }

    private ArrayList<Point> recursiveFirstAndSecondConvexHull() {
        ConvexHullProcessor processor = recursiveSecondConvexHullInit();
        firstArray = recursiveFirstConvexHull();
        return processor.join();
    }

    @Override
    protected ArrayList<Point> compute() {
        if (firstArray.size() <= 3) {
            if (secondArray.size() > 3) {
                secondArray = recursiveSecondConvexHull();
            }
        } else {
            if (secondArray.size() > 3) {
                secondArray = recursiveFirstAndSecondConvexHull();
            } else {
                //save values
                ArrayList<Point> copy = secondArray;
                firstArray = recursiveFirstConvexHull();
                secondArray = copy;
            }
        }

        convexHull = createConvexHull(firstArray, secondArray);
        return convexHull;
    }

    private ArrayList<Point> createConvexHull(ArrayList<Point> firstConvexHull, ArrayList<Point> secondConvexHull) {
        ArrayList<Point> newConvexHull = Computations.concatenate(firstConvexHull, secondConvexHull);
        if (newConvexHull.size() < EPS) {
            return Computations.Grehem(newConvexHull);
        }
        Point randomPointInsideHull = Computations
                .computeCentroidOfTriangle(Computations.generateRandomTriangle(firstConvexHull));
        if (Computations.polygonContains(randomPointInsideHull, secondConvexHull)) {
            return Computations.Grehem(newConvexHull);
        }

        Point nextRight = Computations.findNextRightPointFromSet(randomPointInsideHull, secondConvexHull);
        Point nextLeft = Computations.findNextLeftPointFromSet(randomPointInsideHull, secondConvexHull);

        secondConvexHull = Computations.removeEdgesBetweenVertexes(nextLeft, nextRight, secondConvexHull);
        newConvexHull = Computations.concatenate(firstConvexHull, secondConvexHull);

        return Computations.Grehem(newConvexHull);
    }

}
