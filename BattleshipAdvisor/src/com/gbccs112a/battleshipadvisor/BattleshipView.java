package com.gbccs112a.battleshipadvisor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import android.annotation.SuppressLint;
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
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupMenu;
import android.widget.Toast;

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
	private Stack<Position> guessStack = new Stack<Position>();
	private Stack<String> responseStack = new Stack<String>();
	private Position guess = Position.gridPositions[0][0];
	private View frame = this;
	int ulx, uly, cellSize;
	char[] chArray = "abcdefghij".toCharArray();
	Resources res = null;
	private HashMap<String, Drawable> drawableMap = new HashMap<String, Drawable>();

	public BattleshipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//new MyBattleshipGuesser();
		res = context.getResources();
		//this.guesser = guesser;
		init();
		//new GridPanel( context, attrs);
		// TODO Auto-generated constructor stub
	}
	/*
	public SemaphoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = context.getResources();
		init();
	}*/



	public void init(){
		String[] imageStrings = {"unknown", "miss", "hit", "two", "three", "four", "five"};
		for (String imageString : imageStrings) { 

			System.out.println("imageString:  " + imageString);
			final int ID = getResources().getIdentifier(imageString, "drawable","com.gbccs112a.battleshipadvisor");
			System.out.println("ID:  " + ID);

			drawableMap.put(imageString, getResources().getDrawable(ID));
		}

		// 2) row label images
		for (int i = 0; i < chArray.length; i++){

			System.out.println("label+char:  " + chArray[i]);

			final int ID = getResources().getIdentifier("label" + chArray[i], "drawable", "com.gbccs112a.battleshipadvisor");

			System.out.println("ID #: "+ID);

			drawableMap.put("label" + chArray[i], getResources().getDrawable(ID));


		}

		// 3) column label images
		for (int i = 1; i <= 10; i++){
			final int ID = getResources().getIdentifier("label" + i, "drawable", "com.gbccs112a.battleshipadvisor");
			drawableMap.put("label" + i, getResources().getDrawable(ID));
		}

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
		for (int i = 0; i < guessStack.size(); i++)
			report(guessStack.get(i), responseStack.get(i));
		guess = guesser.getGuess();

		invalidate();
	}
	/*
	class GridPanel extends View implements OnTouchListener {
		HashMap<String, Drawable> imageMap = drawableMap;
		int ulx, uly, cellSize;

		public GridPanel(Context context, AttributeSet attrs) {
			super(context, attrs);
			//TODO this needs to be done correctly with our image names!

			// load images
			// 1) board images
			//String[] imageStrings = {"unknown", "miss", "hit", "2", "3", "4", "5"};
			//for (String imageString : imageStrings) 
			//imageMap.put(imageString, new ImageView("img/" + imageString + ".png"));
			// 2) row label images
			//for (int i = (int) 'a'; i <= (int) 'j'; i++)
			//imageMap.put("label" + (char) i, new ImageView());
			// 3) column label images
			//for (int i = 1; i <= 10; i++)
			//imageMap.put("label" + i, new ImageView("img/label" + i + ".png"));
			// add listener for user guess position selections (to override guess of MyBattleshipGuesser)
			setOnTouchListener(this); //TODO NOT SURE IF THIS WORKS!!
		}


	}
	 */


	public void onClick(View v) { //I think its onClick not onTOuch
		int row = (int) ((getY() - uly) / cellSize - 1);
		int col = (int) ((getX() - ulx) / cellSize - 1);
		//				System.out.println(row + " " + col);
		if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
			Position pos = Position.gridPositions[row][col];
			if (!guessStack.contains(pos))
				guess = pos;
		}
		invalidate();
		//return (Boolean) null; //TODO
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
	/*
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * @Author
	 */
	public void onDraw(Canvas g) {
		Canvas g2 = (Canvas) g;
		//g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		//g2.setBackground(Color.BLACK);
		g2.drawColor(Color.BLACK);
		//g2.setPaint(Color.BLACK);
		Paint myPaint = new Paint();
		myPaint.setColor(Color.BLACK);

		int width = getWidth();
		int height = getHeight();

		//g2.fillRect(0, 0, width, height); // black background
		g2.drawRect((float) 0, (float) 0, (float)width-1, (float)height-1, myPaint);

		// compute the upper-left-x, upper-left-y, and cell size of a 12-by-12 square grid
		// (This grid will include wraparound row/column labels and the 10-by-10 game grid.)
		int margin = 5;
		int gridSize = Math.max(0, Math.min(width - margin * 2, height - margin * 2));
		ulx = (width - gridSize) / 2;
		uly = (height - gridSize) / 2;
		cellSize = gridSize / 12; // 2 label cells plus 10 board cells 

		// draw the row and column labels around the boundaries of a 12-by-12 grid
		for (int i = 0; i < 10; i++) {
			Drawable image = (drawableMap.get("label" +   getResources().getDrawable(getResources().getIdentifier("label"+ (char) ('a' + i), "drawable","com.gbccs112a.battleshipadvisor"))));
			int x = ulx;
			int y = uly + (i + 1) * cellSize;

			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("label"+ (char) ('a' + i), "drawable","com.gbccs112a.battleshipadvisor"));
			g2.drawBitmap(scale(bitmap), x, y, null);

			//g2.drawImage(image, x, y, x + cellSize, y + cellSize, 0, 0, 512, 512, null);

			x = ulx + (SIZE + 1) * cellSize;

			g2.drawBitmap(scale(bitmap), x, y, null);

			bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier("label"+  (i+1), "drawable","com.gbccs112a.battleshipadvisor"));
			//image = imageMap.get("label" + (i + 1)).getImage();
			x = ulx + (i + 1) * cellSize;
			y = uly;
			g2.drawBitmap(scale(bitmap), x, y, null);
			//g2.drawImage(image, x, y, x + cellSize, y + cellSize, 0, 0, 512, 512, null);
			y = uly + (SIZE + 1) * cellSize;
			g2.drawBitmap(scale(bitmap), x, y, null);
			//g2.drawImage(image, x, y, x + cellSize, y + cellSize, 0, 0, 512, 512, null);
		}

		// build grid image names
		String[][] imageNames = new String[SIZE][SIZE];
		for (int i = 0; i < SIZE; i++)
			for (int j = 0; j < SIZE; j++) 
				imageNames[i][j] = "unknown";
		for (int i = 0; i < guessStack.size(); i++) {
			Position guess = guessStack.get(i);
			imageNames[guess.row][guess.col] = responseStack.get(i);
		}

		// draw grid images
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				int x = ulx + (j + 1) * cellSize;
				int y = uly + (i + 1) * cellSize;
				//Drawable icon = imageMap.get(imageNames[i][j]);
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(imageNames[i][j], "drawable","com.gbccs112a.battleshipadvisor"));
				//Image image = icon.getImage();
				g2.drawBitmap(scale(bitmap), x, y, null);
			}
		}

		// highlight guess position
		if (!unsunkLengths.isEmpty()) {
			//g2.setPaint(new Color(1f, 1f, 0f, .5f));
			Paint painter = new Paint();
			painter.setColor(Color.YELLOW); //CLose to neller? TODO
			//painter.setAlpha(1); //half opaque

			g2.drawRect(ulx + (guess.col + 1) * cellSize, uly+ (guess.row+1)*cellSize, ulx + (guess.col + 1) * cellSize + cellSize, uly+ (guess.row+1)*cellSize + cellSize, painter); // Rect(ulx + (guess.col + 1) * cellSize, uly + (guess.row + 1) * cellSize, cellSize, cellSize);
		}

		// draw grid lines
		//g2.setPaint(Color.BLACK);

		Paint painter = new Paint();
		painter.setColor(Color.BLACK); //CLose to neller? TODO

		for (int i = 0; i < SIZE - 1; i++) {
			Paint painter1 = new Paint();
			painter.setColor(Color.GRAY);
			//g2.draw(new Line2D.Double(ulx + (i + 2) * cellSize, uly, ulx + (i + 2) * cellSize, uly + (SIZE + 2) * cellSize));
			//g2.draw(new Line2D.Double(ulx, uly + (i + 2) * cellSize, ulx + (SIZE + 2) * cellSize, uly + (i + 2) * cellSize));
			g2.drawRect((float) ulx + (i + 2) * cellSize, (float) uly, (float) ulx + (i + 2) * cellSize, (float) uly + (SIZE + 2) * cellSize, painter1);
			g2.drawRect((float) ulx, (float) uly + (i + 2) * cellSize, (float) ulx + (SIZE + 2) * cellSize, (float) uly + (i + 2) * cellSize, painter1);
		}
	}
	//ResponseListener responseListener = new ResponseListener(); TODO
	/*
		private View getButtonPanel() {
			View buttonPanel = new View(null); //TODO
			buttonPanel.setLayout(new FlowLayout());

			// miss button
			JButton missButton = new JButton("Miss");
			missButton.setActionCommand("miss");
			missButton.addActionListener(responseListener);
			buttonPanel.add(missButton);

			// hit button
			JButton hitButton = new JButton("Hit");
			hitButton.setActionCommand("hit");
			hitButton.addActionListener(responseListener);
			buttonPanel.add(hitButton);

			// sunk button - causes popup menu with unsunk length options
			final JButton sunkButton = new JButton("Sunk");
			sunkButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					JPopupMenu popup = new JPopupMenu();
					for (int unsunkLength : unsunkLengths) {
						String lengthStr = String.valueOf(unsunkLength);
						JMenuItem item = new JMenuItem(lengthStr);
						item.addActionListener(responseListener);
						popup.add(item);
					}
					popup.show(sunkButton, 0, 0);
				}
			});
			buttonPanel.add(sunkButton);
			return buttonPanel;

		}

		class ResponseListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (unsunkLengths.isEmpty()) return;
				String response = e.getActionCommand();
				guessStack.add(guess);
				responseStack.add(response);
				report(guess, response);
				guess = guesser.getGuess();
				repaint();
			}
		}*/

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
				MainActivity.popUp(guessStack.size());
			}
			
			//JOptionPane.showMessageDialog(frame, "You've won in " + guessStack.size() + " guesses!");

		}
	}
	
	public void hitButton(){
		if (unsunkLengths.isEmpty()) return;
		//System.out.println("getActionCommand String:" + e.getActionCommand());
		String response =  "hit";
		guessStack.add(guess);
		responseStack.add(response);
		report(guess, response);
		guess = guesser.getGuess();
		invalidate();
	}
	public void missButton(){
		if (unsunkLengths.isEmpty()) return;
		//System.out.println("getActionCommand String:" + e.getActionCommand());
		String response =  "miss";
		guessStack.add(guess);
		responseStack.add(response);
		report(guess, response);
		guess = guesser.getGuess();
		invalidate();
	}
	//TODO
	public void sunkButton(){
		final View button = findViewById(R.id.layout);
		//Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(getContext(), frame);

		//Inflating the Popup using xml file
		popup.getMenuInflater()
			.inflate(R.menu.popup_menu, popup.getMenu());
		
		popup.show();
		//registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				sunkButtonClick(item.getTitle().charAt(0));
				/*Toast.makeText(
						getContext(),
						"You Clicked : " + item.getTitle(),
						Toast.LENGTH_SHORT
						).show();
						*/
				return true;
			}
		});
	}
	public void sunkButtonClick(char boat){
		if (unsunkLengths.isEmpty()) return;
		System.out.println("BOat:" + boat);
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

	//Create Menu Bar
	




}


