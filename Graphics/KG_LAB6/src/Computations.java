import java.awt.*;

/**
 * Created by anastasia on 4/12/17.
 */
public class Computations {
    public static boolean isOnTheLeftSide(Point vectorA, Point vectorB) {
        int res = vectorA.x * vectorB.y - vectorA.y * vectorB.x;
        return res > 0;
    }

    public static boolean isBetween(int y, int y1, int y2) {
        return ((y1 <= y) && (y < y2)) || ((y2 <= y) && (y < y1));
    }

    
}
