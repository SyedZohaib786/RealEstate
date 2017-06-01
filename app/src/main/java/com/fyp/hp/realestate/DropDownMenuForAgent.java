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

public class DropDownMenuForAgent extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down_menu);


        Spinner sp = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> arrAdapter = ArrayAdapter.createFromResource(this, R.array.Properties_Selection, android.R.layout.simple_spinner_dropdown_item);
        arrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arrAdapter);

       // sp.setOnItemSelectedListener(this);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position==0) {
//                    Intent myIntent = new Intent(DropDownMenuForAgent.this, All_Agents.class);
//                    startActivity(myIntent);
                }
                else if (position==1){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, All_Agents.class);
                    startActivity(myIntent);
                }
                else if (position==2){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AllProperties.class);
                    startActivity(myIntent);
                }
                else if (position==3){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AddProperty.class);
                    startActivity(myIntent);
                }
                else if (position==4){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, SearchProperty.class);
                    startActivity(myIntent);
                }
                else if (position==5){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AddRealEstate.class);
                    startActivity(myIntent);
                }
                else if (position==6){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, PriceIndex.class);
                    startActivity(myIntent);
                }
                else if (position==7){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AllRealEstates.class);
                    startActivity(myIntent);
                }
                else if (position==8){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AddAgent.class);
                    startActivity(myIntent);
                }
                else if (position==9){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AddClient.class);
                    startActivity(myIntent);
                }
                else if (position == 10){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AllClients.class);
                    startActivity(myIntent);
                }

                else if (position==11){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, SpecificClientSearch.class);
                    startActivity(myIntent);
                }
                else if (position==12){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, AddCommision.class);
                    startActivity(myIntent);
                }
                else if (position==13){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, ShowCommission.class);
                    startActivity(myIntent);
                }
                else if (position==14){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, ContactUsForm.class);
                    startActivity(myIntent);
                }
                else if (position == 15){
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, Map.class);
                    startActivity(myIntent);
                }

                else {
                    SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.clear();
                    editor.commit();
                    Intent myIntent = new Intent(DropDownMenuForAgent.this, Login.class);
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
