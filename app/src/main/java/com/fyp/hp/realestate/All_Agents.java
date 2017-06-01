package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import static com.fyp.hp.realestate.R.layout.activity_all__agents;


public class All_Agents extends AppCompatActivity {

    private String TAG = All_Agents.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = Ngrok.url + "/web_service_new/public/getagent";

    ArrayList<HashMap<String, String>> agentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_all__agents);

        agentList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(All_Agents.this);
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


                        String agentName = jsonobject.getString("agent_name");
                        String agentEmail = jsonobject.getString("email");
                        String phone = jsonobject.getString("phone");
                        String cell = jsonobject.getString("mobile_number");
                        String agentAddrss = jsonobject.getString("address");
                        String estatename = jsonobject.getString("real_estatename");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        contact.put("agent_name", agentName);
                        contact.put("email", agentEmail);
                        contact.put("phone", phone);
                        contact.put("mobile_number", cell);
                        contact.put("address", agentAddrss);
                        contact.put("real_estatename", estatename);
//                                    contact.put("agent_id", estateName);
                        agentList.add(contact);

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
                    All_Agents.this, agentList,
                    R.layout.list_allagents, new String[]{"agent_name", "email",
                    "phone", "mobile_number", "address", "real_estatename"}, new int[]{R.id.name,
                    R.id.email, R.id.phone, R.id.cell, R.id.address, R.id.esname});


            lv.setAdapter(adapter);
        }
    }
}




