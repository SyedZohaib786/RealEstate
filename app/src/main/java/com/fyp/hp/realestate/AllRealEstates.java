package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AllRealEstates extends AppCompatActivity {
    private String TAG = AllRealEstates.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = Ngrok.url + "/web_service_new/public/getestate";

    ArrayList<HashMap<String, String>> estateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_real_estates);

        estateList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        new GetRealEstate().execute();
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetRealEstate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(AllRealEstates.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    JSONArray jsonarray = new JSONArray(jsonStr);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject = jsonarray.getJSONObject(i);


                        String estateName = jsonobject.getString("real_estatename");
                        String des = jsonobject.getString("real_estateDes");
                        String address = jsonobject.getString("address");
                        String cell = jsonobject.getString("phone");
                        String email = jsonobject.getString("email");
                        String area = jsonobject.getString("city_name");
                        String subArea = jsonobject.getString("area_name");

                        // tmp hash map for single contact
                        HashMap<String, String> estates = new HashMap<>();

                        estates.put("real_estatename", estateName);
                        estates.put("real_estateDes", des);
                        estates.put("address", address);
                        estates.put("phone", cell);
                        estates.put("email", email);
                        estates.put("city_name", area);
                        estates.put("area_name", subArea);
                        estateList.add(estates);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    AllRealEstates.this, estateList,
                    R.layout.list_allestates, new String[]{"real_estatename", "real_estateDes",
                    "address", "phone", "email", "city_name", "area_name"}, new int[]{R.id.name,
                    R.id.des, R.id.estateAddress, R.id.phone, R.id.estateEmail, R.id.area, R.id.subArea});


            lv.setAdapter(adapter);
        }
    }
}
