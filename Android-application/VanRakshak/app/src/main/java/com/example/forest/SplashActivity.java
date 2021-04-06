package com.example.forest;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.InputStreamReader;

public class SplashActivity extends AppCompatActivity {
    //Animation variables
    private static int SPLASH_SCREEN=3000;
    Animation topAnim,bottomAnim;
    ImageView imgLogo;
    TextView txtTitle;
    //Animation Variables

    private WifiManager wifiManager;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    public static Intent internetService;
    public static Intent mqttService;
    public static Intent forestService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Animation Variables
        imgLogo = findViewById(R.id.imgLogo);
        txtTitle = findViewById(R.id.txtTitle);

        //Assigning Animations
        imgLogo.setAnimation(topAnim);
        txtTitle.setAnimation(bottomAnim);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        broadcastReceiver = new WifiBroadcastReceiver(wifiManager);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.EXTRA_WIFI_STATE);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(broadcastReceiver, intentFilter);

        internetService = new Intent(SplashActivity.this, InternetService.class);
        mqttService = new Intent(SplashActivity.this, MqttService.class);
        forestService = new Intent(SplashActivity.this, ForestService.class);
        startService(internetService);

//        tvSplash.startAnimation(animation);


        new  Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("opening","app");
                int flag=0;

//                try {
//                    FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
//                    OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                    outputWriter.write("no open");
//                    outputWriter.close();
//
//                    //display file saved message
//                    Toast.makeText(getBaseContext(), "File saved successfully!",
//                            Toast.LENGTH_SHORT).show();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                try {
                    Log.d("atleast","trying");
                    FileInputStream fileIn=openFileInput("mytextfile.txt");
                    InputStreamReader InputRead= new InputStreamReader(fileIn);
//                    Log.d("atleast",InputRead.toString());
                    char[] inputBuffer= new char[100];
                    String s="";
                    int charRead;

                    while ((charRead=InputRead.read(inputBuffer))>0) {
                        // char to string conversion
                        String readstring=String.copyValueOf(inputBuffer,0,charRead);
                        Log.d("atleast","looping in char");
                        s +=readstring;
                    }
                    Log.d("reading",s);
//                    if(s.equals("open")){
//                        FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
//                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                        outputWriter.write("open already");
//                        outputWriter.close();
//                        Log.d("reading","file");
//                    }

//                    try {
//                        Thread.sleep(1000);
//                    }
//                    catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    InputRead.close();
//                    tvSplash.setText(s);

//                    Intent a = new Intent(SplashActivity.this,HomeActivity.class);
//                    startActivity(a);
//                    finish();
                    flag=1;


                } catch (Exception e) {
                    Log.d("trying","failed");
                    try {
//                        FileOutputStream fileout=openFileOutput("mytextfile.txt", MODE_PRIVATE);
//                        OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
//                        outputWriter.write("open");
//                        Log.d("opening","file");
//                        outputWriter.close();
//                        Intent a = new Intent(SplashActivity.this,MainActivity.class);
//                        startActivity(a);
//                        finish();

                        //display file saved message
//                        Toast.makeText(getBaseContext(), "File saved successfully!",
//                                Toast.LENGTH_SHORT).show();

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                Log.d("opening","app1");
                try {
                    Thread.sleep(4000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag==1){
                    Intent a = new Intent(SplashActivity.this,HomeActivity.class);
                    startActivity(a);
                    finish();
                }
                else{
//                    Intent a = new Intent(SplashActivity.this,MainActivity.class);
//                    startActivity(a);
//                    finish();
//                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//
//                            Pair[] pairs=new Pair[2];
//                            pairs[0]=new Pair<View,String>(imgLogo,"logo_image");
//                            pairs[1]=new Pair<View,String>(txtTitle,"logo_text");
//
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                                ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
//                                startActivity(intent,options.toBundle());
//                                finish();
//                            }
//                        }
//                    }, SPLASH_SCREEN);
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this,MainActivity.class);

                            Pair[] pairs=new Pair[2];
                            pairs[0]=new Pair<View,String>(imgLogo,"logo_image");
                            pairs[1]=new Pair<View,String>(txtTitle,"logo_text");

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                ActivityOptions options =
                                        ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                                startActivity(intent,options.toBundle());
                                finish();
                            }
                        }
                    },SPLASH_SCREEN);
                }
            }
        }).start();
    }
}