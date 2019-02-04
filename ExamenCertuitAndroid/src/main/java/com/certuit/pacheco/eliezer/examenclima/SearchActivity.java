package com.certuit.pacheco.eliezer.examenclima;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.certuit.pacheco.eliezer.examenclima.service.OpenWeatherApiService;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import org.jetbrains.annotations.NotNull;

public class SearchActivity extends AppCompatActivity {

  private final int LOCATION_REQUEST = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    setupToolbar();
  }

  @Override
  public void onBackPressed(){
    NavUtils.navigateUpFromSameTask(this);
    super.onBackPressed();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == LOCATION_REQUEST) {
      if (resultCode == RESULT_OK) {
        showProgressBar(true);
        enableSearchByNameButton(false);
        enableSearchByZipcodeButton(false);
        enableSearchByMapButton(false);
        double latitude = data.getExtras().getDouble("latitude");
        double longitude = data.getExtras().getDouble("longitude");
        OpenWeatherApiService.getInstance(getApplicationContext()).getWeatherCoordinates(latitude, longitude, new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            showProgressBar(false);
            enableSearchByNameButton(true);
            enableSearchByZipcodeButton(true);
            enableSearchByMapButton(true);
            if (result != null && result.get("cod").getAsInt() == 200) {
              Intent intent = new Intent();
              intent.putExtra("cityName", result.get("name").getAsString());
              intent.putExtra("countryCode", result.get("sys").getAsJsonObject().get("country").getAsString());
              setResult(RESULT_OK, intent);
              finish();
            } else {
              showToast(getString(R.string.msg_city_not_found));
            }
          }
        });
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
    switch (requestCode) {
      case LOCATION_REQUEST: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          searchMapAction(null);
        }
        break;
      }
    }
  }

  private void setupToolbar(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void searchMapAction(View v) {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_REQUEST);
    } else {
      Intent intent = new Intent(SearchActivity.this, LocationActivity.class);
      startActivityForResult(intent, LOCATION_REQUEST);
    }
  }

  public void searchCityByName(View v){
    EditText cityNameText = findViewById(R.id.city_name);
    Spinner countryCodeSpinner = findViewById(R.id.country_code_name);
    showProgressBar(true);
    enableSearchByNameButton(false);
    enableSearchByZipcodeButton(false);
    enableSearchByMapButton(false);
    try {
      String city = cityNameText.getText().toString();
      String countryCode = countryCodeSpinner.getSelectedItem() == null ? "" : countryCodeSpinner.getSelectedItem().toString();
      if(city.isEmpty()) throw new Exception(getString(R.string.msg_empty_city_name));
      if(countryCode.isEmpty()) throw new Exception(getString(R.string.msg_empty_country_code));
      countryCode = countryCode.split(",")[0];
      OpenWeatherApiService.getInstance(getApplicationContext()).getWeatherCity(city, countryCode, new FutureCallback<JsonObject>() {
        @Override
        public void onCompleted(Exception e, JsonObject result) {
          showProgressBar(false);
          enableSearchByNameButton(true);
          enableSearchByZipcodeButton(true);
          enableSearchByMapButton(true);
          if (result != null && result.get("cod").getAsInt() == 200) {
            Intent intent = new Intent();
            intent.putExtra("cityName", result.get("name").getAsString());
            intent.putExtra("countryCode", result.get("sys").getAsJsonObject().get("country").getAsString());
            setResult(RESULT_OK, intent);
            finish();
          } else {
            showToast(getString(R.string.msg_city_not_found));
          }
        }
      });
    }catch(Exception ex){
      showToast(ex.getMessage());
      showProgressBar(false);
      enableSearchByNameButton(true);
      enableSearchByZipcodeButton(true);
      enableSearchByMapButton(true);
    }
  }

  public void searchCityByZipcode(View v){
    EditText zipcodeText = findViewById(R.id.zipcode);
    Spinner countryCodeSpinner = findViewById(R.id.country_code_zipcode);
    showProgressBar(true);
    enableSearchByNameButton(false);
    enableSearchByZipcodeButton(false);
    enableSearchByMapButton(false);
    try {
      String zipcode = zipcodeText.getText().toString();
      String countryCode = countryCodeSpinner.getSelectedItem() == null ? "" : countryCodeSpinner.getSelectedItem().toString();
      if(zipcode.isEmpty()) throw new Exception(getString(R.string.msg_empty_zipcode));
      if(countryCode.isEmpty()) throw new Exception(getString(R.string.msg_empty_country_code));
      countryCode = countryCode.split(",")[0];
      OpenWeatherApiService.getInstance(getApplicationContext()).getWeatherZipcode(zipcode, countryCode, new FutureCallback<JsonObject>() {
        @Override
        public void onCompleted(Exception e, JsonObject result) {
          showProgressBar(false);
          enableSearchByNameButton(true);
          enableSearchByZipcodeButton(true);
          enableSearchByMapButton(true);
          if (result != null && result.get("cod").getAsInt() == 200) {
            Intent intent = new Intent();
            intent.putExtra("cityName", result.get("name").getAsString());
            intent.putExtra("countryCode", result.get("sys").getAsJsonObject().get("country").getAsString());
            setResult(RESULT_OK, intent);
            finish();
          } else {
            showToast(getString(R.string.msg_city_not_found));
          }
        }
      });
    }catch(Exception ex){
      showToast(ex.getMessage());
      showProgressBar(false);
      enableSearchByNameButton(true);
      enableSearchByZipcodeButton(true);
      enableSearchByMapButton(true);
    }
  }

  public void showProgressBar(final boolean visible){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        ProgressBar view = findViewById(R.id.progressBar2);
        view.setVisibility(visible? View.VISIBLE : View.GONE);
      }
    });
  }

  public void enableSearchByNameButton(final boolean enable){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Button view = findViewById(R.id.btn_search_name);
        view.setEnabled(enable);
      }
    });
  }

  public void enableSearchByZipcodeButton(final boolean enable){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Button view = findViewById(R.id.btn_search_zipcode);
        view.setEnabled(enable);
      }
    });
  }

  public void enableSearchByMapButton(final boolean enable){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        ImageButton view = findViewById(R.id.btn_search);
        view.setEnabled(enable);
      }
    });
  }

  public void showToast(final String message){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
