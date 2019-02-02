package com.certuit.pacheco.eliezer.examenclima;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.certuit.pacheco.eliezer.examenclima.model.Weather;
import com.certuit.pacheco.eliezer.examenclima.service.OpenWeatherApiService;
import com.certuit.pacheco.eliezer.examenclima.model.City;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;

public class WeatherActivity extends AppCompatActivity {

  private City currentCity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_weather);
    setupToolbar();
    showProgressBar(false);
    currentCity = new City();
    refreshWeather(null);
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    if(drawerLayout.isDrawerOpen(Gravity.START)) {
      drawerLayout.closeDrawer(Gravity.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    switch(item.getItemId()) {
      case android.R.id.home:
        drawerLayout.openDrawer(Gravity.START);
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupToolbar(){
    Toolbar toolbar = findViewById(R.id.toolbar);
    toolbar.setNavigationIcon(R.drawable.baseline_menu_white_24);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    setupNavigationMenu();
  }

  private void setupNavigationMenu(){
    final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    NavigationView listView = findViewById(R.id.left_sidebar);
    listView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
          case R.id.favorite_menu_button:
            showToast("Favoritos");
            drawerLayout.closeDrawer(Gravity.START);
            break;
          case R.id.search_menu_button:
            showToast("Buscar");
            drawerLayout.closeDrawer(Gravity.START);
            break;
          default: break;
        }
        return true;
      }
    });
  }


  public void refreshWeather(View v){
    showProgressBar(true);
    enableRefreshButton(false);
    OpenWeatherApiService.getInstance(getApplicationContext())
        .getWeatherCity(currentCity.getName(),currentCity.getCountryCode(), new FutureCallback<JsonObject>() {
          @Override
          public void onCompleted(Exception e, JsonObject result) {
            if (result == null || result.get("cod").getAsInt() != 200) {
              showToast(getString(R.string.msg_error_fetching_weather));
              showProgressBar(false);
              enableRefreshButton(true);
              return;
            }
            final Weather weather = new Weather(result, currentCity);
            OpenWeatherApiService.getInstance(getApplicationContext())
                .getForecastCity(currentCity.getName(),currentCity.getCountryCode(), new FutureCallback<JsonObject>() {
                  @Override
                  public void onCompleted(Exception e, JsonObject result) {
                    if (result == null || result.get("cod").getAsInt() != 200) {
                      showToast(getString(R.string.msg_error_fetching_forecast));
                      showProgressBar(false);
                      enableRefreshButton(true);
                      return;
                    }

                    //Calculate average from the forecast data
                    Stack<Weather> forecastStack = new Stack<>();
                    JsonArray forecastList = result.getAsJsonArray("list");
                    Weather weather1;
                    Weather forecast[] = new Weather[5];
                    String prev = weather.getDateString(), current;
                    int j = 0;
                    for(int i = 0; i < forecastList.size(); ++i){
                      weather1 = new Weather(forecastList.get(i).getAsJsonObject(), currentCity);
                      current = weather1.getDateString();
                      if(prev.equals(current)){
                        forecastStack.push(weather1);
                      } else{
                        prev = current;
                        if(forecastStack.empty()) continue;
                        int size = forecastStack.size();
                        double temp = 0, max = 0, min = 0;
                        Weather w = null;
                        while(!forecastStack.empty()){
                          w = forecastStack.pop();
                          temp += w.getTemperature();
                          max += w.getMaxTemperature();
                          min += w.getMinTemperature();
                        }
                        temp /= size;
                        max /= size;
                        min /= size;
                        forecast[j] = new Weather();
                        forecast[j].setDate(w.getDate());
                        forecast[j].setCity(w.getCity());
                        forecast[j].setConditionId(w.getConditionId());
                        forecast[j].setTemperature(temp);
                        forecast[j].setMaxTemperature(max);
                        forecast[j].setMinTemperature(min);
                        forecastStack.empty();
                        j++;
                      }
                    }

                    updateUI(weather, forecast);
                    showProgressBar(false);
                    enableRefreshButton(true);
                  }
                });
          }
        });
  }

  public void showProgressBar(final boolean visible){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        ProgressBar view = findViewById(R.id.progressBar);
        view.setVisibility(visible? View.VISIBLE : View.GONE);
      }
    });
  }

  public void enableRefreshButton(final boolean enable){
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        ImageButton view = findViewById(R.id.btn_refresh);
        view.setEnabled(enable);
      }
    });
  }

  private void updateUI(final Weather weather, final Weather[] forecast){
    runOnUiThread(new Runnable() {
      @SuppressLint("DefaultLocale")
      @Override
      public void run() {
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        TextView tempLabel = findViewById(R.id.label_temp);
        TextView tempMinLabel = findViewById(R.id.label_temp_min);
        TextView tempMaxLabel = findViewById(R.id.label_temp_max);
        TextView dateLabel = findViewById(R.id.label_date);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        fmt.setCalendar(weather.getDate());

        toolbarTitle.setText(weather.getCity().getName());
        dateLabel.setText(getString(R.string.msg_current_weather)
            .concat(" - ")
            .concat(fmt.format(weather.getDate().getTime())));
        tempLabel.setText(getString(R.string.msg_temp)
            .concat(" : ")
            .concat(String.format("%.2f",weather.getTemperature()))
            .concat(" °C"));
        tempMaxLabel.setText(getString(R.string.msg_max_temp)
            .concat(" : ")
            .concat(String.format("%.2f",weather.getMaxTemperature()))
            .concat(" °C"));
        tempMinLabel.setText(getString(R.string.msg_min_temp)
            .concat(" : ")
            .concat(String.format("%.2f",weather.getMinTemperature()))
            .concat(" °C"));


        FragmentManager fragmentManager = getSupportFragmentManager();
        ForecastFragment fragment1 = (ForecastFragment) fragmentManager.findFragmentById(R.id.fragment_forecast1);
        ForecastFragment fragment2 = (ForecastFragment) fragmentManager.findFragmentById(R.id.fragment_forecast2);
        ForecastFragment fragment3 = (ForecastFragment) fragmentManager.findFragmentById(R.id.fragment_forecast3);
        ForecastFragment fragment4 = (ForecastFragment) fragmentManager.findFragmentById(R.id.fragment_forecast4);
        ForecastFragment fragment5 = (ForecastFragment) fragmentManager.findFragmentById(R.id.fragment_forecast5);

        fragment1.setWeather(forecast[0]);
        fragment2.setWeather(forecast[1]);
        fragment3.setWeather(forecast[2]);
        fragment4.setWeather(forecast[3]);
        fragment5.setWeather(forecast[4]);
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
