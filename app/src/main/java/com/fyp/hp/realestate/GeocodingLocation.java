package com.fyp.hp.realestate;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocodingLocation {

    public static String lati = "";
    public static String longi = "";

    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final String locationAddress,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String resultLat = null;
                String resultLng = null;
                try {
                    List
                            addressList = geocoder.getFromLocationName(locationAddress, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        StringBuilder sbLat = new StringBuilder();
                        StringBuilder sbLng = new StringBuilder();
                        sbLat.append(address.getLatitude());
                        sbLng.append(address.getLongitude());
                        resultLat = sbLat.toString();
                        resultLng = sbLng.toString();

                        lati = resultLat;
                        longi = resultLng;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message1 = Message.obtain();

                    message1.setTarget(handler);
                    if (resultLat != null && resultLng != null) {
                        message1.what = 1;
                        Bundle bundle = new Bundle();
                        resultLat = "\n\nLatitude :\n" + resultLat;
                        bundle.putString("addressLat", resultLat);
                        message1.setData(bundle);


                        Bundle bundleLng = new Bundle();
                        resultLng = "\n\nLongitude :\n" + resultLng;
                        bundleLng.putString("addressLng", resultLng);

                    } else {
                        message1.what = 1;
                        Bundle bundle = new Bundle();
                        resultLat = "Address: " + locationAddress +
                                "\n Unable to get Latitude and Longitude for this address location.";
                        bundle.putString("address", resultLat);
                        message1.setData(bundle);
                    }
                    message1.sendToTarget();
                }
            }
        };
        thread.start();
    }
}