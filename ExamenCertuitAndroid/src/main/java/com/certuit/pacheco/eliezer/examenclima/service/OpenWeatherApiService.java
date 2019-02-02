package com.certuit.pacheco.eliezer.examenclima.service;

import android.content.Context;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class OpenWeatherApiService {
  private Context appContext;
  private static OpenWeatherApiService instance;

  private OpenWeatherApiService(Context appContext) {
    this.appContext = appContext;
  }

  public static OpenWeatherApiService getInstance(Context context){
    if(instance == null) instance = new OpenWeatherApiService(context);
    return instance;
  }

  private void enqueueGETRequest(String api, FutureCallback<JsonObject> callback){
    String BASE_URL = "https://api.openweathermap.org/";
    Ion.with(appContext)
        .load(BASE_URL.concat(api))
        .asJsonObject()
        .setCallback(callback);
  }

  public void getMexicaliForecast(FutureCallback<JsonObject> callback){
    getForecastCity("Mexicali","mx",callback);
  }

  public void getForecastCity(String city, String country, FutureCallback<JsonObject> callback){
    String api = "data/2.5/forecast?q="+city+","+country+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api, callback);
  }

  public void getForecastZipcode(Integer zipcode, String country, FutureCallback<JsonObject> callback){
    String api = "data/2.5/forecast?zip="+zipcode+","+country+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api,callback);
  }

  public void getForecastCityZipcode(String city, Integer zipcode, FutureCallback<JsonObject> callback){
    String api = "data/2.5/forecast?zip="+zipcode+"&q="+city+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api,callback);
  }

  public void getForecastCoordinates(Double latitude, Double longitude, FutureCallback<JsonObject> callback){
    String api = "data/2.5/forecast?lat="+latitude+"&lon="+longitude+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api,callback);
  }

  public void getMexicaliWeather(FutureCallback<JsonObject> callback){
    getForecastCity("Mexicali","mx",callback);
  }

  public void getWeatherCity(String city, String country, FutureCallback<JsonObject> callback){
    String api = "data/2.5/weather?q="+city+","+country+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api, callback);
  }

  public void getWeatherZipcode(Integer zipcode, String country, FutureCallback<JsonObject> callback){
    String api = "data/2.5/weather?zip="+zipcode+","+country+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api,callback);
  }

  public void getWeatherCityZipcode(String city, Integer zipcode, FutureCallback<JsonObject> callback){
    String api = "data/2.5/weather?zip="+zipcode+"&q="+city+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api,callback);
  }

  public void getWeatherCoordinates(Double latitude, Double longitude, FutureCallback<JsonObject> callback){
    String api = "data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=6e581c3cffcdeed1ebb4a7d513531bb0";
    enqueueGETRequest(api,callback);
  }
}
