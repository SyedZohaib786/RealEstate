package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.AppController;

public class SpecificClientSearch extends AppCompatActivity {

    private static String TAG = SpecificClientSearch.class.getSimpleName();
    EditText clname;
    EditText emailadd;
    Button search, back, cancel;

    ArrayList<HashMap<String, String>> contactList;
    private ListView lv;
    private ProgressDialog pDialog;

//    private TextView txtResponse;

    // temporary string to show the parsed response
//    private String jsonResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_client_search);



        lv = (ListView) findViewById(R.id.listViewSpecificClient);

        clname = (EditText) findViewById(R.id.etRealEStateName);
        emailadd = (EditText) findViewById(R.id.etEmailSearch);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        search = (Button) findViewById(R.id.btnSearchClient);
       // txtResponse = (TextView) findViewById(R.id.txtSearchClient);




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final String email = emailadd.getText().toString();
                    if (clname.getText().toString().trim().length() == 0 ||
                            emailadd.getText().toString().trim().length() == 0 ) {
                        Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();

                    }else if (!isEmailValid(email)){
                        emailadd.setError("Kindly Provide Valid E-mail");
                    }else {
                        contactList = new ArrayList<>();
                        makeJsonArrayRequest();
                    }

            }
        });
    }

private void makeJsonArrayRequest() {

    showpDialog();

    JsonArrayRequest req = new JsonArrayRequest(Ngrok.url + "/web_service_new/public/"
            + "getspecificclient?name=" + clname.getText().toString()
                + "&email=" + emailadd.getText().toString(),
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

                            String name = person.getString("client_name");
                            String phone = person.getString("phone");
                            String cell = person.getString("mobile_number");
                            String email = person.getString("email");
                            String addrss = person.getString("address");
                            String agentName = person.getString("agent_name");
                            String estateName = person.getString("real_estatename");

                            HashMap<String, String> contact = new HashMap<>();

                            contact.put("client_name", name);
                            contact.put("phone", phone);
                            contact.put("mobile_number", cell);
                            contact.put("email", email);
                            contact.put("address", addrss);
                            contact.put("agent_name", agentName);
                            contact.put("real_estatename", estateName);

                            contactList.add(contact);


                        }

                    } catch (JSONException e) {
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
            hidepDialog();
        }
    });
    ListAdapter adapter = new SimpleAdapter(
            SpecificClientSearch.this, contactList,
            R.layout.list_specificclient, new String[]{"client_name", "phone",
            "mobile_number", "email", "address", "agent_name", "real_estatename"}, new int[]{R.id.Clname,
            R.id.cphone, R.id.clcell, R.id.clemail, R.id.claddress, R.id.clagname, R.id.clesname});


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
    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
