package com.certuit.pacheco.eliezer.examenclima;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.certuit.pacheco.eliezer.examenclima.model.Weather;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

  private Weather weather;
  private TextView tempLabel;
  private TextView tempMinLabel;
  private TextView tempMaxLabel;
  private TextView dateLabel;

  public ForecastFragment(){}

  public void setWeather(Weather weather){
    this.weather = weather;
    updateUI();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_weather,container);
    tempLabel = v.findViewById(R.id.label_temp);
    tempMinLabel = v.findViewById(R.id.label_temp_min);
    tempMaxLabel = v.findViewById(R.id.label_temp_max);
    dateLabel = v.findViewById(R.id.label_date);
    return v;
  }

  private void updateUI() {
    @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
    fmt.setCalendar(weather.getDate());

    dateLabel.setText(getString(R.string.msg_forecast)
        .concat(" ")
        .concat(fmt.format(weather.getDate().getTime())));
    tempLabel.setText(getString(R.string.msg_temp)
        .concat(" : ")
        .concat(String.format("%.2f", weather.getTemperature()))
        .concat(" °C"));
    tempMaxLabel.setText(getString(R.string.msg_max_temp)
        .concat(" : ")
        .concat(String.format("%.2f", weather.getMaxTemperature()))
        .concat(" °C"));
    tempMinLabel.setText(getString(R.string.msg_min_temp)
        .concat(" : ")
        .concat(String.format("%.2f", weather.getMinTemperature()))
        .concat(" °C"));
  }
}
