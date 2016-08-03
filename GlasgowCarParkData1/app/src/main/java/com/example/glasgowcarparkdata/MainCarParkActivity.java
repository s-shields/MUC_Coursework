package com.example.glasgowcarparkdata;
/*
 * Author: Steven Shields
 * Student ID: s1434230
 * Student User: sshiel202
 * */


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainCarParkActivity extends Activity implements OnClickListener, OnItemSelectedListener {
	
	private TextView cpName;
	private TextView cpTotalSpaces;
	private TextView cpSpaceAvailable;
	private TextView cpSpaceTaken;
	private TextView cpStatus;
	private TextView cpAvailableKey;
	private TextView cpTakenKey;
	private Spinner cpSpinner;
	private String cplat = "0.0";
	private String cpLong = "0.0";
	private String dataURL = "https://api.open.glasgow.gov.uk/traffic/carparks";
	private List<CarParkClass> carParkLocations = null;
	private ArrayList<String> cpNames = new ArrayList<String>();
	private ArrayList<String> cpString = new ArrayList<String>();
	private Button previous_btn;
	private Button next_btn;
	private Button btnDisplay;
	private Button btnLocator;
	private int clickCount;
	private int updateCount = 0;
	private ProgressIndicator mProgressIndicator1;
	private float max = 1;
	private float update = 0;
	private String newMax;
	private Float carParkPercentage;
	private int tempCount;
	private Boolean updating = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		new setUpData().execute();
		initialise();
	}
	
	
	// DATA ORGANISATION ********************************
	
	private void initialise(){
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"fonts/TransportMedium.ttf");
		cpAvailableKey = (TextView) findViewById(R.id.vacantPercentage);
		cpAvailableKey.setBackgroundColor(getResources().getColor(R.color.DeepSkyBlue));
		cpAvailableKey.setTypeface(typeFace);
		cpTakenKey = (TextView) findViewById(R.id.occupiedPrecentage);
		cpTakenKey.setBackgroundColor(getResources().getColor(R.color.RoyalBlue));
		cpTakenKey.setTypeface(typeFace);
		cpName = (TextView) findViewById(R.id.cpName);
		cpName.setTypeface(typeFace);
		cpTotalSpaces = (TextView) findViewById(R.id.cpTotalSpaces);
		cpTotalSpaces.setTypeface(typeFace);
		cpSpaceAvailable = (TextView) findViewById(R.id.cpSpaceAvailable);
		cpSpaceAvailable.setTypeface(typeFace);
		cpSpaceTaken = (TextView) findViewById(R.id.cpSpaceTaken);
		cpSpaceTaken.setTypeface(typeFace);
		cpStatus = (TextView) findViewById(R.id.cpPercentage);
		cpStatus.setTypeface(typeFace);
		previous_btn = (Button) findViewById(R.id.button4);			
		next_btn = (Button) findViewById(R.id.button3);
		btnDisplay = (Button) findViewById(R.id.btnDisplay);
		btnLocator = (Button) findViewById(R.id.btnLocator);
		cpSpinner = (Spinner) findViewById(R.id.cpSpinner);
		mProgressIndicator1 = (ProgressIndicator) findViewById(R.id.determinate_progress_indicator1);
		mProgressIndicator1.setForegroundColor(getResources().getColor(R.color.RoyalBlue));
	    mProgressIndicator1.setBackgroundColor(getResources().getColor(R.color.DeepSkyBlue));
	    mProgressIndicator1.setPieStyle(true);		
	    previous_btn.setOnClickListener(this);
	    next_btn.setOnClickListener(this);
		btnDisplay.setOnClickListener(this);
		btnLocator.setOnClickListener(this);
	    
	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_names, cpNames);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		
		cpSpinner.setAdapter(adapter);
		cpSpinner.setOnItemSelectedListener(this);
	}
	
	private void parseData() {
		try {
				CPPullParser parser = new CPPullParser();
				carParkLocations = parser.parse(dataURL);
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}
	
	private class setUpData extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();	
			
			Toast.makeText(getApplicationContext(), "Data Processing", Toast.LENGTH_LONG).show();
							
		}

		@Override
		protected Void doInBackground(Void... params) {
			
			String temp;
			String status;
			String spaceAvailable;
			int spaceTaken, totalSpace;
			
			parseData();
			cpNames.clear();
			cpNames.add(0, "Please Select Option");	
			
			for(int i = 0; i < carParkLocations.size(); i++){			
				Log.e("Num","Num: " + i);
								
				temp = carParkLocations.get(i).getCarParkName().toString();
				status = carParkLocations.get(i).getCarParkStatus().toString();
				spaceTaken = Integer.parseInt(carParkLocations.get(i).getOccupiedSpaces().toString());
				totalSpace = Integer.parseInt(carParkLocations.get(i).getTotalCapacity().toString());
				spaceAvailable = String.valueOf(totalSpace - spaceTaken);
				
				carParkLocations.get(i).setSpacesAvailable(spaceAvailable);

				cpString.add(i, carParkLocations.get(i).toString());
				Log.e("cpNames","Car park name: " + temp);
				//cpNames.add((i+1),temp);
				
				if(status.equalsIgnoreCase("carParkClosed")){
					cpNames.add((i+1),temp + ": CLOSED");
				}
				else if(status.equalsIgnoreCase("enoughSpacesAvailable")){
					cpNames.add((i+1),temp + ": OPEN");
				}
				else if(status.equalsIgnoreCase("carParkFull")){
					cpNames.add((i+1),temp + ": FULL");
				}			
			}
			
			Collections.sort(cpNames);
			
			while(!cpNames.get(0).contains("Please Select Option")){
				Collections.swap(cpNames, cpNames.indexOf("Please Select Option"), (cpNames.indexOf("Please Select Option") - 1));
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			final SharedPreferences userPrefs = getSharedPreferences("Preferences", 0);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainCarParkActivity.this);
			alertDialogBuilder.setMessage("Would you like to load your favourite car park");

			alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					cpSpinner.setSelection(userPrefs.getInt("Favourite CP", 0));
					dialog.dismiss();
				}
			});

			alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					cpSpinner.setSelection(0);
					dialog.dismiss();
				}
			});

			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();

			Toast.makeText(getApplicationContext(), "Data Processing Complete " + userPrefs.getString("Username", ""), Toast.LENGTH_LONG).show();
			tempCount = clickCount;
			initialise();

			super.onPostExecute(result);
		}
		
	}	
	
	
	// Overall display **********************
	private void setMainData() {
		//btnLocator.setVisibility(View.GONE);
		int appTotalSpaces = 0;
		int appSpacesTaken = 0;
		int appSpacesAvailable = 0;
		float appSpacesPercentage = 0;
		int temp;
		int temp2;
		
		for(int i = 0; i < carParkLocations.size(); i++){
			appTotalSpaces = Integer.parseInt(carParkLocations.get(i).getTotalCapacity().toString()) + appTotalSpaces;
			appSpacesTaken = Integer.parseInt(carParkLocations.get(i).getOccupiedSpaces().toString()) + appSpacesTaken;
		}
		
		appSpacesAvailable = appTotalSpaces - appSpacesTaken;
		appSpacesPercentage = ((float)appSpacesTaken / appTotalSpaces) * 100;
		temp = (int) appSpacesPercentage;
		temp2 = 100 - temp;
		startThread(appSpacesPercentage);
		
		cpName.setText("Overall Availablity"); //ASK WHY!!!!!! THIS WONT WORK WITH ONCREATE.
		cpTotalSpaces.setText("Total Amount: " + String.valueOf(appTotalSpaces));
		cpSpaceAvailable.setText("Vacant \n" + String.valueOf(appSpacesAvailable));
		cpAvailableKey.setText(String.valueOf(temp2) + "%");
		cpSpaceTaken.setText("Occupied \n" + String.valueOf(appSpacesTaken));
		cpTakenKey.setText(String.valueOf(temp) + "%");
		

	}
	
	
	// Individual Car Park display *********************
	private void displayData(int array_pos){

		//btnLocator.setVisibility(View.VISIBLE);
		float progress;
		float availPer;
		int tempPer;
		int tempAvail;
		String temp = carParkLocations.get(array_pos).getCarParkOccupancy(); 
		String status = carParkLocations.get(array_pos).getCarParkStatus().toString();
		cplat = carParkLocations.get(array_pos).getCpMapLat();
		cpLong = carParkLocations.get(array_pos).getCpMapLong();
		
		progress = Float.parseFloat(temp);
		tempPer = (int) progress;
		availPer = 100 - progress;
		tempAvail = (int) availPer;
		
		
		if(status.equalsIgnoreCase("carParkClosed")){
			status = "Car Park Closed";
		}
		else if(status.equalsIgnoreCase("enoughSpacesAvailable")){
			status = "Space Available";
		}
		else if(status.equalsIgnoreCase("carParkFull")){
			status = "Car Park Full";
		}
		
		startThread(tempPer);
		cpName.setText(carParkLocations.get(array_pos).getCarParkName().toString());
		cpStatus.setText(status);
		cpSpaceAvailable.setText("Vacant \n" + carParkLocations.get(array_pos).getSpacesAvailable().toString());
		cpAvailableKey.setText(String.valueOf(tempAvail) + "%");
		cpSpaceTaken.setText("Occupied \n" + carParkLocations.get(array_pos).getOccupiedSpaces().toString());
		cpTakenKey.setText(String.valueOf(tempPer) + "%");
		cpTotalSpaces.setText("Total Amount: " + carParkLocations.get(array_pos).getTotalCapacity().toString());		
		
	}
	
	
	// Pie Chart thread ********************************************
	private void startThread(final float total_amount) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				update = 0;
								
				if(total_amount != 100){
					if(total_amount < 10){
						if(total_amount <= 0){
                            newMax = "0.0";
                            carParkPercentage = Float.parseFloat(newMax);
						}
						else
						{
							String temp = String.valueOf(total_amount);
							String[] split = temp.split("\\.");
							newMax = "0.0" + split[0];
							carParkPercentage = Float.parseFloat(newMax);
						}

					}else{
						String temp = String.valueOf(total_amount);
						String[] split = temp.split("\\.");
						newMax = "0." + split[0];
						carParkPercentage = Float.parseFloat(newMax);
					}					
				}else{
					carParkPercentage = max;
				}
								
				if(carParkPercentage == 0){
					updateProgressIndicatorValue();
				}else{
					while(update <= carParkPercentage){
						update += 0.01;
				        updateProgressIndicatorValue();
				        try{
				        	Thread.sleep(75);
				        }catch(Exception e){
				        	
				        }
					}
				}
			}
		}).start();
	}
	
	private void updateProgressIndicatorValue() {
	   this.runOnUiThread(new Runnable() {
		@Override
		public void run() {
	        mProgressIndicator1.setValue(update);
		}
	   });
    }
	
	
	// Refresh Button triggers resetting the data and menu **********
	@SuppressLint("NewApi")
	public void refreshData(final MenuItem item) {
		/* Attach a rotating ImageView to the refresh item as an ActionView */
		LayoutInflater inflater = (LayoutInflater) getApplication()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(R.layout.iv_refresh,
				null);

		Animation rotation = AnimationUtils.loadAnimation(getApplication(),
				R.anim.rotate_refresh);
		rotation.setRepeatCount(Animation.INFINITE);
		iv.startAnimation(rotation);

		item.setActionView(iv);

		// trigger feed refresh:
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {

				updating = false;
				try{
		        	Thread.sleep(7000);
		        	MainCarParkActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (updating == false) {;
					        item.getActionView().clearAnimation();
							item.setActionView(null);
							
							//startTimer(updateCount);
					    }
					}
				});
				}catch(Exception e){
		        	
		        }
			}
		});
		thread.start();
	}
	

	// MENU AND SELECTION OPTIONS *****************************
	
	// Menu creation and options ******************************
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main_car_park_activity, menu);
		
		new MenuInflater(this).inflate(R.menu.main_car_park_activity, menu);
		return (super.onCreateOptionsMenu(menu));		
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
				
		switch (item.getItemId()){
			case R.id.action_settings:
				Intent userPreferences = new Intent(getApplicationContext(), UserPreferences.class);
				userPreferences.putStringArrayListExtra("cp_Names", cpNames);
				startActivity(userPreferences);
				return true;
				
			case R.id.action_refresh:
							
				Log.e("Refresh Button", "STARTED");
				//updateCount = 0;
				//refreshData(item);
				new setUpData().execute();
				Log.e("Refresh Button", "FINISHED");
				
				return true;

			case R.id.action_about:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
				alertDialogBuilder.setMessage("About test");

				alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				return true;
		}		
		
		return super.onOptionsItemSelected(item);
	}
	
	
	// Button options *******************************
	@Override
	public void onClick(View arg0) {
		
		Log.e("Array_pos","Position before click = " + clickCount);
		
		switch(arg0.getId()) {
			case R.id.button3: // Next Button for textView

				Log.e("Array_pos", "Position during click= " + clickCount);

				if (clickCount < carParkLocations.size()) {
					cpSpinner.setSelection(clickCount + 1);
				}

				if (clickCount == carParkLocations.size()) {
					cpSpinner.setSelection(1);
				}
				break;

			case R.id.button4: //Previous button for textVeiw

				if (clickCount <= carParkLocations.size()) {
					if (clickCount == 0) {
						cpSpinner.setSelection(11);
					} else {
						cpSpinner.setSelection(clickCount - 1);
					}
				}

				if (clickCount == 1) {
					cpSpinner.setSelection(11);
				}
				break;

			case R.id.btnDisplay:

				Intent fullCPList = new Intent(getApplicationContext(), FullCPList.class);
				fullCPList.putStringArrayListExtra("cp_data", cpString);
				startActivity(fullCPList);
				break;

			case R.id.btnLocator:
				Intent cpLocate = new Intent(getApplicationContext(), MapsActivity.class);
				cpLocate.putExtra("MapLat", cplat);
				cpLocate.putExtra("MapLong", cpLong);
				startActivity(cpLocate);
				break;
		}

		
		Log.e("Array_pos", "Position after click= " + clickCount);
	}

	
	// Spinner selection option ********************************
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3){
		
		int position = cpSpinner.getSelectedItemPosition();
		String spinnerName = cpNames.get(position).toString();
		int count = 0;
		
		if(position == 0){

			setMainData();
			
		}
		else{
			while(!spinnerName.contains(carParkLocations.get(count).getCarParkName().toString())){
				count++;
			}
												
			displayData(count);
			clickCount = position;			
		}		
	}	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
		
	}

	
	// Screen orientation changes (Portrait and Landscape) ********************
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);

	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	        Toast.makeText(this, "landscape", Toast.LENGTH_LONG).show();
	        setContentView(R.layout.main_layout);
	        initialise();
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	        Toast.makeText(this, "portrait", Toast.LENGTH_LONG).show();
	        setContentView(R.layout.main_layout);
	        initialise();
	    }
	}

	
	
}
