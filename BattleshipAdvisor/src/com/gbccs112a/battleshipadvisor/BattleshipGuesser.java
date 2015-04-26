package com.gbccs112a.battleshipadvisor;

/**
 * The interface all of our BattleshipGuesser objects will implement.
 * @author Todd W. Neller
 *
 */
public interface BattleshipGuesser {
	/**
	 * Initialize at the beginning of a game.
	 */
	void initialize();
	
	/**
	 * Get the next guess from the Battleship guesser.
	 * @return the next guess from the Battleship guesser
	 */
	Position getGuess();
	
	/**
	 * Report the result of a Battleship guess.
	 * @param guess Position of guess
	 * @param hit whether or not guess was a hit
	 * @param sunkenShipLength length of sunken ship or 0 if no ship was sunk
	 */
	void report(Position guess, boolean hit, int sunkenShipLength);
}
