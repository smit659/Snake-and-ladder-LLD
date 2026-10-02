package snakeandladder.engine;

import snakeandladder.model.Board;
import snakeandladder.model.GameState;
import snakeandladder.model.Jump;
import snakeandladder.model.Player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Main game orchestrator for Snake and Ladder.
 * Manages player turns, rules, overshoot validation, jumpers, and game lifecycle.
 */
public class SnakeAndLadderEngine {
    private final Board board;
    private final Dice dice;
    private final Deque<Player> playerQueue;
    private final List<Player> winners;
    private GameState state;

    // Configurable gameplay rules
    private final boolean extraTurnOnMaxRoll;
    private final int maxConsecutiveBonusTurns;
    private final boolean stopAtFirstWinner;

    // Tracking consecutive bonus rolls for the active player
    private int consecutiveBonusRolls;

    public SnakeAndLadderEngine(Board board, Dice dice, List<Player> players) {
        this(board, dice, players, true, 3, true);
    }

    public SnakeAndLadderEngine(Board board, Dice dice, List<Player> players,
                               boolean extraTurnOnMaxRoll, int maxConsecutiveBonusTurns, boolean stopAtFirstWinner) {
        this.board = Objects.requireNonNull(board, "Board cannot be null");
        this.dice = Objects.requireNonNull(dice, "Dice cannot be null");
        if (players == null || players.size() < 2) {
            throw new IllegalArgumentException("At least 2 players are required to start a game");
        }
        this.playerQueue = new ArrayDeque<>(players);
        this.winners = new ArrayList<>();
        this.state = GameState.NOT_STARTED;
        this.extraTurnOnMaxRoll = extraTurnOnMaxRoll;
        this.maxConsecutiveBonusTurns = maxConsecutiveBonusTurns;
        this.stopAtFirstWinner = stopAtFirstWinner;
        this.consecutiveBonusRolls = 0;
    }

    /**
     * Starts the game session.
     */
    public void start() {
        if (state == GameState.IN_PROGRESS) {
            throw new IllegalStateException("Game is already in progress");
        }
        this.state = GameState.IN_PROGRESS;
    }

    /**
     * Executes the next turn in the game.
     *
     * @return TurnResult containing complete details of the move
     */
    public TurnResult playNextTurn() {
        if (state == GameState.NOT_STARTED) {
            start();
        }
        if (state == GameState.GAME_OVER) {
            throw new IllegalStateException("Game is already over!");
        }

        Player currentPlayer = playerQueue.pollFirst();
        int roll = dice.roll();
        int fromPos = currentPlayer.getPosition();
        int targetPos = fromPos + roll;
        int totalCells = board.getTotalCells();

        boolean moved = false;
        Jump jumpEncountered = null;
        int finalPos = fromPos;
        boolean hasWon = false;
        boolean bonusTurn = false;

        if (targetPos <= totalCells) {
            moved = true;
            // Check for snake or ladder
            if (board.hasJump(targetPos)) {
                jumpEncountered = board.getJumpAt(targetPos);
                finalPos = jumpEncountered.getEnd();
            } else {
                finalPos = targetPos;
            }
            currentPlayer.setPosition(finalPos);

            if (finalPos == totalCells) {
                hasWon = true;
            }
        }

        if (hasWon) {
            winners.add(currentPlayer);
            consecutiveBonusRolls = 0;

            if (stopAtFirstWinner || playerQueue.size() <= 1) {
                // If only 1 player remains in multi-winner mode, add them as the last ranked
                if (!stopAtFirstWinner && playerQueue.size() == 1) {
                    winners.add(playerQueue.pollFirst());
                }
                state = GameState.GAME_OVER;
            }
        } else {
            // Check if player gets a bonus turn
            boolean rolledMax = (roll == dice.getMaxPossibleRoll());
            if (extraTurnOnMaxRoll && rolledMax && consecutiveBonusRolls + 1 < maxConsecutiveBonusTurns) {
                consecutiveBonusRolls++;
                bonusTurn = true;
                playerQueue.addFirst(currentPlayer); // Gets to roll again immediately
            } else {
                consecutiveBonusRolls = 0;
                playerQueue.addLast(currentPlayer); // Enqueue back to the end of line
            }
        }

        return new TurnResult(currentPlayer, roll, fromPos, targetPos, finalPos, jumpEncountered, moved, hasWon, bonusTurn);
    }

    /**
     * Simulates the entire game until completion and returns all winners in rank order.
     */
    public List<Player> playAll(java.util.function.Consumer<TurnResult> turnListener) {
        if (state == GameState.NOT_STARTED) {
            start();
        }
        while (state == GameState.IN_PROGRESS) {
            TurnResult result = playNextTurn();
            if (turnListener != null) {
                turnListener.accept(result);
            }
        }
        return getWinners();
    }

    public GameState getState() {
        return state;
    }

    public Board getBoard() {
        return board;
    }

    public Dice getDice() {
        return dice;
    }

    public List<Player> getWinners() {
        return Collections.unmodifiableList(winners);
    }

    public Player getPrimaryWinner() {
        return winners.isEmpty() ? null : winners.get(0);
    }
}
