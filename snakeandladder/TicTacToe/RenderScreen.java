package snakeandladder.TicTacToe;

public class RenderScreen implements IGameListener{
    @Override 
    public void onListen(Board msg) {
        System.out.println(msg);
    }
}
