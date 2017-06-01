package com.fyp.hp.realestate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;


public class Login extends AppCompatActivity {
    private static String TAG = Login.class.getSimpleName();
    private Button btnLogin;
    RadioButton agent,user;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Pass = "pass";
    SharedPreferences sharedpreferences;


    // Progress dialog
    private ProgressDialog pDialog;

    EditText eUser, ePass;

    private TextView txtResponse;

    // temporary string to show the parsed response
    private String jsonResponse;

    public Login() throws IllegalAccessException, InstantiationException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
            return;
        }


        agent = (RadioButton) findViewById(R.id.agentRadio);
        user = (RadioButton) findViewById(R.id.userRadio);
        btnLogin = (Button) findViewById(R.id.btnlogn);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (eUser.getText().toString().trim().length() == 0 || ePass.getText().toString().trim().length() == 0) {
                        Toast.makeText(getApplication(), "Fill all the fields!!", Toast.LENGTH_LONG).show();
                    }

                    else if (agent.isChecked()) {
                        try {
                            String u  = eUser.getText().toString();
                            String s  = ePass.getText().toString();

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(Name, u);
                            editor.putString(Pass, s);
                            editor.commit();
                            makeJsonObjectRequestAgent();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            String u  = eUser.getText().toString();
                            String s  = ePass.getText().toString();

                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(Name, u);
                            editor.putString(Pass, s);
                            editor.commit();
                            makeJsonObjectRequestClient();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                String u  = eUser.getText().toString();
                String s  = ePass.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Name, u);
                editor.putString(Pass, s);
                editor.commit();

//               Intent in = new Intent(MainActivity.this,second.class);
//                startActivity(in);


            }
        });

        eUser = (EditText)findViewById(R.id.etUserName);
        ePass = (EditText)findViewById(R.id.etPassword);

        Button exit=(Button) findViewById(R.id.buttonexit);
        Button signup=(Button) findViewById(R.id.btnsignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Registeration.class);
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

    private void makeJsonObjectRequestAgent() throws JSONException {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "loginagent?user=" + eUser.getText().toString() + "&pass=" + ePass.getText().toString(),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Intent nextActivity=new Intent(Login.this, DropDownMenuForAgent.class);
                    startActivity(nextActivity);
                    Toast.makeText(getApplication(),"Welcome " + eUser.getText(),Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Wrong Credentials! Please try again..",
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Wrong Credentials");
                Toast.makeText(getApplicationContext(),
                        "Wrong Credentials! Please try again..", Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    private void makeJsonObjectRequestClient() throws JSONException {
        showpDialog();

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Ngrok.url + "/web_service_new/public/"
                + "loginclient?user=" + eUser.getText().toString() + "&pass=" + ePass.getText().toString(),null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Intent nextActivity=new Intent(Login.this, DropDownMenuForClient.class);
                    startActivity(nextActivity);
                    Toast.makeText(getApplication(),"Welcome " + eUser.getText(),Toast.LENGTH_SHORT).show();
                    clearText();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Wrong Credentials! Please try again..",
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error);
                Toast.makeText(getApplicationContext(),
                        "Wrong Credentials! Please try again..", Toast.LENGTH_SHORT).show();
                clearText();
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
    public void clearText()
    {
        eUser.setText("");
        ePass.setText("");
        agent.requestFocus();
        user.requestFocus();
    }


}

