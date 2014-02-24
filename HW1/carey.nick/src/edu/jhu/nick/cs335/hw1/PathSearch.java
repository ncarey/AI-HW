package edu.jhu.nick.cs335.hw1;
import java.util.ArrayList;

import java.io.*;
import edu.jhu.nick.cs335.hw1.SearchAlgorithm;

public class PathSearch
{
  /**
   * A simple test program.
   * <p>
   * This program simply prints Hello World to stdout.
   * It also demonstrates proper package use.
   * 
   *
   * @param  None No arguments are read.
   * @return void Nothing error-code is return. 
   */
  public static void main(String[] args) throws Exception, IOException, FileNotFoundException
  {

    String map_fn = args[0];
    String alg = args[1].trim();
    System.out.println("Selected Map: " + map_fn); 
    System.out.println("Selected Algorithm: " + alg);

    try(BufferedReader br = new BufferedReader(new FileReader(map_fn))) 
    {
      String line = br.readLine();
      ArrayList<String> lines = new ArrayList<String>();

      while (line != null) {
        lines.add(line);
        line = br.readLine();
      }
        
      int y = Integer.parseInt(lines.get(0).split(" ")[0]);
      int x = Integer.parseInt(lines.get(0).split(" ")[1]);
      lines.remove(0); 

      char[][] map = new char[x][y];
      int start_row = 0;
      int start_col = 0;
      int goal_row = 0;
      int goal_col = 0;
      boolean foundStart = false;
      boolean foundGoal = false;

      for(int i = 0; i < lines.size(); i++)
      {
        map[i] = lines.get(i).toCharArray();
        if(foundStart == false || foundGoal == false)
        {
          for(int k = 0; k < map[i].length; k++)
          {
            if(map[i][k] == 's')
            {
              start_row = i;
              start_col = k;
              foundStart = true;
            }
            if(map[i][k] == 'g')
            {
              goal_row = i;
              goal_col = k;
              foundGoal = true;
            }
          }
        }
        //System.out.println(i + " " + new String(map[i]));
      }
      //Start search now
      SearchAlgorithm search;
      if(alg.equals("BFS"))
      {
        search = new BFS(map, start_row, start_col);
      }
      else if(alg.equals("DFS"))
      {
        search = new DFS(map, start_row, start_col);
      }
      else if(alg.equals("A*"))
      {
        search = new AStar(map, start_row, start_col, goal_row, goal_col);
      }else{
        //default to BFS
        search = new BFS(map, start_row, start_col);
      }

      while(search.isFinished() == false)
      {
        search.nextStep();
      }

    }
  } 
}
