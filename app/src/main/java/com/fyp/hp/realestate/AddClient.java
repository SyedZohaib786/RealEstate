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

public class AddClient extends AppCompatActivity {

    private String txtSpinner, txtSpinnerAgent;
    ArrayList<String> listItems = new ArrayList<>();
    static ArrayList<Estates> list;
    ArrayAdapter<String> adapter;

    ArrayList<String> listItemsAgent = new ArrayList<>();
    static ArrayList<agentArray> listAgent;
    ArrayAdapter<String> adapterAgent;


    private static String TAG = AddClient.class.getSimpleName();
    EditText clname;
    EditText emailadd;
    EditText phone;
    EditText cell;
    EditText addrss;
    Button add;
    private ProgressDialog pDialog;

    Spinner agents, realEstateNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        clname = (EditText) findViewById(R.id.etRealEStateName);
        emailadd = (EditText) findViewById(R.id.etEstatePhone);
        phone = (EditText) findViewById(R.id.etDes);
        cell = (EditText) findViewById(R.id.etEstateAddress);
        addrss = (EditText) findViewById(R.id.etAddrssClient);
        agents = (Spinner) findViewById(R.id.spSelectArea);
        realEstateNames = (Spinner) findViewById(R.id.spSelectSubArea);


        adapter = new ArrayAdapter<String>(this, R.layout.spinner_realestates_layout, R.id.txtEstateNames, listItems);
        realEstateNames.setAdapter(adapter);

        realEstateNames.setId(realEstateNames.getId());

        realEstateNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //txtSpinner = String.valueOf(parent.getSelectedItem());

                String arrayName = list.get(position).estateName;
                String arrayID = list.get(position).EstateID;
                txtSpinner = arrayID;

                Toast.makeText(getApplication(), "you selected " + arrayID + " - " + arrayName, Toast.LENGTH_LONG).show();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterAgent = new ArrayAdapter<String>(this, R.layout.spinner_agents_layout, R.id.txtAgentNames, listItemsAgent);
        agents.setAdapter(adapterAgent);

        agents.setId(agents.getId());

        agents.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //txtSpinner = String.valueOf(parent.getSelectedItem());

                String arrayName = listAgent.get(position).name;
                String arrayID = listAgent.get(position).id;
                txtSpinnerAgent = arrayID;

                Toast.makeText(getApplication(), "you selected " + arrayID + " - " + arrayName, Toast.LENGTH_LONG).show();

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
//
        add = (Button) findViewById(R.id.btnAddClient);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String email = emailadd.getText().toString();
//                    if (!isValidEmail(email)) {
//                        emailedittext.setError("Invalid Email");
//                    }
//
                    if (clname.getText().toString().trim().length() == 0 ||
                            addrss.getText().toString().trim().length() == 0 ||
                            emailadd.getText().toString().trim().length() == 0 ||
                            phone.getText().toString().trim().length() == 0 ||
                            cell.getText().toString().trim().length() == 0
//                            estate.getText().toString().trim().length() == 0
                            ) {
                        Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();
//
                    } else if (!isEmailValid(email)) {
                        emailadd.setError("Kindly Provide Valid E-mail");
                    } else
                        addClient();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addClient() throws JSONException {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "addclient?cname=" + clname.getText().toString()
                + "&phone=" + phone.getText().toString()
                + "&cell=" + cell.getText().toString()
                + "&email=" + emailadd.getText().toString()
                + "&address=" + addrss.getText().toString()
                + "&agid=" + txtSpinnerAgent.toString()
                + "&estateid=" + txtSpinner.toString()
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
//
                    Toast.makeText(getApplicationContext(), "Client " + clname + "added Successfully!!", Toast.LENGTH_SHORT).show();

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

    ///For RealEstate spinner
    public void onStart() {
        super.onStart();
        AddClient.BackTaskEstate bt = new AddClient.BackTaskEstate();
        AddClient.BackTaskAgent agent = new AddClient.BackTaskAgent();
        bt.execute();
        agent.execute();
    }

    private class BackTaskEstate extends AsyncTask<Void, Void, Void> {
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

                    Estates item = new Estates();
                    item.estateName = jsonObject.getString("real_estatename");
                    item.EstateID = jsonObject.getString("real_estateid");
                    list.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            for (int a = 0; a < list.size(); a++) {
                listItems.add(list.get(a).estateName);
            }

            adapter.notifyDataSetChanged();
        }


    }


    private class BackTaskAgent extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            listAgent = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httppost = new HttpGet(Ngrok.url + "/web_service_new/public/getagent");
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
                    item.name = jsonObject.getString("agent_name");
                    item.id = jsonObject.getString("agent_id");
                    listAgent.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            for (int a = 0; a < listAgent.size(); a++) {
                listItemsAgent.add(listAgent.get(a).name);
            }

            adapterAgent.notifyDataSetChanged();
        }

    }
}
