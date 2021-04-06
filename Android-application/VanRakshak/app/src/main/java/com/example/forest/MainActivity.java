package com.example.forest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    public static String empid="id_1-Das";
    EditText etUsername,etPassword;
    MaterialButton btnLogin,btnGuestLogin;
    private String urlJsonArry = "https://forestweb.herokuapp.com/applogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //To Remove the default taskbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btnLogin=findViewById(R.id.btnLogin);
        btnGuestLogin=findViewById(R.id.btnGuestLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag=0;
                try {
                    String username=etUsername.getText().toString().trim();
                    String password=etPassword.getText().toString().trim();

                    if (username.length() == 0) {
                        etUsername.setError("Enter Valid Username");
                        flag = 1;
                    } else if (username.length() > 30) {
                        etUsername.setError("Username too long ,Enter Valid Username");
                        etUsername.setText("");
                        etUsername.requestFocus();
                        flag = 1;
                    } else if (password.length() == 0) {
                        etPassword.setError("Enter Valid Password");
                        flag = 1;
                    } else if (password.length() > 30) {
                        etPassword.setError("Password too long ,Enter Valid Password");
                        etPassword.setText("");
                        etPassword.requestFocus();
                        flag = 1;
                    }
                    if (flag == 0) {
                        sendrequest(etUsername, etPassword);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //   Intent a = new Intent(MainActivity.this,HomeActivity.class);
                //   startActivity(a);
//                if (flag == 0) {
//                    Intent a = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(a);
//                    finish();
//                }

            }
        });

        btnGuestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GuestReportActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendrequest(EditText user, EditText pass) throws JSONException {
        Connectivity con =new Connectivity();
        if(con.isConnected(this)) {
            final JSONObject rem = new JSONObject();
//        rem.remove("status");
            rem.put("username", user.getText());
            rem.put("password", pass.getText());
            Log.d("Param", rem.toString());
            final JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, urlJsonArry, rem,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject data) {
                            Log.d("response", data.toString());
                            try {
                                Log.d("response", data.get("id").toString());
                                //if id=='-1' error function

                                if (data.get("id").equals("-1")) {
                                    Toast.makeText(MainActivity.this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show();
                                    etUsername.setText("");
                                    etPassword.setText("");
                                } else {
                                    empid=data.get("id").toString();
                                    Intent a = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(a);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            FileOutputStream fileout = null;
                            try {
                                fileout = openFileOutput("mytextfile.txt", MODE_APPEND);
                                OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
                                outputWriter.write(data.get("id").toString());
                                Log.d("opening", "file");
                                outputWriter.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.e("Error: ", error.getMessage());
                        }
                    });

            Appcontroller.getInstance().addToRequestQueue(req);
        }
        //else
        //no internet so some error shown
    }
}
//if(TextUtils.isEmpty(username)){
//        Toast.makeText(MainActivity.this, "Please Enter Username", Toast.LENGTH_SHORT).show();
//        return;
//        }
//        if(TextUtils.isEmpty(password)){
//        Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
//        return;
//        }
//
//        sendrequest(etUsername,etPassword);