package snakeandladder;

import snakeandladder.engine.Dice;
import snakeandladder.engine.SnakeAndLadderEngine;
import snakeandladder.engine.TurnResult;
import snakeandladder.model.Board;
import snakeandladder.model.Player;
import snakeandladder.strategy.FairDiceStrategy;
import snakeandladder.strategy.ManualDiceStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Demo runner for Snake and Ladder Low-Level Design.
 */
public class SnakeAndLadderDemo {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       SNAKE AND LADDER LLD - DEMO RUNNER       ");
        System.out.println("=================================================\n");

        runDeterministicScenarioDemo();
        System.out.println("\n-------------------------------------------------\n");
        runFullFairGameSimulation();
    }

    /**
     * Demonstrates deterministic gameplay verifying:
     * - Ladder climb
     * - Snake bite
     * - Board boundary / overshoot rule
     * - Win condition
     */
    private static void runDeterministicScenarioDemo() {
        System.out.println("--- Scenario 1: Deterministic Rule Verification ---");

        Board board = new Board(30); // 30-cell board
        board.addLadder(3, 15);      // Ladder from 3 -> 15
        board.addLadder(17, 28);     // Ladder from 17 -> 28
        board.addSnake(20, 5);       // Snake from 20 -> 5
        board.addSnake(29, 2);       // Snake from 29 -> 2

        Player p1 = new Player("1", "Alice");
        Player p2 = new Player("2", "Bob");

        // Alice: rolls 3 (hits ladder 3->15), Bob: rolls 4 (to 4)
        // Alice: rolls 5 (to 20 -> snake bites -> 5), Bob: rolls 6 (to 10)
        // Alice: rolls 6 (to 11), Bob: rolls 6 (to 16)
        // Alice: rolls 6 (to 17 -> ladder 17->28), Alice rolls 2 (28+2=30 WIN!)
        List<Integer> scriptedRolls = Arrays.asList(
            3, 4,
            5, 6,
            6, 6,
            6, 2
        );

        Dice dice = new Dice(1, 6, new ManualDiceStrategy(scriptedRolls));
        SnakeAndLadderEngine engine = new SnakeAndLadderEngine(
            board, dice, Arrays.asList(p1, p2), true, 3, true
        );

        int turnCount = 1;
        while (engine.getState() != snakeandladder.model.GameState.GAME_OVER) {
            TurnResult result = engine.playNextTurn();
            System.out.println("Turn " + (turnCount++) + ": " + result);
        }

        System.out.println("\nWinner: " + engine.getPrimaryWinner().getName());
    }

    /**
     * Demonstrates a standard 100-cell game with standard snakes, ladders, and 3 players.
     */
    private static void runFullFairGameSimulation() {
        System.out.println("--- Scenario 2: Standard 100-Cell Game Simulation ---");

        Board board = new Board(100);

        // Classic Ladders
        board.addLadder(2, 38);
        board.addLadder(7, 14);
        board.addLadder(8, 31);
        board.addLadder(15, 26);
        board.addLadder(21, 42);
        board.addLadder(28, 84);
        board.addLadder(36, 44);
        board.addLadder(51, 67);
        board.addLadder(71, 91);
        board.addLadder(78, 98);
        board.addLadder(87, 94);

        // Classic Snakes
        board.addSnake(16, 6);
        board.addSnake(46, 25);
        board.addSnake(49, 11);
        board.addSnake(62, 19);
        board.addSnake(64, 60);
        board.addSnake(74, 53);
        board.addSnake(89, 68);
        board.addSnake(92, 88);
        board.addSnake(95, 75);
        board.addSnake(99, 80);

        List<Player> players = Arrays.asList(
            new Player("P1", "Emma"),
            new Player("P2", "Liam"),
            new Player("P3", "Sophia")
        );

        Dice dice = new Dice(1, 6, new FairDiceStrategy());
        SnakeAndLadderEngine engine = new SnakeAndLadderEngine(
            board, dice, players, true, 3, false // multi-winner mode (ranks all players)
        );

        int turnCount = 1;
        while (engine.getState() != snakeandladder.model.GameState.GAME_OVER) {
            TurnResult result = engine.playNextTurn();
            System.out.println(String.format("[Round %02d] %s", turnCount++, result));
        }

        System.out.println("\n================ FINAL RANKINGS ================");
        List<Player> rankings = engine.getWinners();
        for (int rank = 0; rank < rankings.size(); rank++) {
            System.out.println("Rank #" + (rank + 1) + ": " + rankings.get(rank).getName() + " (Final Pos: " + rankings.get(rank).getPosition() + ")");
        }
    }
}
