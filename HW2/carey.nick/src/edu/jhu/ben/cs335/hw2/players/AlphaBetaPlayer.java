package edu.jhu.ben.cs335.hw2.players;

import edu.jhu.ben.cs335.hw2.players.Player;
import edu.jhu.ben.cs335.hw2.board.Board;
import edu.jhu.ben.cs335.hw2.board.Move;

/** alpha-beta pruning Player class for Konane game
 *
 * @author Ben Mitchell
 */
public class AlphaBetaPlayer extends Player {

  private int maxDepth;

  public AlphaBetaPlayer(int maxSearchDepth) {
  
    super();
    maxDepth = maxSearchDepth;

  }


  public Move getMove(Board game) {
    Move ret = new Move(-1, -1, -1, -1);

    //TODO

    return ret;
  }


}
