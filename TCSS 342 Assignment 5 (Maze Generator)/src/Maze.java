/*
 * TCSS 342 - Data Structures
 * Assignment 5 - Maze Generator
 */

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * You are responsible for implementing the Maze class that must function
 * according to the  following interface:  
 * 
 * ● Maze(int width, int depth, boolean debug) ­ creates a 2d mazePath of size
 *  n by m and with  the debug flag set to true will show the steps of mazePath
 *  creation.  
 * 
 * ● void display() ­ displays the mazePath using ‘X’ and ‘ ‘ characters matching
 *  the standard  presented in the attached trace.txt.  
 * 
 * Use a special character of your choice to mark the  solution path.  
 * 
 * @author Steve Mwangi
 * @version Spring 2019
 *
 */
public class Maze {
	// Dimensions of the Maze
	private int columns;
	private int rows;
	
	private MazeUnit[][] mazeUnits;
	private char[][] printMazeUnits;
	private char[][] mazeSolution;
	
	private MazeUnit start;
	private MazeUnit end;
	private boolean debug;
	final static char WALL_CHAR = 'X';
	final static char VISITED_CHAR = 'V';
	final static char PATH_CHAR = '+';
	
	// Counts the number of iterations for the printed Maze Units 
	public static int count = 0;
	// Counts the solutions for different maze solutions
	public static int ultimateCount = 1;
	public Random random = new Random();

	/**
	 * Constructor:
	 * Maze(int width, int depth, boolean debug) ­ creates a 2d 
	 * mazePath of size n by m and with  the debug flag set to true
	 * will show the steps of mazePath creation.
	 * 
	 * @param width
	 * @param depth
	 * @param debug
	 */
	public Maze(int width, int depth, boolean debug) {
		this.debug = debug;
		this.columns = width;
		this.rows = depth;
		initializeMazeUnits();
		generateMaze();
		findBestPathForMaze();
	}
	
	/**
	 * Helper method to initialize the Maze Unit Array with Maze Units,
	 * and set the starting and ending points.
	 */
	private void initializeMazeUnits() {
		mazeUnits = new MazeUnit[rows][columns];
		printMazeUnits = new char[2 * rows + 1][2 * columns + 1];
		mazeSolution = new char[2 * rows + 1][2 * columns + 1];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				mazeUnits[i][j] = new MazeUnit(i, j);
			}				
		}
		start = mazeUnits[0][0];
		start.setUpWall(false);
		end = mazeUnits[rows - 1][columns - 1];
		end.setDownWall(false);
	}

	

	/**
	 * Maze Generator.
	 */
	public void generateMaze() {
		Stack<MazeUnit> stack = new Stack<>();
		stack.push(start);
		start.setVisited(true);
		MazeUnit currentUnit = start, lastUnit;
		while (!stack.isEmpty()) {
			HashMap<MazeUnit, Integer> randomMapOfUnits = randomUnits(currentUnit);
			currentUnit.setVisited(true);
			if (randomMapOfUnits.size() <= 0) {
				currentUnit = stack.pop();
			} else {
				lastUnit = currentUnit;
				int seq = -1;
				for (Entry<MazeUnit, Integer> en : randomMapOfUnits.entrySet()) {
					currentUnit = en.getKey();
					seq = en.getValue();
					break;
				}

				// set the wall of related cell
				switch (seq) {
				case 0:
					lastUnit.setUpWall(false);
					currentUnit.setDownWall(false);
					break;
				case 1:
					lastUnit.setRightWall(false);
					currentUnit.setLeftWall(false);
					break;
				case 2:
					lastUnit.setDownWall(false);
					currentUnit.setUpWall(false);
					break;
				case 3:
					lastUnit.setLeftWall(false);
					currentUnit.setRightWall(false);
					break;
				}
				stack.push(currentUnit);
			}
			
			if (debug) {
				initializePrintMazeUnits(VISITED_CHAR, printMazeUnits);
				print();
			} else {
				initializePrintMazeUnits(VISITED_CHAR, printMazeUnits);
			}
		}
	}

	/**
	 * Get the randomized sets for unit neighbors
	 * 
	 * @param mazeUnit
	 * @return the random set for unit
	 */
	public HashMap<MazeUnit, Integer> randomUnits(MazeUnit mazeUnit) {
		HashMap<MazeUnit, Integer> positions = new HashMap<MazeUnit, Integer>();
		int randomSet[] = new int[] { -1, -1, -1, -1 };
		int rowOfUnit = mazeUnit.getPosition()[0];
		int columnOfUnit = mazeUnit.getPosition()[1];
		int first = 0;
		while (first < 4) {
			boolean weNeedV = true;
			int nextRandom = random.nextInt(4);
			for (int i = 0; i < first; i++) {
				if (randomSet[i] == nextRandom) {
					weNeedV = false;
					break;
				}
			}
			if (weNeedV) {
				randomSet[first++] = nextRandom;
			}
		}
		for (int i = 0; i < first; i++) {
			int newRowOfUnit = rowOfUnit, newColOfUnit = columnOfUnit;
			switch (randomSet[i]) {
			case 0:// up
				newRowOfUnit = rowOfUnit - 1;
				break;
			case 1:// right
				newColOfUnit = columnOfUnit + 1;
				break;
			case 2:// down
				newRowOfUnit = rowOfUnit + 1;
				break;
			case 3:// left
				newColOfUnit = columnOfUnit - 1;
				break;

			}
			if (newRowOfUnit > -1 && newRowOfUnit < this.rows
					&& newColOfUnit > -1 && newColOfUnit < this.columns
					&& !mazeUnits[newRowOfUnit][newColOfUnit].getVisited()) {
				positions.put(mazeUnits[newRowOfUnit][newColOfUnit], randomSet[i]);
			}
		}
		return positions;
	}

	/**
	 * Initialize the printMazeUnits based on mazeUnits. 
	 * '+' initializes the solution of mazeUnits. 
	 * 'V' initializes the maze
	 * 
	 * @param character
	 */
	public void initializePrintMazeUnits(char character, char[][] pathGenerated) {
		int pathGenerateHeight = pathGenerated.length;
		int pathGeneratedWidth = pathGenerated[0].length;
		for (int i = 0; i < pathGenerateHeight; i++) {
			if (i == 0) {
				for (int j = 0; j < pathGeneratedWidth; j++) {
					if (!(j % 2 == 0) && !mazeUnits[i][j / 2].getUpWall()) {
						pathGenerated[i][j] = ' ';
					} else {
						pathGenerated[i][j] = WALL_CHAR;
					}
				}
			} else {
				for (int j = 0; j < pathGeneratedWidth; j++) {
					if (i % 2 == 0) {
						if (j % 2 == 0) {
							pathGenerated[i][j] = WALL_CHAR;
						} else {
							if (mazeUnits[i / 2 - 1][j / 2].getDownWall()) {
								pathGenerated[i][j] = WALL_CHAR;
							} else {
								pathGenerated[i][j] = ' ';
							}
						}
					} else {
						if (j % 2 == 0) {
							if (j < pathGeneratedWidth - 1) {
								if (mazeUnits[i / 2][j / 2].getLeftWall()) {
									pathGenerated[i][j] = WALL_CHAR;
								} else {
									pathGenerated[i][j] = ' ';
								}
							} else {
								if (mazeUnits[i / 2][j / 2 - 1].getRightWall()) {
									pathGenerated[i][j] = WALL_CHAR;
								} else {
									pathGenerated[i][j] = ' ';
								}
							}
						} else {
							if (character == PATH_CHAR) {
								if (mazeUnits[i / 2][j / 2].getIsPath()) {
									pathGenerated[i][j] = character;
								} else {
									pathGenerated[i][j] = ' ';
								}
							} else {
								if (mazeUnits[i / 2][j / 2].getVisited()) {
									pathGenerated[i][j] = character;
								} else {
									pathGenerated[i][j] = ' ';
								}
							}
						}
					}
				}
			}

		}
	}
	
	public MazeUnit[][] getMazeUnits() {return mazeUnits;}
	
	/**
	 * Finding the best path for the Maze.
	 */
	public void findBestPathForMaze() {
		Stack<MazeUnit> path = new Stack<MazeUnit>();
		for (int i = 0; i < this.rows; i++) {
			for (int j = 0; j < this.columns; j++) {
				mazeUnits[i][j].setVisited(false);
			}	
		}
		path.push(start);
		start.setVisited(true);
		MazeUnit currentUnit = start;
		MazeUnit previousUnit = start;
		boolean isPopped = false;
		while (!path.isEmpty()) {
			//Get the sequence of units not visited, but are neighbors of currentUnit.
			Set<Entry<MazeUnit, Integer>> randomSet = randomUnits(currentUnit)
					.entrySet();
			// Record true if there's path from previous Unit
			boolean flag = false;
			// Determine unit from sequence of units which can get to previousUnit 
			for (Entry<MazeUnit, Integer> entry : randomSet) {
				switch (entry.getValue()) {
				case 0:
					if (!previousUnit.getUpWall()) {
						flag = true;
					}
					break;
				case 1:
					if (!previousUnit.getRightWall()) {
						flag = true;
					}
					break;
				case 2:
					if (!previousUnit.getDownWall()) {
						flag = true;
					}
					break;
				case 3:
					if (!previousUnit.getLeftWall()) {
						flag = true;
					}
					break;
				}
				if (flag) {
					if (isPopped) {
						path.push(previousUnit);
						isPopped = false;
					}
					currentUnit = entry.getKey();
					previousUnit = currentUnit;
					currentUnit.setVisited(true);
					path.push(currentUnit);

					if (currentUnit == end) {
						// Determine maze best path
						for (int pathi = 0, len = path.size(); pathi < len; pathi++) {
							path.elementAt(pathi).setIsPath(true);
						}
						currentUnit.setIsPath(true);
					}
					// display();
					break;
				}
			}
			// Note when there are not paths.
			if (!flag) {
				isPopped = true;
				currentUnit = path.pop();
				previousUnit = currentUnit;
			}
		}
	}
	
	/**
	 * Default Maze.
	 */
	public void print() {
		int height = printMazeUnits.length;
		int width = printMazeUnits[0].length;
		count++;
		System.out.println("Iteration Number: " + count + "\n" +"The maze: ");
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(printMazeUnits[i][j]);
			}
			System.out.println("\n");
		}
	}

	/**
	 * Displays the mazePath using ‘X’ and ‘ ‘ characters matching
	 * the standard  presented in the attached trace.txt. 
	 */
	public void display() {
		int mazeUnitsHeight = printMazeUnits.length;
		int mazeUnitsWidth = printMazeUnits[0].length;
		System.out.println("\n" + "The Ultimate Maze:" + "MAZE" + ultimateCount);
		for (int i = 0; i < mazeUnitsHeight; i++) {
			for (int j = 0; j < mazeUnitsWidth; j++) {
				System.out.print(printMazeUnits[i][j]);
			}
			System.out.println();
		}
		
		System.out.println("\n" + "The solution of maze: " + "MAZE" + ultimateCount);
		initializePrintMazeUnits(PATH_CHAR, mazeSolution);
		for (int i = 0; i < mazeUnitsHeight; i++) {
			for (int j = 0; j < mazeUnitsWidth; j++) {
				System.out.print(mazeSolution[i][j]);
			}
			System.out.println();
		}
		ultimateCount++;
	}

	/**
	 * This Inner class is used to create maze units, that can either be filled 
	 * with walls or paths.
	 * 
	 * This Inner class sources was from GitHub by:
	 * @author Siyuan Zhou
	 * 
	 * @author Steve Mwangi
	 * @version Spring 2019
	 */
	public class MazeUnit {
		// record the position of a maze unit in the maze.
		private int position[];
		// This array is to record the four borders of a maze unit.
		private boolean wall[] = new boolean[] { true, true, true, true };
		// Checks to see if this path has been traversed.
		private boolean visited = false;
		// Checks to see if there is a way or a wall.
		private boolean isPath = false;
		// Integer to represent Up.
		public int up = 0;
		// Integer to represent Right.
		public int right = 1;
		// Integer to represent Down.
		public int down = 2;
		// Integer to represent Left.
		public int left = 3;
		
		/**
		 * Constructor to initilize each maze unit.
		 * @param i ith row position
		 * @param j jth column
		 */
		public MazeUnit(int i, int j) {
			position = new int[] { i, j };
		}
		
		public void setIsPath(boolean isPath) {
			this.isPath = isPath;
		}

		public boolean getIsPath() {
			return isPath;
		}
		
		// Sets boolean T/F if there is/no a wall
		public void setUpWall(boolean upWall) {
			wall[up] = upWall;
		}

		public boolean getUpWall() {
			return wall[up];
		}

		public void setRightWall(boolean rightWall) {
			wall[right] = rightWall;
		}

		public boolean getRightWall() {
			return wall[right];
		}

		public void setDownWall(boolean downWall) {
			wall[down] = downWall;
		}

		public boolean getDownWall() {
			return wall[down];
		}

		public void setLeftWall(boolean leftWall) {
			wall[left] = leftWall;
		}

		public boolean getLeftWall() {
			return wall[left];
		}

		public void setVisited(boolean v) {
			visited = v;
		}

		public boolean getVisited() {
			return visited;
		}

		public int[] getPosition() {
			return position;
		}

		public String toString() {
			String wallString = "";
			for (boolean b : wall) {
				wallString += String.valueOf(b) + ",";
			}
			return "Position: (" + position[0] + "," + position[1]
					+ "), wall:[" + wallString.substring(0, wallString.length() - 1)
					+ "]";
		}

		public void print() {
			System.out.println(this.toString());
		}
	}
}
