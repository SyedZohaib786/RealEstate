package com.fyp.hp.realestate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.AppController;

public class AllProperties extends Activity {
    private static final String tag = AllProperties.class.getSimpleName();
    private static final String url = Ngrok.url + "/web_service_new/public/getpropertydescription";
    private List<DataSet> list = new ArrayList<>();
    private ListView listView;
    private AdapterForProperty adapter;
    Toolbar tlBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_properties);



//        tlBar = (Toolbar) findViewById(R.id.toolbarforViewPropertyWithImage);
//        tlBar.setTitle(getResources().getString(R.string.app_name));


        listView = (ListView) findViewById(R.id.list);
        adapter = new com.fyp.hp.realestate.AdapterForProperty(this, list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(AllProperties.this, ViewPropertyOnClick.class);
                intent.putExtra("Selected-Property", parent.getSelectedItemPosition());
                startActivity(intent);
            }
        });



        JsonArrayRequest jsonreq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                com.fyp.hp.realestate.DataSet dataSet = new com.fyp.hp.realestate.DataSet();
                                dataSet.setPropname(obj.getString("property_name"));
                                dataSet.setDes(obj.getString("property_des"));
                                dataSet.setImage(obj.getString("image"));
                                dataSet.setAmount(obj.getString("property_amount"));

                                list.add(dataSet);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder add = new AlertDialog.Builder(AllProperties.this);
                add.setMessage(error.getMessage()).setCancelable(true);
                AlertDialog alert = add.create();
                alert.setTitle("Error!!!");
                alert.show();
            }
        });
        AppController.getPermission().addToRequestQueue(jsonreq);
    }


}
