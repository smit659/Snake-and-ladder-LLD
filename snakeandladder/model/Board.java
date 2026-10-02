package snakeandladder.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Snake and Ladder Board.
 * Manages cells, boundaries, snakes, and ladders.
 */
public class Board {
    private final int totalCells;
    private final Map<Integer, Jump> jumps;

    public Board(int totalCells) {
        if (totalCells <= 0) {
            throw new IllegalArgumentException("Board size must be greater than 0");
        }
        this.totalCells = totalCells;
        this.jumps = new HashMap<>();
    }

    public void addSnake(int head, int tail) {
        validatePosition(head, "Snake head");
        validatePosition(tail, "Snake tail");
        if (head == totalCells) {
            throw new IllegalArgumentException("Snake head cannot be at the winning cell (" + totalCells + ")");
        }
        addJump(new Snake(head, tail));
    }

    public void addLadder(int start, int end) {
        validatePosition(start, "Ladder start");
        validatePosition(end, "Ladder end");
        if (start == 1) {
            // Optional check: start cell shouldn't immediately jump if not desired, but valid if needed
        }
        addJump(new Ladder(start, end));
    }

    public void addJump(Jump jump) {
        if (jump == null) {
            throw new IllegalArgumentException("Jump cannot be null");
        }
        int start = jump.getStart();
        int end = jump.getEnd();

        validatePosition(start, "Jump start");
        validatePosition(end, "Jump end");

        if (jumps.containsKey(start)) {
            throw new IllegalArgumentException("A snake or ladder already exists at cell " + start);
        }

        // Check against direct cycle (e.g., jump from start to end, where end has a jump back to start)
        if (jumps.containsKey(end) && jumps.get(end).getEnd() == start) {
            throw new IllegalArgumentException("Direct cycle detected between cell " + start + " and " + end);
        }

        jumps.put(start, jump);
    }

    private void validatePosition(int position, String name) {
        if (position < 1 || position > totalCells) {
            throw new IllegalArgumentException(name + " (" + position + ") is out of board bounds [1, " + totalCells + "]");
        }
    }

    public Jump getJumpAt(int position) {
        return jumps.get(position);
    }

    public boolean hasJump(int position) {
        return jumps.containsKey(position);
    }

    public int getTotalCells() {
        return totalCells;
    }

    public Map<Integer, Jump> getJumps() {
        return Collections.unmodifiableMap(jumps);
    }
}
