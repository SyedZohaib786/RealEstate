package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import app.AppController;

public class AddProperty extends AppCompatActivity {
    Button addressButton;
    TextView addressTV;
    TextView latLongTV1=null;
    TextView latLongTV2=null;


    //for image upload
    public static final String UPLOAD_URL = Ngrok.url + "/VolleyUpload/upload.php";
    public static final String UPLOAD_KEY = "image";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;

    private static String TAG = AddProperty.class.getSimpleName();
    EditText propertyName, propertyDes, propertyArea, clName, amount, cell;
    Spinner realestatenames, agents;
    Button add, selectionOFAGENT, remove;
    private ProgressDialog pDialog;

    //for spinner Feature's Name
    static ArrayList<featuresArray> listFeatures;
    ArrayList<String> listItems_features = new ArrayList<>();
    ArrayAdapter<String> adapterFeatures;
    Spinner spFeatres;
    private String txtFtr;

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

    //for spinner Property SubType Names
    static ArrayList<subPropArray> listPropSubType;
    ArrayList<String> listItems_PropertySubType = new ArrayList<>();
    ArrayAdapter<String> adapterPropertySubType;
    Spinner spPropertySubType;
    private String txtSubType;

    //for spinner Area Names
    static ArrayList<areaArray> listArea;
    ArrayList<String> listItems_Area = new ArrayList<>();
    ArrayAdapter<String> adapterArea;
    Spinner spArea;
    private String txtArea;

    //for spinner Sub Area Names
    static ArrayList<areaArray> listSubArea;
    ArrayList<String> listItems_subArea = new ArrayList<>();
    ArrayAdapter<String> adaptersubArea;
    Spinner spsubArea;
    private String txtSubArea;

    //for spinner Agent Names
    static ArrayList<agentArray> listAgents;
    ArrayList<String> listItems_Agent = new ArrayList<>();
    ArrayAdapter<String> adapterAgent;
    Spinner spinnerAgent;
    private String txtAgent;

    //for spinner RealEstate Names
    static ArrayList<agentArray> listEstates;
    ArrayList<String> listItems_Estate = new ArrayList<>();
    ArrayAdapter<String> adapterEstate;
    Spinner spinnerEstate;
    private String txtEstate;

    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        latLongTV1 = (TextView) findViewById(R.id.txtLat);
        latLongTV2 = (TextView) findViewById(R.id.txtLng);



        addressButton = (Button) findViewById(R.id.btnSearchLocation);
        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                EditText editText = (EditText) findViewById(R.id.etLocation);
                String address = editText.getText().toString();

                GeocodingLocation locationAddress = new GeocodingLocation();
                locationAddress.getAddressFromLocation(address,
                        getApplicationContext(), new GeocoderHandler());
            }
        });


        buttonChoose = (Button) findViewById(R.id.btnSelectImage);
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//For selecting multiple images
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        imageView = (ImageView) findViewById(R.id.imageViewForSelection);

        //for spinner'// STOPSHIP: 03/04/2017
        spFeatres = (Spinner) findViewById(R.id.spfeature);
        adapterFeatures = new ArrayAdapter<String>(this, R.layout.spinner_features, R.id.txtfeatures, listItems_features);
        spFeatres.setAdapter(adapterFeatures);

        spFeatres.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrName = listFeatures.get(position).name;
                String arrId = listFeatures.get(position).id;
                txtFtr = arrId;
                Toast.makeText(getApplication(), "you selected " + arrId , Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplication(), "you selected " + arrId , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //for spinner Property Type// STOPSHIP: 03/04/2017
        spPropertyType = (Spinner) findViewById(R.id.spPropertyType);
        adapterPropertyType = new ArrayAdapter<String>(this, R.layout.spinner_propertytype, R.id.txtPropertyType, listItems_PropertyType);
        spPropertyType.setAdapter(adapterPropertyType);

        spPropertyType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrName = listPropType.get(position).name;
                String arrId = listPropType.get(position).Id;
                txtProp = arrId;
                Toast.makeText(getApplication(), "you selected " + arrId , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //for spinner Property Sub Type// STOPSHIP: 03/04/2017
        spPropertySubType = (Spinner) findViewById(R.id.spSubType);
        adapterPropertySubType = new ArrayAdapter<String>(this, R.layout.spinner_subtype, R.id.txtSubType, listItems_PropertySubType);
        spPropertySubType.setAdapter(adapterPropertySubType);

        spPropertySubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrName = listPropSubType.get(position).name;
                String arrId = listPropSubType.get(position).ID;
                txtSubType = arrId;
                Toast.makeText(getApplication(), "you selected " + arrId , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
                Toast.makeText(getApplication(), "you selected " + arrayID , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //for spinner Sub Area// STOPSHIP: 03/04/2017
        spsubArea = (Spinner) findViewById(R.id.spSubArea);
        adaptersubArea = new ArrayAdapter<String>(this, R.layout.spinner_subarea_layout, R.id.txtSubAreaNames, listItems_subArea);
        spsubArea.setAdapter(adaptersubArea);

        spsubArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrayName = listSubArea.get(position).name;
                String arrayID = listSubArea.get(position).id;
                txtSubArea = arrayID;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //for spinner Agent's// STOPSHIP: 03/04/2017
        spinnerAgent = (Spinner) findViewById(R.id.spAgent);
        adapterAgent = new ArrayAdapter<String>(this, R.layout.spinner_agents_layout, R.id.txtAgentNames, listItems_Agent);
        spinnerAgent.setAdapter(adapterAgent);

        spinnerAgent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrayName = listAgents.get(position).name;
                String arrayID = listAgents.get(position).id;
                txtAgent = arrayID;

                Toast.makeText(getApplication() ,"you selected " +  arrayID + " - " + arrayName, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //for spinner RealEstates// STOPSHIP: 03/04/2017
        spinnerEstate = (Spinner) findViewById(R.id.spEstate);
        adapterEstate = new ArrayAdapter<String>(this, R.layout.spinner_realestates_layout, R.id.txtEstateNames, listItems_Estate);
        spinnerEstate.setAdapter(adapterEstate);

        spinnerEstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String arrayName = listEstates.get(position).name;
                String arrayID = listEstates.get(position).id;
                txtEstate = arrayID;

                Toast.makeText(getApplication() ,"you selected " +  arrayID + " - " + arrayName, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        propertyName = (EditText) findViewById(R.id.etPropName);
        propertyDes = (EditText) findViewById(R.id.etPropDes);
        propertyArea = (EditText) findViewById(R.id.etPropArea);
        amount = (EditText) findViewById(R.id.etAmountProperty);
        clName = (EditText) findViewById(R.id.etClientName);
        cell = (EditText) findViewById(R.id.etCell);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

//        remove = (Button) findViewById(R.id.btnDltProperty);
//        remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(AddProperty.this, DeleteProperty.class);
//                startActivity(i);
//            }
//        });

        add = (Button) findViewById(R.id.btnAddProperty);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {//
                    if (propertyName.getText().toString().trim().length() == 0 ||
                            propertyDes.getText().toString().trim().length() == 0 ||
                            propertyArea.getText().toString().trim().length() == 0 ||
                            amount.getText().toString().trim().length() == 0 ||
                            clName.getText().toString().trim().length() == 0 ||
                            cell.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();
                    } else if ( flag == 1 ) {
                        makeJsonObjectRequestWithAgent();
//                        uploadImage();

                    }else {
                        makeJsonObjectRequestWithoutAgent();
//                       uploadImage();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        selectionOFAGENT = (Button) findViewById(R.id.btnSelection);
        selectionOFAGENT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                spinnerAgent.setVisibility(View.VISIBLE);//to show agents
                spinnerEstate.setVisibility(View.VISIBLE);//to show estates
            }
        });
    }
    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            latLongTV1.setText(GeocodingLocation.lati);
            latLongTV2.setText(GeocodingLocation.longi);
        }
    }
    private void makeJsonObjectRequestWithAgent() throws JSONException {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "addpropertywithag?pname=" + propertyName.getText().toString()
                + "&des=" + propertyDes.getText().toString()
                + "&area=" + propertyArea.getText().toString()
                + "&amount=" + amount.getText().toString()
                + "&cell=" + cell.getText().toString()
                + "&cname=" + clName.getText().toString()
                + "&ftr=" + txtFtr.toString()
                + "&purpose=" + txtPurpose.toString()
                + "&type=" + txtProp.toString()
                + "&subtype=" + txtSubType.toString()
                + "&city=" + txtArea.toString()
                + "&area=" + txtSubArea.toString()
                + "&agent=" + txtAgent.toString()
                + "&estate=" + txtEstate.toString()
                + "&lat=" + GeocodingLocation.lati.toString()
                + "&lng=" + GeocodingLocation.longi.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Toast.makeText(getApplicationContext(), "Property added Successfully!!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Property Added Successfuly..",
                            Toast.LENGTH_LONG).show();

                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Property Added Successfuly..", Toast.LENGTH_SHORT).show();
                clearText();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }




    private void makeJsonObjectRequestWithoutAgent() throws JSONException {

        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "addpropertywithoutagent?pname=" + propertyName.getText().toString()
                + "&des=" + propertyDes.getText().toString()
                + "&area=" + propertyArea.getText().toString()
                + "&amount=" + amount.getText().toString()
                + "&cell=" + cell.getText().toString()
                + "&cname=" + clName.getText().toString()
                + "&ftr=" + txtFtr.toString()
                + "&purpose=" + txtPurpose.toString()
                + "&type=" + txtProp.toString()
                + "&subtype=" + txtSubType.toString()
                + "&city=" + txtArea.toString()
                + "&area=" + txtSubArea.toString()
                + "&lat=" + GeocodingLocation.lati.toString()
                + "&lng=" + GeocodingLocation.longi.toString()

                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Toast.makeText(getApplicationContext(), "Property added Successfully!!", Toast.LENGTH_SHORT).show();

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
                        "Property Added Successfuly..", Toast.LENGTH_SHORT).show();
                clearText();

                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void clearText(){
        propertyName.setText("");
        propertyArea.setText("");
        propertyDes.setText("");
        amount.setText("");
        cell.setText("");
        clName.setText("");
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    ///For Features spinner
    public void onStart() {
        super.onStart();
        AddProperty.BackTaskFeatures bt = new AddProperty.BackTaskFeatures();
        AddProperty.BackTaskPurpose purpose = new AddProperty.BackTaskPurpose();
        AddProperty.BackTaskPropertyType type = new AddProperty.BackTaskPropertyType();
        AddProperty.BackTaskPropertySubType sbtype = new AddProperty.BackTaskPropertySubType();
        AddProperty.BackTaskPropertyArea area = new AddProperty.BackTaskPropertyArea();
        AddProperty.BackTaskPropertySubArea subarea = new AddProperty.BackTaskPropertySubArea();
        AddProperty.BackTaskAgents agents = new AddProperty.BackTaskAgents();
        AddProperty.BackTaskEstate estate = new AddProperty.BackTaskEstate();
        purpose.execute();
        bt.execute();
        type.execute();
        sbtype.execute();
        area.execute();
        subarea.execute();
        agents.execute();
        estate.execute();
    }

    private class BackTaskFeatures extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            listFeatures = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/ftr");
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

                    featuresArray items = new featuresArray();
                    items.id = jsonObject.getString("features_id");
                    items.name = jsonObject.getString("features_name");
                    listFeatures.add(items);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a= 0; a<listFeatures.size(); a++){
                listItems_features.add(listFeatures.get(a).name);
            }
            adapterFeatures.notifyDataSetChanged();
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
            for (int a= 0; a<listPurpose.size(); a++){
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
            for (int a= 0; a<listPropType.size(); a++){
                listItems_PropertyType.add(listPropType.get(a).name);
            }

            adapterPropertyType.notifyDataSetChanged();
        }

    }

    ///For Property Sub Type spinner
    private class BackTaskPropertySubType extends AsyncTask<Void, Void, Void> {
        protected void onPreExecute() {
            super.onPreExecute();
            listPropSubType = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/subtype");
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
                    subPropArray item = new subPropArray();
                    item.ID = jsonObject.getString("propertysubtype_id");
                    item.name = jsonObject.getString("propertysubtype_name");
                    listPropSubType.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a = 0; a < listPropSubType.size(); a++) {
                listItems_PropertySubType.add(listPropSubType.get(a).name);
                adapterPropertySubType.notifyDataSetChanged();
            }

        }
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
            for (int a=0;a<listArea.size();a++) {
                listItems_Area.add(listArea.get(a).name);
            }
            adapterArea.notifyDataSetChanged();
        }

    }

    ///For Property Sub Area spinner
    private class BackTaskPropertySubArea extends AsyncTask<Void, Void, Void> {


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
                listItems_subArea.add(listSubArea.get(i).name);
            }
            adaptersubArea.notifyDataSetChanged();
        }

    }

    ///For Agent's spinner
    private class BackTaskAgents extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            listAgents = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/getagent");
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
                    agentArray item = new agentArray();
                    item.name = jsonObject.getString("agent_name");
                    item.id = jsonObject.getString("agent_id");
                    listAgents.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a=0;a<listAgents.size();a++) {
                listItems_Agent.add(listAgents.get(a).name);
            }
            adapterAgent.notifyDataSetChanged();
        }

    }

    ///For Agent's spinner
    private class BackTaskEstate extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            listEstates = new ArrayList<>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(Ngrok.url + "/web_service_new/public/getestates");
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
                    agentArray item = new agentArray();
                    item.name = jsonObject.getString("real_estatename");
                    item.id = jsonObject.getString("real_estateid");
                    listEstates.add(item);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            for (int a=0;a<listEstates.size();a++) {
                listItems_Estate.add(listEstates.get(a).name);
            }
            adapterEstate.notifyDataSetChanged();
        }

    }

    ////////////////////////////////////////for uploading image////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandlerForUploadImage rh = new RequestHandlerForUploadImage();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddProperty.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Successful",Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

}







