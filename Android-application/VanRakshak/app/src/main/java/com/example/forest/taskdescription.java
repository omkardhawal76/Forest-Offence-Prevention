package com.example.forest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class taskdescription extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1;
    static final String Result_DATA="OK";
    String result;
    TextView tvConfirmation;
    TextInputEditText etEventDesc;
    MaterialButton btnCamera,submit;
    ImageView doneBg,doneImg;
    AnimatedVectorDrawable avd;
    Animation doneAnimation;
    int flag=0;
    Handler handler;
    Runnable runnable;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_taken);
        tvConfirmation = findViewById(R.id.tvConfirmation);
        doneBg = findViewById(R.id.doneBg);
        doneImg = findViewById(R.id.doneImg);
        doneAnimation = AnimationUtils.loadAnimation(this,R.anim.a1);

        if (HomeActivity.description.length()!=0) {
            etEventDesc = findViewById(R.id.etEventDesc);
            etEventDesc.setText(HomeActivity.description, TextView.BufferType.EDITABLE);
        }

        btnCamera = findViewById(R.id.btnCamera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEventDesc = findViewById(R.id.etEventDesc);
                HomeActivity.description = etEventDesc.getText().toString();
                Log.d("got", HomeActivity.description);
                callCamera();
            }
        });
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendrequest();

                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (result.equals("Success")) {
                                Intent i = new Intent(taskdescription.this, TaskActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra(Result_DATA, result);
                                setResult(RESULT_OK, i);
                                startActivity(i);
                                finish();
                                Log.d("MyTag", "flag now: " + flag);
                            }
                        }
                    };
                    handler.postDelayed(runnable,5000);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    HomeActivity.encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);


                    // Inserting Contacts
//                    Log.d("Insert: ", String.valueOf(imageInByte[0]));
                    Log.d("Insert: ", HomeActivity.encodedImage+ HomeActivity.description);
                    Intent i = new Intent(taskdescription.this,
                            taskdescription.class);
                    startActivity(i);
                    finish();
                }
                Toast.makeText(this, "Photo has been taken", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 200);
    }

    private void sendrequest() throws JSONException {
//        try {
//            Log.d("sending image", HomeActivity.encodedImage);
//            Log.d("description", HomeActivity.description);
            Map<String, Object> data = new HashMap<>();


        //Check for encodedImage
        if (HomeActivity.encodedImage != null){
            data.put("image", HomeActivity.encodedImage);
        }
        else{
            data.put("image" , " ");
        }
        //Check for description
        if (HomeActivity.description != null){
            data.put("description", HomeActivity.description);
            Log.d("MyTag","HomeActivity.description "+HomeActivity.description);
        }
        else{
            data.put("description" , " ");
            Log.d("MyTag","HomeActivity.description "+HomeActivity.description);
        }
//            if (HomeActivity.encodedImage != null) {
//                data.put("image", HomeActivity.encodedImage);
//            }
//            else {
//                result = "Failure";
//                Toast.makeText(this, "You have not entered Event Description", Toast.LENGTH_SHORT).show();
//                tvConfirmation.setVisibility(View.VISIBLE);
//                tvConfirmation.setText("You have not taken any image");
//            }
//
//            if (etEventDesc.getText().toString().trim().length()>0
//                    && HomeActivity.description.trim().length()>0) {
//                data.put("description", HomeActivity.description.trim());
//            }
//            else {
//                result = "Failure";
//                Toast.makeText(this, "You have not entered Event Description", Toast.LENGTH_SHORT).show();
//                tvConfirmation.setVisibility(View.VISIBLE);
//                tvConfirmation.setText("You have not entered Event Description");
//            }

            data.put("task_id", TaskActivity.selected_task);
            data.put("empid", MainActivity.empid);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("task").document("complete")
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("MyTag", "DocumentSnapshot successfully written!");
                            HomeActivity.description = "";
                            HomeActivity.encodedImage = "";
                            TaskActivity.selected_task = "";

                            flag = 1;
                            Log.d("MyTag", "flag in sendrequest " + flag);
                            if (flag == 1) {
                                drawable = doneImg.getDrawable();
                                Log.d("MyTag", "flag in if : " + flag);
                                if (drawable instanceof AnimatedVectorDrawable) {
                                    doneBg.setAnimation(doneAnimation);
                                    doneImg.setAnimation(doneAnimation);
                                    doneBg.setVisibility(View.VISIBLE);
                                    doneImg.setVisibility(View.VISIBLE);
                                    avd = (AnimatedVectorDrawable) drawable;
                                    avd.start();
                                    tvConfirmation.setVisibility(View.VISIBLE);
                                    tvConfirmation.setText("Successfully submitted");
                                    result = "Success";
                                }
                            } else {
                                Log.d("MyTag", "flag in else : " + flag);
                                result = "Failure";
                                tvConfirmation.setVisibility(View.VISIBLE);
                                tvConfirmation.setText("You have not entered any Event Description ");
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("MyTag", "Error writing document", e);
                        }
                    });
            //else
            //no internet so some error shown
//        }
//        catch (Exception e){
//            Log.d("MyTag", "flag in catch : " + flag);
//            result = "Failure";
//            if (HomeActivity.description == null && HomeActivity.encodedImage == null){
//                Toast.makeText(this, "You have neither entered any Event Description nor you have taken any image ", Toast.LENGTH_SHORT).show();
//                tvConfirmation.setVisibility(View.VISIBLE);
//                tvConfirmation.setText("You have neither entered any Event Description nor you have taken any image ");
//            }
//            else if (HomeActivity.description == null && etEventDesc.getText().toString().trim().length()>0){
//                Log.d("MyTag", "flag in description catch : " + flag);
//                Toast.makeText(this, "You have not entered Event Description", Toast.LENGTH_SHORT).show();
//                tvConfirmation.setVisibility(View.VISIBLE);
//                tvConfirmation.setText("You have not entered Event Description");
//            }
//            else if (HomeActivity.encodedImage == null){
//                Toast.makeText(this, "You have not taken Any Image", Toast.LENGTH_SHORT).show();
//                tvConfirmation.setVisibility(View.VISIBLE);
//                tvConfirmation.setText("You have not taken Any Image");
//            }
//        }
    }

}