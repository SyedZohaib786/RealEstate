package com.fyp.hp.realestate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainScreenForRealEstate extends Activity {
    private static int SPLASH_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen_for_real_estate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              Intent i =  new Intent(MainScreenForRealEstate.this, Login.class);
                startActivity(i);

                finish();
            }
        },SPLASH_TIMEOUT );

    }
}
