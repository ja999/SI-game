/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.snortsnortplayer;

import java.util.List;
import java.util.Random;
import put.ai.snort.game.Board;
import put.ai.snort.game.Move;
import put.ai.snort.game.Player;

public class SnortSnort extends Player {

    private Random random=new Random(0xdeadbeef);

    @Override
    public String getName() {
        return "Aleksander Wdowiński 106525,\n"
        		+ "Michał Ślusarczyk 106643 (Gracz typu Snort-Snort. Tak rodzi się legenda.)";
    }

    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());
        int max = - b.getSize();
        Move best = null;
        int alfa = - b.getSize();
        int beta = b.getSize();
        for(Move move : moves) {
        	b.doMove(move);
        	alfa = alphabeta(b,depth - 1, alfa, beta, getColor());
        	if (max<alfa) {
        		max = alfa;
        		best = move;
        	}

        	b.undoMove(move);
        }
        return best;
    }

    public int alphabeta(Board b, int depth, int alfa, int beta, Color color){
        if (depth == 0) {
            return heuristic(b, color);
        }
        else if (b.getMovesFor(color).size()) {
            return -64;
        }
        if (color == getColor()) {
            for (Move move: b.getMovesFor(color)) {
                b.doMove(move);
                Move last_move = move;
                alfa = Math.max(alfa, alphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                if (beta <= alfa){
                    break;
                }
                b.undoMove(move);
            }
            return alfa;
        }
        else {
            for (Move move: b.getMovesFor(color)) {
                b.doMove(move);
                Move last_move = move;
                beta = Math.min(beta, -alphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                if (beta <= alfa){
                    break;
                }
                b.undoMove(move);
            }
            return beta;
        }
    }

    public int heuristic (Board b, Color color) {
        List<Move> myMoves = b.getMovesFor(color);
        List<Move> opMoves = b.getMovesFor(getOpponent(color));
        if (color == getColor())
        	return myMoves.size() - opMoves.size();
        else
        	return opMoves.size() - myMoves.size();
    }
}
