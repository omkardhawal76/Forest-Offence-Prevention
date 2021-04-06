package com.example.forest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    //Button btnMap,btnPrediction,btnTask;
    private static final String TAG = "HomeActivity";
    public static String encodedImage,description="";

    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

    public static boolean mqttflag = false;

    private static final int PERMISSIONS_REQUEST = 1;
    private static final String PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static boolean location_access = false;

    public static Map<String, List<String>> animal_info = new HashMap<>();
    public static Map<String, List<String>> animal_location_info = new HashMap<>();

    LinearLayout cvMap,cvPrediction, cvTask, cvAlert, cvObservation, cvAnimal, cvSosResolve;

    public static Intent locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        cvMap=findViewById(R.id.cvMap);
        cvPrediction=findViewById(R.id.cvPrediction);
        cvTask=findViewById(R.id.cvTask);
        cvAlert=findViewById(R.id.cvAlert);
        cvObservation=findViewById(R.id.cvObservation);
        cvAnimal=findViewById(R.id.cvAnimal);
        cvSosResolve=findViewById(R.id.cvSosResolve);

        if (hasPermission()) {
            getCurrentLocation();
        } else {
            requestPermission();
        }

        locationService = new Intent(HomeActivity.this, LocationService.class);
        startService(locationService);

        cvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this, TaskActivity.class);
                startActivity(a);
            }
        });

        cvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this, LocationActivity.class);
                startActivity(a);
            }
        });

        cvPrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this, Prediction.class);
                startActivity(a);
            }
        });

        cvAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this, AlertActivity.class);
                a.putExtra("callingActivity", 1001);
                startActivity(a);
            }
        });

        cvObservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this,ObservationActivity.class);
                startActivity(a);
            }
        });

        cvAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this, DetectorActivity.class);
                startActivity(a);
            }
        });

        cvSosResolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this, AlertActivity.class);
                a.putExtra("callingActivity", 1002);
                startActivity(a);
            }
        });

    }

    private void getCurrentLocation() {
        Log.d(TAG, "permission granted");
        location_access = true;
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (allPermissionsGranted(grantResults)) {
                getCurrentLocation();
            } else {
                requestPermission();
            }
        }
    }

    private static boolean allPermissionsGranted(final int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_LOCATION)) {
                Toast.makeText(
                        HomeActivity.this,
                        "Location permission is required for this demo",
                        Toast.LENGTH_LONG)
                        .show();
            }
            requestPermissions(new String[] {PERMISSION_LOCATION}, PERMISSIONS_REQUEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.opt_about:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_profile:
                Toast.makeText(this, "Your Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt_logout:
                Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

