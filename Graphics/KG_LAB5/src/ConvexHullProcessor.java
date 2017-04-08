import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

/**
 * Created by anastasia on 4/8/17.
 */
public class ConvexHullProcessor extends RecursiveTask<ArrayList<Point>> {
    private ArrayList<Point> firstArray;
    private ArrayList<Point> secondArray;
    private ArrayList<Point> convexHull;
    private final int EPS = 6;

    public ConvexHullProcessor(){}

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

    private ConvexHullProcessor recursiveSecondConvexHullInit (ConvexHullProcessor processor) {
        ArrayList<Point> halfSecondConvexHull = Computations.splitArrayInTwoParts(secondArray);
        processor = new ConvexHullProcessor(secondArray, halfSecondConvexHull);
        processor.fork();
        return processor;
    }

    private ArrayList<Point> recursiveSecondConvexHull(ConvexHullProcessor processor) {
        processor = recursiveSecondConvexHullInit(processor);
        return processor.join();
    }

    private ArrayList<Point> recursiveFirstAndSecondConvexHull(ConvexHullProcessor processor) {
        processor = recursiveSecondConvexHullInit(processor);
        firstArray = recursiveFirstConvexHull();
        return processor.join();
    }

    @Override
    protected ArrayList<Point> compute() {
        ConvexHullProcessor processor = new ConvexHullProcessor();

        if (firstArray.size() <= 3) {
            if (secondArray.size() > 3) {
                secondArray = recursiveSecondConvexHull(processor);
            }
        } else {
            if (secondArray.size() > 3) {
                secondArray = recursiveFirstAndSecondConvexHull(processor);
            } else {
                //save values
                ArrayList<Point> copy = (ArrayList<Point>) secondArray.clone();
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

        Point nextRight = Computations.findNextRightToPointInSet(randomPointInsideHull, secondConvexHull);
        Point nextLeft = Computations.findNextLeftToPointInSet(randomPointInsideHull, secondConvexHull);

        secondConvexHull = Computations.removeEdgesBetweenVertexes(nextLeft, nextRight, secondConvexHull);
        newConvexHull = Computations.concatenate(firstConvexHull, secondConvexHull);

        return Computations.Grehem(newConvexHull);
    }

}