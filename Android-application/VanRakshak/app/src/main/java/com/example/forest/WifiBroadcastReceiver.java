package com.example.forest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiBroadcastReceiver";

    private WifiManager wifiManager;
    public ConnectivityManager connectivityManager;

    public WifiBroadcastReceiver(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {

            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiManager.WIFI_STATE_ENABLED) {
                Log.d(TAG, "wifi state enabled called");
                HomeActivity.mqttflag = true;
                context.startService(SplashActivity.mqttService);
                context.startService(SplashActivity.forestService);
            } else if (state == WifiManager.WIFI_STATE_DISABLED) {
                Log.d(TAG,"wifi state disabled called");
                Toast.makeText(context, "Wifi is disabled. Enable Wifi", Toast.LENGTH_SHORT).show();
                HomeActivity.mqttflag = false;
                context.stopService(SplashActivity.mqttService);
                context.stopService(SplashActivity.forestService);
            }
        }
    }

//    private class Networkconnectivity extends AsyncTask<Boolean, String, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(Boolean... bools) {
//            if (bools[0]) {
//                if (isNetworkAvailable()) {
//                    if (isInternetAvailable()) {
//                        return true;
//                    }
//                }
//            } else {
//                if (bools[1]) {
//                    if (isInternetAvailable()) {
//                        return true;
//                    }
//                } else {
//                    if (isMobileDataEnabled()) {
//                        return true;
//                    }
//                }
//            }
//            return false;
//        }
//
//        private boolean isNetworkAvailable() {
//            System.out.println("network function called");
//            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        }
//
//        public boolean isInternetAvailable() {
//            System.out.println("internet function called");
//            try {
//                InetAddress ipAddr = InetAddress.getByName("google.com");
//                //You can replace it with your name
//                return !ipAddr.equals("");
//
//            } catch (Exception e) {
//                return false;
//            }
//        }
//
//        private boolean isMobileDataEnabled() {
//            System.out.println("mobile data function called");
//            boolean mobileDataEnabled = false; // Assume disabled
//            try {
//                Class cmClass = Class.forName(connectivityManager.getClass().getName());
//                Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
//                method.setAccessible(true); // Make the method callable
//                // get the setting for "mobile data"
//                mobileDataEnabled = (Boolean)method.invoke(connectivityManager);
//            } catch (Exception e) {
//                // Some problem accessible private API
//                e.printStackTrace();
//            }
//            return mobileDataEnabled;
//        }
//    }
}

