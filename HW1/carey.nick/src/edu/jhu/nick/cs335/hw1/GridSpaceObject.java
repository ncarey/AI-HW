package edu.jhu.nick.cs335.hw1;
import java.util.ArrayList;


public class GridSpaceObject
{

  private int row,col;  
  private char mchar;
  private GridSpaceObject parent;
  private ArrayList<GridSpaceObject> children;
  public GridSpaceObject(int row_in, int col_in, char char_in, GridSpaceObject parent_in)
  {
    row = row_in;
    col = col_in;
    mchar = char_in;
    parent = parent_in;
    children = new ArrayList<GridSpaceObject>();
  }
  public GridSpaceObject getParent()
  {
    return parent;
  }
  public void addChild(GridSpaceObject child_in)
  {
    children.add(child_in);
  }
  public int getRow()
  {
    return row;
  }
  public int getCol()
  {
    return col;
  }
  public char getChar()
  {
    return mchar;
  }
  @Override
  public boolean equals(Object obj)
  {
    if(obj == null)
    {
      return false;
    } 
    GridSpaceObject other = (GridSpaceObject)obj;
    if(this.row == other.getRow() && this.col == other.getCol())
    {
      return true;
    }
    else
    {
      return false;
    }
  }
}
