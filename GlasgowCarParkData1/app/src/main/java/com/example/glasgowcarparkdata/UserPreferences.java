package com.example.glasgowcarparkdata;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Shieldsy on 01/08/2016.
 */
public class UserPreferences extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private ArrayList<String> carParkLocations;
    String username;
    Integer favouriteCarPark;

    public static String userPrefFileName = "Preferences";
    SharedPreferences userPrefs;

    EditText userName;
    Button btnBack;
    Button btnUpdate;
    Spinner spinCPList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userpreferences);

        userPrefs = getSharedPreferences(userPrefFileName, 0);

        userName = (EditText) findViewById(R.id.etUsername);
        spinCPList = (Spinner) findViewById(R.id.spinCPList);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        Intent mainAct = getIntent();
        carParkLocations = mainAct.getStringArrayListExtra("cp_Names");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_names, carParkLocations);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinCPList.setAdapter(adapter);
        spinCPList.setOnItemSelectedListener(this);

        checkUserPrefs();
    }

    public void checkUserPrefs() {
        userPrefs = getSharedPreferences(userPrefFileName, 0);
        String storedUserName = userPrefs.getString("Username", "No Username Found");
        Integer storedFavCP = userPrefs.getInt("Favourite CP", 0);

        userName.setText(storedUserName);
        spinCPList.setSelection(storedFavCP);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnUpdate:
                username = userName.getText().toString();
                SharedPreferences.Editor editor = userPrefs.edit();
                editor.putString("Username", username);
                editor.putInt("Favourite CP", favouriteCarPark);
                editor.commit();
                return;
            case R.id.btnBack:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        int position = spinCPList.getSelectedItemPosition();

        favouriteCarPark = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
