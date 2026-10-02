package snakeandladder.engine;

import snakeandladder.strategy.DiceStrategy;
import snakeandladder.strategy.FairDiceStrategy;

/**
 * Dice model configured with count, number of sides, and rolling strategy.
 */
public class Dice {
    private final int count;
    private final int sides;
    private DiceStrategy strategy;

    public Dice(int count, int sides, DiceStrategy strategy) {
        if (count < 1) {
            throw new IllegalArgumentException("Dice count must be at least 1");
        }
        if (sides < 2) {
            throw new IllegalArgumentException("Dice sides must be at least 2");
        }
        this.count = count;
        this.sides = sides;
        this.strategy = strategy != null ? strategy : new FairDiceStrategy();
    }

    public Dice(int count) {
        this(count, 6, new FairDiceStrategy());
    }

    public Dice() {
        this(1, 6, new FairDiceStrategy());
    }

    public int roll() {
        return strategy.roll(count, sides);
    }

    public int getMaxPossibleRoll() {
        return count * sides;
    }

    public int getCount() {
        return count;
    }

    public int getSides() {
        return sides;
    }

    public void setStrategy(DiceStrategy strategy) {
        if (strategy != null) {
            this.strategy = strategy;
        }
    }
}
