package com.example.appbao2.BroadcastReciver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

public class Internet extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        // Kiểm tra trạng thái kết nối Internet
        if (isConnectedToInternet(context)) {
            // Xử lý khi có kết nối Internet
            Toast.makeText(context, "Connected to Internet", Toast.LENGTH_SHORT).show();
        } else {
            // Xử lý khi không có kết nối Internet
            Toast.makeText(context, "Disconnected from Internet", Toast.LENGTH_SHORT).show();
        }
    }

    // Phương thức kiểm tra kết nối Internet
    private boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}

