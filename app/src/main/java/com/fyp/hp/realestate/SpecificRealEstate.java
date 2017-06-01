package com.fyp.hp.realestate;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;


public class SpecificRealEstate extends AppCompatActivity {
    //for Area's spinner
    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> adapterArea;
    Spinner spArea,spsubArea;

    ArrayAdapter<String> adapterSubArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_real_estate);

        spArea = (Spinner)findViewById(R.id.spSelectArea);
        spsubArea = (Spinner)findViewById(R.id.spSelectSubArea);


        adapterSubArea=new ArrayAdapter<String>(this,R.layout.spinner_subarea_layout,R.id.txtSubAreaNames,listItems);
        spArea.setAdapter(adapterSubArea);


        adapterArea=new ArrayAdapter<String>(this,R.layout.spinner_area_layout,R.id.txtAreaNames,listItems);
        spArea.setAdapter(adapterArea);

        //for Area
        spArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp1=String.valueOf(spArea.getSelectedItem());
                String area = parent.getItemAtPosition(position).toString();
                if (sp1.contentEquals(area)){
                    List<String> list = new ArrayList<String>();
                    list.add("city_id");
                    spsubArea.setAdapter(adapterSubArea);

                }

//                Toast.makeText(AddRealEstate.this, area , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    ///For Agent spinner
    public void onStart() {
        super.onStart();
        SpecificRealEstate.BackTaskArea bt = new SpecificRealEstate.BackTaskArea();
        SpecificRealEstate.BackTaskSubArea sbArea = new SpecificRealEstate.BackTaskSubArea();
        bt.execute();
        sbArea.execute();
    }

    private class BackTaskArea extends AsyncTask<Void, Void, Void> {
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
            listItems.addAll(list);
            adapterArea.notifyDataSetChanged();
        }

    }
    //    for SubArea
    private class BackTaskSubArea extends AsyncTask<Void, Void, Void> {
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
            listItems.addAll(list);
            adapterSubArea.notifyDataSetChanged();
        }

    }

}
