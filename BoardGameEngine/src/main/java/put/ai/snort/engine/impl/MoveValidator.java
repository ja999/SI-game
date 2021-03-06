/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.engine.impl;

import java.util.List;
import put.ai.snort.game.Board;
import put.ai.snort.game.Move;
import put.ai.snort.game.Player;
import put.ai.snort.game.moves.MoveMove;
import put.ai.snort.game.moves.PlaceMove;
import put.ai.snort.game.moves.SkipMove;

public class MoveValidator {

    private MoveValidator() {
    }

    private static boolean matchesMM(MoveMove a, MoveMove b) {
        return a.getSrcX() == b.getSrcX() && a.getSrcY() == b.getSrcY() && a.getDstX() == b.getDstX() && a.getDstY() == b.getDstY();
    }

    private static boolean matchesPM(PlaceMove a, PlaceMove b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }

    private static boolean matchesSM(SkipMove a, SkipMove b) {
        return true;
    }

    public static boolean matches(Move a, Move b) {
        if (a.getColor() != b.getColor()) {
            return false;
        }
        if (a instanceof MoveMove && b instanceof MoveMove) {
            return matchesMM((MoveMove) a, (MoveMove) b);
        }
        if (a instanceof PlaceMove && b instanceof PlaceMove) {
            return matchesPM((PlaceMove) a, (PlaceMove) b);
        }
        if (a instanceof SkipMove && b instanceof SkipMove) {
            return matchesSM((SkipMove) a, (SkipMove) b);
        }
        return false;
    }

    public static boolean isValidMove(Move m, Board b, Player.Color color) {
        List<Move> allowedMoves = b.getMovesFor(color);
        for (Move allowed : allowedMoves) {
            if (matches(m, allowed)) {
                return true;
            }
        }
        return false;
    }
}
