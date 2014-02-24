package edu.jhu.nick.cs335.hw1;
import java.util.ArrayList;
import edu.jhu.nick.cs335.hw1.SearchAlgorithm;
import edu.jhu.nick.cs335.hw1.GridSpaceObject;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.Integer;
import java.lang.Math;

public class AStar extends SearchAlgorithm
{

  private HashSet<GridSpaceObject> closedSet;
  private HashSet<GridSpaceObject> openSet;  
  private HashMap<GridSpaceObject, GridSpaceObject> cameFrom;
  private HashMap<GridSpaceObject, Integer> gScore;
  private HashMap<GridSpaceObject, Integer> fScore;
  private GridSpaceObject goal;
  public AStar(char[][] map_in, int start_r, int start_c, int goal_r, int goal_c)
  {
    super(map_in, start_r, start_c);
    closedSet = new HashSet<GridSpaceObject>();
    openSet = new HashSet<GridSpaceObject>();
    GridSpaceObject start = new GridSpaceObject(cur_row, cur_col, map[cur_row][cur_col], null);
    goal = new GridSpaceObject(goal_r, goal_c, map[goal_r][goal_c], null);
    openSet.add(start);
    cameFrom = new HashMap<GridSpaceObject, GridSpaceObject>();
    gScore = new HashMap<GridSpaceObject, Integer>();
    gScore.put(start, new Integer(0));
    fScore = new HashMap<GridSpaceObject, Integer>();
    fScore.put(start, costEstimate(start));
  }
 
  public void nextStep() throws Exception
  {
    while(!openSet.isEmpty())
    {
      //get GridSpaceObject in openSet with least fscore
      Iterator<GridSpaceObject> it = openSet.iterator();
      GridSpaceObject current = it.next();
      Integer minScore = fScore.get(current);
      GridSpaceObject tmp = current;
      while(it.hasNext())
      {
        tmp = it.next();
        if(fScore.get(tmp) < minScore)
        {
          minScore = fScore.get(tmp);
          current = tmp;
        }
      }
      //check if at goal
      if(current.getChar() == 'g')
      {
        amFinished = true;
        String path = reconstruct_path(current);
        path = path.substring(0, path.length() - 4);
        printSuccess(path);
        return;
      }

      //remove current from openSet
      openSet.remove(current);
      cur_nodes++;
      //add current to closedSet
      closedSet.add(current);
      ArrayList<GridSpaceObject> neighbors = new ArrayList<GridSpaceObject>();
      //add only neighbors not in closedset
      neighbors.add(new GridSpaceObject(current.getRow()+1, current.getCol(), map[current.getRow()+1][current.getCol()], null));
      neighbors.add(new GridSpaceObject(current.getRow(), current.getCol()+1, map[current.getRow()][current.getCol()+1], null));
      neighbors.add(new GridSpaceObject(current.getRow()-1, current.getCol(), map[current.getRow()-1][current.getCol()], null));
      neighbors.add(new GridSpaceObject(current.getRow(), current.getCol()-1, map[current.getRow()][current.getCol()-1], null));
      for(int i = 0; i < neighbors.size(); i++)
      {
        if(neighbors.get(i).getChar() == '#' || neighbors.get(i).getChar() == 's' || closedSet.contains(neighbors.get(i)))
        {
          neighbors.remove(i);
          i--;
        }
      
      }
      for(int i = 0; i < neighbors.size(); i++)
      {
        Integer tentative_g_score = gScore.get(current) + 1; //TODO change this based on ',' or '.'      
        Integer ngScore;
        if(gScore.keySet().contains(neighbors.get(i)))
        {
          ngScore = gScore.get(neighbors.get(i));
        }
        else
        {
          ngScore = Integer.MAX_VALUE;
        }

        if(!openSet.contains(neighbors.get(i)) || tentative_g_score < ngScore)
        {
          cameFrom.put(neighbors.get(i), current);
          gScore.put(neighbors.get(i), tentative_g_score);
          fScore.put(neighbors.get(i), tentative_g_score + costEstimate(neighbors.get(i)));
          if(!openSet.contains(neighbors.get(i)))
          {
            openSet.add(neighbors.get(i));
          }

        }
        
      }
    }
    printFailure();
    amFinished = true;
  }

  private String reconstruct_path(GridSpaceObject b)
  {
    if(cameFrom.keySet().contains(b))
    {
      return reconstruct_path(cameFrom.get(b)) + "(" + b.getRow() + "," + b.getCol() + ") -> ";
    }
    else
    {
      return "(" + b.getRow() + "," + b.getCol() + ") -> ";
    }
  }
 
  private Integer costEstimate(GridSpaceObject cur)
  {
    return new Integer(Math.abs((cur.getRow() - goal.getRow())) + Math.abs((cur.getCol() - goal.getCol())));
  }

}
