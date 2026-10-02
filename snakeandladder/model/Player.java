package snakeandladder.model;

import java.util.Objects;

/**
 * Represents a player in the game.
 */
public class Player {
    private final String id;
    private final String name;
    private int position;

    public Player(String id, String name) {
        this(id, name, 0);
    }

    public Player(String id, String name, int initialPosition) {
        this.id = Objects.requireNonNull(id, "Player id must not be null");
        this.name = Objects.requireNonNull(name, "Player name must not be null");
        this.position = initialPosition;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (Pos: " + position + ")";
    }
}
