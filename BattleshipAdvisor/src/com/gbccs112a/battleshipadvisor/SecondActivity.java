package com.gbccs112a.battleshipadvisor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class SecondActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		 this.setContentView(R.layout.second);
		super.onCreate(savedInstanceState);
		
		int Id = R.id.text;
		TextView text = (TextView)findViewById(Id); 
		text.setTextColor(Color.CYAN);
		text.setText("You won in " + BattleshipView.guessStack.size() + " guesses");
		
		findViewById(R.id.newGame).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				
				//define a new Intent for the second Activity
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				BattleshipView.guessStack.clear();
				BattleshipView.responseStack.clear();
				
				//start the second Activity
				getApplicationContext().startActivity(intent);
				
				
			}
		});
		//text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
	}
}

