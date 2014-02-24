package edu.jhu.nick.cs335.hw1;
import java.util.ArrayList;
import edu.jhu.nick.cs335.hw1.SearchAlgorithm;
import edu.jhu.nick.cs335.hw1.GridSpaceObject;

public class DFS extends SearchAlgorithm
{
  
  private ArrayList<GridSpaceObject> mqueue;
 
  public DFS(char[][] map_in, int start_r, int start_c)
  {
    super(map_in, start_r, start_c);
    mqueue = new ArrayList<GridSpaceObject>();
    mqueue.add(0, new GridSpaceObject(cur_row, cur_col, map[cur_row][cur_col], null));

  }

  public void nextStep() throws Exception
  {
    //check if queue empty. If it is, failed search
    if(mqueue.size() == 0)
    {
      amFinished = true;
      printFailure();
      return;
    }

    //pop queue, examine
    GridSpaceObject cur_gridspace = mqueue.remove(0);
    //increment number of nodes expanded.
    cur_nodes++;
    cur_row = cur_gridspace.getRow();
    cur_col = cur_gridspace.getCol();

    //mark gridspace as examined
    if(cur_gridspace.getChar() == '.')
    {
      map[cur_gridspace.getRow()][cur_gridspace.getCol()] = 'P';
    }
    else if(cur_gridspace.getChar() == ',')
    {
      map[cur_gridspace.getRow()][cur_gridspace.getCol()] = 'C';
    }

    if(cur_gridspace.getChar() == 'g')
    {
      amFinished = true;
      //construct path
      ArrayList<GridSpaceObject> pathArr = new ArrayList<GridSpaceObject>();
      GridSpaceObject tmp = cur_gridspace;
      while(tmp != null)
      {
        pathArr.add(0, tmp);
        tmp = tmp.getParent();
      }
      String path = "";
      cur_cost = 0;
      for(int j = 0; j < pathArr.size(); j++)
      {
        path = path + "(" + pathArr.get(j).getRow() + "," + pathArr.get(j).getCol() + ") -> "; 
        //calculate cost
        if(j == 0){
          //do nothing to update cost
        }
        else if(pathArr.get(j).getChar() == '.' || pathArr.get(j).getChar() == 'g')
        {
          cur_cost += 1;
        }
        else if(pathArr.get(j).getChar() == ',')
        {
          cur_cost += 2;
        }
        else
        {
          throw new Exception("illegal PathArr char: " + pathArr.get(j).getChar());
        }
      }
      path = path.substring(0, path.length() - 4);
      printSuccess(path);
      
    }
    else 
    {
      //enqueue successors that havnt been examined yet
      
      GridSpaceObject[] neighbors = new GridSpaceObject[4];
      neighbors[0] = new GridSpaceObject(cur_row+1, cur_col, map[cur_row+1][cur_col], cur_gridspace); 
      neighbors[1] = new GridSpaceObject(cur_row, cur_col+1, map[cur_row][cur_col+1], cur_gridspace); 
      neighbors[2] = new GridSpaceObject(cur_row-1, cur_col, map[cur_row-1][cur_col], cur_gridspace); 
      neighbors[3] = new GridSpaceObject(cur_row, cur_col-1, map[cur_row][cur_col-1], cur_gridspace); 
    
      for(int i = 0; i < 4; i++)
      {
        if(neighbors[i].getChar() == 's' || neighbors[i].getChar() == '#' 
          || neighbors[i].getChar() == 'P' || neighbors[i].getChar() == 'C')
        {
          //dont need to put on queue as it isnt a viable option.. X means already examined.
          continue;
        }
        else if(neighbors[i].getChar() == ',' || neighbors[i].getChar() == '.' || neighbors[i].getChar() == 'g')
        {
          mqueue.add(0, neighbors[i]);
        }
        else
        {
          throw new Exception("illegal char in neighbors: " + neighbors[i].getChar());
        }
      }
    }
  }
}
