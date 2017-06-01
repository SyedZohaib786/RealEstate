package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

public class AddCommision extends AppCompatActivity {

    private static String TAG = AddCommision.class.getSimpleName();
    EditText clname;
    EditText amount;
    EditText propertyName;
    EditText date;
    Button add, back,cancel;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_commision);

        clname = (EditText) findViewById(R.id.etRealEStateName);
        amount = (EditText) findViewById(R.id.etAmount);
        propertyName = (EditText) findViewById(R.id.etPropertyName);
        date = (EditText) findViewById(R.id.etDate);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        add = (Button) findViewById(R.id.btnAddCommision);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (clname.getText().toString().trim().length() == 0 ||
                            amount.getText().toString().trim().length() == 0 ||
                            propertyName.getText().toString().trim().length() == 0 ||
                            date.getText().toString().trim().length() == 0 ) {
                        Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();
                    } else
                        makeJsonObjectRequest1();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void makeJsonObjectRequest1() throws JSONException {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "comm?cname=" + clname.getText().toString()
                + "&amount=" + amount.getText().toString()
                + "&pname=" + propertyName.getText().toString()
                + "&date=" + date.getText().toString()
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());


                try {
//                        // Parsing json object response
//                        // response will be a json object
                        String name = response.getString("cname");
                        String pname = response.getString("pname");
                        JSONObject number = response.getJSONObject("amount");
                        String amount = number.getString("amount");
//                        String mobile = phone.getString("cell");
//                    String addrss = response.getString("");
                    Toast.makeText(getApplicationContext(), "Commission added Successfully!!", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
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
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

