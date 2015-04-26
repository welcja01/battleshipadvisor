package com.gbccs112a.battleshipadvisor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Position - Class for representing all (row, column) pairs in Battleship along with utilities to convert back
 * and forth between 0-based [row][col] indexing and standard game letter-number String representations. 
 * The toString() method returns the standard letter and 1-based number notation of the traditional game. So
 * gridPositions[0][0] and gridPositions[9][9] have toString() representations of "a1" and "j10", respectively.
 * 
 * @author Todd W. Neller
 */
public class Position {
	/**
	 * length of square grid, that is, the number of rows and number of columns
	 */
	public static final int SIZE = 10;
	/**
	 * all grid Position objects, indexed by row and column of Position
	 */
	public static final Position[][] gridPositions = new Position[SIZE][SIZE];
	/**
	 * ArrayList of all grid Position objects
	 */
	public static final ArrayList<Position> allPositions = new ArrayList<Position>();
	/**
	 * A hashtable mapping the String representation of a Position, such as "j10", to the associated Position object
	 */
	public static final HashMap<String, Position> strToPos = new HashMap<String, Position>();

	static { // initialize all static variables
		for (int r = 0; r < SIZE; r++)
			for (int c = 0; c < SIZE; c++) {
				Position p = new Position(r, c);
				gridPositions[r][c] = p;
				allPositions.add(p);
				strToPos.put(p.toString(), p);
			}
	}
	
	/**
	 * Position row in the range [0, SIZE - 1]
	 */
	public final int row;
	/**
	 * Position column in the range [0, SIZE - 1]
	 */
	public final int col;
	
	/**
	 * @param row Position row in the range [0, SIZE - 1]
	 * @param col Position column in the range [0, SIZE - 1]
	 */
	public Position(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Return the String representation of Position as the standard lowercase letter and non-zero integer pair.  
	 * The upper-leftmost position is "a1" and the lower-rightmost position is "j10".
	 * @return the String representation of Position as the standard lowercase letter and non-zero integer pair
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (char)('a' + row) + Integer.toString(col + 1);
	}

}
