package edu.jhu.nick.cs335.hw1;
import java.util.ArrayList;

import java.io.*;
import edu.jhu.nick.cs335.hw1.Helper;
import edu.jhu.nick.cs335.hw1.SearchAlgorithm;
import edu.jhu.nick.cs335.hw1.helperPackage.Helper2;

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

    Helper f = new Helper();
    f.setFoo(3);
    Helper2 b = new Helper2();
    b.setBar(5);
    
    String map_fn = args[0];
    String alg = args[1];
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
      boolean foundStart = false;
      for(int i = 0; i < lines.size(); i++)
      {
        map[i] = lines.get(i).toCharArray();
        if(foundStart == false)
        {
          for(int k = 0; k < map[i].length; k++)
          {
            if(map[i][k] == 's')
            {
              start_row = i;
              start_col = k;
              foundStart = true;
              break;
            }
          }
        }
        //System.out.println(i + " " + new String(map[i]));
      }
      //System.out.println("Start: " + start_row + " " + start_col);
      //Start search now
      SearchAlgorithm bfs = new BFS(map, start_row, start_col);
      while(bfs.isFinished() == false)
      {
        bfs.nextStep();
      }

    }
  } 
}
