package com.example.glasgowcarparkdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shieldsy's on 14/12/2015.
 */
public class FullCPList extends Activity implements View.OnClickListener{

    private ListView carParkList;
    private ArrayList<String> carParkLocations;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullcplist);

        carParkList = (ListView) findViewById(R.id.cp_list);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        Intent mainAct = getIntent();
        carParkLocations = mainAct.getStringArrayListExtra("cp_data");

        try{
            ArrayAdapter<String> adapter = new  ArrayAdapter<String>(this, R.layout.cp_list, carParkLocations);

            carParkList.setAdapter(adapter);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
