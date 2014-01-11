/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package put.ai.snort.snortsnortplayer;

import java.util.List;
import java.util.PriorityQueue;
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
    }/*
    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());
        int max = - boardSize;
        Move best = null;
        for(Move move : moves) {
                b.doMove(move);
                int heur = heuristic(b, getColor());
                if (max<heur) {
                        max = heur;
                        best = move;
                }
                
                b.undoMove(move);
        }
        return best;
    }*/
    int boardSize;
    @Override
    public Move nextMove(Board b) {
    	int depth = 4;
        List<Move> moves = b.getMovesFor(getColor());
    	boardSize = b.getSize()*b.getSize();
        int max = - boardSize-1;
        Move best = null;
        
        int alfa = - boardSize;
        int beta = boardSize;
    	PriorityQueue<HeuristicMove> movesQueue = new PriorityQueue<HeuristicMove>(moves.size(), new HeuristicMoveComparator());
        for (Move move : moves) {
        	b.doMove(move);
        	movesQueue.add(new HeuristicMove(-heuristic(b,getColor()), move));
        	b.undoMove(move);
        }
        Move move;
        Color opColor = getOpponent(getColor());
        while(!movesQueue.isEmpty()) {
        	move = movesQueue.poll().move;
        	b.doMove(move);
        	alfa = opAlphabeta(b,depth - 1, alfa, beta, opColor);
        	if (max<alfa) {
        		max = alfa;
        		best = move;
        	}

        	b.undoMove(move);
        }
        return best;
    }

    public int alphabeta(Board b, int depth, int alfa, int beta, Color color){
    	//if(true) return 0;
        if (b.getMovesFor(color).size() == 0) {
        	if (color == getColor())
        		return -boardSize;
        	else
        		return boardSize;
        }
        else if (depth == 0) {
            return heuristic(b, getOpponent(color));
        }
        else if (color == getColor()) {
            for (Move move: b.getMovesFor(color)) {
                b.doMove(move);
                Move last_move = move;
                alfa = Math.max(alfa, alphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alfa){
                    break;
                }
            }
            return alfa;
        }
        else {
            for (Move move: b.getMovesFor(color)) {
                b.doMove(move);
                Move last_move = move;
                beta = Math.min(beta, alphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alfa){
                    break;
                }
            }
            return beta;
        }
    }

    public int opAlphabeta(Board b, int depth, int alfa, int beta, Color color){
        if (b.getMovesFor(color).size() == 0) {
        	if (color == getColor())
        		return -boardSize;
        	else
        		return boardSize;
        }
        else if (depth == 0) {
            return heuristic(b, getOpponent(color));
        }
        else if (color == getColor()) {
        	List<Move> moves = b.getMovesFor(color);
        	PriorityQueue<HeuristicMove> movesQueue = new PriorityQueue<HeuristicMove>(moves.size(), new HeuristicMoveComparator());
            for (Move move: moves) {
            	b.doMove(move);
            	movesQueue.add(new HeuristicMove(heuristic(b,color), move));
            	b.undoMove(move);
            }
            Move move;
            while(!movesQueue.isEmpty()) {
            	move = movesQueue.poll().move;
            	b.doMove(move);
                alfa = Math.max(alfa, opAlphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alfa){
                	movesQueue.clear();
                    break;
                }
            }
            return alfa;
        }
        else {
        	List<Move> moves = b.getMovesFor(color);
        	PriorityQueue<HeuristicMove> movesQueue = new PriorityQueue<HeuristicMove>(moves.size(), new HeuristicMoveComparator());
            for (Move move: moves) {
            	b.doMove(move);
            	movesQueue.add(new HeuristicMove(heuristic(b,getOpponent(color)), move));
            	b.undoMove(move);
            }
            Move move;
            while(!movesQueue.isEmpty()) {
            	move = movesQueue.poll().move;
                b.doMove(move);
                beta = Math.min(beta, opAlphabeta(b, depth - 1, alfa, beta, getOpponent(color)));
                b.undoMove(move);
                if (beta <= alfa){
                    break;
                }
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
