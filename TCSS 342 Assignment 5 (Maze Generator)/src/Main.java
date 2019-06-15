import java.util.Random;

/*
 * TCSS 342 - Data Structures
 * Assignment 5 - Maze Generator
 */

/**
 * You will also create a Main class to control the Maze Generator by:  
 * ● generating a 5x5 maze with debugging on to show the steps of your algorithm.
 * ● generating a larger maze with debugging off.  
 * ● testing and debugging components.  
 * 
 * ● (Optional)​  create a graphical display for the maze.  
 * 
 * @author Steve Mwangi
 * @version Spring 2019
 *
 */
public class Main {	
	public static void main(String[] args) {
		/**
		 * ● Generating a 5x5 maze with debugging on to show the steps of 
		 * 	 your algorithm.  
		 */
		String m1 = "Small 5x5 maze with Debugging ON.";
		Maze maze1 = new Maze(5, 5, true);
		System.out.println("\n" + m1);
		maze1.display();
		
		/**
		 * This is from the MazeGUI class.
		 * (Optional)​  create a graphical display for the maze.
		 */
		new MazeGUI(maze1, m1);
		
		/**
		 * Generating a larger maze with debugging off.
		 */
		String m2 = "Larger 35x35 maze with Debugging OFF.";
		Maze maze2 = new Maze(35, 35, false); // Change to false!?
		System.out.println("\n" + m2);
		maze2.display();
		
		/**
		 * This is from the MazeGUI class.
		 * (Optional)​  create a graphical display for the maze.
		 */
		new MazeGUI(maze2, m2);

		/**
		 * Test Methods.
		 */
		System.out.println("\n\nTests for The Maze Class.");
		Random r = new Random();
		int e = 1 + r.nextInt(3);
		int f = 1 + r.nextInt(3);		
		Maze.MazeUnit testUnit = new Maze(e, f, false).new MazeUnit(e,f);
		System.out.println("\nTesting Maze Units.");
		System.out.println(testUnit);
		System.out.println("\nSmall random maze:");
		new Maze(4, 4, false).display();
		try {
			new Maze(0, 0, false).display();
		} catch(Exception n) {
			System.out.println("\nMaze is 0x0: ");
			System.out.println("Dimensions are same as the start unit. Can't be a One unit Maze!");
		}
	}	
}