import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Stack;

import robot.Mouse;
import robot.graph.Direction;
import robot.graph.Vertex;

/**
 * An UI element representing a given vertex
 * 
 * @author Nican
 * 
 */
public class MazeSquare extends Component {

	private static final Font font = new Font("Serif", Font.PLAIN, 12);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1504491135251095028L;
	public static final int borderSize = 1;
	public static final int mouseSize = 16;

	public final int x;
	public final int y;
	public final Maze maze;

	public int weightValue;

	public MazeSquare(Maze maze, int x, int y) {
		this.maze = maze;
		this.x = x;
		this.y = y;

		this.setPreferredSize(new Dimension(45, 45));
	}

	public Vertex getVertex() {
		return maze.graph.get(x, y);
	}

	public Color getBackgroundColor() {

		if (getMouse().isDiscovered(getVertex()))
			return Color.green;

		return Color.white;

	}

	public Mouse getMouse() {
		return maze.mouse;
	}

	public Color edgeColor(Direction direction) {

		Vertex vertex = getVertex();
		Vertex relative = vertex.getRelative(direction);

		if (relative == null)
			return Color.BLACK;

		if (getMouse().knowsEdge(vertex, relative))
			return Color.RED;

		return Color.BLACK;

	}

	@Override
	public void paint(Graphics g) {
		Vertex vertex = getVertex();
		Mouse mouse = getMouse();

		if (vertex.emptyEdges()) {
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());

		} else {

			g.setColor(getBackgroundColor());
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setColor(Color.black);

			if (!vertex.hasNeighbor(Direction.NORTH)) {
				g.setColor(edgeColor(Direction.NORTH));
				g.fillRect(borderSize, 0, getWidth() - borderSize,
						borderSize * 2);
			}

			if (!vertex.hasNeighbor(Direction.SOUTH)) {
				g.setColor(edgeColor(Direction.SOUTH));
				g.fillRect(borderSize, this.getHeight() - borderSize,
						getWidth() - borderSize * 2, borderSize);
			}

			if (!vertex.hasNeighbor(Direction.WEST)) {
				g.setColor(edgeColor(Direction.WEST));
				g.fillRect(0, borderSize, borderSize, getHeight() - borderSize
						* 2);
			}

			if (!vertex.hasNeighbor(Direction.EAST)) {
				g.setColor(edgeColor(Direction.EAST));
				g.fillRect(getWidth() - borderSize, borderSize, borderSize,
						getHeight() - borderSize * 2);
			}

		}

		if (getMouse().current.equals(vertex)) {
			g.setColor(Color.pink);
			g.fillOval(getWidth() / 2 - mouseSize / 2, getHeight() / 2
					- mouseSize / 2, mouseSize, mouseSize);
		}

		g.setFont(font);
		g.setColor(Color.red);
		g.drawString(x + ", " + y, 3, 12);
		g.drawString(Integer.toString(weightValue), 3, 24);

		drawPath((Graphics2D) g, mouse.minPath.path, Color.blue);

		Stack<Vertex> pathToMinPath = maze.pathToMinPath;

		if (pathToMinPath != null)
			drawPath((Graphics2D) g, pathToMinPath, Color.gray);
	}

	public void drawPath(Graphics2D g, Stack<Vertex> path, Color color) {

		g.setStroke(new BasicStroke(2.0f));
		g.setColor(color);

		Vertex vertex = getVertex();

		int index = path.indexOf(vertex);

		if (index == -1)
			return;

		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;

		// Check if it is the first one
		if (index == 0) {
			// Check if the array actually has a next element
			if (path.size() == 1)
				return;

			Vertex next = path.get(1);

			Direction direction = vertex.getDirection(next);

			g.drawLine(centerX, centerY, centerX + centerX * direction.x,
					centerY + centerY * direction.y);

			return;
		}

		// Check if it is the middle of the road
		if (index > 0 && index < path.size() - 1) {
			Vertex previus = path.get(index - 1);
			Vertex next = path.get(index + 1);

			Direction direction1 = vertex.getDirection(previus);
			Direction direction2 = vertex.getDirection(next);

			g.drawLine(centerX, centerY, centerX + centerX * direction1.x,
					centerY + centerY * direction1.y);
			g.drawLine(centerX, centerY, centerX + centerX * direction2.x,
					centerY + centerY * direction2.y);

		}

		// The end of the maze
		if (index == path.size() - 1) {
			Vertex previus = path.get(index - 1);
			Direction direction = vertex.getDirection(previus);

			g.drawLine(centerX, centerY, centerX + centerX * direction.x,
					centerY + centerY * direction.y);

		}

	}
}
