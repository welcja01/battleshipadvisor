package com.gbccs112a.battleshipadvisor;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements Runnable{

	private static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.missButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/*//run();
				MediaPlayer mPlayer2;
				mPlayer2= MediaPlayer.create(getBaseContext(), R.raw.miss);
				mPlayer2.start();
				mPlayer2.release();
				mPlayer2.reset();
				mPlayer2.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer mPlayer2){
					mPlayer2.reset();
					mPlayer2.release();
					mPlayer2 = null;
					}
				});*/

				((BattleshipView) findViewById(R.id.BattleshipView)).missButton();
			}
		});


		findViewById(R.id.hitButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				//run();
				((BattleshipView) findViewById(R.id.BattleshipView)).hitButton();
			}
		});

		findViewById(R.id.sunkButton).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				//run();
				((BattleshipView) findViewById(R.id.BattleshipView)).sunkButton();
			}
		});

		MainActivity.context = getApplicationContext();
		setTitle("Battleship Advisor");
		setVisible(true);
		//new BattleshipAdvisorGUI(new MyBattleshipGuesser());
	}



	public synchronized static Context getAppContext() {
		return MainActivity.context;
	}

	public static void startSecondActivity(){
		//define a new Intent for the second Activity
		Intent intent = new Intent(getAppContext(),SecondActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//start the second Activity
		getAppContext().startActivity(intent);
	}

	@Override
	public void run() {
		/*
		try {
			mPlayer2.wait(mPlayer2.getDuration());
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			mPlayer2.release();
		}
		 */
		//mPlayer2.release();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game:
			// call new game method in BattleshipView.java
			((BattleshipView) findViewById(R.id.BattleshipView)).newGame();
			return true;
		case R.id.undo:
			// call undo method in BattleshipView.java
			((BattleshipView) findViewById(R.id.BattleshipView)).undo();
			return true;
		default:
			return false;
		}
	}




}

