package com.certuit.pacheco.eliezer.examenclima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(checkPermissions()) openNextActivity();
    }

    private boolean checkPermissions(){
      return true;
    }

    private void openNextActivity(){
      Intent i = new Intent(SplashscreenActivity.this, WeatherActivity.class);
      startActivity(i);
      finish();
    }
}
