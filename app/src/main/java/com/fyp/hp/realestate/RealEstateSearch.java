package com.fyp.hp.realestate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class RealEstateSearch extends AppCompatActivity {
    //for spinner Area Names
    ArrayList listItems_Area = new ArrayList<>();
    ArrayAdapter<String> adapterArea;
    Spinner spArea;
    //for spinner Sub Area Names
    ArrayList<String> listItems_subArea = new ArrayList<>();
    ArrayAdapter<String> adaptersubArea;
    Spinner spsubArea;
    public String[] subAreaValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_search);

        //for spinner Area// STOPSHIP: 03/04/2017
        spArea = (Spinner) findViewById(R.id.spSelectAreaForEstate);
        adapterArea = new ArrayAdapter<String>(this, R.layout.spinner_area_layout, R.id.txtAreaNames, listItems_Area);
        spArea.setAdapter(adapterArea);

        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String areaId = String.valueOf(listItems_Area.get(position));
                String areaNames = parent.getItemAtPosition(position).toString();

                Log.d("RealEstate Search", "AREA-SELECTED-ID " + areaId);

                int size = listItems_Area.size();
                subAreaValues = new String[listItems_Area.size()];
                for (int i = 0; i < size; i++) {
                    subAreaValues[i] = String.valueOf(listItems_Area.get(Integer.parseInt(areaId)));
                }
                setSpinnerorSubArea();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setSpinnerorSubArea() {
        //for spinner Sub Area// STOPSHIP: 03/04/2017
        spsubArea = (Spinner) findViewById(R.id.spSubArea);
        adaptersubArea = new ArrayAdapter<String>(this, R.layout.spinner_subarea_layout, R.id.txtSubAreaNames, listItems_subArea);
        spsubArea.setAdapter(adapterArea);

    }

    public void onStart() {
        super.onStart();
        RealEstateSearch.BackTaskPropertyArea bt = new RealEstateSearch.BackTaskPropertyArea();
        RealEstateSearch.BackTaskPropertySubArea btt = new RealEstateSearch.BackTaskPropertySubArea();
        bt.execute();
        btt.execute();
    }

    ///For Property Area spinner
    private class BackTaskPropertyArea extends AsyncTask<Void, Void, Void> {
        ArrayList<String> list;

        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<>();
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
                    // add interviewee name to arraylist
                    list.add(jsonObject.getString("city_name"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            listItems_Area.addAll(list);
            adapterArea.notifyDataSetChanged();
        }

    }
        ///For Property Sub Area spinner
        private class BackTaskPropertySubArea extends AsyncTask<Void, Void, Void> {
            ArrayList<String> list;

            protected void onPreExecute() {
                super.onPreExecute();
                list = new ArrayList<>();
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
                        // add interviewee name to arraylist
                        list.add(jsonObject.getString("area_name"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                listItems_subArea.addAll(list);
                adaptersubArea.notifyDataSetChanged();
            }

        }
    }

