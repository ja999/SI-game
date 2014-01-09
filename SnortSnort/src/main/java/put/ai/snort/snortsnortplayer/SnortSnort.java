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
        return moves.get(random.nextInt(moves.size()));
    }
}
