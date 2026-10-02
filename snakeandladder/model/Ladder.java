package snakeandladder.model;

/**
 * Ladder entity: transports a player upwards from base to top (start < end).
 */
public class Ladder extends Jump {

    public Ladder(int start, int end) {
        super(start, end);
        if (start >= end) {
            throw new IllegalArgumentException("Ladder start (" + start + ") must be less than end (" + end + ")");
        }
    }

    public int getBottom() {
        return start;
    }

    public int getTop() {
        return end;
    }

    @Override
    public String getEntityName() {
        return "Ladder";
    }

    @Override
    public boolean isSnake() {
        return false;
    }
}
