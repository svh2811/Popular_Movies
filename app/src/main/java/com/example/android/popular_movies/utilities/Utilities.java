package com.example.android.popular_movies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by SARVESH UPADHYAY on 12-03-2016.
 */
public class Utilities {

    private Context context;
    private ConnectivityManager cm;

    public Utilities(){
    }

    public Utilities(Context context) {
        this.context = context;
        this.cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnectedToInternet() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public boolean isNetworkWifi() {
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    public boolean isNetworkMobileData() {
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public boolean isNetworkEthernet() {
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET;
    }

    public boolean isNetworkBluetooth() {
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_BLUETOOTH;
    }

    public boolean isNetworkWimax() {
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIMAX;
    }
}
