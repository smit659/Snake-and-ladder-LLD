package snakeandladder.strategy;

/**
 * Strategy interface for rolling dice.
 */
public interface DiceStrategy {
    /**
     * Rolls dice and returns the total sum.
     *
     * @param diceCount Number of dice to roll
     * @param sidesPerDice Number of sides on each die (usually 6)
     * @return Total value rolled
     */
    int roll(int diceCount, int sidesPerDice);
}
