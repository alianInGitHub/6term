import java.awt.*;

/**
 * Graph edge class
 * Created by anastasia on 4/23/17.
 */
public class Edge {
    private Point from, to;
    private int fromId, toId;
    private int weight;

    public Edge(Point v1, int v1Id, Point v2, int v2Id) {
        from = v1;
        fromId = v1Id;
        to = v2;
        toId = v2Id;
    }

    public Point toVector() {
        if(to.y < from.y) {
            return new Point(to.x - from.x, to.y - from.y);
        }
        return new Point(from.x - to.x, from.y - to.y);
    }
    public Point lowerVertex() {
        if(to.y > from.y)
            return to;
        return from;
    }

    public void swapDirection() {
        Point c = from;
        from = to;
        to = c;

        int cId = fromId;
        fromId = toId;
        toId = cId;
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(Point from) {
        this.from = from;
    }

    public Point getTo() {
        return to;
    }

    public void setTo(Point to) {
        this.to = to;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
