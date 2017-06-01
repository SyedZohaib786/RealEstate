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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

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
import java.util.HashMap;

import app.AppController;

public class SearchProperty extends AppCompatActivity {
    private static String TAG = SearchProperty.class.getSimpleName();
    EditText amountTOSEarch;
    EditText areaTOSearch;

    ArrayList<HashMap<String, String>> contactList;
    private ListView lv;
    private ProgressDialog pDialog;

    Button search;

    //for spinner Area Names
    static ArrayList<areaArray> listArea;
    ArrayList<String> listItems_Area = new ArrayList<>();
    ArrayAdapter<String> adapterArea;
    Spinner spArea;
    private String txtArea;

    //for spinner Property Purpose Names
    static ArrayList<purposeArray> listPurpose;
    ArrayList<String> listItems_Purpose = new ArrayList<>();
    ArrayAdapter<String> adapterPurpose;
    Spinner spPurpose;
    private String txtPurpose;

    //for spinner Property Type Names
    static ArrayList<propArray> listPropType;
    ArrayList<String> listItems_PropertyType = new ArrayList<>();
    ArrayAdapter<String> adapterPropertyType;
    Spinner spPropertyType;
    private String txtProp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_property);

        lv = (ListView)findViewById(R.id.list);
        amountTOSEarch = (EditText) findViewById(R.id.etAmount);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        search = (Button) findViewById(R.id.btnSearch);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountTOSEarch.getText().toString().trim().length() == 0 )
                       // areaTOSearch.getText().toString().trim().length() == 0 ) {
                    Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();

                else {
                    contactList = new ArrayList<>();
//                    Intent i = new Intent(SearchProperty.this, ViewSearchedProperty.class);
//                    startActivity(i);
                    makeJsonArrayRequest();
                }
            }
        });
        //for spinner Area// STOPSHIP: 03/04/2017
        spArea = (Spinner) findViewById(R.id.spArea);
        adapterArea = new ArrayAdapter<String>(this, R.layout.spinner_area_layout, R.id.txtAreaNames, listItems_Area);
        spArea.setAdapter(adapterArea);

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrayName = listArea.get(position).name;
                String arrayID = listArea.get(position).id;
                txtArea = arrayID;
                Toast.makeText(getApplication(), "you selected " + arrayID, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //for spinner Property Purpose// STOPSHIP: 03/04/2017
        spPurpose = (Spinner) findViewById(R.id.spPurpose);
        adapterPurpose = new ArrayAdapter<String>(this, R.layout.spinner_purpose, R.id.txtPurpose, listItems_Purpose);
        spPurpose.setAdapter(adapterPurpose);

        spPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrName = listPurpose.get(position).name;
                String arrId = listPurpose.get(position).id;
                txtPurpose = arrId;
                Toast.makeText(getApplication(), "you selected " + arrId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //for spinner Property Type// STOPSHIP: 03/04/2017
        spPropertyType = (Spinner) findViewById(R.id.spType);
        adapterPropertyType = new ArrayAdapter<String>(this, R.layout.spinner_propertytype, R.id.txtPropertyType, listItems_PropertyType);
        spPropertyType.setAdapter(adapterPropertyType);

        spPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrName = listPropType.get(position).name;
                String arrId = listPropType.get(position).Id;
                txtProp = arrId;
                Toast.makeText(getApplication(), "you selected " + arrId, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(Ngrok.url + "/web_service_new/public/"
                + "propertysearch?amount=" + amountTOSEarch.getText().toString()
                + "&area=" + txtArea.toString()
                + "&type=" + txtProp.toString()
                + "&pr=" + txtPurpose.toString()
                ,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
//                        jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject person = (JSONObject) response
                                        .get(i);

                                String name = person.getString("property_name");
                                String des = person.getString("property_des");
                                String amnt = person.getString("property_amount");
                                String cell = person.getString("cell");
                                String clname = person.getString("client_name");
                                String agentName = person.getString("agent_name");
                                String estateName = person.getString("real_estatename");
                                String proparea = person.getString("property_area");
                                //String city = person.getString("city_name");
                                //String area = person.getString("area_name");
                                //String purpose = person.getString("propertypurpose_name");
                                //String proptype = person.getString("propertytype_name");
                                String subtype = person.getString("propertysubtype_name");
                                String featre = person.getString("features_name");


                                HashMap<String, String> contact = new HashMap<>();

                                contact.put("property_name", name);
                                contact.put("property_des", des);
                                contact.put("property_amount", amnt);
                                contact.put("cell", cell);
                                contact.put("client_name", clname);
                                contact.put("agent_name", agentName);
                                contact.put("real_estatename", estateName);
                                contact.put("property_area", proparea);
                                //contact.put("city_name", city);
//                                contact.put("area_name", area);
                                //contact.put("propertypurpose_name", purpose);
                                //contact.put("propertytype_name", proptype);
                                contact.put("propertysubtype_name", subtype);
                                contact.put("features_name", featre);

                                contactList.add(contact);


                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplication(),
                                    "No such property found",
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "no such property found");
                Toast.makeText(getApplication(),
                        "no such property found..", Toast.LENGTH_LONG).show();


                hidepDialog();
            }
        });
        ListAdapter adapter = new SimpleAdapter(
                SearchProperty.this, contactList,
                R.layout.listshowsearchproperties, new String[]{"property_name", "property_des", "property_amount",
                "cell", "client_name", "property_area", "agent_name", "real_estatename",
                 "propertysubtype_name", "features_name"}, new int[]{R.id.propertyname,
                R.id.prop_des, R.id.prop_amount, R.id.cell, R.id.clname, R.id.sq_yrds, R.id.agname, R.id.esname,
                 R.id.prop_subtype, R.id.prop_feature});

//        , "city_name", "area_name"
//        R.id.city, R.id.area,
//        "propertypurpose_name", "propertytype_name",
//        R.id.prop_purpose, R.id.prop_type,
                lv.setAdapter(adapter);

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void onStart() {
        super.onStart();
        SearchProperty.BackTaskPropertyArea bt = new SearchProperty.BackTaskPropertyArea();
        SearchProperty.BackTaskPurpose purpose = new SearchProperty.BackTaskPurpose();
        SearchProperty.BackTaskPropertyType type = new SearchProperty.BackTaskPropertyType();
                bt.execute();
        purpose.execute();
        type.execute();

    }

    ///For Property Area spinner
    private class BackTaskPropertyArea extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            listArea = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/cities");
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
                    areaArray item = new areaArray();
                    item.name = jsonObject.getString("city_name");
                    item.id = jsonObject.getString("city_id");
                    listArea.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a = 0; a < listArea.size(); a++) {
                listItems_Area.add(listArea.get(a).name);
            }
            adapterArea.notifyDataSetChanged();
        }

    }
    ///For Property Purpose spinner
    private class BackTaskPurpose extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            listPurpose = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/purpose");
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
                    purposeArray items = new purposeArray();
                    items.id = jsonObject.getString("propertypurpose_id");
                    items.name = jsonObject.getString("propertypurpose_name");
                    listPurpose.add(items);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a = 0; a < listPurpose.size(); a++) {
                listItems_Purpose.add(listPurpose.get(a).name);
            }

            adapterPurpose.notifyDataSetChanged();
        }

    }

    ///For Property Type spinner
    private class BackTaskPropertyType extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            listPropType = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/type");
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

                    propArray item = new propArray();
                    item.Id = jsonObject.getString("propertytype_id");
                    item.name = jsonObject.getString("propertytype_name");
                    listPropType.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a = 0; a < listPropType.size(); a++) {
                listItems_PropertyType.add(listPropType.get(a).name);
            }

            adapterPropertyType.notifyDataSetChanged();
        }

    }


}
