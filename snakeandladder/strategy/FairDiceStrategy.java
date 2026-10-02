package snakeandladder.strategy;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Standard fair random dice rolling strategy.
 */
public class FairDiceStrategy implements DiceStrategy {

    @Override
    public int roll(int diceCount, int sidesPerDice) {
        int total = 0;
        for (int i = 0; i < diceCount; i++) {
            total += ThreadLocalRandom.current().nextInt(1, sidesPerDice + 1);
        }
        return total;
    }
}
