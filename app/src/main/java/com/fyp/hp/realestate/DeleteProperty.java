package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DeleteProperty extends AppCompatActivity{

    private static String TAG = DeleteProperty.class.getSimpleName();
    EditText amount;
    EditText sqYrds;
    Button dlt, back, cancel;

    private String id;

    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_property);

        amount = (EditText) findViewById(R.id.etAmount);
        sqYrds = (EditText) findViewById(R.id.etAreaSqYrds);
        dlt = (Button) findViewById(R.id.btnDlt);

        dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteEmployee();
//                contactList = new ArrayList<>();
//                 new GetContacts().execute();
//                try {
//                    makeJsonObjectRequestAgent();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });

    }
    private void deleteEmployee(){
        class DeleteEmployee extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(DeleteProperty.this, "Updating...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(DeleteProperty.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Ngrok.url + "/web_service_new/public/"
                + "dlt?amount=" + amount + "&area=" + sqYrds, id);
                return s;
            }
        }

        DeleteEmployee de = new DeleteEmployee();
        de.execute();
    }

    private void confirmDeleteEmployee(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to delete this property?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEmployee();
                        startActivity(new Intent(DeleteProperty.this,Login.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

//
//    private void makeJsonObjectRequestAgent() throws JSONException {
//        showpDialog();
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
//                + "dlt?amount=" + amount.getText().toString() + "&area=" + sqYrds.getText().toString(),null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
//
//                try {
//                    Intent nextActivity=new Intent(DeleteProperty.this, DropDownMenuForAgent.class);
//                    startActivity(nextActivity);
////                    clearText();
//
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(),
//                            "Wrong Credentials! Please try again..",
//                            Toast.LENGTH_LONG).show();
//                }
//                hidepDialog();
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d(TAG, "Wrong Credentials");
//                Toast.makeText(getApplicationContext(),
//                        "Wrong Credentials! Please try again..", Toast.LENGTH_SHORT).show();
////                clearText();
//                // hide the progress dialog
//                hidepDialog();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//
//    }

    //    private class GetContacts extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(DeleteProperty.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            HttpHandler sh = new HttpHandler();
//
//            // Making a request to url and getting response
//            String jsonStr = sh.makeServiceCall(Ngrok.url + "/web_service_new/public/dlt?amount=" + amount +"&area=" + sqYrds);
//
//            Log.e(TAG, "Response from url: " + jsonStr);
//
//            if (jsonStr != null) {
//                try {
//                    JSONArray jsonarray = new JSONArray(jsonStr);
//                    for (int i = 0; i < jsonarray.length(); i++) {
//                        JSONObject jsonobject = jsonarray.getJSONObject(i);
//
//
//                        String clientName = jsonobject.getString("property_name");
//                        String phone = jsonobject.getString("property_des");
//                        String cell = jsonobject.getString("property_amount");
//                        String clientEmail = jsonobject.getString("property_area");
//                        String clientAddrss = jsonobject.getString("address");
//                        String agentName = jsonobject.getString("agent_name");
//                        String estatename = jsonobject.getString("real_estatename");
////
////                        // tmp hash map for single contact
////                        HashMap<String, String> contact = new HashMap<>();
////                        contact.put("client_name", clientName);
////                        contact.put("phone", phone);
////                        contact.put("mobile_number", cell);
////                        contact.put("email", clientEmail);
////                        contact.put("address", clientAddrss);
////                        contact.put("agent_name", agentName);
////                        contact.put("real_estatename", estatename);
////
////                        contactList.add(contact);
//
//                    }
//                } catch (final JSONException e) {
//                    Log.e(TAG, "Json parsing error: " + e.getMessage());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "Json parsing error: " + e.getMessage(),
//                                    Toast.LENGTH_LONG)
//                                    .show();
//                        }
//                    });
//
//                }
//            } else {
//                Log.e(TAG, "Couldn't get json from server.");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getApplicationContext(),
//                                "Couldn't get json from server. Check LogCat for possible errors!",
//                                Toast.LENGTH_LONG)
//                                .show();
//                    }
//                });
//
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            /**
//             * Updating parsed JSON data into ListView
//             * */
//
//        }
//    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
