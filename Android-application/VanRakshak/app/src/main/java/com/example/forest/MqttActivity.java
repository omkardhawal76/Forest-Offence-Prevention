package com.example.forest;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

public class MqttActivity extends Thread {
    public static final String TAG = "MqttActivity";

    public final static String ipAddress = "192.168.49.229";

    private final String protocol = "tcp";
    private final String port = "1883";
    private final String url = protocol + "://" + ipAddress + ":" + port;

    private final String clientId = MqttClient.generateClientId();
    private Context context;

    // animal sightings will be published here based on animal
    private final String elephant_topic = "forest/animal/elephant";
    private final String pig_topic = "forest/animal/pig";
    private final String monkey_topic = "forest/animal/monkey";
    private final String civet_topic = "forest/animal/civet";
    private final String deer_topic = "forest/animal/deer";

    // message payloads which will be published on various topics
//    private String animal_payload = camera_id+"/"+latitude+"/"+longitude;
    private String animal_payload = "animal";

    private static MqttAndroidClient client;
    private static IMqttToken token;

    private String payload;

    public MqttActivity (Context context, String payload) {
        this.payload = payload;
        this.context = context;
    }

    public void run () {
        Log.d(TAG, "inside run");
//        Log.d(TAG, token.getClient().toString());
//        Log.d(TAG, payload);
        client = new MqttAndroidClient(context, url, clientId);
        try {
            token = client.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // We are connected
                Log.d(TAG, "inside success");
                byte[] encodedPayload = new byte[0];
                MqttMessage message;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                try {
                    animal_payload += "/"+timestamp.toString();
                    encodedPayload = animal_payload.getBytes("UTF-8");
                    message = new MqttMessage(encodedPayload);
                    if (payload.equals("Elephant")) {
                        client.publish(elephant_topic, message);
                    } else if (payload.equals("Monkey")) {
                        client.publish(monkey_topic, message);
                    } else if (payload.equals("Civet")) {
                        client.publish(civet_topic, message);
                    } else if (payload.equals("Pig")) {
                        client.publish(pig_topic, message);
                    } else if (payload.equals("Deer")) {
                        client.publish(deer_topic, message);
                    }
                    System.out.println("message published");
                } catch (NullPointerException | UnsupportedEncodingException | MqttException e) {
                    System.out.println("error occured");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                System.out.println("Inside failure");
                Log.d(TAG, String.valueOf(exception));
            }
        });

    }

}
