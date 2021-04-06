package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SosActivity extends AppCompatActivity {

    TextInputEditText etSosEventDesc,etSosContactNum;
    MaterialButton btnSosSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        etSosEventDesc = findViewById(R.id.etSosEventDesc);
        etSosContactNum = findViewById(R.id.etSosContactNum);
        btnSosSubmit = findViewById(R.id.btnSosSubmit);

        btnSosSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

    }
}