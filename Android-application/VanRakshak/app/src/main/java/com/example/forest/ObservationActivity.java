package com.example.forest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ObservationActivity extends AppCompatActivity {
    TextView tvObservation;
    Spinner spnActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        tvObservation = findViewById(R.id.tvObservation);
        spnActivity = findViewById(R.id.spnActivity);

        List<String> activityList = new ArrayList<>();
        activityList.add("Select one");
        activityList.add("Animal Offences");
        activityList.add("Tree Accidents");
        activityList.add("Other");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,activityList);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnActivity.setAdapter(arrayAdapter);
        spnActivity.setSelection(0);

        spnActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                switch(item){
                    case "Tree Accidents":
                        Intent intent = new Intent(ObservationActivity.this,TreeAccidentActivity.class);
                        startActivity(intent);
                        break;
                    case "Other":
                        Intent i = new Intent(ObservationActivity.this,OtherIncidentActivity.class);
                        startActivity(i);
                        break;
                    case "Animal Offences":
                        Intent in = new Intent(ObservationActivity.this,ActionTakenActivity.class);
                        startActivity(in);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}