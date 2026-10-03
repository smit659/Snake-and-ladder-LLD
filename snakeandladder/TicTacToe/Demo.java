package snakeandladder.TicTacToe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Demo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Player p1 = Player.builder().setId(1).setName("smit").setSymbol('X').build();
        Player p2 = Player.builder().setId(2).setName("sohit").setSymbol('O').build();

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        Board board = new Board(3, 3);
        GameEngine gameEngine = new GameEngine(board, players);

        // Register the observer — all board updates now go through the listener
        gameEngine.addListener(new RenderScreen());

        Scanner sc = new Scanner(System.in);

        // Bug fix #11: was !=COMPLETED, so DRAW state looped forever
        while (gameEngine.getGameState() == GameState.IN_PROGRESS) {
            // Bug fix #15: use public API instead of accessing package-private q directly
            Player curr = gameEngine.getCurrentPlayer();
            System.out.println("Player " + curr.getName() + " (" + curr.getSymbol() + "), enter row col:");
            int r = sc.nextInt();
            int c = sc.nextInt();
            gameEngine.makeMove(r, c);
        }

        System.out.println("Game ended: " + gameEngine.getGameState());
        board.shutdown(); // clean up ExecutorService
        sc.close();
    }
}
