import java.awt.*;

/**
 * Created by Анастасия on 23.03.2017.
 */
public class Arch {

    private double EPS = 0.2;

    private double p;
    private Point leftBreakingPoint, rightBreakingPoint;
    private Point focus;

    public Point getFocus() {
        return focus;
    }

    public Point getLeftBreakingPoint() {
        return leftBreakingPoint;
    }

    public Point getRightBreakingPoint() {
        return rightBreakingPoint;
    }

    public Arch(){}

    public Arch(Point site) {
        focus = site;
    }

    public int f(int x) {
        return (int) (x * x / (2 * p));
    }

    public int argF(int y) {
        return (int) (2 * p * y);
    }

    public void findIntersection(Arch anotherArch, double linePosition) {
        double distanceToThisArchFocus = linePosition - focus.getY();
        double distanceToAnotherArchFocus = linePosition - anotherArch.focus.getY();

        if (distanceToThisArchFocus <= EPS) {
            leftBreakingPoint = rightBreakingPoint = getIntersection();
        } else {
            double a1 = 1 / distanceToThisArchFocus;
            double a2 = 1 / distanceToAnotherArchFocus;
            double a = a2 - a1;

            double b1 = - focus.getX() / distanceToThisArchFocus;
            double b2 = - anotherArch.focus.getX() / distanceToAnotherArchFocus;
            double b = b2 - b1;

            double c1 = Math.pow(focus.getX(), 2) + Math.pow(focus.getY(), 2)
                    - Math.pow(linePosition, 2) / distanceToThisArchFocus;
            double c2 = Math.pow(anotherArch.focus.getX(), 2) + Math.pow(anotherArch.focus.getY(), 2)
                    - Math.pow(linePosition, 2) / distanceToThisArchFocus;
            double c = c2 - c1;

            double D = Math.pow(b, 2) + 4 * a * c;
            if (D < 0) {
                // where is no intersection;
                leftBreakingPoint = rightBreakingPoint = null;
            } else if (D < EPS) {
                int x = (int)(- b / (2 * a));
                int y = (int)(a1 * Math.pow(x, 2) + b1 * x + c1);

                rightBreakingPoint = leftBreakingPoint = new Point(x, y);
            } else {
                double x1 = (-b - Math.sqrt(D)) / (2 * a);
                double x2 = (-b + Math.sqrt(D)) / (2 * a);
                double y1 = a1 * Math.pow(x1, 2) + b1 * x1 + c1;
                double y2 = a1 * Math.pow(x2, 2) + b1 * x2 + c1;

                rightBreakingPoint = new Point((int)x1, (int)y1);
                leftBreakingPoint = new Point((int)x2, (int)y2);
            }
        }
    }

    public Point getIntersection() {
        return new Point();
    }
}
