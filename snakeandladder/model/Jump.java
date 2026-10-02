package snakeandladder.model;

/**
 * Base abstraction for any board entity that transports a player from one cell to another (Snake or Ladder).
 */
public abstract class Jump {
    protected final int start;
    protected final int end;

    public Jump(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public abstract String getEntityName();

    public abstract boolean isSnake();

    public boolean isLadder() {
        return !isSnake();
    }

    @Override
    public String toString() {
        return getEntityName() + " [" + start + " -> " + end + "]";
    }
}
