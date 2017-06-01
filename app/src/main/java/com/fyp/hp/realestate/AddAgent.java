package com.fyp.hp.realestate;

import android.annotation.SuppressLint;
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

public class AddAgent extends AppCompatActivity {
    private String txtSpinner ;
    ArrayList<String> listItems = new ArrayList<>();
    static  ArrayList<agentArray> list;

    ArrayAdapter<String> adapter;
   Spinner realestatenames;

    private static String TAG = AddAgent.class.getSimpleName();
    EditText agname;
    EditText emailadd;
    EditText phone;
    EditText cell;
    EditText addrss;
    Button add;
    private ProgressDialog pDialog;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_agent);




        //Spinner for RealEstate Names
        realestatenames = (Spinner) findViewById(R.id.spEstateNames);

        adapter = new ArrayAdapter<String>(this, R.layout.spinner_realestates_layout, R.id.txtEstateNames, listItems);
        realestatenames.setAdapter(adapter);

        realestatenames.setId(realestatenames.getId());

        realestatenames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String arrayName = list.get(position).name;
                String arrayID = list.get(position).id;
                txtSpinner = arrayID;

                Toast.makeText(getApplication() ,"you selected " +  arrayID + " - " + arrayName, Toast.LENGTH_LONG).show();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        agname = (EditText) findViewById(R.id.etname);
        emailadd = (EditText) findViewById(R.id.etEmailSearch);
        phone = (EditText) findViewById(R.id.etphone);
        cell = (EditText) findViewById(R.id.etcell);
        addrss = (EditText) findViewById(R.id.etAddress);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        add = (Button) findViewById(R.id.btnadd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String email = emailadd.getText().toString();

                    if (agname.getText().toString().trim().length() == 0 ||
                            addrss.getText().toString().trim().length() == 0 ||
                            emailadd.getText().toString().trim().length() == 0 ||
                            phone.getText().toString().trim().length() == 0 ||
                            cell.getText().toString().trim().length() == 0
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
                + "addagent?ag_name=" + agname.getText().toString()
                + "&email=" + emailadd.getText().toString()
                + "&phone=" + phone.getText().toString()
                + "&cell=" + cell.getText().toString()
                + "&address=" + addrss.getText().toString()
                + "&reid=" + txtSpinner.toString()



                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Toast.makeText(getApplicationContext(), "Agent " + agname.getText().toString().trim() + " added Successfully!!", Toast.LENGTH_SHORT).show();

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

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    ///For spinner
    public void onStart() {
        super.onStart();
        BackTask bt = new BackTask();
        bt.execute();
    }

    private class BackTask extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(Ngrok.url + "/web_service_new/public/getestates");
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

                    agentArray item = new agentArray();
                    item.name = jsonObject.getString("real_estatename");
                    item.id = jsonObject.getString("real_estateid");
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

            adapter.notifyDataSetChanged();
        }

    }

}





