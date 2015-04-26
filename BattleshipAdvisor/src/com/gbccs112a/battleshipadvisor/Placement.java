package com.gbccs112a.battleshipadvisor;
import java.util.ArrayList;

/**
 * An object representing a ship's placement in the Battleship grid.
 * @author Todd W. Neller
 *
 */
public class Placement {
	/**
	 * horizontal orientation across columns
	 */
	public static final int ACROSS = 0;
	/**
	 * vertical orientation down rows
	 */
	public static final int DOWN = 1;

	/**
	 * The length of the ship being placed.  Each ship is width 1 and thus occupies length cells.
	 */
	public final int length;
	/**
	 * The upper-leftmost position occupied by the ship.
	 */
	public final Position position;
	/**
	 * The ACROSS or DOWN orientation of the ship in the grid.
	 */
	public final int orientation;
	/**
	 * The list of positions occupied by this ship placement.
	 */
	private final ArrayList<Position> posList = new ArrayList<Position>();

	
	/**
	 * Construct a ship placement consists of (1) a ship object length, (2) the upper-leftmost position of the ship, and (3) the
	 * orientation of the ship (ACROSS/DOWN)
	 * @param length length of ship being placed
	 * @param position the upper-leftmost position of the ship
	 * @param orientation the orientation of the ship (ACROSS/DOWN)
	 */
	public Placement(int length, Position position, int orientation) {
		this.length = length;
		this.position = position;
		this.orientation = orientation;
		int r = position.row;
		int c = position.col;
		for (int i = 0; i < length; i++) {
			posList.add(Position.gridPositions[r][c]);
			 if (orientation == ACROSS)
				 c++;
			 else
				 r++;
		}
	}
	
	/**
	 * Return a list of all Positions occupied by this Placement. 
	 * @return a list of all Positions occupied by this Placement
	 */
	ArrayList<Position> getPositions() {
		return posList;
	}
	
	/**
	 * Return whether or not this placement intersects with (that is, overlaps) another given placement
	 * @param p another given placement
	 * @return whether or not this placement intersects with (that is, overlaps) another given placement
	 */
	boolean intersectsWith(Placement p) {
		ArrayList<Position> posList1 = getPositions();
		ArrayList<Position> posList2 = p.getPositions();
		for (Position pos : posList1)
			if (posList2.contains(pos))
				return true;
		return false;
	}
	
	/**
	 * Return whether or not this Placement contains a given Position.
	 * @param pos a given Position
	 * @return whether or not this Placement contains a given Position
	 */
	boolean contains(Position pos) {
		return getPositions().contains(pos);
	}
	
    @Override
    public String toString() {
            return "Placement [length=" + length + ", position=" + position
                            + ", orientation=" + orientation + "]";
    }

}
