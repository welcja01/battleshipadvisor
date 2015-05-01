package com.gbccs112a.battleshipadvisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

/**
 * SHips
 * @JavaBattleshipGUI by Todd Neller
 * @ported by William Czubakowski with help from Alex and Trevor
 * @buttons by Will and Alex
 * @artwork by Jenny
 * @sound by Yifeng (sound was not implemented due to time constraints)
 * @menu by Tessa and Skyler
 * @versionManagement by Jamie
 * @projectManagement by Trevor
 * @XML by Victoria, August, and Valencia, with modifications by Will.
 */

public class BattleshipView extends View {

	@SuppressLint("DrawAllocation")

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
	public static Stack<Position> guessStack = new Stack<Position>();
	public static Stack<String> responseStack = new Stack<String>();
	private Position guess = Position.gridPositions[0][0];
	public  View frame = this;
	int ulx, uly, cellSize;
	char[] chArray = "abcdefghij".toCharArray();
	Resources res = null;
	private HashMap<String, Drawable> drawableMap = new HashMap<String, Drawable>();



	public BattleshipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = context.getResources();
		init();
	}


	public void init(){
		String[] imageStrings = {"unknown", "miss", "hit", "two", "three", "four", "five"};
		for (String imageString : imageStrings) { 

			final int ID = getResources().getIdentifier(imageString, "drawable","com.gbccs112a.battleshipadvisor");

			drawableMap.put(imageString, getResources().getDrawable(ID));
		}

		// 2) row label images
		for (int i = 0; i < chArray.length; i++){

			final int ID = getResources().getIdentifier("label" + chArray[i], "drawable", "com.gbccs112a.battleshipadvisor");
			;
			drawableMap.put("label" + chArray[i], getResources().getDrawable(ID));
		}

		// 3) column label images
		for (int i = 1; i <= 10; i++){
			final int ID = getResources().getIdentifier("label" + i, "drawable", "com.gbccs112a.battleshipadvisor");
			drawableMap.put("label" + i, getResources().getDrawable(ID));
		}

		frame.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int row = (int) ((event.getY() - uly) / cellSize - 1);
				int col = (int) ((event.getX() - ulx) / cellSize - 1);

				if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
					Position pos = Position.gridPositions[row][col];
					if (!guessStack.contains(pos))
						guess = pos;
				}
				invalidate();
				return true;
			}
		});
		reset();
	}

	/**
	 * Go back to initial guesser state and report all guesses and responses to guesser from the beginning.
	 */
	private void reset() {
		guesser.initialize();
		unsunkLengths.clear();
		for (int length : new int[] {5, 4, 3, 3, 2})
			unsunkLengths.add(length);
		//System.out.println("Guess Stack Size: " + guessStack.size());
		//System.out.println("Response Stack Size: " + responseStack.size());
		for (int i = 0; i < guessStack.size(); i++)
			
			report(guessStack.get(i), responseStack.get(i));
		guess = guesser.getGuess();
		invalidate();
	}

	/*
	 * @author jeet.chanchawat from stack overflow
	 * @implemented by William Czubakowski
	 */
	private Bitmap scale(Bitmap bitmap){

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float scaleWidth = ((float) cellSize) / width;
		float scaleHeight = ((float) cellSize) / height;

		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();

		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
		return resizedBitmap;
	}

	private Paint highPaint(){
		Paint painter = new Paint();
		painter.setColor(Color.CYAN); 
		painter.setAlpha(100); //less opaque
		return painter;
	}

	//default painter
	private Paint defPaint(){
		Paint painter = new Paint();
		painter.setColor(Color.BLACK); 
		return painter;
	}

	private Bitmap labelRowBit(int i){
		// draw the row and column labels around the boundaries of a 12-by-12 grid

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("label"+ (char) ('a' + i), "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;

	}

	private Bitmap labelColBit(int i ){
		// draw the row and column labels around the boundaries of a 12-by-12 grid

		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("label"+  (i+1), "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;
	}

	private Bitmap bit2(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("two", "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;
	}

	private Bitmap bit3(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("three", "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;
	}

	private Bitmap bit4(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("four", "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;
	}

	private Bitmap bit5(){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("five", "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;
	}

	private Bitmap unBit(String s){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(s, "drawable","com.gbccs112a.battleshipadvisor"));
		return bitmap;
	}

	private Bitmap multiBit(String s){
		//There was a small glitch where it would try to call the int 2-5 rather than there actual names.  This is a brute force way of fixing the issue.
		Bitmap bitmap;

		if(s.equals("2")) {
			bitmap = bit2();
		}

		else if (s.equals("3")){
			bitmap = bit3();
		}

		else if (s.equals("4")){
			bitmap = bit4();
		}

		else if ( s.equals("5")){
			bitmap = bit5();
		}

		else {
			bitmap = unBit(s);
		}
		return bitmap;
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	public void onDraw(Canvas g) {
		Canvas g2 = (Canvas) g;

		g2.drawColor(Color.BLACK);

		int width = getWidth();
		int height = getHeight();

		g2.drawRect((float) 0, (float) 0, (float)width-1, (float)height-1, defPaint());

		// compute the upper-left-x, upper-left-y, and cell size of a 12-by-12 square grid
		// (This grid will include wraparound row/column labels and the 10-by-10 game grid.)
		int margin = 5;
		int gridSize = Math.max(0, Math.min(width - margin * 2, height - margin * 2));
		ulx = (width - gridSize) / 2;
		uly = (height - gridSize) / 2;
		cellSize = gridSize / 12; 

		// draw the row and column labels around the boundaries of a 12-by-12 grid
		for (int i = 0; i < 10; i++) {

			int x = ulx;
			int y = uly + (i + 1) * cellSize;

			Bitmap bitmap = labelRowBit(i);
			g2.drawBitmap(scale(bitmap), x, y, null);

			x = ulx + (SIZE + 1) * cellSize;

			g2.drawBitmap(scale(bitmap), x, y, null);

			bitmap = labelColBit(i);

			x = ulx + (i + 1) * cellSize;
			y = uly;
			g2.drawBitmap(scale(bitmap), x, y, null);

			y = uly + (SIZE + 1) * cellSize;
			g2.drawBitmap(scale(bitmap), x, y, null);
		}

		// build grid image names
		String[][] imageNames = new String[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++){
			for (int j = 0; j < SIZE; j++) {
				imageNames[i][j] = "unknown";
			}
		}

		for (int i = 0; i < guessStack.size(); i++) {
			Position guess = guessStack.get(i);
			imageNames[guess.row][guess.col] = responseStack.get(i);
		}

		// draw grid images
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int x = ulx + (j + 1) * cellSize;
				int y = uly + (i + 1) * cellSize;

				Bitmap scaledBitmap = scale(multiBit(imageNames[i][j]));
				g2.drawBitmap(scaledBitmap, x, y, null);
			}
		}

		// highlight guess position
		if (!unsunkLengths.isEmpty()) {
			g2.drawRect(ulx + (guess.col + 1) * cellSize, uly+ (guess.row+1)*cellSize, ulx + (guess.col + 1) * cellSize + cellSize, uly+ (guess.row+1)*cellSize + cellSize, highPaint());
		}

		for (int i = 0; i < SIZE - 1; i++) {
			g2.drawRect((float) ulx + (i + 2) * cellSize, (float) uly, (float) ulx + (i + 2) * cellSize, (float) uly + (SIZE + 2) * cellSize, defPaint());
			g2.drawRect((float) ulx, (float) uly + (i + 2) * cellSize, (float) ulx + (SIZE + 2) * cellSize, (float) uly + (i + 2) * cellSize, defPaint());
		}
	}

	/**
	 * Report to guesser on currently selected guess position according to user's button selection.
	 * @param guess guess position
	 * @param response response action command
	 */
	private void report(Position guess, String response) {
		if (response.equals("miss"))
			guesser.report(guess, false, 0);
		else if (response.equals("hit"))
			guesser.report(guess, true, 0);
		else {

			int sunkLength = Integer.parseInt(response);
			unsunkLengths.remove((Integer) sunkLength);
			guesser.report(guess, true, sunkLength);
			if (unsunkLengths.isEmpty()){
				MainActivity.startSecondActivity();
				//popUp(guessStack.size());//
				//newButton(5);
				
				
			}
		}
	}

	/*
	public void newButton(int n){ //TODO end game action
		Button myButton = new Button(MainActivity.getAppContext());
		myButton.setText("Push Me" + n);

		RelativeLayout ll = (RelativeLayout)findViewById(R.id.RelLayout);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		ll.addView(myButton, lp);
	}
	 */
	public static void popUp(int i){ //TODO DOES NOT WORK
		LayoutInflater layoutInflater 
		= (LayoutInflater)MainActivity.getAppContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  

		
		//System.out.println("I ran");
		
		final View popupView = layoutInflater.inflate(R.layout.popup, null);  
		final PopupWindow popupWindow = new PopupWindow(
				popupView, 
				LayoutParams.WRAP_CONTENT,  
				LayoutParams.WRAP_CONTENT);  

		Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
		
		(new Runnable() {
			   public void run() {
				   popupWindow.showAtLocation(popupView, Gravity.NO_GRAVITY, 150, 150);
				   }
				}).run();
		btnDismiss.setOnClickListener(new Button.OnClickListener(){
			

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}});



		/*
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.getAppContext());

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage("You've won in " + " guesses!")
		.setTitle("You Win!");

		// 3. Get the AlertDialog from create()
		final AlertDialog dialog = builder.create();

		//dialog.show();
		/*
		new Runnable() {
			   public void run() {
				     dialog.show();
				   }
				};
		 */
		//display the alert dialog
		//dialog.show();



	}
	public void hitButton(){
		if (unsunkLengths.isEmpty()) return;
		String response =  "hit";
		guessStack.add(guess);
		responseStack.add(response);
		report(guess, response);
		guess = guesser.getGuess();
		invalidate();
	}

	public void missButton(){
		if (unsunkLengths.isEmpty()) return;
		String response =  "miss";
		guessStack.add(guess);
		responseStack.add(response);
		report(guess, response);
		guess = guesser.getGuess();
		invalidate();
	}

	public void sunkButton(){ //TODO reduce list items after they are selected
		//final View button = findViewById(R.id.layout);
		//Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(getContext(), frame);//TODO how to get this anchored to the button

		for (int unsunkLength: unsunkLengths){
			if( unsunkLength==2){
				popup.getMenu().add(unsunkLength+": Patrol Boat");
			}
			else if(unsunkLength == 3){
				popup.getMenu().add(unsunkLength+": Destroyer");
			}
			else if(unsunkLength == 3){
				popup.getMenu().add(unsunkLength+": Submarine");
			}
			else if (unsunkLength == 4){
				popup.getMenu().add(unsunkLength+": Battleship");
			}
			else{
				popup.getMenu().add(unsunkLength+": Aircraft Carrier");
			}
		}
		/*
		popup.getMenu().add("2: Patrol Boat");
		popup.getMenu().add("3: Destroyer");
		popup.getMenu().add("3: Submarine");
		popup.getMenu().add("4: Battleship");
		popup.getMenu().add("5: Aircraft Carrier");
		 */
		//Inflating the Popup using xml file
		popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
		popup.show();

		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				sunkButtonClick(item.getTitle().charAt(0));
				return true;
			}
		});
	}

	public void sunkButtonClick(char boat){ 
		if (unsunkLengths.isEmpty()) return;
		String response = ""+boat;
		guessStack.add(guess);
		responseStack.add(response);
		report(guess, response);
		guess = guesser.getGuess();
		invalidate();
	}

	void newGame(){
		guessStack.clear();
		responseStack.clear();
		reset();
	}

	void undo(){
		if (!guessStack.isEmpty()) {
			guessStack.pop();
			responseStack.pop();
			reset();
		}
	}
}


