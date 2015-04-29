package com.gbccs112a.battleshipadvisor;

import java.util.ArrayList;
import java.util.Random;


public class MyBattleshipGuesser implements BattleshipGuesser {

	private static final Random random = new Random();

	private static final int UNKNOWN = 0, MISS = 1, HIT = 2, SUNK = 3;

	private static final int SIZE = Position.SIZE;

	private int[] shipLengths = {5, 4, 3, 3, 2};

	private ArrayList<Integer> unsunkShips = new ArrayList<Integer>(); // a list of the sizes of unsunk ships, initially the integers in shipLengths

	private int[][] grid = new int[SIZE][SIZE]; // the current state of knowledge: either UNKNOWN, MISS, HIT, or SUNK

	private int[][] gridCount = new int[SIZE][SIZE]; // the count used to select the next guess

	private ArrayList<Position> hits = new ArrayList<Position>(); // a list of hits not yet resolved to sunken ships

	private int sinkCount = 0; // the number of ship positions sunk but not yet marked as sunk

	private ArrayList<Position> hitstory = new ArrayList<Position>(); // history of hits

	private ArrayList<Position> bestGuesses = new ArrayList<Position>(); // a list of the best guess candidates

	private int targetHitWeight = 1000; // the weight to add to the gridCount when the placement overlaps one or more hits

	private int numHitsContained;

	private boolean possibleSink;

	private Placement possibleShip;

	private ArrayList<Placement> possibleSunkenShip = new ArrayList<Placement>();

	@Override
	public void initialize() {
		unsunkShips.clear();
		for (int i = 0; i < shipLengths.length; i++)
			unsunkShips.add(shipLengths[i]);
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++)
				grid[i][j] = UNKNOWN;
		}
		hits.clear();
		sinkCount = 0;
		hitstory.clear();	
	}

	
	@Override
	

	public Position getGuess() {
		// clear grid counts
		gridCount = new int[SIZE][SIZE];
		int endRow;
		int endCol;
		int maxCount = 0;
		boolean missOrSunkPos = false;
		Placement placement;
		for (int length : unsunkShips) { // for each unsunk length
			for (int row = 0; row < SIZE; row++) // for each row
				for (int col = 0; col < SIZE; col++) // for each col
					for (int orient = 0; orient <= 1; orient ++) { //for each orientation
						if (orient == 0) {
							endRow = row;
							endCol = col + length - 1;
						}
						else {
							endRow = row + length -1;
							endCol = col;
						}
						if (endRow < SIZE && endCol < SIZE) { // if placement fits in grid
							placement = new Placement(length, Position.gridPositions[row][col], orient);
							numHitsContained = 0;
							missOrSunkPos = false;
							for (Position p : placement.getPositions()){
								// check to see if placement contains misses or sunk positions, or hits
								if (grid[p.row][p.col] == MISS || grid[p.row][p.col] == SUNK)	
									missOrSunkPos = true;
								else if (grid[p.row][p.col] == HIT)
									numHitsContained ++;
							}
							// add 1000 or 1 to the grid count for each position of the placement depending on whether or not it contains a hit
							if (missOrSunkPos == false) {
								if (numHitsContained > 0){
									for (Position p : placement.getPositions()){
										gridCount[p.row][p.col] += targetHitWeight*numHitsContained;
									}
								}
								else {
									for (Position p : placement.getPositions()){
										gridCount[p.row][p.col] += 6 - length;
									}
								}

							}

						}

					}
		}
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				if (gridCount[row][col] == maxCount && grid[row][col] == UNKNOWN)
					bestGuesses.add(Position.gridPositions[row][col]);
				else if (gridCount[row][col] > maxCount && grid[row][col] == UNKNOWN) {
					bestGuesses.clear();
					bestGuesses.add(Position.gridPositions[row][col]);
					maxCount = gridCount[row][col];
				}
			}
		}

		int guessIndex = random.nextInt(bestGuesses.size());
		return bestGuesses.get(guessIndex);
	}

	@Override
	public void report(Position guess, boolean hit, int sunkenShipLength) {
		// at first mark the guess position with hit/miss
		if (hit) {
			grid[guess.row][guess.col] = HIT;
			// if it's a new hit, add it to the hitstory & hits
			if (!hitstory.contains(guess)) {
				hitstory.add(guess);
				hits.add(guess);
			}
		}
		else {
			grid[guess.row][guess.col] = MISS;
		}
		// if ship sunk
		if (sunkenShipLength != 0) {
			// remove ship length from unsunkShips
			unsunkShips.remove(unsunkShips.indexOf(sunkenShipLength));
			// if sinkCount and new sunken ship length sum to length of hit list
			if (sinkCount + sunkenShipLength == hits.size()) {
				// mark all hits as sunk, clear hit list, and zero sinkCount
				for (int row = 0; row < SIZE; row++) {
					for (int col = 0; col < SIZE; col++) {
						if (grid[row][col] == HIT)
							grid[row][col] = SUNK;
					}
				}
				hits.clear();
				sinkCount = 0;
			}
			else {
				possibleSunkenShip.clear();
				for (Position p : hits) {
					if (p.col + sunkenShipLength - 1 < SIZE) {
						possibleSink = true;
						possibleShip = new Placement(sunkenShipLength, p, Placement.ACROSS); 
						for (Position pos : possibleShip.getPositions()){
							if (grid[pos.row][pos.col] != HIT) {
								possibleSink = false;
							}
						}
						if (possibleSink && possibleShip.contains(guess)) {
							possibleSunkenShip.add(possibleShip);
						}
					}
					if (p.row + sunkenShipLength - 1 < SIZE) {
						possibleSink = true;
						possibleShip = new Placement(sunkenShipLength, p, Placement.DOWN); 
						for (Position pos : possibleShip.getPositions()){
							if (grid[pos.row][pos.col] != HIT) {
								possibleSink = false;
							}
						}
						if (possibleSink && possibleShip.contains(guess)) {
							possibleSunkenShip.add(possibleShip);
						}
					}

				}
				if (possibleSunkenShip.size() == 1) {
					for (Position pos : possibleSunkenShip.get(0).getPositions()) {
						hits.remove(pos);
						grid[pos.row][pos.col] = SUNK;
					}
				}
				else {
					sinkCount += sunkenShipLength;
				}
			}
		}
	}

	public void printGrid() {
		String[] markers = {".", "M", "H", "S"}; // .=UNKNOWN, M=MISS, H=HIT, S=SUNK
		System.out.print(" ");
		for (int i = 1; i <= SIZE; i++)
			System.out.printf(" %2d", i);
		System.out.println();
		for (int r = 0; r < SIZE; r++) {
			System.out.printf("%s", "" + (char)('a' + r));
			for (int c = 0; c < SIZE; c++)
				System.out.print("  " + markers[grid[r][c]]);
			System.out.println();
		}
	}


	public static void main(String[] args) {
	
	}
}