package com.gbccs112a.battleshipadvisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/*
 *
 */
public class MainActivity extends Activity {
	/**
	 * Size of the Battleship grid.
	 */
	static final int SIZE = Position.SIZE;
	/**
	 * Constant indicating that the guesser has not yet guessed this grid cell.
	 */
	static final int UNKNOWN = 0;
	/**
	 * Constant indicating that the guesser has guessed and missed in this grid cell.
	 */
	static final int MISS = 1;
	/**
	 * Constant indicating that the guesser has guessed and hit (but not sunk) a ship in this grid cell.
	 */
	static final int HIT = 2;
	/**
	 * Constant indicating that the guesser has guessed, hit, and sunk a ship in this grid cell.
	 */
	static final int SUNK = 3;

	private BattleshipGuesser guesser = new MyBattleshipGuesser();
	private ArrayList<Integer> unsunkLengths = new ArrayList<Integer>();
	private Stack<Position> guessStack = new Stack<Position>();
	private Stack<String> responseStack = new Stack<String>();
	private Position guess = Position.gridPositions[0][0];
	private Button missButton, hitButton, sunkButton;

	private HashMap<String, Drawable> drawableMap = new HashMap<String, Drawable>();
	HashMap<String, ImageView> imageMap = new HashMap<String, ImageView>();
	int ulx, uly, cellSize;
	char[] chArray = "abcdefghij".toCharArray();



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		missButton = (Button) findViewById(R.id.missButton);
		hitButton = (Button) findViewById(R.id.hitButton);
		sunkButton = (Button) findViewById(R.id.sunkButton);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
		missButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (unsunkLengths.isEmpty()) return;
				String response = "miss";
				guessStack.add(guess);
				responseStack.add(response);
				report(guess, response);
				guess = guesser.getGuess();
			}
		});
		
		hitButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (unsunkLengths.isEmpty()) return;
				String response = "hit";
				guessStack.add(guess);
				responseStack.add(response);
				report(guess, response);
				guess = guesser.getGuess();
			}
		});
		sunkButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (unsunkLengths.isEmpty()) return;
				String response = "sunk";
				guessStack.add(guess);
				responseStack.add(response);
				report(guess, response);
				guess = guesser.getGuess();
			}
		});
		*/
		
		drawableMap.put("one", getResources().getDrawable(R.drawable.one));
		drawableMap.put("hit", getResources().getDrawable(R.drawable.hit));
		drawableMap.put("miss", getResources().getDrawable(R.drawable.miss));
		drawableMap.put("unknown", getResources().getDrawable(R.drawable.unknown));

		if (!unsunkLengths.isEmpty()){
			System.out.println("IM RUNNINGGGGGGGGGGGGGGGA;SLDKF;ASLDKFJAS;DFLKAJS;DFLKAJS;DFLKAJS;DFLKAJSD;FLKJAS;DFLKAJS;DLFJKA;SLDKFJ");
			char row = chArray[guess.row];
			highlightGuess(guess.col + 1, row);
		}

		setTitle("Battleship Advisor");
		setVisible(true);
		reset();	
	}
	/**SHOULD I ADD A NEW METHOD THATS RUN FROM A WHILE LOOP, WHICH WILL BE THE PLAY METHOD SO THAT THE
	 * GAME KEEPS PLAYING UNTIL GAME IS OVER AND ITS RESET?
	 */

	/**
	 * Go back to initial guesser state and report all guesses and responses to guesser from the beginning.
	 */
	public void reset() {
		//set all cell images to unknown by default
		for (char ch : chArray) {
			for(int i = 1; i < 11; i++){
				setImage("one", i, ch);
			}
		}

		guesser.initialize();
		unsunkLengths.clear();
		for (int length : new int[] {5, 4, 3, 3, 2})
			unsunkLengths.add(length);
		for (int i = 0; i < guessStack.size(); i++)
			report(guessStack.get(i), responseStack.get(i));
		guess = guesser.getGuess();

	}

	private ImageView getCell(int n, char c){
		int layoutID = getResources().getIdentifier("" + c + n, "id", getPackageName());
		System.out.println("n value: " + n);
		return (ImageView)findViewById(layoutID);
	}

	private void setImage(final String newImageName, int n, char c) {
		//System.out.println("n+1 value: " + n+1);
		//System.out.println("c value: " + c);
		ImageView cell = getCell(n, c);//Error : this is returning null. Why?

		System.out.println("cell: " + cell); //Error : this is returning null. Why?
		System.out.println("newImageName: " + newImageName);

		cell.setImageDrawable(drawableMap.get(newImageName)); //TODO HERE IS THE ERROR
	}



	private void highlightGuess(int n, char c){
		ImageView cell = getCell(n, c);
		cell.setAlpha(128);
	}







	/**
	 * TODO you win message
	 * Report to guesser on currently selected guess position according to user's button selection.
	 * @param guess guess position
	 * @param response response action command
	 */
	public void report(Position guess, String response) {
		if (response.equals("miss"))
			guesser.report(guess, false, 0);
		else if (response.equals("hit"))
			guesser.report(guess, true, 0);
		else {
			int sunkLength = Integer.parseInt(response);
			unsunkLengths.remove((Integer) sunkLength);
			guesser.report(guess, true, sunkLength);
			if (unsunkLengths.isEmpty()){
				// 1. Instantiate an AlertDialog.Builder with its constructor
				AlertDialog.Builder builder = new AlertDialog.Builder(this);

				// 2. Chain together various setter methods to set the dialog characteristics
				builder.setMessage("You've won in " + guessStack.size() + " guesses!")
				.setTitle("You Win!");

				// 3. Get the AlertDialog from create()
				AlertDialog dialog = builder.create();

				//display the alert dialog
				dialog.show();

				//JOptionPane.showMessageDialog(frame, "You've won in " + guessStack.size() + " guesses!");
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

