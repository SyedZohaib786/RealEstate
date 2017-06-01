package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

public class ShowCommission extends AppCompatActivity {


    // json array response url
    private String urlJsonArry = Ngrok.url + "/web_service_new/public/getcomm";

    private static String TAG = ShowCommission.class.getSimpleName();
    private Button btnMakeArrayRequest;

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_commission);
        btnMakeArrayRequest = (Button) findViewById(R.id.btnGetCommision);
        txtResponse = (TextView) findViewById(R.id.txtViewCommList);
        txtResponse.setMovementMethod(new ScrollingMovementMethod());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please be Calm...");
        pDialog.setCancelable(false);

        btnMakeArrayRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // making json array request
                makeJsonArrayRequest();

            }
        });
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String id = person.getString("commision_id");
                                String comAmount = person.getString("commision_amount");
                                String clientName = person.getString("Client_name");
                                String pName = person.getString("Property_Name");
                                String date = person.getString("date");


                                jsonResponse += "S.NO: " + id + "\n\n";
                                jsonResponse += "Amount: " + comAmount + "\n\n";
                                jsonResponse += "Client: " + clientName + "\n\n";
                                jsonResponse += "Property: " + pName + "\n\n";
                                jsonResponse += "Date: " + date + "\n\n";
                            }

                            txtResponse.setText(jsonResponse);



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }
}
