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
    }
    int boardSize;
    long startTime;
    double late;
    
    @Override
    public Move nextMove(Board b) {
    	startTime = System.currentTimeMillis();
    	long appStartTime = System.nanoTime();
    	int depth;
    	int heur = 0;
    	long appTime=0;
        List<Move> moves = b.getMovesFor(getColor());
        if (getTime()>=200) {
        	heur = heuristic(b,getColor());
	    	b.doMove(moves.get(moves.size()/2));
	    	b.undoMove(moves.get(moves.size()/2));
	    	b.doMove(moves.get(moves.size()-1));
	    	heuristic(b,getColor());
	    	b.undoMove(moves.get(moves.size()-1));
	    	appTime=(System.nanoTime()-appStartTime)/2;
	        depth = (int) Math.floor(3.5*Math.log((getTime()-100)*1000000/appTime)/Math.log(moves.size()*(moves.size()-heur)));
        }
        else
        	depth =1;
        
        if(late*4>1)
        	depth--;
        System.out.println(depth + " " + appTime + " " + late*4);
        
    	boardSize = b.getSize()*b.getSize();
        int max = - boardSize-1;
        Move best = null;
        
        int alfa = - boardSize;
        int beta = boardSize;
        int newHeur;
        int safe=0;
    	PriorityQueue<HeuristicMove> movesQueue = new PriorityQueue<HeuristicMove>(moves.size(), new HeuristicMoveComparator());
        for (Move move : moves) {
        	b.doMove(move);
        	newHeur = heuristic(b,getColor());
        	/*if(heur>newHeur)
        		safe++;
            SnortMove m = (SnortMove) move;
        	if(b.getState(m.getX(), m.getY()) == getColor())*/
        	if (newHeur - heur != -1)
        		movesQueue.add(new HeuristicMove(-newHeur, move));
        	b.undoMove(move);
        }
        if(!movesQueue.isEmpty())
        	best = movesQueue.peek().move;
        else
        	best = moves.get(0);
        Move move;
        Color opColor = getOpponent(getColor());
        try {
        	if(depth>1)
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
	    	long end_time = System.currentTimeMillis();
	    	double difference = (end_time - startTime);
	    	System.out.println(difference);
	    	late=0;
        } catch (NoMoreTimeException e) {
        	late=movesQueue.size()/(double)moves.size();
        	System.out.println("I was too slow... I left " + movesQueue.size() + "/" + moves.size() + " nodes alone :( ");
        }
        return best;
    }

    public int opAlphabeta(Board b, int depth, int alfa, int beta, Color color) throws NoMoreTimeException{
    	if (getTime() <= System.currentTimeMillis() - startTime + 100)
    		throw new NoMoreTimeException();
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
            	movesQueue.add(new HeuristicMove(-heuristic(b,color), move));
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
            	movesQueue.add(new HeuristicMove(heuristic(b,color), move));
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
