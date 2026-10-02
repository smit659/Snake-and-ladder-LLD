package snakeandladder.engine;

import snakeandladder.model.Jump;
import snakeandladder.model.Player;

/**
 * Encapsulates the complete outcome of a single turn.
 */
public class TurnResult {
    private final Player player;
    private final int diceRoll;
    private final int fromPosition;
    private final int rawPosition;
    private final int finalPosition;
    private final Jump jump;
    private final boolean moved;
    private final boolean won;
    private final boolean grantedBonusTurn;

    public TurnResult(Player player, int diceRoll, int fromPosition, int rawPosition, 
                      int finalPosition, Jump jump, boolean moved, boolean won, boolean grantedBonusTurn) {
        this.player = player;
        this.diceRoll = diceRoll;
        this.fromPosition = fromPosition;
        this.rawPosition = rawPosition;
        this.finalPosition = finalPosition;
        this.jump = jump;
        this.moved = moved;
        this.won = won;
        this.grantedBonusTurn = grantedBonusTurn;
    }

    public Player getPlayer() {
        return player;
    }

    public int getDiceRoll() {
        return diceRoll;
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public int getRawPosition() {
        return rawPosition;
    }

    public int getFinalPosition() {
        return finalPosition;
    }

    public Jump getJump() {
        return jump;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean isWon() {
        return won;
    }

    public boolean isGrantedBonusTurn() {
        return grantedBonusTurn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getName()).append(" rolled a ").append(diceRoll).append(". ");
        if (!moved) {
            sb.append("Overshot board! Stays at ").append(fromPosition).append(".");
        } else {
            sb.append("Moved from ").append(fromPosition).append(" to ").append(rawPosition);
            if (jump != null) {
                if (jump.isSnake()) {
                    sb.append(" -> Bit by Snake! Slid down to ").append(finalPosition);
                } else {
                    sb.append(" -> Climbed Ladder! Moved up to ").append(finalPosition);
                }
            } else {
                sb.append(".");
            }
        }
        if (won) {
            sb.append(" [WON THE GAME!]");
        } else if (grantedBonusTurn) {
            sb.append(" [Rolled max! Gets a bonus turn!]");
        }
        return sb.toString();
    }
}
