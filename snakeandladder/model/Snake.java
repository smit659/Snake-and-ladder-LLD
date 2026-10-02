package snakeandladder.model;

/**
 * Snake entity: transports a player downwards from head to tail (head > tail).
 */
public class Snake extends Jump {

    public Snake(int head, int tail) {
        super(head, tail);
        if (head <= tail) {
            throw new IllegalArgumentException("Snake head (" + head + ") must be greater than tail (" + tail + ")");
        }
    }

    public int getHead() {
        return start;
    }

    public int getTail() {
        return end;
    }

    @Override
    public String getEntityName() {
        return "Snake";
    }

    @Override
    public boolean isSnake() {
        return true;
    }
}
