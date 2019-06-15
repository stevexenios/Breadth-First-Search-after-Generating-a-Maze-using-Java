/*
 * TCSS 342 - Data Structures
 * Assignment 5 - Maze Generator
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is the GUI to display the Maze.
 *  
 *  (Optional)​  create a graphical display for the maze.  
 * 
 * @author Steve Mwangi
 * @version Spring 2019
 */
public class MazeGUI {
	// GUI Frame.
	public JFrame frame;
	// Dimensions for the GUI creation
	public final static int FRAME_WIDTH = 750;
	public final static int FRAME_HEIGHT = 750;
	// Dimensions for the Maze Units
	public final static int MAZE_UNIT_WIDTH = 25;
	public final static int MAZE_UNIT_HEIGHT = 25;
	// Margin Dimension
	public final static int LEFT_MARGIN = 20;
	public final static int UP_MARGIN = 20;
	// Maze
	public Maze mazeForGUI;
	// Maze Unit
	public Maze.MazeUnit[][] mazeUnits;
	public String string = "Generated Maze: ";

	/**
	 * Constructor for the Maze GUI.
	 */
	public MazeGUI(Maze maze, String n) {
		string += n;
		mazeForGUI = maze;
		ImageIcon icon = setIcon(new ImageIcon("Maze.png"));
		frame = new JFrame(string);
		frame.setIconImage(icon.getImage());
		frame.setBounds(2*MAZE_UNIT_WIDTH, 2*MAZE_UNIT_HEIGHT, FRAME_WIDTH, FRAME_HEIGHT);
		mazeUnits = mazeForGUI.getMazeUnits();
		graphicalDisplay(mazeUnits, frame);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/**
	 * This a method to create the graphical display for the Maze.
	 * 
	 * @param mazeUnits
	 * @param frame
	 */
	public void graphicalDisplay(Maze.MazeUnit[][] mazeUnits, JFrame frame) {
		SketchMaze area = new SketchMaze(mazeUnits);
		area.setPreferredSize(new Dimension(mazeUnits[0].length * MAZE_UNIT_WIDTH + 50,
				mazeUnits.length * MAZE_UNIT_HEIGHT + 50));
		area.repaint();
		frame.add(area);
	}
	
	/**
     * Helper to set the Icon to small Icon values. 
     * @param theIcon the icon to set for this Action 
     */
    private ImageIcon setIcon(final ImageIcon theIcon) {
        final ImageIcon icon = (ImageIcon) theIcon;
        final Image smallImage =
            icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(smallImage);
    }

    
    /**
     * Inner class for developing the JPanel with the Maze.
     * 
     * @author Steve Mwangi
     * @version Spring 2019
     */
	public class SketchMaze extends JPanel {
		// Auto-generated SVUID
		private static final long serialVersionUID = -1230069551411144482L;
		private Maze.MazeUnit[][] mUnits;

		public SketchMaze(Maze.MazeUnit[][] mUnits) {
			this.mUnits = mUnits;
		}

		public void setPrintG(Maze.MazeUnit[][] printG) {
			this.mUnits = printG;
		}

		/**
		 * This is a method to add graphics to the maze.
		 */
		public void paint(Graphics g2) {
			Graphics2D pen = (Graphics2D) g2;
			pen.setStroke(new BasicStroke(10));
			int widthOfmUnits = mUnits[0].length;
			int heightOfmUnits = mUnits.length;
			for (int i = 0; i < heightOfmUnits; i++) {
				for (int j = 0; j < widthOfmUnits; j++) {
//			        g2.draw(new Line2D.Float(30, 20, 80, 90));
					if(mUnits[i][j].getIsPath()){
						if(i == heightOfmUnits - 1 && j == widthOfmUnits - 1) {
							pen.setColor(Color.RED);;
							pen.fillOval(j * MAZE_UNIT_WIDTH + LEFT_MARGIN, i
									* MAZE_UNIT_HEIGHT + UP_MARGIN, MAZE_UNIT_HEIGHT-7,MAZE_UNIT_WIDTH-7);
							pen.setColor(getBackground());
						} else if(i == 0 && j == 0){
							pen.setColor(Color.BLUE);
							pen.fillOval(j * MAZE_UNIT_WIDTH + LEFT_MARGIN, i
									* MAZE_UNIT_HEIGHT + UP_MARGIN, MAZE_UNIT_HEIGHT-7,MAZE_UNIT_WIDTH-7);
							pen.setColor(getBackground());
							
						} else {
							pen.setColor(Color.GREEN);;
							pen.fillOval(j * MAZE_UNIT_WIDTH + LEFT_MARGIN, i
									* MAZE_UNIT_HEIGHT + UP_MARGIN, MAZE_UNIT_HEIGHT-10,MAZE_UNIT_WIDTH-10);
							pen.setColor(getBackground());
						}
					}
					if (mUnits[i][j].getUpWall()) {
						pen.setColor(Color.BLACK);
						pen.drawLine(j * MAZE_UNIT_WIDTH + LEFT_MARGIN, i
								* MAZE_UNIT_HEIGHT + UP_MARGIN, (j + 1) * MAZE_UNIT_WIDTH
								+ LEFT_MARGIN, i * MAZE_UNIT_HEIGHT + UP_MARGIN);
						pen.setColor(getBackground());
					}
					if (mUnits[i][j].getRightWall()) {
						pen.setColor(Color.BLACK);
						pen.drawLine((j + 1) * MAZE_UNIT_WIDTH + LEFT_MARGIN, i
								* MAZE_UNIT_HEIGHT + UP_MARGIN, (j + 1) * MAZE_UNIT_WIDTH
								+ LEFT_MARGIN, (i + 1) * MAZE_UNIT_HEIGHT
								+ UP_MARGIN);
						pen.setColor(getBackground());
					}
					if (mUnits[i][j].getDownWall()) {
						pen.setColor(Color.BLACK);
						pen.drawLine((j + 1) * MAZE_UNIT_WIDTH + LEFT_MARGIN, (i + 1)
								* MAZE_UNIT_HEIGHT + UP_MARGIN, (j) * MAZE_UNIT_WIDTH
								+ LEFT_MARGIN, (i + 1) * MAZE_UNIT_HEIGHT
								+ UP_MARGIN);
						pen.setColor(getBackground());
					}
					if (mUnits[i][j].getLeftWall()) {
						pen.setColor(Color.BLACK);
						pen.drawLine((j) * MAZE_UNIT_WIDTH + LEFT_MARGIN, (i + 1)
								* MAZE_UNIT_HEIGHT + UP_MARGIN, (j) * MAZE_UNIT_WIDTH
								+ LEFT_MARGIN, (i) * MAZE_UNIT_HEIGHT + UP_MARGIN);
						pen.setColor(getBackground());
					}
				}
			}
		}
	}
}

