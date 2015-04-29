package com.gbccs112a.battleshipadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;


public class MainActivity extends Activity{

	private Button missButton, hitButton, sunkButton;

	/*
	 * sets up the buttons
	 *@author William Czubakowski
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		missButton = (Button) findViewById(R.id.missButton);
		hitButton = (Button) findViewById(R.id.hitButton);
		sunkButton = (Button) findViewById(R.id.sunkButton);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/*
		sunkButton = (Button) findViewById(R.id.sunkButton);
		sunkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("IM RUNNING!!!");
				//Creating the instance of PopupMenu
				PopupMenu popup = new PopupMenu(MainActivity.this, sunkButton);

				//Inflating the Popup using xml file
				popup.getMenuInflater()
				.inflate(R.menu.popup_menu, popup.getMenu());

				//registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Toast.makeText(
								MainActivity.this,
								"You Clicked : " + item.getTitle(),
								Toast.LENGTH_SHORT
								).show();
						return true;
					}
				});

				popup.show(); //showing popup menu
			}
		}); //closing the setOnClickListener method
*/
		findViewById(R.id.missButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((BattleshipView) findViewById(R.id.BattleshipView)).missButton();
			}
		});

		findViewById(R.id.hitButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((BattleshipView) findViewById(R.id.BattleshipView)).hitButton();
			}
		});

		findViewById(R.id.sunkButton).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				((BattleshipView) findViewById(R.id.BattleshipView)).sunkButton();
				
				
			}
		});


		setTitle("Battleship Advisor");
		setVisible(true);
		//new BattleshipAdvisorGUI(new MyBattleshipGuesser());
	}
	/*
	boolean click = true;
	PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    LayoutParams params;
    LinearLayout mainLayout = null;
    Button but;

	public void showPopup(View v) {



        popUp = new PopupWindow(this);
        layout = new LinearLayout(this);
        mainLayout = new LinearLayout(this);
        tv = new TextView(this);
		sunkButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (click) {
                    popUp.showAtLocation(mainLayout, Gravity.BOTTOM, 10, 10);
                    popUp.update(50, 50, 300, 80);
                    click = false;
                } else {
                    popUp.dismiss();
                    click = true;
                }
            }

        });

	}
	 */
	public static void popUp(int n){
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(null); //TODO CANT HAVE NULL HERE
		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage("You've won in " + n + " guesses!")
		.setTitle("You Win!");
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		//display the alert dialog
		dialog.show();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}



}

