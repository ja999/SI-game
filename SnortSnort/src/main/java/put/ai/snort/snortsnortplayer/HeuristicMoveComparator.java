package put.ai.snort.snortsnortplayer;

import java.util.Comparator;

import put.ai.snort.game.Move;

public class HeuristicMoveComparator implements Comparator<HeuristicMove> {

	@Override
	public int compare(HeuristicMove o1, HeuristicMove o2) {
		return o1.heuristic-o2.heuristic;
	}

}
