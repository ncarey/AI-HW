package edu.jhu.nick.cs335.hw1;

public abstract class SearchAlgorithm
{

  protected char[][] map;
  protected int cur_row;
  protected int cur_col;
  protected int prev_row;
  protected int prev_col;
  protected int cur_cost;
  protected int cur_nodes;
  protected boolean amFinished;

  public SearchAlgorithm(char[][] map_in, int start_r, int start_c)
  {
    map = map_in;
    cur_row = start_r;
    cur_col = start_c;
    cur_cost = 0;
    cur_nodes = 0;   
    amFinished = false;
  }

  boolean isFinished()
  {
    return amFinished;
  }

  abstract void nextStep() throws Exception;
  
  int getCost(int row, int col)
  {
    if(map[row][col] == '.' || map[row][col] == 's' || map[row][col] == 'g')
    {
      return 1;
    }
    else if(map[row][col] == ',')
    {
      return 2;
    }
    else
    {
      return 0;
    }
  }

  void printSuccess(String path)
  {
    System.out.println("Path Taken: " + path);
    System.out.println("Cost: " + cur_cost);
    System.out.println("Nodes Expanded: " + cur_nodes);
  }  
  void printFailure()
  {
    System.out.println("There exists no path to the goal");
    System.out.println("Cost: Infinity");
    System.out.println("Nodes Expanded: " + cur_nodes);
  } 
}
