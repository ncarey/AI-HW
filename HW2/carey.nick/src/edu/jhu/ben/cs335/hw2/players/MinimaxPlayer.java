package edu.jhu.ben.cs335.hw2.players;

import edu.jhu.ben.cs335.hw2.players.Player;
import edu.jhu.ben.cs335.hw2.board.Board;
import edu.jhu.ben.cs335.hw2.board.Move;
import edu.jhu.ben.cs335.hw2.board.Chip;
import edu.jhu.ben.cs335.hw2.board.InvalidMoveException;
import java.lang.Integer;
import java.lang.Math;
import java.util.ArrayList;

/** minimax Player class for Konane game
 *
 * Minimax Search Algorithm:
 * 
 *  global max_search_depth
 *  
 * function Minimax-decision(Board) returns a Move
 *   utility_val = -infinity
 *   ret = null
 *   for each possible action:
 *     cur_val = Min-Value(action applied to Board, 1)
 *     if cur_val > utility_val
 *       ret = action
 *       utility_val = cur_val
 *   return ret
 *
 * function Min-Value(Board, cur_depth) returns a utility value
 *   if curoffTest(Board, cur_depth)
 *     return boardUtilityEvaluation(Board)
 *   ret = infinity
 *   for each possible action:
 *     ret = min(ret, Max-Value(action applied to Board, cur_depth+1)
 *   return ret
 *
 * function Max-Value(Board, cur_depth) returns a utility value
 *   if cutoffTest(Board, cur_depth)
 *     return boardUtilityEvaluation(Board)
 *   ret = -infinity
 *   for each possible action:
 *     ret = max(ret, Min-Value(action applied to Board, cur_depth+1)
 *   return ret
 *
 * function cutoffTest(Board, depth) returns a boolean
 *   if Board is end-game state
 *     return true
 *   if depth >= max_search_depth
 *     return true
 *   return false
 *
 * function boardUtilityEvaluation(Board) returns a heuristic utility value
 *   if Board is in end-game state
 *     return MAX_UTIL_VAL if victory
 *     return MIN_UTIL_VAL if loss
 *   return MIN_UTIL_VAL less than *ret* less than MAX_UTIL_VAL 
 *      where ret is based on a heuristic
 *
 * @author Nick Carey
 */

public class MinimaxPlayer extends Player {

  /** 
   * maximum minimax search tree depth specified at runtime
   */
  private int maxDepth;

  /**
   * Chip color representing player 
   * used for boardUtilityEvaluation function.
   */
  private Chip player;

  /**
   * Construct a MinimaxPlayer object by specifiying the maximum search depth
   * The minimax algorithm will look maxDepth moves ahead when determining the 
   * optimal move to make
   *
   * @param maxSearchDepth the maximum search depth for the minimax search tree
   * @param playerColor integer representing player - 0 is black, anything else is white
   */
  public MinimaxPlayer(int maxSearchDepth, int playerColor) {
  
    super();
    maxDepth = maxSearchDepth;
    
    if(playerColor == 0) {
      player = Chip.BLACK;
    }else{
      player = Chip.WHITE;
    }
  }

  /**
   * Method that returns the player's next move according to the Minimax Search Algorithm 
   *
   * function Minimax-decision(Board) returns a Move
   *   utility_val = -infinity
   *   ret = null
   *   for each possible action:
   *     cur_val = Min-Value(action applied to Board, 1)
   *     if cur_val > utility_val
   *       ret = action
   *       utility_val = cur_val
   *   return ret
   *
   * @param game the current game state; state is gaurenteed by the driver to not be an end-game state
   */
  public Move getMove(Board game){
    Move ret = null;
    int maxUtil = Integer.MIN_VALUE;
    
    int curUtil;    
    ArrayList<Move> legalMoves = game.getLegalMoves();
    for(int i = 0; i < legalMoves.size(); i++) {
      //get copy of game board
      Board copy = new Board(game);

      //execute move on game board copy
      try {
        copy.executeMove(legalMoves.get(i));
      } catch(InvalidMoveException e) {
        System.out.println("ERROR: Minimax examined an invalid move. Something is seriously wrong. Continuing");
        continue;
      }
      //Examine what optimal opponent will do. 
      //Choose move that results in best case for self given optimal opponent's decision
      curUtil = minValue(copy, 1);
      if(curUtil > maxUtil) {
        ret = legalMoves.get(i);
        maxUtil = curUtil;
      }
    } 
    System.out.println("Executing move " + ret.toString());   
    return ret;

  }

  /**
   * function Min-Value(Board, cur_depth) returns a utility value
   *   if curoffTest(Board, cur_depth)
   *     return boardUtilityEvaluation(Board)
   *   ret = infinity
   *   for each possible action:
   *     ret = min(ret, Max-Value(action applied to Board, cur_depth+1)
   *   return ret
   *
   * @param game game state of current node in search tree
   * @param depth current depth of search
   */
  private int minValue(Board game, int depth) {

    if(searchCutOffTest(game, depth)) {
      return boardUtilityEvaluation(game);
    }

    int minUtil = Integer.MAX_VALUE;
    ArrayList<Move> legalMoves = game.getLegalMoves();
    for(int i = 0; i < legalMoves.size(); i++) {
      Board copy = new Board(game);
      try {
        copy.executeMove(legalMoves.get(i));
      } catch(InvalidMoveException e) {
        System.out.println("ERROR: Minimax examined an invalid move. Something is seriously wrong. Continuing");
        continue;
      }
      minUtil = Math.min(minUtil, maxValue(copy, depth+1));
    }    
  
    return minUtil;

  }

  /**
   * function Max-Value(Board, cur_depth) returns a utility value
   *   if cutoffTest(Board, cur_depth)
   *     return boardUtilityEvaluation(Board)
   *   ret = -infinity
   *   for each possible action:
   *     ret = max(ret, Min-Value(action applied to Board, cur_depth+1)
   *   return ret
   *
   * @param game game state of current node in search tree
   * @param depth current depth of search\
   */
  private int maxValue(Board game, int depth) {

    if(searchCutOffTest(game, depth)) {
      return boardUtilityEvaluation(game);
    }

    int maxUtil = Integer.MIN_VALUE;
    ArrayList<Move> legalMoves = game.getLegalMoves();
    for(int i = 0; i < legalMoves.size(); i++) {
      Board copy = new Board(game);
      try {
        copy.executeMove(legalMoves.get(i));
      } catch(InvalidMoveException e) {
        System.out.println("ERROR: Minimax examined an invalid move. Something is seriously wrong. Continuing");
        continue;
      }
      maxUtil = Math.max(maxUtil, minValue(copy, depth+1));
    }    
  
    return maxUtil;

  }

  private int boardUtilityEvaluation(Board game) {
  
    int maxUtilValue = game.getSize() * game.getSize();
    int minUtilValue = -1 * maxUtilValue;
    // Check game-over conditions 
    if(game.gameWon() != Chip.NONE) {
      if(game.gameWon() == this.player) {
        return maxUtilValue;
      }else{
        return minUtilValue;
      }
    }else{
      ArrayList<Move> legalMoves = game.getLegalMoves();
      int score = legalMoves.size();
      if(game.getTile(legalMoves.get(0).pointFrom()).getChip() != this.player){
        score = score * -1;
      }
      return score;

    }
  }

/* Utility function based on number of remaining chips. Turned out not so great...
  private int boardUtilityEvaluation(Board game) {

    int maxUtilValue = game.getSize() * game.getSize();
    int minUtilValue = 0;
    // Check game-over conditions 
    if(game.gameWon() != Chip.NONE) {
      if(game.gameWon() == this.player) {
        return maxUtilValue;
      }else{
        return minUtilValue;
      }
    }else{
    // Utility will be: number of own chips + number of chips opponent has lost           
      int maxChips = maxUtilValue / 2;
      int myChips = 0;
      int hisChips = 0;
      
      Chip curChip;
      for(int row = 0; row < game.getSize(); row++) {
        for(int col = 0; col < game.getSize(); col++) {

          try {
            curChip = game.getTile(row, col).getChip();
            if(curChip == this.player) {
              myChips++;
            }else if(curChip == Chip.NONE) {
              //nothing
            }else{
              hisChips++;
            }            
          } catch(IndexOutOfBoundsException e) {
            System.out.println("This should never have happened... Index out of bounds. Continuing");
            continue;
          }

        }
      }

      return myChips + (maxChips - hisChips);

    }
  }
*/


  /**
   * Private helper method for determining whether the minimax search tree should
   * halt and compute a board state utility evaluation
   * Board utility evaluation will occur if the Board is in an end-game state
   * or if the maximum search tree depth has been reached.
   * 
   * @param game the board game state that will be tested for end-game status
   * @param depth the current depth of the minimax search tree
   */
  private boolean searchCutOffTest(Board game, int depth) {
    
    if(depth >= this.maxDepth) {
      return true;
    }
    if(game.gameWon() != Chip.NONE){
      return true;
    }
 
    return false;

  }

}
