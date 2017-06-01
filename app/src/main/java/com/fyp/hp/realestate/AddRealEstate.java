package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import app.AppController;

public class AddRealEstate extends AppCompatActivity {
    private String txtSpinnerArea, txtSpinnerSubArea;

    private static String TAG = AddAgent.class.getSimpleName();
    EditText estateName,desc,address,contact_number;
    Button add, back;
    private ProgressDialog pDialog;
    EditText emailadd;

    //for Area's spinner
    static  ArrayList<areaArray> list;
    static  ArrayList<areaArray> listSubArea;
    ArrayList<String> listItems=new ArrayList<>();
    ArrayList<String> listItemsSubArea=new ArrayList<>();
    ArrayAdapter<String> adapterArea;
    Spinner spArea,spSubArea;

    //for SubArea Spinner
    ArrayAdapter<String> adapterSubArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_real_estate);

        spArea = (Spinner)findViewById(R.id.spSelectArea);
        spSubArea = (Spinner)findViewById(R.id.spSelectSubArea);


        adapterArea = new ArrayAdapter<String>(this, R.layout.spinner_area_layout, R.id.txtAreaNames, listItems);
        spArea.setAdapter(adapterArea);
        spArea.setId(spArea.getId());
        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String arrayName = list.get(position).name;
                String arrayID = list.get(position).id;
                txtSpinnerArea = arrayID;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  } });

        adapterSubArea = new ArrayAdapter<String>(this, R.layout.spinner_subarea_layout, R.id.txtSubAreaNames, listItemsSubArea);
        spSubArea.setAdapter(adapterSubArea);
        spSubArea.setId(spSubArea.getId());
        spSubArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String arrayName = list.get(position).name;
                String arrayID = list.get(position).id;
                txtSpinnerSubArea = arrayID;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  } });





        estateName = (EditText) findViewById(R.id.etRealEStateName);
        emailadd = (EditText) findViewById(R.id.etEmail);
        desc = (EditText) findViewById(R.id.etDes);
        address = (EditText) findViewById(R.id.etEstateAddress);
        contact_number = (EditText) findViewById(R.id.etEstatePhone);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please be Calm...");
        pDialog.setCancelable(false);

        add = (Button) findViewById(R.id.btnAddClient);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String email = emailadd.getText().toString();
                    if     ( estateName.getText().toString().trim().length() == 0 ||
                            emailadd.getText().toString().trim().length() == 0 ||
                            desc.getText().toString().trim().length() == 0 ||
                            address.getText().toString().trim().length() == 0 ||
                            contact_number.getText().toString().trim().length() == 0
                            ) {
                        Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();
                    } else if (!isEmailValid(email)) {
                        emailadd.setError("Kindly Provide Valid E-mail");

                    } else
                        makeJsonObjectRequest1();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void makeJsonObjectRequest1() throws JSONException, IOException {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "addestate?name=" + estateName.getText().toString()
                + "&des=" + desc.getText().toString()
                + "&addrss=" + address.getText().toString()
                + "&email=" + emailadd.getText().toString()
                + "&phone=" + contact_number.getText().toString()
                + "&area=" + txtSpinnerArea.toString()
                + "&subarea=" + txtSpinnerSubArea.toString()

                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Toast.makeText(getApplicationContext(), "Estate " + estateName + "added Successfully!!", Toast.LENGTH_SHORT).show();

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

//    /For Agent spinner
    public void onStart() {
        super.onStart();
        AddRealEstate.BackTaskArea bt = new AddRealEstate.BackTaskArea();
        AddRealEstate.BackTaskSubArea sbArea = new AddRealEstate.BackTaskSubArea();
        bt.execute();
        sbArea.execute();
    }
    private class BackTaskArea extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(Ngrok.url + "/web_service_new/public/cities");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                is.close();
                //result=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // parse json data
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);

                    areaArray item = new areaArray();
                    item.name = jsonObject.getString("city_name");
                    item.id = jsonObject.getString("city_id");
                    list.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            for (int a=0;a<list.size();a++) {
                listItems.add(list.get(a).name);
            }

            adapterArea.notifyDataSetChanged();
        }

    }
////    for SubArea

private class BackTaskSubArea extends AsyncTask<Void, Void, Void> {


    protected void onPreExecute() {
        super.onPreExecute();
        listSubArea = new ArrayList<>();
    }

    protected Void doInBackground(Void... params) {
        InputStream is = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/area");
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            // Get our response as a String.
            is = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                result += line;
            }
            is.close();
            //result=sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // parse json data
        try {
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonObject = jArray.getJSONObject(i);

                areaArray subSubrea = new areaArray();
                subSubrea.id = jsonObject.getString("area_id");
                subSubrea.name = jsonObject.getString("area_name");
                listSubArea.add(subSubrea);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        for (int i =0; i<listSubArea.size(); i++){
            listItemsSubArea.add(listSubArea.get(i).name);
        }
        adapterSubArea.notifyDataSetChanged();
    }

}
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
