package com.fyp.hp.realestate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class DropDownMenuForClient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down_menu_for_client);

        Spinner sp = (Spinner) findViewById(R.id.spinnerForClients);
        ArrayAdapter<CharSequence> arrAdapter = ArrayAdapter.createFromResource(this, R.array.Options_Selection, android.R.layout.simple_spinner_dropdown_item);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arrAdapter);


        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0) {

                }
                else if (position==1){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, All_Agents.class);
                    startActivity(myIntent);
                }
                else if (position==2){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, AllProperties.class);
                    startActivity(myIntent);
                }
                else if (position==3){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, AddPropertyClient.class);
                    startActivity(myIntent);
                }
                else if (position==4){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, SearchProperty.class);
                    startActivity(myIntent);
                }
                else if (position==5){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, PriceIndex.class);
                    startActivity(myIntent);
                }

                else if (position==6){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, AllRealEstates.class);
                    startActivity(myIntent);
                }
                else if (position==7) {
                    Intent myIntent = new Intent(DropDownMenuForClient.this, ContactUs.class);
                    startActivity(myIntent);
                }
                else if (position == 8){
                    Intent myIntent = new Intent(DropDownMenuForClient.this, Map.class);
                    startActivity(myIntent);
                }
                else {
                    SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent myIntent = new Intent(DropDownMenuForClient.this, Login.class);
                    startActivity(myIntent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(this,sSelected,Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }

}
