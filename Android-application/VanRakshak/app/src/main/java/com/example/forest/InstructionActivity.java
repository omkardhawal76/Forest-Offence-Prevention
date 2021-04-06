package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InstructionActivity extends AppCompatActivity {
    TextInputEditText etLocalContactNum;
    MaterialButton btnSubmit;
    String FILE_NAME="Local_Contact";
    SharedPreferences sharedPreferences;
    String MyTag = "MyTag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        etLocalContactNum = findViewById(R.id.etLocalContactNum);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLocalContactNum.getText().toString().trim().length() != 10) {
                    etLocalContactNum.setError("Enter a Valid Contact Number");
                } else {
                    collectContactNumber(etLocalContactNum.getText().toString().trim());
                    Intent i = new Intent(InstructionActivity.this, GuestReportActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    private void collectContactNumber(String trim) {
        File file = new File(String.valueOf(getApplicationContext().getFilesDir()),FILE_NAME);
        Log.d(MyTag,"Path: "+String.valueOf(getApplicationContext().getFilesDir()));

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String number = etLocalContactNum.getText().toString().trim();
            bufferedWriter.write(number);
            bufferedWriter.close();
            Toast.makeText(this, "Contact No. collected", Toast.LENGTH_SHORT).show();
            Log.d(MyTag,"Number: "+etLocalContactNum.getText().toString().trim());

            sharedPreferences = getSharedPreferences("LocalFlag.txt",MODE_PRIVATE);
            int flag = sharedPreferences.getInt("Flag",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Flag",1);
                    editor.commit();
                    Log.d(MyTag,"Flag= "+flag);

        } catch (IOException e) {
            Toast.makeText(this, "Contact No. not collected", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}