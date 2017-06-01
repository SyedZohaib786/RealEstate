package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

public class Registeration extends AppCompatActivity {
    private static String TAG = Registeration.class.getSimpleName();
    EditText fname;
    EditText addrss;
    EditText emailedittext;
    EditText number;
    EditText uname;
    EditText password;
    Button submitForm;
    private ProgressDialog pDialog;
    RadioButton agent,client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        fname     = (EditText) findViewById(R.id.etFname);
        addrss    = (EditText) findViewById(R.id.etAddress);
        emailedittext     = (EditText) findViewById(R.id.etEmailSearch);
        number    = (EditText) findViewById(R.id.etNumber);
        uname     = (EditText) findViewById(R.id.txtuname);
        password  = (EditText) findViewById(R.id.txtupass);
        agent     = (RadioButton) findViewById(R.id.radioAgent);
        client    = (RadioButton) findViewById(R.id.radioClient);



        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        submitForm = (Button) findViewById(R.id.btnSubmit);
        submitForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String email = emailedittext.getText().toString();
//                    if (!isValidEmail(email)) {
//                        emailedittext.setError("Invalid Email");
//                    }

                    if (fname.getText().toString().trim().length() == 0 ||
                            addrss.getText().toString().trim().length() == 0 ||
                            emailedittext.getText().toString().trim().length() == 0 ||
                            number.getText().toString().trim().length() == 0 ||
                            uname.getText().toString().trim().length() == 0 ||
                            password.getText().toString().trim().length() == 0 ) {
                        Toast.makeText(getApplication(), "Kindly Fill all the fields!!", Toast.LENGTH_LONG).show();

                    }else if (!isEmailValid(email)){
                        emailedittext.setError("Kindly Provide Valid E-mail");
                    }
                    else if (agent.isChecked())
                        makeJsonObjectRequest1();
                    else if (client.isChecked())
                        makeJsonObjectRequest2();

                    else
                        Toast.makeText(getApplication(),"Sorry no operation",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Button exit=(Button) findViewById(R.id.btnexit);
        Button backToLogin=(Button) findViewById(R.id.btnReg);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Registeration.this, Login.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }
    private void makeJsonObjectRequest1() throws JSONException {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "regagent?agname=" + fname.getText().toString()
                + "&addrss=" + addrss.getText().toString()
                + "&email=" + emailedittext.getText().toString()
                + "&phone=" + number.getText().toString()
                + "&uname=" + uname.getText().toString()
                + "&pasword=" + password.getText().toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

//                    JSONObject phone = response.getJSONObject("phone");
//                    String username = response.getString("user_name");
//                    String upass = response.getString("pasword");
//                    String usertype = response.getString("user_typeid");


                    Intent nextActivity = new Intent(Registeration.this, Login.class);
                    startActivity(nextActivity);
                    Toast.makeText(getApplication(), "Registered Successfully!!Please Login to Proceed " + fname.getText()
                            , Toast.LENGTH_SHORT).show();
                    clearText();

//                    jsonResponse = "";
//                    jsonResponse += "User Id: " + userId + "\n\n";
//                    jsonResponse += "Full Name: " + fName + "\n\n";
//                    jsonResponse += "Name: " + uName + "\n\n";
//                    jsonResponse += "Password: " + pass + "\n\n";
//                    jsonResponse += "Phone: " + phone + "\n\n";
//                    jsonResponse += "Adress: " + addrs + "\n\n";
//                    jsonResponse += "City: " + city + "\n\n";
//                    jsonResponse += "E-mail: " + email + "\n\n";
//                    jsonResponse += "User Type Id: " + usertype + "\n\n";

//                    txtResponse.setText(jsonResponse);

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
                clearText();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makeJsonObjectRequest2() throws JSONException {
        showpDialog();
        JsonObjectRequest jsonObjReq2 = new JsonObjectRequest(Ngrok.url+"/web_service_new/public/"
                + "regclient?clname=" + fname.getText().toString()
                + "&addrss=" + addrss.getText().toString()
                + "&email=" + emailedittext.getText().toString()
                + "&phone=" + number.getText().toString()
                + "&uname=" + uname.getText().toString()
                + "&pasword=" + password.getText().toString(),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

//                    JSONObject phone = response.getJSONObject("phone");
//                    String username = response.getString("user_name");
//                    String upass = response.getString("pasword");
//                    String usertype = response.getString("user_typeid");


                    Intent nextActivity=new Intent(Registeration.this, Login.class);
                    startActivity(nextActivity);
                    Toast.makeText(getApplication(), "Registered Successfully!!Please Login to Proceed " + fname.getText()
                            , Toast.LENGTH_SHORT).show();
                    clearText();

//                    jsonResponse = "";
//                    jsonResponse += "User Id: " + userId + "\n\n";
//                    jsonResponse += "Full Name: " + fName + "\n\n";
//                    jsonResponse += "Name: " + uName + "\n\n";
//                    jsonResponse += "Password: " + pass + "\n\n";
//                    jsonResponse += "Phone: " + phone + "\n\n";
//                    jsonResponse += "Adress: " + addrs + "\n\n";
//                    jsonResponse += "City: " + city + "\n\n";
//                    jsonResponse += "E-mail: " + email + "\n\n";
//                    jsonResponse += "User Type Id: " + usertype + "\n\n";

//                    txtResponse.setText(jsonResponse);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                    clearText();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                clearText();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq2);

    }
    private void clearText(){
        fname.setText("");
        emailedittext.setText("");
        addrss.setText("");
        number.setText("");
        uname.setText("");
        password.setText("");
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
