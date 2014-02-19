package edu.jhu.ben.cs335.hw1;

import java.io.*;
import edu.jhu.ben.cs335.hw1.Helper;
import edu.jhu.ben.cs335.hw1.helperPackage.Helper2;

public class HelloWorld
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
  public static void main(String[] args) throws IOException
  {

    Helper f = new Helper();
    f.setFoo(3);
    Helper2 b = new Helper2();
    b.setBar(5);

    System.out.println("Hello world!");
  }

}
