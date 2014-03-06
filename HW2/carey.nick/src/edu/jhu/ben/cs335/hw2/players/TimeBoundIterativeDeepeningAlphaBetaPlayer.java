package edu.jhu.ben.cs335.hw2.players;

import edu.jhu.ben.cs335.hw2.players.Player;
import edu.jhu.ben.cs335.hw2.board.Board;
import edu.jhu.ben.cs335.hw2.board.Move;
import edu.jhu.ben.cs335.hw2.board.Chip;
import edu.jhu.ben.cs335.hw2.board.InvalidMoveException;

import java.util.ArrayList;
import java.lang.Math;
import java.lang.Integer;
/** alpha-beta pruning Player class for Konane game
 *
 * AlphaBeta Search Algorithm:
 *
 * global max_search_depth
 *
 * function AlphaBeta-decision(Board) returns a Move
 *   alpha = -infinity, beta = infinity
 *   v = Max-Value(Board, alpha, beta, 1)
 *   return Move with value v
 *
 * function Max-Value(Board, alpha, beta, cur_depth) returns a utility value
 *   if cutOffTest(Board, cur_depth)
 *     return boardUtilityEvaluation(Board)
 *   ret = -infinity
 *   for each possible action on Board:
 *     ret = max(ret, Min-Value(action applied to Board, alpha, beta, cur_depth+1)
 *     if ret >= beta 
 *       return ret
 *     alpha = max(ret, alpha)
 *   return ret
 *
 * function Min-Value(Board, alpha, beta, cur_depth) returns a utility value
 *   if cutOffTest(Board, cur_depth)
 *     return boardUtilityEvaluation(Board)
 *   ret = infinity
 *   for each possible action on Board:
 *     ret = min(ret, Max-Value(action applied to Board, alpha, beta, cur_depth+1)
 *     if ret less than or eq to alpha
 *       return ret
 *     beta = min(ret, beta)
 *   return ret
 *
 * NOTE: functions cutOffTest, boardUtilityEvaluation are the same as in MinimaxPlayer
 *
 *
 * @author Nick Carey
 */
public class TimeBoundIterativeDeepeningAlphaBetaPlayer extends Player {

  /**
   * Variable used for controlling debugging statements - 1 on, 0 off
   */
  private static final int DEBUG = 1;

  /** 
   * the current max depth - increases each iteration 
   */
  private int maxDepth;
  /** 
   * maximum time allowed by user per turn
   */
  private double maxTime;

  /**
   * Chip color representing player 
   * used for boardUtilityEvaluation function.
   */
  private Chip player;

  /**
   * counter variable to keep track of expanded game state nodes
   */
  private int totalNodesExplored;

  /**
   * counter variable to keep track of expanded game state nodes per turn
   * this variable is reset at each call to getMove()
   */
  private int currentNodesExplored;

  /**
   * Boolean set at construction specifying whether to use move-ordering heuristic
   */
  private boolean orderMoves;

  /**
   * Construct a AlphaBetaPlayer object by specifiying the maximum search depth
   * The alpha-beta algorithm will look maxDepth moves ahead when determining the 
   * optimal move to make
   *
   * @param maxSearchDepth the maximum search depth for the search tree
   * @param playerColor integer representing player - 0 is black, anything else is white
   */
  public TimeBoundIterativeDeepeningAlphaBetaPlayer(double timeBound, int playerColor, boolean toOrderMoves) {

    super();
    maxTime = timeBound;
    totalNodesExplored = 0;
    this.orderMoves = toOrderMoves;

    if(playerColor == 0) {
      player = Chip.BLACK;
    }else{
      player = Chip.WHITE;
    }
  }

  /**
   * Getter function for total nodes explored by player 
   */
  public int getTotalNodesExplored() {
    return totalNodesExplored;
  }

  /**
   * Method that returns the player's next move according to the time bound AlphaBeta Search Algorithm 
   * This method is really just a driver for the original getMove method
   * It calls the original getMove over and over with increasing depth limits until time runs out
   *
   * @param game the current game state; state is gaurenteed by the driver to not be an end-game state
   */
  public Move getMove(Board game) {
    long startTime = System.nanoTime();
    currentNodesExplored = 0;
    Move ret = null;
    maxDepth = 0;
    long endTime = System.nanoTime();
    double duration = ((double)(endTime - startTime)) / 1000000000.0;

    /* rationale here is that the next depth level search will probably take 
     * current execution duration time quadrupled, due to the search tree leaf nodes
     * expanding exponentially by a power of about 4 as depth increases
     */
    while(duration*4 < maxTime) {
      maxDepth++;
      ret = getMoveHelper(game);
      endTime = System.nanoTime();
      duration = ((double)(endTime - startTime)) / 1000000000.0;
    }

    if(DEBUG == 1) {
      System.out.println("  Current move decision has explored " + currentNodesExplored + " nodes.");
      System.out.println("  Current move decision has taken " + duration + " seconds.");
    }
    System.out.println("Executing move " + ret.toString());
    return ret;
  }

  /**
   * Method that returns the player's next move according to the AlphaBeta Search Algorithm
   *
   * @param game the current game state, gaurenteed by the driver to have an available move
   */
  public Move getMoveHelper(Board game) {
    
    Move ret = null;
    int mAlpha = Integer.MIN_VALUE;
    int mBeta = Integer.MAX_VALUE;
   
    /* Implementing a modified version of maxValue here so we can remember the moves
      associated with each value */
    int depth = 0;
    
    int maxUtil = Integer.MIN_VALUE;
    int curUtil;
    ArrayList<Move> legalMoves = game.getLegalMoves();

    if(DEBUG == 1) {
      System.out.println("  Current player has " + legalMoves.size() + " moves to examine");
    }

    /* apply each move to seperate copy of board */
    ArrayList<Board> moveBoard = new ArrayList<Board>();
    for(int i = 0; i < legalMoves.size(); i++) {
      try {
        Board copy = new Board(game);
        copy.executeMove(legalMoves.get(i));
        moveBoard.add(copy);
      } catch(InvalidMoveException e) {
        System.out.println("ERROR: AlphaBeta examined an invalid move. Something is seriously wrong. Continuing");
        continue;
      }
    }

    /* If user wants move ordering, sort list of boards and moves based on heuristic function */
    if(this.orderMoves) {
      //orderBoards(moveBoard, legalMoves);
      int[] scores = new int[moveBoard.size()];
      for(int i = 0; i < moveBoard.size(); i++) {
        scores[i] = boardUtilityEvaluation(moveBoard.get(i));
      }
      //yay n^2 sort!
      for(int i = 0; i < moveBoard.size(); i++) {
        for(int j = i+1; j < moveBoard.size(); j++) {
          if(scores[i] < scores[j]) {

            int tmp = scores[i];
            scores[i] = scores[j];
            scores[j] = tmp;
            moveBoard.set(j, (moveBoard.set(i, moveBoard.get(j))));
            legalMoves.set(j, (legalMoves.set(i, legalMoves.get(j))));
          }
        }
      }
    }


    for(int i = 0; i < moveBoard.size(); i++) {
      //execute move on eval board
      Board copy = moveBoard.get(i);
      
      curUtil = minValue(copy, mAlpha, mBeta, depth+1);

      if(DEBUG == 1) {
        System.out.println("  Move: " + legalMoves.get(i).toString() + " has a util value of " + curUtil);
      }


      if( curUtil > maxUtil) {
        ret = legalMoves.get(i);
        maxUtil = curUtil;
      }

      if(maxUtil >= mBeta) {
        return ret;
      }
      mAlpha = Math.max(maxUtil, mAlpha);

    }

    return ret;

  }
  /**
   * Method that estimates the optimal move for this player
   *
   * @param game game state of current node in search tree
   * @param depth current depth of search tree
   * @param alpha bookeeping variable for branch pruning in alphabeta search algorithm
   * @param beta bookeeping variable for branch pruning in alphabeta search algorithm 
   */
  private int maxValue(Board game, int alpha, int beta, int depth) {

    int mAlpha = alpha;
    int mBeta = beta;
 
    totalNodesExplored++;
    currentNodesExplored++; 

    if(searchCutOffTest(game, depth)) {
      return boardUtilityEvaluation(game);
    }

    int maxUtil = Integer.MIN_VALUE;
    ArrayList<Move> legalMoves = game.getLegalMoves();
    /* apply each move to seperate copy of board */
    ArrayList<Board> moveBoard = new ArrayList<Board>();
    for(int i = 0; i < legalMoves.size(); i++) {
      try {
        Board copy = new Board(game);
        copy.executeMove(legalMoves.get(i));
        moveBoard.add(copy);
      } catch(InvalidMoveException e) {
        System.out.println("ERROR: AlphaBeta examined an invalid move. Something is seriously wrong. Continuing");
        continue;
      }
    }

    /* If user wants move ordering, sort list of boards and moves based on heuristic function */
    if(this.orderMoves) {
      int[] scores = new int[moveBoard.size()];
      for(int i = 0; i < moveBoard.size(); i++) {
        scores[i] = boardUtilityEvaluation(moveBoard.get(i));
      }
      //yay n^2 sort!
      for(int i = 0; i < moveBoard.size(); i++) {
        for(int j = i+1; j < moveBoard.size(); j++) {
          if(scores[i] < scores[j]) {

            int tmp = scores[i];
            scores[i] = scores[j];
            scores[j] = tmp;
            moveBoard.set(j, (moveBoard.set(i, moveBoard.get(j))));
            legalMoves.set(j, (legalMoves.set(i, legalMoves.get(j))));
          }
        }
      }
    }

    for(int i = 0; i < moveBoard.size(); i++) {
      //execute move on eval board
      Board copy = moveBoard.get(i);
      
      maxUtil = Math.max(maxUtil, minValue(copy, mAlpha, mBeta, depth+1));
      if(maxUtil >= mBeta) {
        return maxUtil;
      }
      mAlpha = Math.max(maxUtil, mAlpha);

    }
    return maxUtil;
 
  }


  /**
   * Method that estimates the opposing player's optimal move
   *
   * @param game game state of current node in search tree
   * @param depth current depth of search tree
   * @param alpha bookeeping variable for branch pruning in alphabeta search algorithm
   * @param beta bookeeping variable for branch pruning in alphabeta search algorithm 
   */
  private int minValue(Board game, int alpha, int beta, int depth) {

    int mAlpha = alpha;
    int mBeta = beta;    

    totalNodesExplored++;
    currentNodesExplored++;

    if(searchCutOffTest(game, depth)) {
      return boardUtilityEvaluation(game);
    }

    int minUtil = Integer.MAX_VALUE;
    ArrayList<Move> legalMoves = game.getLegalMoves();
    /* apply each move to seperate copy of board */
    ArrayList<Board> moveBoard = new ArrayList<Board>();
    for(int i = 0; i < legalMoves.size(); i++) {
      try {
        Board copy = new Board(game);
        copy.executeMove(legalMoves.get(i));
        moveBoard.add(copy);
      } catch(InvalidMoveException e) {
        System.out.println("ERROR: AlphaBeta examined an invalid move. Something is seriously wrong. Continuing");
        continue;
      }
    }

    /* If user wants move ordering, sort list of boards and moves based on heuristic function */
    if(this.orderMoves) {
      int[] scores = new int[moveBoard.size()];
      for(int i = 0; i < moveBoard.size(); i++) {
        scores[i] = boardUtilityEvaluation(moveBoard.get(i));
      }
      //yay n^2 sort!
      for(int i = 0; i < moveBoard.size(); i++) {
        for(int j = i+1; j < moveBoard.size(); j++) {
          if(scores[i] < scores[j]) {

            int tmp = scores[i];
            scores[i] = scores[j];
            scores[j] = tmp;
            moveBoard.set(j, (moveBoard.set(i, moveBoard.get(j))));
            legalMoves.set(j, (legalMoves.set(i, legalMoves.get(j))));
          }
        }
      }
    }
    //want to examine boards with minimum utility first
    for(int i = moveBoard.size() - 1; i >= 0; i--) {
      //execute move on eval board
      Board copy = moveBoard.get(i);
  
      minUtil = Math.min(minUtil, maxValue(copy, mAlpha, mBeta, depth+1));
      if(minUtil <= mAlpha) {
        return minUtil;
      }
      mBeta = Math.min(minUtil, mBeta);

    }
    return minUtil;

  }

  /**
   * Board Utility Evaluation function based on number of moves available
   * Since the game is all about being able to make a move, this function
   * evaluated board utility based on how many moves available to you and your opponent
   * 
   * @param game game state whose utility is to be examined
   */
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


  /**
   * Private helper method for determining whether the search tree should
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
