package com.example.forest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlertActivity extends AppCompatActivity {
    private static final String TAG = "AlertActivity";

    public static HashMap<String, List<String>> map = new HashMap();
    public static HashMap<String, List<String>> map_c = new HashMap();
    public static HashMap<String, List<String>> map_l = new HashMap();
    public static HashMap<String, List<String>> map_a = new HashMap();

    public static List<List<String>> map1 = new ArrayList<>();
    public static List<List<String>> map2 = new ArrayList<>();
    FileInputStream is;
    BufferedReader reader;
    File folder;
    File file;
    Button alertButton, alertButton1, sosButton;

    ListView listView;
    ArrayAdapter adapter;

//    int [] icons_id;
//    String[] camera_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        setContentView(R.layout.activity_alert);
        listView = (ListView) findViewById(R.id.mobile_list);

        if (getIntent().getIntExtra("callingActivity", 0) == 1001) {
            readFromFile();
            getAlerts();
        } else if (getIntent().getIntExtra("callingActivity", 0) == 1002) {
            readFromFile1();
        } else {
            getNotificationAlerts();
        }

    }

    private void readFromFile() {
        map1.clear();
        map2.clear();

        folder = new File(getFilesDir()+"/forest");
        file = new File(folder.getAbsolutePath()+"/alert.txt");

        if (file.exists()) {
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(line != null){
                Log.d(TAG, line);
                String[] values = line.split("/");
                if (values.length == 4) {
                    map1.add(Arrays.asList(values));
                } else {
                    map2.add(Arrays.asList(values));
                }
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readFromFile1() {
        folder = new File(getFilesDir()+"/forest");
        file = new File(folder.getAbsolutePath()+"/local-alert.txt");

        List<String> local = new ArrayList<>();

        for (Map.Entry<String, List<String>> set : map_l.entrySet()) {
            String s = set.getKey();
            local.add(s);
        }
        String[] locals = local.toArray(new String[0]);

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, locals);

        listView.setAdapter(adapter);
    }

    public void sendLocation1(View view) {
//        alertButton1 = (Button) findViewById(R.id.label1);
        alertButton1 = (Button) findViewById(R.id.label_btn);
        System.out.println("method called");

        String text = (String) alertButton1.getText();
        String[] values = text.split(" ");
        String id = values[4];

        int val = 0;
        List<String> list;
        if (values[0].equals("Alert")) {
            for (int i=0; i<map1.size(); i++) {
                if (map1.get(i).get(0) == id){
                    val = i;
                    break;
                }
            }
            list = map1.get(val);
            System.out.println(list);
        } else {
            for (int i=0; i<map2.size(); i++) {
                if (map2.get(i).get(0) == id){
                    val = i;
                    break;
                }
            }
            list = map2.get(val);
            System.out.println(list);
        }

        String latitude = list.get(1);
        String longitude = list.get(2);

        Intent intent = new Intent(AlertActivity.this, AlertMap.class);
        intent.putExtra("latitude", Double.parseDouble(latitude));
        intent.putExtra("longitude", Double.parseDouble(longitude));
        startActivity(intent);
    }

    public void sendLocation(View view) {
        alertButton = (Button) findViewById(R.id.label);
//        alertButton = (Button) findViewById(R.id.label_btn1);
        System.out.println("method called");

        String text = (String) alertButton.getText();
        Log.d(TAG, text);
        String[] values = text.split(" ");
        System.out.println(values);
        String id = values[4];
        Log.d(TAG, "local map");
        System.out.println(map_l);

        List<String> list;
        if (values[0].equals("Alert")) {
            System.out.println("alert called");
            list = map.get(id);
            System.out.println(list);
        } else if(values[0].equals("Camera")) {
            System.out.println("camera called");
            list = map_c.get(id);
            System.out.println(list);
        } else if (values[0].equals("Animal")) {
            System.out.println("camera called");
            list = map_c.get(id);
            System.out.println(list);
        }
        else {
            System.out.println("part called");
            list = map_l.get("l-777");
            System.out.println(map_l);
            System.out.println(list);
        }

        String latitude = list.get(0);
        String longitude = list.get(1);

        alertButton.setBackgroundColor(Color.GREEN);

        Intent intent = new Intent(AlertActivity.this, AlertMap.class);
        intent.putExtra("latitude", Double.parseDouble(latitude));
        intent.putExtra("longitude", Double.parseDouble(longitude));
        startActivity(intent);
    }

    private void getNotificationAlerts() {
        Log.d(TAG, "notification adapter");
        final List<String> camera = new ArrayList<>();
        final List<Integer> icons = new ArrayList<>();
        Log.d(TAG, "local map");
        System.out.println(map_l);
        for (Map.Entry<String, List<String>> set : map.entrySet()) {
            String s = set.getKey();
            List<String> t = set.getValue();
            camera.add("Alert spotted at camera "+s+" at time "+ t.get(2));
            Log.d("MyTag",camera.get(0));
        }
        for (Map.Entry<String, List<String>> set : map_c.entrySet()) {
            String s = set.getKey();
            List<String> t = set.getValue();
            camera.add("Camera "+s+" broken at time "+t.get(2));
        }
        for (Map.Entry<String, List<String>> set : map_l.entrySet()) {
            String s = set.getKey();
            List<String> t = set.getValue();
            camera.add("SOS alert at time "+t.get(2));
        }
        for (Map.Entry<String, List<String>> set : map_a.entrySet()) {
            String s = set.getKey();
            List<String> t = set.getValue();
            camera.add(t.get(3)+" spotted at camera "+s+" at time "+t.get(2));
        }
        String[] camera_id = camera.toArray(new String[0]);

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, camera_id);

        listView.setAdapter(adapter);
//        NotificationAlertListAdapter myAdapter = new NotificationAlertListAdapter(this,camera_id,icons_id);
//        listView.setAdapter(myAdapter);

//        map.clear();
//        map_c.clear();
    }

    private void getAlerts() {
        Log.d(TAG, "app alerts adatper");
        final List<String> camera = new ArrayList<>();
        final List<Integer> icons = new ArrayList<>();
        for (List<String> strings : map1) {
            String s = strings.get(0);
            String t = strings.get(3);
            camera.add("Alert spotted at camera "+s+" at time "+t);
            icons.add(R.drawable.alert_icon);
        }
        for (List<String> strings : map2) {
            String s = strings.get(0);
            String t = strings.get(3);
//            camera.add("Camera broken at camera "+s+" at time "+t);
            camera.add("Camera "+s+" broken at time "+t);
            icons.add(R.drawable.camera_error);
        }
        String[] camera_id = camera.toArray(new String[0]);
        Log.d("MyTag",camera_id[0]);

        int [] icons_id = new int[icons.size()];
        for (int i =0;i<icons.size();i++){
            icons_id[i]=icons.get(i);
        }

//        adapter = new ArrayAdapter<String>(this,
//                R.layout.activity_listview1, camera_id);
//        listView.setAdapter(adapter);

        AlertListAdapter myAdapter = new AlertListAdapter(this,camera_id,icons_id);
        listView.setAdapter(myAdapter);
    }

}
