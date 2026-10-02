package snakeandladder.strategy;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Deterministic dice strategy using a sequence of pre-configured values.
 * Useful for unit tests, simulations, and edge case reproduction.
 */
public class ManualDiceStrategy implements DiceStrategy {
    private final Queue<Integer> predefinedRolls;

    public ManualDiceStrategy(List<Integer> rolls) {
        this.predefinedRolls = new LinkedList<>(rolls);
    }

    public void addRoll(int roll) {
        predefinedRolls.offer(roll);
    }

    @Override
    public int roll(int diceCount, int sidesPerDice) {
        if (predefinedRolls.isEmpty()) {
            return diceCount; // default fallback if empty
        }
        return predefinedRolls.poll();
    }
}
