package edu.jhu.ben.cs335.hw2;

import java.util.Scanner;

import edu.jhu.ben.cs335.hw2.board.Board;
import edu.jhu.ben.cs335.hw2.board.Move;
import edu.jhu.ben.cs335.hw2.board.Chip;
import edu.jhu.ben.cs335.hw2.players.Player;
import edu.jhu.ben.cs335.hw2.players.HumanPlayer;
import edu.jhu.ben.cs335.hw2.players.MinimaxPlayer;
import edu.jhu.ben.cs335.hw2.players.AlphaBetaPlayer;
import edu.jhu.ben.cs335.hw2.players.TimeBoundIterativeDeepeningMinimaxPlayer;
import edu.jhu.ben.cs335.hw2.players.TimeBoundIterativeDeepeningAlphaBetaPlayer;

/**
 * Driver class for Konane program; see README for description and terms of use
 * @author Ben Mitchell
 */
public class Konane {

  public static void main(String[] args) {
    Board game;
    int boardSize;
    Player[] players = new Player[2];
    Scanner stdin = new Scanner(System.in);

    try {

      /* prompt user for game setup */
      System.out.println("Let's play Konane!");
      System.out.print("Enter board size: ");
      boardSize = stdin.nextInt();

      /* initialize game */
      game = new Board(boardSize);

      for (int i=0; i<2; i++) {
        System.out.print("Enter player type for ");
        if (i == 0)
          System.out.print("Black");
        else
          System.out.print("White");

        System.out.print(" (1=human, 2=minmax, 3=alphabeta): ");
        int choice = stdin.nextInt();
        int maxSearchDepth = 1;
        int bound = 0;
        switch (choice) {
          case 1:
            players[i] = new HumanPlayer();
            break;
          case 2:
            System.out.print(" Specify 0 for fixed search depth, 1 for time-bounded search: ");
            bound = stdin.nextInt();
            if(bound == 0) {
              System.out.print(" Specify Maximum Search Depth: ");
              maxSearchDepth = stdin.nextInt();
              players[i] = new MinimaxPlayer(maxSearchDepth, i);
            }else{
              System.out.print(" Specify Time Allowed per Turn in Seconds: ");
              double timeAllowed = stdin.nextDouble();
              players[i] = new TimeBoundIterativeDeepeningMinimaxPlayer(timeAllowed, i);
            }
            break;
          case 3:
            System.out.print(" Specify 0 for naive move-ordering, 1 for heuristic-based move ordering: ");
            int moveOrder = stdin.nextInt();
            boolean moveBool = false;
            if(moveOrder == 0) {
              moveBool = false;
            } else {
              moveBool = true;
            }
    
            System.out.print(" Specify 0 for fixed search depth, 1 for time-bounded search: ");
            bound = stdin.nextInt();
            if(bound == 0) {
              System.out.print(" Specify Maximum Search Depth: ");
              maxSearchDepth = stdin.nextInt();
              players[i] = new AlphaBetaPlayer(maxSearchDepth, i, moveBool);
            }else{
              System.out.print(" Specify Time Allowed per Turn in Seconds: ");
              double timeAllowed = stdin.nextDouble();
              players[i] = new TimeBoundIterativeDeepeningAlphaBetaPlayer(timeAllowed, i, moveBool);
            }
            break;
          default:
            System.out.println("bad agent type given, please try again...");
            i--;
        }
      }

      System.out.println("\n===================");

      /* take turns until gameover */
      while ( game.gameWon() == Chip.NONE ) {  
        System.out.print("Turn " + game.getTurn() + ", ");
        if (game.getTurn()%2 == 0) {
          System.out.println("black to play:");
        } else {
          System.out.println("white to play:");
        }

        System.out.println(game);

        Move m = players[game.getTurn()%players.length].getMove(game);
        game.executeMove(m);
      }

      System.out.println("Game over!  Final board state:\n" + game);

      if (game.gameWon() == Chip.BLACK) {
        System.out.println("Game won by Black after " + game.getTurn() + " turns");
      } else {
        System.out.println("Game won by White after " + game.getTurn() + " turns");
      }
      
      System.out.println("Black Player explored a total of " + players[0].getTotalNodesExplored() + " nodes");
      System.out.println("White Player explored a total of " + players[1].getTotalNodesExplored() + " nodes");

    } catch (Exception e) {
      System.err.println("Caught an exception: \n\t" + e.toString());
    }

  }

}
