package snakeandladder.TicTacToe;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GameEngine {
    private final Board b;
    private final Deque<Player> q;
    private volatile GameState gameState;
    // Bug fix #3: was never initialized → NullPointerException on addListener/publishEvent
    private final List<IGameListener> listeners = new ArrayList<>();

    GameEngine(Board b, List<Player> players) {
        this.q = new ArrayDeque<>(players);
        this.b = b;
        this.gameState = GameState.IN_PROGRESS;
    }

    public void addListener(IGameListener l) {
        listeners.add(l);
    }

    // Bug fix #15: expose state via public API instead of direct field access from Demo
    public GameState getGameState() {
        return gameState;
    }

    public Player getCurrentPlayer() {
        return q.peekFirst();
    }

    public boolean makeMove(int row, int col) throws InterruptedException, ExecutionException {
        if (gameState != GameState.IN_PROGRESS)
            return false;

        Player cPlayer = q.pollFirst();

        if (!b.isValid(row, col)) {
            // Bug fix #9: removed direct println — listeners handle all output
            q.addFirst(cPlayer);
            publishEvent();
            return false;
        }

        b.setSymbol(row, col, cPlayer.getSymbol());

        // Bug fix #4: draw check was BEFORE setSymbol — the last cell could never be a win.
        // Now: check winner first, then draw, then rotate player.
        if (b.checkWinner(cPlayer.getSymbol(), row, col)) {
            gameState = GameState.COMPLETED;
        } else if (b.isBoardFull()) {
            gameState = GameState.DRAW;
        } else {
            q.addLast(cPlayer);
        }

        publishEvent();
        return true;
    }

    public void publishEvent() {
        for (IGameListener l : listeners) {
            l.onListen(b);
        }
    }
}