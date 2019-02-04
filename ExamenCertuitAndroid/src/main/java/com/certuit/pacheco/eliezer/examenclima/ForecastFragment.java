package com.certuit.pacheco.eliezer.examenclima;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
  private ImageView weatherImage;

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
    weatherImage = v.findViewById(R.id.weather_image);
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
    weatherImage.setImageDrawable(getContext().getDrawable(getCorrectIconWeather(weather)));
  }

  private int getCorrectIconWeather(Weather weather){
    if(weather == null) return R.drawable.w01n;
    if(weather.getConditionId() >= 200 && weather.getConditionId() <= 299){
      return weather.getConditionType().contains("d")? R.drawable.w11d : R.drawable.w11n;
    }
    if(weather.getConditionId() >= 300 && weather.getConditionId() <= 399){
      return weather.getConditionType().contains("d")? R.drawable.w09d : R.drawable.w09n;
    }
    if(weather.getConditionId() >= 500 && weather.getConditionId() <= 599){
      return weather.getConditionType().contains("d")? R.drawable.w10d : R.drawable.w10n;
    }
    if(weather.getConditionId() >= 600 && weather.getConditionId() <= 699){
      return weather.getConditionType().contains("d")? R.drawable.w13d : R.drawable.w13n;
    }
    if(weather.getConditionId() >= 700 && weather.getConditionId() <= 799){
      return weather.getConditionType().contains("d")? R.drawable.w50d : R.drawable.w50n;
    }
    if(weather.getConditionId() == 800){
      return weather.getConditionType().contains("d")? R.drawable.w01d : R.drawable.w01n;
    }
    if(weather.getConditionId() == 801){
      return weather.getConditionType().contains("d")? R.drawable.w02d : R.drawable.w02n;
    }
    if(weather.getConditionId() == 802){
      return weather.getConditionType().contains("d")? R.drawable.w03d : R.drawable.w03n;
    }
    if(weather.getConditionId() == 803 || weather.getConditionId() == 804){
      return weather.getConditionType().contains("d")? R.drawable.w04d : R.drawable.w04n;
    }
    return R.drawable.w01d;
  }
}
