package edu.jhu.nick.cs335.hw1;
import java.util.ArrayList;

import java.io.*;
import edu.jhu.nick.cs335.hw1.Helper;
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
  public static void main(String[] args) throws IOException, FileNotFoundException
  {

    Helper f = new Helper();
    f.setFoo(3);
    Helper2 b = new Helper2();
    b.setBar(5);
    
    String map = args[0];
    String alg = args[1];
    System.out.println("Selected Map: " + map); 
    System.out.println("Selected Algorithm: " + alg);

    readMap(map);


  }

  public static char[][] readMap(String filename) throws FileNotFoundException, IOException
  {
    try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line = br.readLine();
        ArrayList<String> lines = new ArrayList<String>();

        while (line != null) {
            lines.add(line);
            line = br.readLine();
        }
        
        int x = Integer.parseInt(lines.get(0).split(" ")[0]);
        int y = Integer.parseInt(lines.get(0).split(" ")[1]);
        lines.remove(0); 

        char[][] map = new char[x][y];
        int i = 0;
        for(String mline: lines)
        {
          map[i] = mline.toCharArray();
          System.out.println(i + " " + new String(map[i]));
          i++;
        }
        return map;
    }
  }
}
