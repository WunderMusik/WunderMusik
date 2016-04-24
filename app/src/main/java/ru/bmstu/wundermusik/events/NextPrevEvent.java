package ru.bmstu.wundermusik.events;

/**
 * Created by Nikita on 4/24/2016.
 */
public class NextPrevEvent {
    public enum Direction {
        NEXT,
        PREV
    }
    private Direction direction;
    public NextPrevEvent(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
