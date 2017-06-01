package com.fyp.hp.realestate;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class ViewPropertyOnClick extends AppCompatActivity {
    Toolbar mToolbar;

    private static final String tag = AllProperties.class.getSimpleName();
    private static final String url = Ngrok.url + "/web_service_new/public/getpropertydescription";
    private List<DataSet> list = new ArrayList<>();
    private ListView listView;
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property_on_click);

//        Intent intent = getIntent();
//
//        // fetch value from key-value pair and make it visible on TextView.
//        String item = intent.getStringExtra("Selected-Property");
//        listView.setAdapter(adapter);

        mToolbar = (Toolbar) findViewById(R.id.toolbarforViewPropertyWithImage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mToolbar.setTitle(bundle.getString("property_name"));
        }

        listView = (ListView) findViewById(R.id.listVw);
        adapter = new com.fyp.hp.realestate.Adapter(this, list);

        listView.setAdapter(adapter);
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
                                dataSet.setSquareyrds(obj.getString("property_area"));
                                dataSet.setCell(obj.getString("cell"));
                                dataSet.setCity(obj.getString("city_name"));
                                dataSet.setArea(obj.getString("area_name"));
                                dataSet.setPurpose(obj.getString("propertypurpose_name"));
                                dataSet.setType(obj.getString("propertytype_name"));
                                dataSet.setSubtype(obj.getString("propertysubtype_name"));
                                dataSet.setFeature(obj.getString("features_name"));
                                dataSet.setClient(obj.getString("client_name"));
                                dataSet.setAgent(obj.getString("agent_name"));
                                dataSet.setEstate(obj.getString("real_estatename"));

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
                AlertDialog.Builder add = new AlertDialog.Builder(ViewPropertyOnClick.this);
                add.setMessage(error.getMessage()).setCancelable(true);
                AlertDialog alert = add.create();
                alert.setTitle("Error!!!");
                alert.show();
            }
        });
        AppController.getPermission().addToRequestQueue(jsonreq);
    }

}

