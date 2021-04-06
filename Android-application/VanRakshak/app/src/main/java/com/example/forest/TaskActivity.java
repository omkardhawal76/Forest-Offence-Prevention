package com.example.forest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

//Volley
//JSON

public class TaskActivity extends AppCompatActivity {
    private static final int TASK_POSITION_REQ = 123;
    public static String selected_task;
    private ListView lv;
    public Adapter adapter;
    public ArrayList<Model> modelArrayList;
    public JSONArray data_array=new JSONArray();
    TextView tvNoTask;
    int elementPostion;
//    public JSONArray cache=new JSONArray();
//    public int flag=0,flag1=0;

    Connectivity con=new Connectivity();

//    private String urlJsonArry = "https://forestweb.herokuapp.com/apptask";
//    private String urltask = "https://forestweb.herokuapp.com/gettask";

    private static String TAG = MainActivity.class.getSimpleName();

//    private String jsonResponse;

    private String[] myList = new String[]{"Benz", "Bike",
            "Car","Carrera"
            ,"Ferrari","Harly",
            "Lamborghini","Silver"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        tvNoTask = findViewById(R.id.tvNoTask);
        lv = (ListView) findViewById(R.id.listview);
        modelArrayList = populateList();
        Log.d("before", modelArrayList.toString());
        Log.d("internet", String.valueOf(con.isConnected(this)));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("task").document("assign");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        final ArrayList<Model> list = new ArrayList<>();
                        List<Object> data = (List<Object>) document.getData().get(MainActivity.empid);
                        Log.d("from cache", data.toString());
                        try {

                            JSONArray tasks = listToJSONArray(data);

                            Log.d("list", tasks.toString());

                            for (int i = 0; i < tasks.length(); i++) {
                                Model model = new Model();
                                JSONObject curr = tasks.getJSONObject(i);
                                model.setName(curr.getString("task_info"));
                                list.add(model);
                            }

                            Log.d("add list", list.toString());
                            data_array = tasks;
                            modelArrayList = list;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        next();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private JSONArray listToJSONArray(List<Object> list) {
        JSONArray arr = new JSONArray();
        for(Object obj: list) {
            if (obj instanceof Map) {
                arr.put(mapToJSON((Map) obj));
            }
            else if(obj instanceof List) {
                arr.put(listToJSONArray((List) obj));
            }
            else {
                arr.put(obj);
            }
        }
        return arr;
    }

    private JSONObject mapToJSON(Map<String, Object> map) {
        JSONObject obj = new JSONObject();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                if (value instanceof Map) {
                    Map<String, Object> subMap = (Map<String, Object>) value;
                    obj.put(key, mapToJSON(subMap));
                } else if (value instanceof List) {
                    obj.put(key, listToJSONArray((List) value));
                } else {
                    obj.put(key, value);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
//        if(con.isConnected(this))
//        {
//            new CountDownTimer(3000, 1000) {
//                //get new task
//
//                public void onTick(long millisUntilFinished) {
//                    if (flag==0){
//                        Log.d("wait","wait function");
//
//                        Log.d("wait","wait function continue");
//                        try {
//                            modelArrayList=makeJsonArrayRequest();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        Log.d("hello",modelArrayList.toString());
//                    }
//                    flag=1;
//                }
//
//                public void onFinish() {
////                adapter = new Adapter(this,modelArrayList);
////                lv.setAdapter(adapter);
//                    next();
//                }
//            }.start();
//
//        }
//        else{
//            //Offline
//            String data=readFromFile("tasks.txt");
//            final ArrayList<Model> list = new ArrayList<>();
//            Log.d("from cache",data);
//            try {
//                JSONArray tasks = new JSONArray(data);
//
//                Log.d("list",tasks.toString());
//
//                for(int i = 0; i < tasks.length(); i++){
//                    Model model = new Model();
//                    JSONObject curr = tasks.getJSONObject(i);
//                    model.setName(curr.getString("task_info"));
//                    list.add(model);
//                }
//
//                Log.d("add list",list.toString());
//                data_array=tasks;
//                modelArrayList=list;
//            }
//            catch(JSONException e){
//                e.printStackTrace();
//            }
//            next();
//        }

//        adapter = new Adapter(this,modelArrayList);
//        lv.setAdapter(adapter);
//
//        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
//                new SwipeToDismissTouchListener<>(
//                        new ListViewAdapter(lv),
//                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
//                            @Override
//                            public boolean canDismiss(int position) {
//                                return true;
//                            }
//
//                            @Override
//                            public void onDismiss(ListViewAdapter view, int position) {
//                                adapter.remove(position);
//                            }
//                        });
//
//        lv.setOnTouchListener(touchListener);
//        lv.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (touchListener.existPendingDismisses()) {
//                    touchListener.undoPendingDismiss();
//                } else {
//                    Toast.makeText(TaskActivity.this, "Position " + position, LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

    public void next(){
        Log.d("check list",modelArrayList.toString());
        //if list is empty display no task
        try {
        adapter = new Adapter(this,modelArrayList);
            lv.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(this, "No Task" , LENGTH_SHORT).show();
            Log.d("MyTag","modelArrayList.size() : "+modelArrayList.size());
//            tvNoTask.setText("Apparently There are No Tasks ");
            tvNoTask.setVisibility(View.VISIBLE);
        }


        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(lv),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                adapter.remove(position);
//                                try {
//                                    sendrequest(position);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
                            }
                        });

        lv.setOnTouchListener(touchListener);
        lv.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
//                    Toast.makeText(TaskActivity.this, "Position " + position, LENGTH_SHORT).show();
                    try {
                        JSONObject task = (JSONObject) data_array.get(position);
                        selected_task = (String) task.get("task_id");
                        Log.d(selected_task,task.toString());
                        Log.d("MyTag","selected task: "+selected_task);
                        Log.d("MyTag","ModelArray list position: "+position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    elementPostion = position;
                    Log.d("MyTag","elementPostion: "+elementPostion);
                    Intent a = new Intent(TaskActivity.this, taskdescription.class);
                    startActivityForResult(a, TASK_POSITION_REQ);
//                    startActivity(a);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("MyTag","elementPostion: "+elementPostion);
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode== TASK_POSITION_REQ) && (resultCode==RESULT_OK)){
            Log.d("MyTag","inside if elementPostion: "+elementPostion);
            if (data.getStringExtra(taskdescription.Result_DATA).equals("Success")){
                modelArrayList.remove(elementPostion);
                adapter = new Adapter(this,modelArrayList);
                lv.setAdapter(adapter);
            }
            else{
                Toast.makeText(this, "Failure & position: "+elementPostion, LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<Model> populateList(){

        ArrayList<Model> list = new ArrayList<>();

        for(int i = 0; i < myList.length; i++){
            Model model = new Model();
            model.setName(myList[i]);
            list.add(model);
        }

        return list;

    }

//server request
//    private ArrayList<Model> makeJsonArrayRequest() throws JSONException {
//
////        showpDialog();
//        final ArrayList<Model> list = new ArrayList<>();
//        String temp = "";
//        FileInputStream fin = null;
//        try {
//            fin = openFileInput("mytextfile.txt");
//
//
//            int c;
//            while ((c = fin.read()) != -1) {
//                temp = temp + Character.toString((char) c);
//            }
//
//            fin.close();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        JSONObject d=new JSONObject();
//        d.put("id",temp);
//        JSONArray xyz=new JSONArray();
//        xyz.put(temp);
//        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST,urltask,xyz,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("json array", response.toString());
//
//                        try {
////                            JSONObject jsonObject = new JSONObject();
////                            jsonObject.put("task", response.toString());
////                            writeToFile(response.toString());
//
//                            JSONArray tasks = new JSONArray(response.toString());
//
//                            Log.d("list",tasks.toString());
//
//                            for(int i = 0; i < tasks.length(); i++){
//                                Model model = new Model();
//                                JSONObject curr = tasks.getJSONObject(i);
//                                model.setName(curr.getString("task_info"));
//                                list.add(model);
//                            }
//
//                            Log.d("add list",list.toString());
//                            data_array=tasks;
////                            return(list);
//
//                        }
//                     catch(JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Error: " + error.getMessage());
//            }
//        });
//
////         Adding request to request queue
//        Appcontroller.getInstance().addToRequestQueue(req);
//        Log.d("function",list.toString());
//        return list;
//    }

//    private void sendrequest(int i) throws JSONException {
//        if(con.isConnected(this)) {
//            JSONObject rem = (JSONObject) data_array.get(i);
//            rem.remove("status");
//            rem.put("status", "complete");
//            JsonObjectRequest req = new JsonObjectRequest(urlJsonArry, rem,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject data) {
//                            Log.d("response", data.toString());
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            VolleyLog.d(TAG, "Error: " + error.getMessage());
//                            Toast.makeText(getApplicationContext(),
//                                    error.getMessage(), Toast.LENGTH_SHORT).show();
//                            //                hidepDialog();
//                        }
//                    });
//            // Adding request to request queue
//            Appcontroller.getInstance().addToRequestQueue(req);
//        }
//
//        Connectivity con=new Connectivity();
//        if(!con.isConnected(this))
//        {
//            JSONObject rem = (JSONObject) data_array.get(i);
//            rem.remove("status");
//            rem.put("status", "complete");
//            cache.put(rem);
//            writeToFilecache(cache.toString());
//        }
//
//        int len=data_array.length();
//        JSONArray temp=new JSONArray();
//        for (int x=0;x<len;x++)
//        {
//            //Excluding the item at position
//            if (x != i)
//            {
//                temp.put(data_array.get(x));
//            }
//        }
//        data_array=temp;
//        writeToFile(data_array.toString());
//
//
//        Log.d("function",modelArrayList.toString());
//
//    }
//
//    private void writeToFile(String data) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("tasks.txt", MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//
//    private void writeToFilecache(String data) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("cache.txt", MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
//
    private String readFromFile(String file) {

        String ret = "";

        try {
            InputStream inputStream = openFileInput(file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}


