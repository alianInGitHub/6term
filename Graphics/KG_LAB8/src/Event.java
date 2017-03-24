import java.awt.Point;

/**
 * Created by Анастасия on 12.03.2017.
 */

enum EventType {
    POINT_EVENT, CIRCLE_EVENT
}

class Event {
    private EventType eventType;
    private Point point;

    public Event(Point point) {
        this.point = point;
        eventType = EventType.POINT_EVENT;
    }

    public void setEventType(EventType type) {
        eventType = type;
    }

    public EventType getEventType(){
        return eventType;
    }

    public Point getPoint() {
        return point;
    }
}
